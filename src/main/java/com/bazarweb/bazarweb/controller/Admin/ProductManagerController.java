package com.bazarweb.bazarweb.controller.Admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.ProductDTO;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.service.Product.ProductService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product productEntity = productDTO.toEntity();

            Product product = productService.productCreate(productEntity);
            ProductDTO createdProductDTO = ProductDTO.fromEntity(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
