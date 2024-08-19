package com.order_lunch.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.order_lunch.entity.AddMeals;
import com.order_lunch.entity.AddMealsDetail;
import com.order_lunch.entity.Product;
import com.order_lunch.entity.Shop;
import com.order_lunch.model.request.AddMealsRequest;
import com.order_lunch.repository.IAddMealsDetailRepository;
import com.order_lunch.repository.IAddMealsRepository;
import com.order_lunch.service.IAddMealsService;
import com.order_lunch.service.IProductService;
import com.order_lunch.service.IShopService;
import com.order_lunch.service.IUserService;

@Service
public class AddMealsService implements IAddMealsService {
    @Autowired
    IAddMealsRepository iAddMealsRepository;
    @Autowired
    IAddMealsDetailRepository iAddMealsDetailRepository;
    @Autowired
    IUserService iUserService;
    @Autowired
    IShopService iShopService;
    @Autowired
    IProductService iProductService;

    @Override
    public Set<AddMeals> getAddMeals(int userId, int shopId) {

        Set<AddMeals> addMealsSet = iAddMealsRepository.findByShopIdAndShopUserId(shopId, userId);

        return addMealsSet;
    }

    @Transactional
    @Override
    public boolean putAddMeals(int userId, int addMealsId, AddMealsRequest addMealsProductRequest) {

        AddMeals addMeals = findAddMeals(userId, addMealsId);
        Shop shop = addMeals.getShop();

        checkAddMealsDetailProduct(shop, addMealsProductRequest);

        addMeals.setName(addMealsProductRequest.getName());
        addMeals.setShelve(addMealsProductRequest.isShelve());
        List<AddMealsDetail> addMealsDetails = addMeals.getAddMealsDetails();
        for(int i=0;addMealsDetails.size()>i;i++ ){
            // @Modifying 
            // iAddMealsDetailRepository.delete(addMealsDetails.get(i));
            // Product product = addMealsDetails.get(i).getProduct();
            // AddMealsDetail addMealsDetail = addMealsDetails.get(i);
            // addMealsDetail.remove(addMealsDetail.getProduct());
            // addMealsDetail.se
            
        }


        iAddMealsDetailRepository.deleteAllByAddMeals_id(addMeals.getId());
        addMeals.getAddMealsDetails().removeAll(addMealsDetails);


        // 
        // addMealsDetails.stream().forEach(v->
        // iAddMealsDetailRepository.deleteAll(v));
        for(int i=0;addMealsDetails.size()>i;i++ ){
            // @Modifying 
            iAddMealsDetailRepository.delete(addMealsDetails.get(i));
        }
        
        // iAddMealsDetailRepository.deleteAll(addMealsDetails);


        // @Modifying
        // iAddMealsDetailRepository.deleteAll(addMealsDetails);
        // addMeals.setAddMealsDetails(addMealsDetails);

        // Shop shop = orElseThrow.getShop();
        // shop.getTabs().remove(orElseThrow);
        // iTabRepository.delete(orElseThrow);


        // addMeals.setAddMealsDetails(null);
        // List<AddMealsDetail> addMealsDetails = addMeals.getAddMealsDetails();
        // addMealsDetails.stream().forEach(v->{
        //     iAddMealsDetailRepository.delete(v);
        //     // iAddMealsDetailRepository.save(v);
        // });
        // addMeals.setAddMealsDetails(new ArrayList<>());
        // iAddMealsDetailRepository.deleteAll(addMeals.getAddMealsDetails());
        addMeals.setShop(shop);

        iAddMealsRepository.save(addMeals);

        List<AddMealsDetail> collect = addMealsProductRequest.getAddProducts().stream()
                .map(v -> new AddMealsDetail(addMeals, iProductService.getProductById(v.getProductId()), v.getPrice()))
                .collect(Collectors.toList());

        addMeals.setAddMealsDetails(collect);
        iAddMealsRepository.save(addMeals);

        // List<AddMealsDetail> insertAddMealsDetails = saveAddMealsDetail(addMeals,
        // addMealsProductRequest);
        // return insertAddMealsDetails.size() !=
        // addMealsProductRequest.getAddProducts().size();

        // saveAddMealsDetail(addMeals,
        //         addMealsProductRequest);

        AddMeals addMeals2 = findAddMeals(userId, addMeals.getId());

        return addMeals2.getAddMealsDetails().size() == addMealsProductRequest.getAddProducts().size();

    }

    @Transactional
    @Override
    public boolean addAddMealsByShopId(int userId, AddMealsRequest addMealsProductRequest) {

        Shop shop = iShopService.getShop(userId, addMealsProductRequest.getShopId());

        checkAddMealsDetailProduct(shop, addMealsProductRequest);

        AddMeals addMeals = new AddMeals(shop, addMealsProductRequest);
        addMeals = iAddMealsRepository.save(addMeals);
        if (addMeals.getId() == null) {

            throw new DataIntegrityViolationException("Unimplemented method 'addAddMealsByShopId'");
        }

        ArrayList<AddMealsDetail> arrayList = new ArrayList<AddMealsDetail>();
        for (AddMealsRequest.AddProduct v : addMealsProductRequest.getAddProducts()) {

            Product product = iProductService.getProductById(v.getProductId());
            AddMealsDetail addMealsDetail = new AddMealsDetail(addMeals, product, v.getPrice());
            arrayList.add(addMealsDetail);
        }

        List<AddMealsDetail> saveAll = iAddMealsDetailRepository.saveAll(arrayList);

        return saveAll.size() == addMealsProductRequest.getAddProducts().size();

    }

    @Override
    public boolean deleteAddMeals(int userId, int addMealsId) {

        findAddMeals(userId, addMealsId);

        iAddMealsRepository.deleteById(addMealsId);
        Optional<AddMeals> byId = iAddMealsRepository.findByIdAndShopUserId(addMealsId, userId);
        return !byId.isPresent();
    }

    public AddMeals findAddMeals(int userId, int addMealsId) {

        Optional<AddMeals> byId = iAddMealsRepository.findByIdAndShopUserId(addMealsId, userId);
        AddMeals addMeals = byId
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AddMeals.class.getName()));

        return addMeals;
    }

    void checkAddMealsDetailProduct(Shop shop, AddMealsRequest addMealsProductRequest) {

        List<Integer> productIds = addMealsProductRequest.getProductIds();
        List<Product> result = shop.getProducts().stream()
                .filter(product -> productIds.contains(product.getId()))
                .collect(Collectors.toList());

        if (addMealsProductRequest.getAddProducts().size() != result.size()) {
            throw new UnsupportedOperationException("Unimplemented method 'setAddMealsByShopId'");
        }
    }

    void saveAddMealsDetail(AddMeals addMeals, AddMealsRequest addMealsProductRequest) {

        // List<AddMealsDetail> priceList =
        // addMealsProductRequest.getAddProducts().stream()
        // .map(v -> new AddMealsDetail(addMeals,
        // iProductService.getProductById(v.getProductId()), v))
        // .collect(Collectors.toList());

        // List<AddMealsDetail> priceList =
        // addMealsProductRequest.getAddProducts().stream()
        // .map(v -> iAddMealsDetailRepository.insertAddMealsDetails(addMeals.getId(),
        // v.getProductId(),
        // v.getPrice()))
        // .collect(Collectors.toList());

        // addMealsProductRequest.getAddProducts().stream()
        // .forEach(v ->
        // iAddMealsDetailRepository.insertAddMealsDetails(addMeals.getId(),
        // v.getProductId(),
        // v.getPrice()));

    }

}
