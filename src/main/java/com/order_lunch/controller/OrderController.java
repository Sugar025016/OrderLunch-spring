package com.order_lunch.controller;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Cart;
import com.order_lunch.entity.Schedule;
import com.order_lunch.entity.Shop;
import com.order_lunch.enums.NewErrorStatus;
import com.order_lunch.enums.OrderCategory;
import com.order_lunch.enums.OrderStatus;
import com.order_lunch.model.ErrorResponse;
import com.order_lunch.model.request.OrderRequest;
import com.order_lunch.model.response.OrderFinishResponse;
import com.order_lunch.model.response.OrderResponse;
import com.order_lunch.service.Impl.CartService;
import com.order_lunch.service.Impl.OrderService;
import com.order_lunch.service.Impl.UserService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    /**
     * @param customUserDetails
     * @param orderRequest
     * @return
     */
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<?> addOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody OrderRequest orderRequest) {
        ErrorResponse errorResponse = new ErrorResponse();

        List<Cart> carts = cartService.getAllByUserId(customUserDetails.getId());
        if (carts.size() == 0) {
            errorResponse.setCode(NewErrorStatus.CART_NULL.getKey());
            errorResponse.setMessage(NewErrorStatus.CART_NULL.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Shop shop = carts.get(0).getProduct().getShop();
        int cartsPriceSum = carts.stream().mapToInt(v -> v.getQty() * v.getProduct().getPrice()).sum();
        if (cartsPriceSum < shop.getDeliveryPrice()) {
            errorResponse.setCode(NewErrorStatus.CART_PRICE_IS_INSUFFICIENT.getKey());
            errorResponse.setMessage(NewErrorStatus.CART_PRICE_IS_INSUFFICIENT.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        LocalDateTime currentDateTime = LocalDateTime.now();

        DayOfWeek dayOfWeek = orderRequest.getTakeTime().getDayOfWeek();
        LocalTime localTime = orderRequest.getTakeTime().toLocalTime();
        List<Schedule> schedulesForDayOfWeek = shop.getSchedulesForDayOfWeek(dayOfWeek.getValue());

        boolean anyMatch = schedulesForDayOfWeek.stream().anyMatch(
                v -> v.getStartTime().compareTo(localTime) <= 0
                        && (v.getEndTime().equals(LocalTime.MIDNIGHT) || v.getEndTime().compareTo(localTime) >= 0));
        if (currentDateTime.plusHours(1).isAfter(orderRequest.getTakeTime()) && !anyMatch) {
            errorResponse.setCode(NewErrorStatus.GET_PRODUCT_TIME_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.GET_PRODUCT_TIME_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (!orderService.addOrder(customUserDetails.getId(), orderRequest)) {
            errorResponse.setCode(NewErrorStatus.ORDER_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.ORDER_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/{page}", method = RequestMethod.GET)
    public ResponseEntity<Page<OrderResponse>> getOrderByUserPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int page) {

        // Pageable pageable = PageRequest.of(page - 1, 10);
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<OrderResponse> order = orderService.getOrder(customUserDetails.getId(), OrderCategory.END.getValue(),
                pageable);
        return ResponseEntity.ok()
                .body(order);
    }

    @RequestMapping(path = "/sell/{shopId}", method = RequestMethod.GET)
    public ResponseEntity<Page<OrderFinishResponse>> getOrderByShop(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int shopId, @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam int classify) {
        Sort sort = Sort.by(Sort.Direction.ASC, "takeTime"); // 使用 "takeTime" 欄位進行升序排序

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        List<Integer> keyByClassify = OrderStatus.getKeyByClassify(classify);

        return ResponseEntity.ok()
                .body(orderService.getOrderByShop(customUserDetails.getId(), shopId, keyByClassify, pageRequest));
    }

    @RequestMapping(path = "/{shop}/{status}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putOrderByShop(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable int shop,
            @PathVariable @Valid @Min(12) int status, @RequestBody List<Integer> orderIds) {
        if (status < 12) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        return ResponseEntity.ok()
                .body(orderService.putOrderByShop(customUserDetails.getId(), shop, status, orderIds));
    }

    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public ResponseEntity<List<OrderResponse>> getNewOrderByUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok()
                .body(orderService.getNewOrderByUser(customUserDetails.getId()));
    }

    @RequestMapping(path = "/{status}", method = RequestMethod.PUT)
    public ResponseEntity<?> putOrderStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable @Valid @Min(12) int status, @RequestBody List<Integer> orderIds) {
        if (orderService.putOrderStatus(customUserDetails.getId(), status, orderIds)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

}
