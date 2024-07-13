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
import com.order_lunch.entity.Tab;
import com.order_lunch.model.request.TabProductRequest;
import com.order_lunch.model.response.TabProductResponse;
import com.order_lunch.service.Impl.TabService;

import lombok.NonNull;

@Validated
@RestController
@RequestMapping("/tab")
public class TabController {

    @Autowired
    TabService tabService;

    @RequestMapping(path = "/{shopId}", method = RequestMethod.GET)
    public ResponseEntity<List<TabProductResponse>> getTabProducts(@PathVariable int shopId) {
        Set<Tab> findTabByShopId = tabService.findTabByShopId(shopId);

        List<TabProductResponse> collect = findTabByShopId.stream().map(v -> new TabProductResponse(v, shopId))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @RequestMapping(path = "/{tabId}", method = RequestMethod.PUT)
    public ResponseEntity<?> putTabProducts(@NonNull @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int tabId, @Valid @RequestBody TabProductRequest tabProductRequest) {
        if (tabService.setTabByShopId(tabId, tabProductRequest, customUserDetails.getId())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<?> putTabProducts(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody TabProductRequest tabProductRequest) {
        // return ResponseEntity.ok().body(tabService.addTabByShopId(tabProductRequest,
        // customUserDetails.getId()));

        if (tabService.addTabByShopId(tabProductRequest, customUserDetails.getId())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(path = "/{tabId}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteTabProducts(
            @NonNull @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int tabId) {
        if (tabService.deleteTab(tabId, customUserDetails.getId())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
        // return ResponseEntity.ok().body(tabService.deleteTab(tabId,
        // customUserDetails.getId()));
    }
}
