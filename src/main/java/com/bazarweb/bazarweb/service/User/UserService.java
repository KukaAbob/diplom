package com.bazarweb.bazarweb.service.User;

import com.bazarweb.bazarweb.configuration.EncryptionConfiguration;
import com.bazarweb.bazarweb.dto.Requests.Auth.UpdateUserRequest;
import com.bazarweb.bazarweb.model.User.User;
import com.bazarweb.bazarweb.repository.User.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionConfiguration encryptionConfiguration; // Внедряем бина из конфигурации

    public User userCreate(User user){
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(user);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getUserByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Оригинальный метод updateProfile остается для обратной совместимости
    public void updateProfile(String username, User updatedUser) {
        User existingUser = findByUsername(username);
        
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        
        if (updatedUser.getPhone() != null) {
            existingUser.setPhone(updatedUser.getPhone());
        }
        existingUser.setAddress(updatedUser.getAddress());
        
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encodedPassword = encryptionConfiguration.passwordEncoder().encode(updatedUser.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        
        userRepository.save(existingUser);
    }
    
    // Новый метод для работы с DTO
    public void updateUserProfile(String username, UpdateUserRequest profileDTO) {
        User existingUser = findByUsername(username);
        
        // Обновляем email если он предоставлен
        if (profileDTO.getEmail() != null && !profileDTO.getEmail().isEmpty()) {
            existingUser.setEmail(profileDTO.getEmail());
        }
        
        // Обновляем телефон если он предоставлен
        if (profileDTO.getPhone() != null) {
            existingUser.setPhone(profileDTO.getPhone());
            System.out.println("Setting phone number: " + profileDTO.getPhone());
        }
        
        // Сохраняем обновленного пользователя
        User savedUser = userRepository.save(existingUser);
        System.out.println("Saved user with phone: " + savedUser.getPhone());
    }

    public void deleteUser(String username) {
        User user = findByUsername(username);
        userRepository.delete(user);
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}