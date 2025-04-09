package com.bazarweb.bazarweb.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private int id;
    private String cardNumber;
    private Date expiryDate;
    private String cvvCode;
}
