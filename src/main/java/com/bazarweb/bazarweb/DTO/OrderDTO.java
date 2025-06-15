package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.bazarweb.bazarweb.enums.OrderStatus;
import com.bazarweb.bazarweb.model.Order.Order;

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
    private List<OrderItemDTO> orderItems;

    public static OrderDTO fromEntity(Order order){
        return OrderDTO.builder()
            .userId(order.getId())
            .addressId(order.getAddress().getId())
            .paymentId(order.getPayment().getId())
            .date(order.getDate())
            .status(order.getStatus())
            .total(order.getTotal())
            .executed(order.isExecuted())
            .orderItems(
                order.getOrderItems() != null
                ? order.getOrderItems().stream()
                    .map(OrderItemDTO::fromEntity)
                    .collect(Collectors.toList())
                : List.of()
            )
            .build();
    }
}