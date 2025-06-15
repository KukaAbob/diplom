package com.bazarweb.bazarweb.controller.Admin;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.UserDTO;
import com.bazarweb.bazarweb.service.User.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class UserManagerController {
    
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> userDTOs = userService.findAllUsers().stream()
            .map(UserDTO::fromEntity)
            .toList();
        return userDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(userDTOs);
    }
}
