package com.hitruong.OrderService.external.response;

import com.hitruong.OrderService.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private long paymentId;
    private PaymentMode paymentMode;
    private String paymentStatus;
    private long amount;
    private long orderId;
    private Instant paymentDate;
}
