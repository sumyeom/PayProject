package com.example.payproject.product.presentation.dto;

import com.example.payproject.product.application.dto.ProductCommand;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 제품 API에서 쓰이는 요청 DTO.
 */
public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String status,
        String operatorId
) {

    public ProductCommand toCommand() {
        UUID operator = operatorId != null ? UUID.fromString(operatorId) : null;
        return new ProductCommand(name, description, price, stock, status, operator);
    }
}