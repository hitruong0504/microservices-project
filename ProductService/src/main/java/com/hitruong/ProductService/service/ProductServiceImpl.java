package com.hitruong.ProductService.service;


import com.hitruong.ProductService.entiry.Product;
import com.hitruong.ProductService.exception.ProductServiceCustomException;
import com.hitruong.ProductService.model.ProductRequest;
import com.hitruong.ProductService.model.ProductResponse;
import com.hitruong.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product " + productRequest.getName());

        Product product
                = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product Created.");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productID) {
        log.info("Getting product " + productID);

        Product product
                = productRepository.findById(productID)
                .orElseThrow(
                        () -> new ProductServiceCustomException("PRODUCT_NOT_FOUND", "Product not found")
                );

        ProductResponse productResponse
                = new ProductResponse();
        copyProperties(product, productResponse);

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce quantity {} for ID: {}", productId, quantity);
        Product product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "PRODUCT_NOT_FOUND",
                        "Product not found"
                ));
        if (product.getQuantity() < quantity){
            throw new ProductServiceCustomException(
                    "PRODUCT_NOT_ENOUGH_QUANTITY",
                    "Product does not have sufficient quantities"
            );
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity Reduced.");
    }
}
