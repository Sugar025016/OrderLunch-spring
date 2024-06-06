package com.order_lunch.controller;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.model.request.CartRequest;
import com.order_lunch.model.response.ShopCartResponse;
import com.order_lunch.service.Impl.CartService;

@RestController
@RequestMapping("/cart")

public class CartController {

    @Autowired
    CartService cartService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<ShopCartResponse> get(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException, ServletException {
        return ResponseEntity.ok().body(cartService.getAllByUserId(customUserDetails.getId()));
    }

    @ExceptionHandler(ServletException.class)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Integer> post(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CartRequest cartRequest) throws IOException, ServletException   {
        return ResponseEntity.ok().body(cartService.addCart(customUserDetails.getId(), cartRequest));
    }

    // @RequestMapping(path = "/{cartId}", method = RequestMethod.PUT)
    // public ResponseEntity<ShopCartResponse> put(@PathVariable int
    // cartId,@RequestBody CartRequest cartRequest,
    // @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    // ShopCartResponse putCart = cartService.putCart(customUserDetails.getId(),
    // cartId, cartRequest);
    // return ResponseEntity.ok().body(putCart);
    // }

    @RequestMapping(path = "/{cartId}/{qty}", method = RequestMethod.PUT)
    public ResponseEntity<ShopCartResponse> put(@PathVariable int cartId, @PathVariable int qty,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException, ServletException {
        ShopCartResponse putCart = cartService.putCart(customUserDetails.getId(), cartId, qty);
        return ResponseEntity.ok().body(putCart);
    }

    @RequestMapping(path = "/{cartId}", method = RequestMethod.DELETE)
    public ResponseEntity<ShopCartResponse> delete(@PathVariable int cartId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException, ServletException {
        ShopCartResponse deleteCart = cartService.deleteCart(cartId, customUserDetails.getId());
        return ResponseEntity.ok().body(deleteCart);
    }

}
