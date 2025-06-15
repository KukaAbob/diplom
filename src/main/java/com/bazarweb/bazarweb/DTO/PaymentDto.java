package com.bazarweb.bazarweb.dto;

import java.util.Date;

import com.bazarweb.bazarweb.model.User.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentDto {
    private int id;
    private String cardNumber;
    private Date expiryDate;
    private String cvvCode;

    public static PaymentDto fromEntity(Payment payment){
        return PaymentDto.builder()
            .id(payment.getId())
            .cardNumber(payment.getCardNumber())
            .expiryDate(payment.getExpiryDate())
            .cvvCode(payment.getCvvCode())
            .build();
    }
}
