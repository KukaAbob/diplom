package com.bazarweb.bazarweb.DTO.Requests.Auth;

import lombok.Data;

@Data
public class SignInRequest {

    private String email;
    private String password;
}
