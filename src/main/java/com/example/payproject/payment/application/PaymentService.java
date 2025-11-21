package com.example.payproject.payment.application;

import com.example.payproject.common.ResponseEntity;
import com.example.payproject.payment.application.dto.PaymentCommand;
import com.example.payproject.payment.application.dto.PaymentFailCommand;
import com.example.payproject.payment.application.dto.PaymentFailureInfo;
import com.example.payproject.payment.application.dto.PaymentInfo;
import com.example.payproject.payment.client.TossPaymentClient;
import com.example.payproject.payment.client.dto.TossPaymentResponse;
import com.example.payproject.payment.domain.Payment;
import com.example.payproject.payment.domain.PaymentFailure;
import com.example.payproject.payment.domain.PaymentFailureRepository;
import com.example.payproject.payment.domain.PaymentRepository;
import com.example.payproject.payment.presentation.dto.PaymentFailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentFailureRepository failureRepository;
    private final TossPaymentClient tossPaymentClient;

    public ResponseEntity<List<PaymentInfo>> findAll(Pageable pageable){
        Page<Payment> page = paymentRepository.findAll(pageable);
        List<PaymentInfo> list = page.stream()
                .map(PaymentInfo::from)
                .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), list, page.getTotalElements());
    }

    public ResponseEntity<PaymentInfo> confirm(PaymentCommand command){
        TossPaymentResponse tossPayment = tossPaymentClient.confirm(command);
        Payment payment = Payment.create(
                tossPayment.paymentKey(),
                tossPayment.orderId(),
                tossPayment.totalAmount()
        );

        LocalDateTime approvedAt = tossPayment.approvedAt() != null ? tossPayment.approvedAt().toLocalDateTime() : null;
        LocalDateTime requestedAt = tossPayment.requestedAt() != null ? tossPayment.requestedAt().toLocalDateTime() : null;
        payment.markConfirmed(tossPayment.method(), approvedAt, requestedAt);

        Payment saved = paymentRepository.save(payment);
        return new ResponseEntity<>(HttpStatus.CREATED.value(), PaymentInfo.from(saved),1);
    }

    public ResponseEntity<PaymentFailureInfo> recordFailure(PaymentFailCommand command){
        PaymentFailure failure = PaymentFailure.from(
            command.orderId(),
            command.paymentKey(),
            command.errorCode(),
            command.errorMessage(),
            command.amount(),
            command.rawPayload()
        );

        PaymentFailure saved = failureRepository.save(failure);
        return new ResponseEntity<>(HttpStatus.OK.value(), PaymentFailureInfo.from(saved), 1);
    }
}
