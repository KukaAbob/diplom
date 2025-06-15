package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;

import com.bazarweb.bazarweb.model.Order.OrderItem;

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

    public static OrderItemDTO fromEntity(OrderItem orderItem){
        return OrderItemDTO.builder()
            .productId(orderItem.getId())
            .quantity(orderItem.getQuantity())
            .price(orderItem.getPrice())
            .build();
    }
}