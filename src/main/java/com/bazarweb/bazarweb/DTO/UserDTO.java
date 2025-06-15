package com.bazarweb.bazarweb.dto;

import java.util.List;

import com.bazarweb.bazarweb.enums.UserRole;
import com.bazarweb.bazarweb.model.User.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String username;
    private String email;
    private String phone;
    private List<AddressDto> address;
    private UserRole role;
    private List<OrderDTO> orders;
    private List<PaymentDto> payment;

    public static UserDTO fromEntity(User user){
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .phone(user.getPhone())
            .address(
                user.getAddress() != null
                    ? user.getAddress().stream()
                        .map(AddressDto::fromEntity)
                        .toList()
                    : List.of()
            )
            .role(user.getRole())
            .orders(
                user.getOrders() != null
                    ? user.getOrders().stream()
                        .map(OrderDTO::fromEntity)
                        .toList()
                    : List.of()
            )
            .payment(
                user.getPayment() != null
                    ? user.getPayment().stream()
                        .map(PaymentDto::fromEntity)
                        .toList()
                    : List.of()
            )
            .build();
    }
}
