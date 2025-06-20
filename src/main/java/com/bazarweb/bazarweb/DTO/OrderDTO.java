package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.bazarweb.bazarweb.enums.OrderStatus;
import com.bazarweb.bazarweb.model.Order.Order;
import com.bazarweb.bazarweb.model.User.Address;
import com.bazarweb.bazarweb.model.User.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private int id;                    // Added missing order ID
    private int userId;
    private int addressId;
    private int paymentId;
    private LocalDateTime date;
    private OrderStatus status;
    private BigDecimal total;
    private boolean executed;
    private List<OrderItemDTO> orderItems;
    
    // Option 1: Remove these to avoid circular references (recommended)
    // private UserDTO user;
    // private Address address;
    
    // Option 2: If you need them, use simplified versions
    private String username;           // Instead of full UserDTO
    private String addressString;      // Instead of full Address
    
    public static OrderDTO fromEntity(Order order) {
        return OrderDTO.builder()
            .id(order.getId())                    // Fixed: use order ID, not user ID
            .userId(order.getUser().getId())      // Fixed: get actual user ID
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
            // Option 2 implementation:
            .username(order.getUser().getUsername())
            .addressString(order.getAddress().getStreet() + ", " + 
                          order.getAddress().getCity())
            .build();
    }
    
    // Alternative: Create a version without circular references
    public static OrderDTO fromEntitySimple(Order order) {
        return OrderDTO.builder()
            .id(order.getId())
            .userId(order.getUser().getId())
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