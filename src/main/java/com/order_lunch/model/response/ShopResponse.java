package com.order_lunch.model.response;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.AddressData;
import com.order_lunch.entity.Shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShopResponse {

    private Integer id;

    private String name;
    private String address;
    private String description;
    private String imgUrl;
    private boolean isOrderable;

    public ShopResponse(Shop shop) {
        BeanUtils.copyProperties(shop, this);

        if (shop.getFileData() != null) {
            this.imgUrl = shop.getFileData().getFileName();
        }
        AddressData addressData = shop.getShopAddress().getAddressData();
        this.address = addressData.getCity() + addressData.getArea() + addressData.getStreet()
                + shop.getShopAddress().getDetail();
    }
}
