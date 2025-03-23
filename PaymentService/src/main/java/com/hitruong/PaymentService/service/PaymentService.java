package com.hitruong.PaymentService.service;

import com.hitruong.PaymentService.model.PaymentRequest;
import com.hitruong.PaymentService.model.PaymentResponse;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
