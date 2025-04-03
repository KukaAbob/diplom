package com.bazarweb.bazarweb.DTO.Requests.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String phone;
}