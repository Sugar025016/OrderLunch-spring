package com.order_lunch.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.enums.NewErrorStatus;
import com.order_lunch.model.ErrorResponse;
import com.order_lunch.model.request.PasswordRequest;
import com.order_lunch.model.request.UserPutRequest;
import com.order_lunch.model.request.UserRequest;
import com.order_lunch.model.response.ShopResponse;
import com.order_lunch.model.response.UserResponse;
import com.order_lunch.repository.IAddressDataRepository;
import com.order_lunch.service.Impl.AddressDataService;
import com.order_lunch.service.Impl.AddressService;
import com.order_lunch.service.Impl.CartService;
import com.order_lunch.service.Impl.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    IAddressDataRepository addressDataRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressDataService addressDataService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(HttpSession session,
            @RequestBody() @Valid UserRequest userRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String storedCaptcha = (String) session.getAttribute("captchaText");

        if (storedCaptcha == null) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.CAPTCHA_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        if (!storedCaptcha.equals(userRequest.getVerifyCode())) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.CAPTCHA_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        if (customUserDetails != null && customUserDetails.getId() != 0) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.HAVE_LOGIN.getKey());
            errorResponse.setMessage(NewErrorStatus.HAVE_LOGIN.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        if (userService.accountExists(userRequest.getAccount())) {
            ErrorResponse errorResponse = new ErrorResponse(NewErrorStatus.ACCOUNT_EXISTS.getKey(),
                    NewErrorStatus.ACCOUNT_EXISTS.getChinese());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        session.removeAttribute("captchaText"); // 驗證成功後從Session中移除
        userService.addMember(userRequest);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User findByAccount = userService.findById(customUserDetails.getId());
        UserResponse userResponse = new UserResponse(findByAccount);
        return ResponseEntity.ok().body(userResponse);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putUser(@RequestBody @Valid UserPutRequest userPutRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boolean putUser = userService.putUser(userPutRequest, customUserDetails.getId());
        return ResponseEntity.ok().body(putUser);
    }

    @RequestMapping(path = "/pwd", method = RequestMethod.PUT)
    public ResponseEntity<?> putUserPassword(@RequestBody @Valid PasswordRequest passwordRequest,
            HttpServletRequest request, HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = userService.findById(customUserDetails.getId());

        if (!user.getPassword().equals(passwordRequest.getPassword())) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.PASSWORD_MISTAKE.getKey());
            errorResponse.setMessage(NewErrorStatus.PASSWORD_MISTAKE.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        userService.putUserPassword(passwordRequest, user);

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/favorite", method = RequestMethod.GET)
    public ResponseEntity<List<ShopResponse>> getLoves(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Shop> findLoveByAccount = userService.findLoveByAccount(customUserDetails.getId());
        List<ShopResponse> collect = findLoveByAccount.stream().map(v -> new ShopResponse(v))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @RequestMapping(path = "/favorite/{shopId}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> addOrDeleteUserLove(@PathVariable int shopId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Boolean findLoveByAccount = userService.addOrDeleteUserLove(customUserDetails.getId(),
                shopId);
        return ResponseEntity.ok().body(findLoveByAccount);
    }

}
