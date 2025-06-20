package com.bazarweb.bazarweb.controller.Admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.ProductDTO;
import com.bazarweb.bazarweb.dto.Requests.admin.ProductCreateRequest;
import com.bazarweb.bazarweb.dto.Requests.admin.ProductUpdateRequest;
import com.bazarweb.bazarweb.enums.Gender;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.bazarweb.bazarweb.model.Catalog.Category;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.service.Product.ProductService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/dev/product")
@RequiredArgsConstructor
public class ProductManagerController {
    
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOs = productService.getAllProducts().stream()
            .map(ProductDTO::fromEntity)
            .toList();
        return productDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productDTOs);
    }


@PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest request) {
        try {
            // Логирование для отладки
            System.out.println("Получен запрос на создание продукта:");
            System.out.println("Raw request: " + request);
            System.out.println("Name: '" + request.getName() + "'");
            System.out.println("Name is null: " + (request.getName() == null));
            System.out.println("Name is empty: " + (request.getName() != null && request.getName().isEmpty()));
            System.out.println("Name after trim: '" + (request.getName() != null ? request.getName().trim() : "null") + "'");
            
            // Валидация с детальным логированием
            List<String> errors = new ArrayList<>();
            
            if (request.getName() == null) {
                errors.add("Поле name равно null");
            } else if (request.getName().trim().isEmpty()) {
                errors.add("Поле name пустое после trim");
            }
            
            if (request.getDescription() == null) {
                errors.add("Поле description равно null");
            } else if (request.getDescription().trim().isEmpty()) {
                errors.add("Поле description пустое после trim");
            }
            
            if (request.getPrice() == null) {
                errors.add("Поле price равно null");
            } else if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Цена должна быть больше нуля");
            }
            
            if (request.getCategoryId() == null) {
                errors.add("Поле categoryId равно null");
            }
            
            if (request.getGender() == null) {
                errors.add("Поле gender равно null");
            }
            
            if (request.getProductStatus() == null) {
                errors.add("Поле productStatus равно null");
            }
            
            if (!errors.isEmpty()) {
                System.out.println("Ошибки валидации: " + errors);
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибки валидации: " + String.join(", ", errors)));
            }

            // Создание продукта
            Product product = productService.createFromRequest(request);
            ProductDTO createdProductDTO = ProductDTO.fromEntity(product);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDTO);
            
        } catch (EntityNotFoundException e) {
            System.out.println("Категория не найдена: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Категория не найдена: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Внутренняя ошибка сервера: " + e.getMessage()));
        }
    }

    

@PutMapping("/update/{id}")
public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
    try {
        // Log received data
        System.out.println("Updating product ID: " + id);
        System.out.println("Received data: " + productDTO);
        
        if (productDTO == null) {
            return ResponseEntity.badRequest().body("Request body is empty");
        }

        // Set the ID from path variable
        productDTO.setId(id);
        
        // Update the product
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
        
    } catch (Exception e) {
        System.err.println("Error updating product: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error updating product: " + e.getMessage());
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
