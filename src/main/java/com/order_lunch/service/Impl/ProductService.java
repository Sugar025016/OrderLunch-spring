package com.order_lunch.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.order_lunch.entity.FileData;
import com.order_lunch.entity.Product;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.Tab;
import com.order_lunch.model.request.BackstageProductAddRequest;
import com.order_lunch.model.request.BackstageProductPutRequest;
import com.order_lunch.model.request.SellProductRequest;
import com.order_lunch.model.response.BackstageProductResponse;
import com.order_lunch.repository.IFileDateRepository;
import com.order_lunch.repository.IProductRepository;
import com.order_lunch.repository.IShopRepository;
import com.order_lunch.repository.ITabRepository;
import com.order_lunch.service.IProductService;

@Service
@Transactional
public class ProductService implements IProductService {

    @Autowired
    IProductRepository iProductRepository;
    @Autowired
    IShopRepository iShopRepository;
    @Autowired
    IFileDateRepository iFileDateRepository;
    @Autowired
    FileService fileService;
    @Autowired
    ShopService iShopService;
    @Autowired
    ITabRepository iTabRepository;

    @Override
    public List<Product> findAll() {
        // TODO Auto-generated method stub
        return iProductRepository.findAll();
    }

    @Override
    public Product getProductById(int id) {
        Optional<Product> findById = iProductRepository.findById(id);
        if (!findById.isPresent()) {
            return null;
        }
        return findById.get();
    }

    @Transactional
    @Override
    public boolean deleteProductById(int productId, int userId) {
        Optional<Product> productOptional = iProductRepository.findByIdAndShopUserId(productId, userId);

        Product product = productOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        product.setDelete(true);

        Product save = iProductRepository.save(product);

        return save.isDelete();
    }

    @Override
    public List<Product> getProductsByShopId(int shopId) {
        return iProductRepository.getAllByShop_id(shopId);
    }

    @Transactional
    @Override
    public Page<BackstageProductResponse> getByPage(Integer shopId, Pageable pageable) {
        return iProductRepository.findAllByShopIdAndDeleteIsFalse(shopId, pageable);
    }

    @Transactional
    @Override
    public boolean putShop(BackstageProductPutRequest productPutRequest) {

        Optional<Product> findById2 = iProductRepository.findById(productPutRequest.getId());
        if (!findById2.isPresent()) {
            throw new NullPointerException();
        }

        Product product = findById2.get();

        product.setProduct(productPutRequest);

        if (productPutRequest.getImgId() != null) {
            Optional<FileData> findById3 = iFileDateRepository.findById(productPutRequest.getImgId());
            if (!findById3.isPresent()) {
                throw new NullPointerException();
            }
            product.setFileData(findById3.get());
        }

        iProductRepository.save(product);

        return true;
    }

    @Transactional
    @Override
    public boolean addProduct(BackstageProductAddRequest productAddRequest) {

        Product product = new Product(productAddRequest);

        Optional<Shop> findById = iShopRepository.findById(productAddRequest.getShopId());
        if (!findById.isPresent()) {
            throw new NullPointerException();
        }

        Optional<Tab> tab = iTabRepository.findById(productAddRequest.getTabId());
        if (!tab.isPresent()) {
            throw new NullPointerException();
        }

        // product.setTab(tab.get());

        if (productAddRequest.getImgId() != null) {
            FileData fileById = fileService.getFileById(productAddRequest.getImgId());
            product.setFileData(fileById);
        }

        iProductRepository.save(product);
        return true;
    }

    @Transactional
    @Override
    public boolean setOrderable(int productId, boolean isOrderable, int userId) {
        Optional<Product> productOptional = iProductRepository.findByIdAndShopUserId(productId, userId);

        Product product = productOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        product.setOrderable(isOrderable);

        iProductRepository.save(product);
        return !(iProductRepository.save(product)==null);
    }

    @Override
    public List<Product> getProductsByShopIdAndUserID(int shopId, int userId) {

        List<Product> productOptional = iProductRepository.getProductByShopIdAndShopUserIdAndIsDeleteIsFalse(shopId, userId);
        return productOptional;
    }


    @Transactional
    @Override
    public boolean putSellProduct(SellProductRequest sellProductRequest ,int productId,int userId) {

        Optional<Product> findById2 = iProductRepository.findByIdAndShopUserIdAndIsDeleteIsFalse(productId,userId);
        Product product = findById2.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        product.setSellProduct(sellProductRequest);

        if (sellProductRequest.getImgId() != null) {
            FileData fileById = fileService.getFileById(sellProductRequest.getImgId());
            product.setFileData(fileById);
        }

        Product save = iProductRepository.save(product);

        return save.getId()!=null;
    }

    @Transactional
    @Override
    public boolean addSellProduct(SellProductRequest sellProductRequest,int shopId,int userId) {
        
        Shop shopByUserId = iShopService.getShopByUserId(userId, shopId);

        Product product = new Product(sellProductRequest);

        product.setShop(shopByUserId);

        if (sellProductRequest.getImgId() != null) {
            FileData fileById = fileService.getFileById(sellProductRequest.getImgId());
            product.setFileData(fileById);
        }

        Product save = iProductRepository.save(product);

        return save.getId()!=null;
    }
}
