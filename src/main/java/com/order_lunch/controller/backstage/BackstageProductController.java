package com.order_lunch.controller.backstage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.model.request.BackstageProductAddRequest;
import com.order_lunch.model.request.BackstageProductPutRequest;
import com.order_lunch.model.response.BackstageProductResponse;
import com.order_lunch.service.Impl.ProductService;

@RestController
@RequestMapping("/backstage/product")
public class BackstageProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<Page<BackstageProductResponse>> getProducts(
            @RequestParam(value = "shopId", required = false) Integer shopId,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<BackstageProductResponse> byPage = productService.getByPage(shopId, pageable);
        byPage.stream().forEach(v -> {
            v.setImgUrl(v.getImgUrl());
        });
        return ResponseEntity.ok().body(byPage);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putProduct(@RequestBody BackstageProductPutRequest productPutRequest) {

        return ResponseEntity.ok().body(productService.putShop(productPutRequest));
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Boolean> postProduct(@RequestBody BackstageProductAddRequest productAddRequest) {
        return ResponseEntity.ok().body(productService.addProduct(productAddRequest));
    }

    @RequestMapping(path = "/{productId}/{isOrderable}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> deleteProduct(@PathVariable() int productId,@PathVariable() boolean isOrderable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(productService.setOrderable(productId,isOrderable,customUserDetails.getId()));
    }

}
