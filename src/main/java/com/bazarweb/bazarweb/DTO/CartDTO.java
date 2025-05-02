package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartDTO {
    private int id;
    private String email;
    private BigDecimal totalPrice;
    private List<CartItemDTO> items;
}