package com.order_lunch.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.AddressData;
import com.order_lunch.entity.Category;
import com.order_lunch.entity.FileData;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.ShopAddress;
import com.order_lunch.entity.User;
import com.order_lunch.model.request.BackstageShopAddRequest;
import com.order_lunch.model.request.BackstageShopPutRequest;
import com.order_lunch.model.request.ShopRequest;
import com.order_lunch.model.request.ShopSearchRequest;
import com.order_lunch.model.response.BackstageShopResponse;
import com.order_lunch.model.response.ShopResponse;
import com.order_lunch.repository.ICategoryRepository;
import com.order_lunch.repository.IFileDateRepository;
import com.order_lunch.repository.IShopAddressRepository;
import com.order_lunch.repository.IShopRepository;
import com.order_lunch.repository.IUserRepository;
import com.order_lunch.service.IAddressDataService;
import com.order_lunch.service.IShopService;

@Service
public class ShopService implements IShopService {

    @Autowired
    IShopRepository iShopRepository;
    @Autowired
    IFileDateRepository iFileDateRepository;
    @Autowired
    IShopAddressRepository iShopAddressRepository;
    @Autowired
    IAddressDataService iAddressDataService;
    @Autowired
    AddressService addressService;
    @Autowired
    ShopAddressService shopAddressService;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    ICategoryRepository iCategoryRepository;

    // @Lazy
    // @Autowired
    // UserService userService;

    @Override
    public Page<ShopResponse> findShops(ShopSearchRequest shopRequest, Pageable pageable) {

        Page<Shop> shopPage = iShopRepository.findByShopAddress_CityAndShopAddress_AreaAndCategory_IdAndCategory_name(
                shopRequest.getCity(),
                shopRequest.getArea(), shopRequest.getCategoryId(), shopRequest.getOther(), pageable);

        return shopPage.map(v -> new ShopResponse(v));

    }

    @Override
    public Page<ShopResponse> findShops(User user, ShopSearchRequest shopRequest, Pageable pageable) {

        Page<Shop> shopPage = null;

        
        if (user.getAddressDelivery() != null) {
            Address addressDelivery = user.getAddressDelivery();

            // Address userAddress = addressService.getUserAddress(userId, shopRequest.getUserAddressId());

            shopPage = iShopRepository.findBy(addressDelivery.getLat(), addressDelivery.getLng(),
                    shopRequest.getCategoryId(), shopRequest.getOther(), pageable);
        } else {
            shopPage = iShopRepository.findByShopAddress_CityAndShopAddress_AreaAndCategory_IdAndCategory_name(
                    shopRequest.getCity(),
                    shopRequest.getArea(), shopRequest.getCategoryId(), shopRequest.getOther(), pageable);
        }

        return shopPage.map(v -> new ShopResponse(v));

    }

    @Override
    public List<Shop> findShopsByName(String name) {
        return iShopRepository.findFirst6ByNameLikeAndIsDeleteIsFalse("%" + name + "%");
    }

    @Override
    public Shop getShopById(int id) {

        Optional<Shop> findById = iShopRepository.findById(id);
        if (!findById.isPresent()) {
            return null;
        }
        return findById.get();
    }

    @Override
    public Page<BackstageShopResponse> findShopsForAdmin(ShopSearchRequest shopRequest, Pageable pageable) {

        Page<Shop> shopPage = iShopRepository.findByShopAddress_CityAndShopAddress_AreaAndCategory_IdAndCategory_name(
                shopRequest.getCity(),
                shopRequest.getArea(), shopRequest.getCategoryId(), shopRequest.getOther(), pageable);

        return shopPage.map(v -> new BackstageShopResponse(v));

    }

    @Override
    @Transactional
    public Shop addShop(ShopRequest shopRequest,User user) {
        // User user = userService.findById(userId);

        Shop shop = new Shop(shopRequest);
        shop.setUser(user);
        AddressData addressData = iAddressDataService.getAddressData(shopRequest.getAddressId());
        ShopAddress address = shopAddressService.addAddress(addressData, shopRequest.getAddressDetail());

        shop.setShopAddress(address);
        Shop save = iShopRepository.save(shop);
        return save;
    }

    @Transactional
    @Override
    public boolean  putShop(BackstageShopPutRequest shopPutRequest) {

        Optional<Shop> shopOptional = iShopRepository.findById(shopPutRequest.getId());
        if (!shopOptional.isPresent()) {
            throw new NullPointerException();
        }

        Optional<ShopAddress> addressOptional = iShopAddressRepository.findById(shopPutRequest.getAddress().getId());
        if (!addressOptional.isPresent()) {
            throw new NullPointerException();
        }

        Optional<FileData> fileDataOptional = iFileDateRepository.findById(shopPutRequest.getImgId());
        // if (!findById3.isPresent()) {
        // throw new NullPointerException();
        // }

        ShopAddress shopAddress = addressOptional.get();

        // shopAddress.setAddress(shopPutRequest.getAddress());

        ShopAddress newShopAddress = shopAddressService.putShopAddress(shopAddress, shopPutRequest.getAddress());

        ShopAddress address = iShopAddressRepository.save(newShopAddress);
        Shop shop = shopOptional.get();

        List<Category> categoryList = iCategoryRepository.getCategoryByIdIn(shopPutRequest.getCategory());
        // public void setShop(BackstageShopPutRequest shopPutRequest, ShopAddress
        // shopAddress, FileData fileData) {

        if (!fileDataOptional.isPresent()) {
            shop.setShop(shopPutRequest, address ,categoryList);
        } else {

            shop.setShop(shopPutRequest, address, categoryList,fileDataOptional.get());
        }

        iShopRepository.save(shop);

        return true;

    }

    @Transactional
    @Override
    public boolean addShop(BackstageShopAddRequest shopAddRequest) {

        Optional<FileData> findById3 = iFileDateRepository.findById(shopAddRequest.getImgId());
        if (!findById3.isPresent()) {
            throw new NullPointerException();
        }
        Optional<User> findById = iUserRepository.findByAccount(shopAddRequest.getAccount());
        if (!findById.isPresent()) {
            throw new NullPointerException();
        }

        ShopAddress address = new ShopAddress(shopAddRequest.getAddress());
        ShopAddress save = iShopAddressRepository.save(address);

        Shop shop = new Shop(shopAddRequest, save, findById3.get(), findById.get());

        return iShopRepository.save(shop) != null;
    }

    @Override
    public Set<Shop> findShopsLim() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findShopsLim'");
    }

    @Override
    public List<Shop> getShopsByUserId(int id) {
        return iShopRepository.getShopsByUserIdAndIsDeleteIsFalse(id);
    }

    @Override
    public Shop getShop(int UserId, int ShopId) {
        Optional<Shop> shopsByIdAndUserId = iShopRepository.getShopsByIdAndUserIdAndIsDeleteIsFalse(ShopId, UserId);
        Shop orElseThrow = shopsByIdAndUserId
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
        return orElseThrow;
    }

    @Override
    public boolean deleteShop(int id) {
        Optional<Shop> shopsByIdAndUserId = iShopRepository.findById(id);
        Shop shop = shopsByIdAndUserId
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

        shop.setIsDelete(true);
        Shop save = iShopRepository.save(shop);
        return save.isDelete();
    }

    private static final double EARTH_RADIUS = 6371.0;
    
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度转换为弧度
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    @Override
    public boolean existsByName(String name) {
        return iShopRepository.existsByName(name);
    }






}
