package com.example.payproject.payment.infrastructure;

import com.example.payproject.payment.domain.PaymentFailure;
import com.example.payproject.payment.domain.PaymentFailureRepository;
import com.example.payproject.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentFailureRepositoryAdaptor implements PaymentFailureRepository {
    private final PaymentFailureJpaRepository failureJpaRepository;
    @Override
    public PaymentFailure save(PaymentFailure failure) {
        return failureJpaRepository.save(failure);
    }
}
