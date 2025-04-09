package com.bazarweb.bazarweb.dto.Requests.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String email;
    private String username;
    private String password;
    private String phone;
}
