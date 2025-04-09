package com.bazarweb.bazarweb.service.Admin;

import com.bazarweb.bazarweb.JWT.JwtService;
import com.bazarweb.bazarweb.dto.Requests.Auth.JwtAuthenticationResponse;
import com.bazarweb.bazarweb.dto.Requests.Auth.SignInRequest;
import com.bazarweb.bazarweb.dto.Requests.Auth.SignUpRequest;
import com.bazarweb.bazarweb.enums.UserRole;
import com.bazarweb.bazarweb.model.User.User;
import com.bazarweb.bazarweb.service.User.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(UserRole.USER)
                        .build();

        userService.userCreate(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
    
        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());
    
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
    
}
