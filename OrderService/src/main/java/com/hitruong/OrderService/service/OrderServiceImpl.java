package com.hitruong.OrderService.service;

import com.hitruong.OrderService.entity.Order;
import com.hitruong.OrderService.exception.CustomException;
import com.hitruong.OrderService.external.client.PaymentService;
import com.hitruong.OrderService.external.client.ProductService;
import com.hitruong.OrderService.external.request.PaymentRequest;
import com.hitruong.OrderService.external.response.PaymentResponse;
import com.hitruong.OrderService.model.OrderRequest;
import com.hitruong.OrderService.model.OrderResponse;
import com.hitruong.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        /*
        * 1. Order entity -> save the data with status order created
        * 2. Product service -> block products(reduce the quantity)
        * 3. Payment service -> payment -> success -> completed, else -> cancelled
        */

        log.info("Placing order: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("Creating Order with Status CREATED");

        Order order
                = Order.builder()
                . amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);
        log.info("Calling Payment Service to complete Order");
        PaymentRequest paymentRequest
                = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment completed. Changing the Order status to PLACED.");
            orderStatus = "PLACED";
        }catch(Exception e) {
            log.info("Payment failed. Changing the Order status to FAILED.");
            orderStatus = "FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Placed Successfully with Order ID: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Retrieving Order Details for Order ID: {}", orderId);
        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", "ORDER_NOT_FOUND", 404));

        log.info("Invoking Product Service to fetch the product for id: {}", order.getProductId());

        OrderResponse.ProductDetails productDetails
                = restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                OrderResponse.ProductDetails.class
        );

        log.info("Getting payment information from the payment service");

        PaymentResponse paymentResponse
                = restTemplate.getForObject(
                        "http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class
        );

        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentStatus(paymentResponse.getPaymentStatus())
                .build();

        OrderResponse orderResponse
                = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }
}
