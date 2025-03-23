package com.hitruong.OrderService.service;

import com.hitruong.OrderService.model.OrderRequest;
import com.hitruong.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
