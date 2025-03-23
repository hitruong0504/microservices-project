package com.hitruong.ProductService.controller;

import com.hitruong.ProductService.model.ProductRequest;
import com.hitruong.ProductService.model.ProductResponse;
import com.hitruong.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PreAuthorize("hasAnyRole('Admin')")
    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest) {
        long productID = productService.addProduct(productRequest);
        return new ResponseEntity<>(productID, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long productID) {
        ProductResponse productResponse
                = productService.getProductById(productID);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") long productId,
            @RequestParam long quantity
    ){
        productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
