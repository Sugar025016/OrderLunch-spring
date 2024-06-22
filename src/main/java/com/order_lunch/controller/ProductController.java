package com.order_lunch.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.entity.Product;
import com.order_lunch.model.response.ProductResponse;
import com.order_lunch.service.Impl.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(path = "/{shopId}", method = RequestMethod.GET)
    public ResponseEntity<List<ProductResponse>> getProducts(@PathVariable int shopId) {

        List<Product> productsByShopId = productService.getProductsByShopId(shopId);
        List<ProductResponse> collect = productsByShopId.stream().map(v -> new ProductResponse(v, shopId))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(collect);
    }

    // @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    // public ResponseEntity<String> deleteProduct(@PathVariable int id) {
    // return productService.deleteProductById(id) != null ?
    // ResponseEntity.ok().build()
    // : ResponseEntity.notFound().build();
    // }
}
