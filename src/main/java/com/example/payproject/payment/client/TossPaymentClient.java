package com.example.payproject.payment.client;

import com.example.payproject.payment.application.dto.PaymentCommand;
import com.example.payproject.payment.client.dto.TossPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TossPaymentClient {
    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final RestTemplate restTemplate;
    //private final TossPaymentProperties properties;

    @Value("${payment.toss.secret-key}")
    private String secretKey ;

    public TossPaymentClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public TossPaymentResponse confirm(PaymentCommand command){
        log.info(secretKey);
        if(secretKey == null){
            throw new IllegalStateException("Toss secret key is not configured");
        }

        HttpHeaders headers = createHeaders();

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey",command.paymentKey());
        body.put("orderId",command.orderId());
        body.put("amount", command.amount());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try{
            return restTemplate.postForObject(CONFIRM_URL, entity, TossPaymentResponse.class);
        }catch(HttpStatusCodeException ex){
            HttpStatusCode statusCode = ex.getStatusCode();
            String responseBody = ex.getResponseBodyAsString();
            throw new IllegalStateException("Toss confirm failed (" + statusCode + ") :" +responseBody,ex);
        }
    }

    /**
     * 토스에 전달하는 헤더값
     */
    private HttpHeaders createHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = secretKey+ ":";
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " +encoded);
        return headers;
    }

}
