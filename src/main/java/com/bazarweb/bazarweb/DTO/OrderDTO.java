package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.bazarweb.bazarweb.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private int userId;
    private int addressId;
    private int paymentId;
    private LocalDateTime date;
    private OrderStatus status;
    private BigDecimal total;
    private boolean executed;
    private List<OrderItemDTO> orderItems;  // список товаров
}