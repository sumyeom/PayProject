package com.example.payproject.payment.presentation.dto;

import com.example.payproject.payment.application.dto.PaymentFailCommand;

public record PaymentFailRequest(
        String orderId,
         String paymentKey,
         String errorCode,
         String errorMessage,
         Long amount,
         String rawPayload
) {
    public PaymentFailCommand toCommand(){
        return new PaymentFailCommand(orderId, paymentKey, errorCode,errorMessage,amount,rawPayload);
    }
}
