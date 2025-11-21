package com.example.payproject.payment.application.dto;

public record PaymentCommand(
        String paymentKey,
        String orderId,
        Long amount
) {
}
