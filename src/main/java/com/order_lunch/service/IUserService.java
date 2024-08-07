package com.order_lunch.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.model.request.PasswordRequest;
import com.order_lunch.model.request.UserPutRequest;
import com.order_lunch.model.request.UserRequest;
import com.order_lunch.model.response.BackstageUserResponse;

public interface IUserService {
    User findById(int id);

    User findByAccount(String account);

    Page<BackstageUserResponse> findByName(Pageable pageable, String name);

    boolean addUser(UserRequest userRequest);

    boolean existByAccount(String account);

    boolean putUser(UserPutRequest userPutRequest, int id);

    User putUserAddressDelivery(int userId, int addressId);

    void putUserPassword(PasswordRequest passwordRequest, User user);

    List<Shop> findLoveByAccount(int id);

    Boolean addOrDeleteUserLove(int id, int shopId);

    List<String> findByAccounts(String account);

    List<Address> putUserAddress(int userId, List<Address> addresses);

    User addMember(UserRequest userRequest);

    Address addUserAddress(int userId, Address address);

    boolean deleteAddressDelivery(int id, int addressId);

    boolean accountExists(String user);
}
