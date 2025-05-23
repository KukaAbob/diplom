package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    private int productId;    // ID товара
    private int quantity;     // количество
    private BigDecimal price; // цена за единицу
}