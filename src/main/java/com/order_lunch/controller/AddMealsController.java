package com.order_lunch.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.AddMeals;
import com.order_lunch.model.request.AddMealsRequest;
import com.order_lunch.model.response.AddMealsResponse;
import com.order_lunch.service.Impl.AddMealsService;
import com.order_lunch.service.Impl.ShopService;

@Validated
@RestController
@RequestMapping("/api/addMeals")
public class AddMealsController {

    @Autowired
    AddMealsService addMealsService;

    @Autowired
    ShopService shopService;

    @RequestMapping(path = "/{shopId}", method = RequestMethod.GET)
    public ResponseEntity<List<AddMealsResponse>> getAddMeals(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int shopId) {
        Set<AddMeals> findTabByShopId = addMealsService.getAddMeals(customUserDetails.getId(), shopId);
        List<AddMealsResponse> collect = findTabByShopId.stream().map( AddMealsResponse::new ).collect(Collectors.toList());
        // List<OrderResponse> orderResponses = orderList.stream().map(OrderResponse::new).collect(Collectors.toList());
        
        return ResponseEntity.ok().body(collect);
    }

    @RequestMapping(path = "/{addMealsId}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putAddMeals(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int addMealsId, @Valid @RequestBody AddMealsRequest addMealsRequest) {

        return ResponseEntity.ok()
                .body(addMealsService.putAddMeals(customUserDetails.getId(), addMealsId, addMealsRequest));
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Boolean> putAddMeals(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody AddMealsRequest addMealsRequest) {

        return ResponseEntity.ok()
                .body(addMealsService.addAddMealsByShopId(customUserDetails.getId(), addMealsRequest));
    }

    @RequestMapping(path = "/{addMealsId}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteAddMeals(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int addMealsId) {
        if (addMealsService.deleteAddMeals(customUserDetails.getId(), addMealsId)) {

            return ResponseEntity.ok().build();
        } else {

            return ResponseEntity.internalServerError().build();
        }

    }

}
