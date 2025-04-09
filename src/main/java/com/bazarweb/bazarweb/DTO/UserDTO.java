package com.bazarweb.bazarweb.dto;

import java.util.List;

import com.bazarweb.bazarweb.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
}
