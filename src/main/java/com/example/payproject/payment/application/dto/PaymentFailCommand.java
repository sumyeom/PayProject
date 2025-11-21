package com.example.payproject.payment.application.dto;

public record PaymentFailCommand(
        String orderId,
        String paymentKey,
        String errorCode,
        String errorMessage,
        Long amount,
        String rawPayload
) {
}
