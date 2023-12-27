package com.order_lunch.model.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.enums.Status;

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

    private int cartCount;
    private int orderCount;
    private int shopOrderCount;

    private Address address;

    public UserResponse(User user) {
        BeanUtils.copyProperties(user, this);
        // this.nickname = user.getNickname();
        this.cartCount = user.getCarts().stream().mapToInt(v -> v.getQty()).sum();

        this.orderCount = user.getOrders().stream().filter(v -> v.getStatus() < Status.ONGOING.getKey())
                .collect(Collectors.toList()).size();

        // this.shopOrderCount = user.getShops().stream().filter(v -> v.getOrders().size() > 0).mapToInt(v -> v.getOrders().size()).sum();
        this.shopOrderCount = user.getShops().stream().mapToInt(v -> (int)v.getOrders().stream().filter(f->f.getStatus()==Status.WAIT_STORE_ACCEPT.getKey()).count()).sum();

        // this.shopOrderCount = user.getCarts().stream().mapToInt(v ->
        // v.getQty()).mapToInt(v->v.size()).sum();
        // this.account=user.getAccount();
        // this.favoriteShops = user.getLoves().stream().map(v -> new
        // FavoriteShop(v)).collect(Collectors.toList());
        this.address = user.getDeliveryAddress();
        this.favoriteShops = user.getLoves().stream().map(v -> new FavoriteShop(v)).collect(Collectors.toList());

    }

    // private int Integer(long count) {
    //     return 0;
    // }

    @Getter
    @Setter
    public class FavoriteShop {

        private Integer id;

        private String name;
        private String address;
        private String description;
        private String img;
        private boolean isOrderable;

        public FavoriteShop(Shop shop) {
            BeanUtils.copyProperties(shop, this);
            if (shop.getFileData() != null) {
                this.img = shop.getFileData().getFileName();
            }
            this.address = shop.getAddress().getCity() + shop.getAddress().getArea()
                    + shop.getAddress().getDetail();

        }
    }
}
