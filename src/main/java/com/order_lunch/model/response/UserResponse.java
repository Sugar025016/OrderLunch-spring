package com.order_lunch.model.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;

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

    private Address address;

    public UserResponse(User user) {
        BeanUtils.copyProperties(user, this);
        // this.nickname = user.getNickname();
        this.cartCount = user.getCarts().stream().mapToInt(v -> v.getQty()).sum();
        // this.account=user.getAccount();
        // this.favoriteShops = user.getLoves().stream().map(v -> new
        // FavoriteShop(v)).collect(Collectors.toList());
        this.address = user.getDeliveryAddress();
        this.favoriteShops = user.getLoves().stream().map(v -> new FavoriteShop(v)).collect(Collectors.toList());

    }

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
