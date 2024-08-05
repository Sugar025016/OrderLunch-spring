package com.order_lunch.model.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.order_lunch.entity.AddressData;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.enums.OrderStatus;
import com.order_lunch.model.AddressResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private String account;

    private String name;

    private String email;

    private String phone;

    private List<FavoriteShop> favoriteShops;

    private int cartShopId;
    private int cartCount;
    private Double cartLat;
    private Double cartLng;
    private Double cartDeliveryKm;
    private int orderCount;
    private int shopOrderCount;
    private List<ShopNames> shopNames;

    @JsonProperty("address")
    private AddressResponse address;

    public UserResponse(User user) {
        BeanUtils.copyProperties(user, this);
        // this.nickname = user.getNickname();
        this.cartCount = user.getCarts().stream().mapToInt(v -> v.getQty()).sum();
        if (cartCount > 0) {
            Shop shop = user.getCarts().get(0).getProduct().getShop();
            this.cartShopId = shop.getId();
            this.cartLat = shop.getShopAddress().getLat();
            this.cartLng = shop.getShopAddress().getLng();
            this.cartDeliveryKm = shop.getDeliveryKm();
        }

        this.orderCount = user.getOrders().stream().filter(v -> v.getStatus() < OrderStatus.ONGOING.getKey())
                .collect(Collectors.toList()).size();

        // this.shopOrderCount = user.getShops().stream().filter(v ->
        // v.getOrders().size() > 0).mapToInt(v -> v.getOrders().size()).sum();
        this.shopOrderCount = user.getShops().stream().mapToInt(v -> (int) v.getOrders().stream()
                .filter(f -> f.getStatus() == OrderStatus.WAIT_STORE_ACCEPT.getKey()).count()).sum();

        // this.shopOrderCount = user.getCarts().stream().mapToInt(v ->
        // v.getQty()).mapToInt(v->v.size()).sum();
        // this.account=user.getAccount();
        // this.favoriteShops = user.getLoves().stream().map(v -> new
        // FavoriteShop(v)).collect(Collectors.toList());
        if (user.getAddressDelivery() != null) {

            this.address = new AddressResponse(user.getAddressDelivery());
        }
        // this.address = user.getAddressDelivery();
        this.favoriteShops = user.getLoves().stream().map(v -> new FavoriteShop(v)).collect(Collectors.toList());
        this.shopNames = user.getShops().stream().map(shop -> new ShopNames(shop)).collect(Collectors.toList());

    }

    // private int Integer(long count) {
    // return 0;
    // }

    @Getter
    @Setter
    public class FavoriteShop {

        private Integer id;

        private String name;
        private String address;
        private String description;
        private String imgUrl;
        private boolean isOrderable;

        public FavoriteShop(Shop shop) {
            BeanUtils.copyProperties(shop, this);
            if (shop.getFileData() != null) {
                this.imgUrl = shop.getFileData().getFileName();
            }

            AddressData addressData = shop.getShopAddress().getAddressData();
            this.address = addressData.getCity() + addressData.getArea() + addressData.getStreet()
                    + shop.getShopAddress().getDetail();

        }
    }

    @Getter
    @Setter
    public class ShopNames {

        private Integer id;

        private String name;

        public ShopNames(Shop shop) {
            BeanUtils.copyProperties(shop, this);
            if (!shop.isDelete()) {
                this.id = shop.getId();
                this.name = shop.getName();
            }

        }
    }
}
