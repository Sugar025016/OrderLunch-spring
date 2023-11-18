package com.order_lunch.controller.sell;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Product;
import com.order_lunch.model.request.SellProductRequest;
import com.order_lunch.model.response.SellProductResponse;
import com.order_lunch.service.Impl.ProductService;

@RestController
@RequestMapping("/sell/product")
public class SellProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(path = "/{shopId}", method = RequestMethod.GET)
    public ResponseEntity<List<SellProductResponse>> getProducts(
            @PathVariable() int shopId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Product> productsByShopIdAndUserID = productService.getProductsByShopIdAndUserID(shopId,
                customUserDetails.getId());
        List<SellProductResponse> sellProductResponses = productsByShopIdAndUserID.stream()
                .map(v -> new SellProductResponse(v)).collect(Collectors.toList());

        return ResponseEntity.ok().body(sellProductResponses);
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteProducts(
            @PathVariable() int productId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boolean deleteProductById = productService.deleteProductById(productId, customUserDetails.getId());
        return ResponseEntity.ok().body(deleteProductById);
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putProduct(@PathVariable() int productId,
            @RequestBody SellProductRequest sellProductPutRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok()
                .body(productService.putSellProduct(sellProductPutRequest, productId, customUserDetails.getId()));
    }

    @RequestMapping(path = "/{shopId}", method = RequestMethod.POST)
    public ResponseEntity<Boolean> postProduct(@PathVariable() int shopId,
            @RequestBody SellProductRequest sellProductAddRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok()
                .body(productService.addSellProduct(sellProductAddRequest, shopId, customUserDetails.getId()));
    }

}
