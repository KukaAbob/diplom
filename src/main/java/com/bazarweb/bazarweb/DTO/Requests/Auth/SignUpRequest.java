package com.bazarweb.bazarweb.DTO.Requests.Auth;

import lombok.Data;

@Data
public class SignUpRequest {

    private String email;
    private String username;
    private String phone;
    private String password;
}
