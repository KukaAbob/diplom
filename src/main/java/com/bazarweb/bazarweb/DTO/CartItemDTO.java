package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemDTO {
    private int id;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private int productId;
}