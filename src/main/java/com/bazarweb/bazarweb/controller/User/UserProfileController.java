package com.bazarweb.bazarweb.controller.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.AddressDto;
import com.bazarweb.bazarweb.dto.OrderDTO;
import com.bazarweb.bazarweb.dto.PaymentDto;
import com.bazarweb.bazarweb.dto.UserDTO;
import com.bazarweb.bazarweb.dto.Requests.Auth.UpdateUserRequest;
import com.bazarweb.bazarweb.enums.UserRole;
import com.bazarweb.bazarweb.model.User.User;
import com.bazarweb.bazarweb.service.User.UserService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id) {
        User user = userService.findById(id); // Получение сущности пользователя
        List<OrderDTO> orderDTOs = user.getOrders().stream()
            .map(order -> new OrderDTO(order.getId(), order.getAddress().getId(), order.getPayment().getId(), order.getDate(), order.getStatus(), order.getTotal(), order.isExecuted(), null))
            .collect(Collectors.toList());
        List<AddressDto> addressDtos = user.getAddress().stream()
            .map(address -> new AddressDto(address.getId(), address.getCity(), address.getCountry(), address.getZipCode(), address.getStreet(), address.getUser().getId()))
            .collect(Collectors.toList());
        List<PaymentDto> paymenDtos = user.getPayment().stream()
            .map(payment -> new PaymentDto(payment.getId(), payment.getCardNumber(), payment.getExpiryDate(), payment.getCvvCode()))
            .collect(Collectors.toList());
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), addressDtos, UserRole.USER, orderDTOs, paymenDtos);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserRequest profileDTO, Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }
        
        try {
            // Выводим информацию для отладки
            System.out.println("Updating user: " + principal.getName());
            System.out.println("Request data: " + profileDTO);
            
            userService.updateUserProfile(principal.getName(), profileDTO);
            
            return ResponseEntity.ok(Map.of(
                "message", "Profile updated successfully",
                "email", profileDTO.getEmail(),
                "phoneNumber", profileDTO.getPhone()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error updating profile: " + e.getMessage());
        }
    }
}
