package com.order_lunch.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.model.request.PasswordRequest;
import com.order_lunch.model.request.UserPutRequest;
import com.order_lunch.model.request.UserRequest;
import com.order_lunch.model.response.BackstageUserResponse;
import com.order_lunch.repository.IShopRepository;
import com.order_lunch.repository.IUserRepository;
import com.order_lunch.service.IUserService;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    IShopRepository iShopRepository;

    @Override
    public User findById(int id) {
        Optional<User> findById = iUserRepository.findById(id);
        if (!findById.isPresent()) {

            throw new NullPointerException();
        }
        return findById.get();
    }

    @Override
    public User findByAccount(String account) {
        Optional<User> findById = iUserRepository.findByAccount(account);

        if (!findById.isPresent()) {
            throw new NullPointerException();
        }
        return findById.get();
    }

    @Override
    public List<String> findByAccounts(String account) {
        account = "%" + account + "%";
        List<User> findById = iUserRepository.findFirst6ByAccountLike(account);

        List<String> collect = findById.stream().map(v -> v.getAccount()).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Page<BackstageUserResponse> findByName(Pageable pageable, String name) {
        return iUserRepository.findUsersByName(name, pageable);

    }

    @Override
    public boolean addUser(UserRequest userRequest) {
        User user = new User(userRequest);
        iUserRepository.save(user);
        return true;
    }

    @Override
    public boolean existByAccount(String account) {

        return iUserRepository.existByAccount(account);
    }

    @Override
    public boolean putUser(UserPutRequest userPutRequest, int id) {
        Optional<User> findById = iUserRepository.findById(id);
        if (!findById.isPresent()) {
            throw new NullPointerException();
        }
        User user = findById.get();

        user.setUser(userPutRequest);
        iUserRepository.save(user);

        return true;
    }

    @Override
    public boolean putUser(UserRequest userRequest, int id) {
        Optional<User> findById = iUserRepository.findById(id);
        if (!findById.isPresent()) {
            throw new NullPointerException();
        }
        User user = findById.get();

        user.setUser(userRequest);
        iUserRepository.save(user);

        return true;
    }

    @Override
    public boolean putUserPassword(PasswordRequest userPutRequest, int id) {
        Optional<User> findById = iUserRepository.findById(id);
        User user = findById.orElseThrow(
                () -> new IllegalArgumentException("Value not found"));

        if (!user.getPassword().equals(userPutRequest.getPassword())) {
            throw new BadCredentialsException("密碼錯誤");
        }

        user.setPassword(userPutRequest.getNewPassword());
        iUserRepository.save(user);

        return true;
    }

    @Override
    public List<Shop> findLoveByAccount(int id) {
        Optional<User> findById = iUserRepository.findById(id);
        User user = findById.orElseThrow(
                () -> new IllegalArgumentException("Value not found"));
        List<Shop> shopLoveList = user.getLoves();
        return shopLoveList;

    }

    @Transient
    @Override
    public Boolean addOrDeleteUserLove(int id, int shopId) {

        // Optional<User> findById = iUserRepository.findById(id);
        // User user = findById.orElseThrow(
        // () -> new IllegalArgumentException("Value not found"));
        // Shop findByShopIdAndLovesUserId = iShopRepository.findByIdAndLovesId(shopId,
        // id).orElse(null);

        // if (findByShopIdAndLovesUserId == null) {
        // Shop shop = iShopRepository.findByIdAndIsDeleteIsFalse(shopId).orElseThrow(
        // () -> new IllegalArgumentException("Value not found"));
        // user.getLoves().add(shop);
        // } else {

        // user.getLoves().removeIf(v->v.getId()==findByShopIdAndLovesUserId.getId());

        // }
        /////////////////////////////////////////////////////////////////////////////
        // User user = iUserRepository.findById(id).orElseThrow(
        // () -> new IllegalArgumentException("Value not found"));
        // Optional<Shop> findByIdAndDeleteIsFalse =
        // iShopRepository.findByIdAndIsDeleteIsFalse(shopId);
        // Shop shop = findByIdAndDeleteIsFalse.orElseThrow(
        // () -> new IllegalArgumentException("Value not found"));
        // boolean anyMatch = user.getLoves().stream().anyMatch(v -> v.equals(shop));
        // if (anyMatch) {
        // user.getLoves().remove(shop);
        // } else {
        // user.getLoves().add(shop);
        // }

        // user = iUserRepository.save(user);

        User user = iUserRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Value not found"));
        List<Shop> loves = user.getLoves();
        Optional<Shop> findAny = loves.stream().filter(v -> v.getId() == shopId).findAny();
        if (findAny.isPresent()) {
            user.getLoves().remove(findAny.get());
        } else {
            Shop orElseThrow = iShopRepository.findById(shopId).orElseThrow(
                    () -> new IllegalArgumentException("Value not found"));
            user.getLoves().add(orElseThrow);
        }
        user = iUserRepository.save(user);
        return user != null;
    }

    @Override
    public List<Address> putUserAddress( int userId  ,List<Address> addresses) {

        User user = findById(userId);
        user.setAddresses(addresses);
        User save = iUserRepository.save(user);

        return save.getAddresses();
    }

    

}
