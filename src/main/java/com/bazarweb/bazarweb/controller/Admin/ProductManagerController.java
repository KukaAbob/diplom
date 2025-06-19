package com.bazarweb.bazarweb.controller.Admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.ProductDTO;
import com.bazarweb.bazarweb.dto.Requests.admin.ProductUpdateRequest;
import com.bazarweb.bazarweb.enums.Gender;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.bazarweb.bazarweb.model.Catalog.Category;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.service.Product.ProductService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    // @PostMapping("/create")
    // public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
    //     try {
    //         Product product = productService.productCreate(productDTO);
    //         ProductDTO createdProductDTO = ProductDTO.fromEntity(product);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDTO);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

@PostMapping("/create")
public ResponseEntity<?> createProduct(@RequestBody Product product, HttpServletRequest request) {
    try {
        // Детальное логирование
        System.out.println("=== DETAILED LOGGING ===");
        System.out.println("Content-Type: " + request.getContentType());
        System.out.println("Request Method: " + request.getMethod());
        
        // Логируем сам объект
        System.out.println("Received Product object: " + product);
        System.out.println("Product class: " + product.getClass().getName());
        
        // Детально проверяем каждое поле
        System.out.println("=== FIELD BY FIELD ===");
        System.out.println("Name: '" + product.getName() + "' (length: " + 
            (product.getName() != null ? product.getName().length() : "null") + ")");
        System.out.println("Name equals 'null'? " + "null".equals(product.getName()));
        System.out.println("Name is null? " + (product.getName() == null));
        
        System.out.println("Description: '" + product.getDescription() + "' (length: " + 
            (product.getDescription() != null ? product.getDescription().length() : "null") + ")");
        System.out.println("Description equals 'null'? " + "null".equals(product.getDescription()));
        
        System.out.println("Price: " + product.getPrice());
        System.out.println("Collection: '" + product.getCollection() + "'");
        System.out.println("Category: " + product.getCategory());
        System.out.println("Gender: " + product.getGender());
        System.out.println("Status: " + product.getProductStatus());
        System.out.println("Variants: " + product.getVariants());
        
        // Проверяем и очищаем данные от строк "null"
        if ("null".equals(product.getName())) {
            System.out.println("Converting string 'null' to actual null for name");
            product.setName(null);
        }
        if ("null".equals(product.getDescription())) {
            System.out.println("Converting string 'null' to actual null for description");
            product.setDescription(null);
        }
        if ("null".equals(product.getCollection())) {
            System.out.println("Converting string 'null' to actual null for collection");
            product.setCollection(null);
        }
        
        System.out.println("=== AFTER CLEANUP ===");
        System.out.println("Name after cleanup: '" + product.getName() + "'");
        System.out.println("Description after cleanup: '" + product.getDescription() + "'");
        
        Product savedProduct = productService.productCreate(product);
       
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    } catch (RuntimeException e) {
        System.out.println("RuntimeException caught: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
        System.out.println("General exception caught: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Internal server error: " + e.getMessage()));
    }
}

@PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, 
                                         @RequestBody ProductUpdateRequest request) {
        System.out.println("=== ОБНОВЛЕНИЕ ПРОДУКТА ID: " + id + " ===");
        System.out.println("Данные: " + request.getName());
        
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, request);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Ошибка выполнения: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Внутренняя ошибка сервера");
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
