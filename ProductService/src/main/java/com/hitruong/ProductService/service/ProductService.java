package com.hitruong.ProductService.service;

import com.hitruong.ProductService.model.ProductRequest;
import com.hitruong.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productID);

    void reduceQuantity(long productId, long quantity);
}
