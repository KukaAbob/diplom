package com.bazarweb.bazarweb.controller.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bazarweb.bazarweb.DTO.ProductDTO;
import com.bazarweb.bazarweb.DTO.ProductVariantDTO;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.service.Product.ProductService;
import com.bazarweb.bazarweb.service.Product.ProductVariantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductVariantService productVariantService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDtos = productService.getAllProducts().stream()
            .map(ProductDTO::fromEntity)
            .collect(Collectors.toList());

        return productDtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        
        ProductDTO productDetails = ProductDTO.fromEntity(product);

        return ResponseEntity.ok(productDetails);
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductVariantDTO>> getProductVariants(@PathVariable int productId) {
        List<ProductVariantDTO> variants = productVariantService.getVariantsByProductId(productId).stream()
            .map(ProductVariantDTO::fromEntity)
            .collect(Collectors.toList());

        return ResponseEntity.ok(variants);
    }

    @PostMapping("/variants")
    public ResponseEntity<ProductVariantDTO> addVariant(@RequestBody ProductVariantDTO variantDto) {
        ProductVariant variant = productVariantService.addProductVariant(variantDto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductVariantDTO.fromEntity(variant));
    }

    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<Void> deleteVariant(@PathVariable int variantId) {
        productVariantService.deleteProductVariant(variantId);
        return ResponseEntity.noContent().build();
    }

    // @PostMapping("/admin/create")
    // public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDto) {
    //     Product product = productDto.toEntity();
    //     Product savedProduct = productService.productCreate(product);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(ProductDTO.fromEntity(savedProduct));
    // }

    // @PutMapping("/admin/edit/{id}")
    // public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id, @RequestBody ProductDTO updatedProductDto) {
    //     Product updatedProduct = updatedProductDto.toEntity();
    //     Product savedProduct = productService.updateProduct(id, updatedProduct);
    //     return ResponseEntity.ok(ProductDTO.fromEntity(savedProduct));
    // }

    // @DeleteMapping("/admin/delete/{id}")
    // public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
    //     productService.deleteProduct(id);
    //     return ResponseEntity.noContent().build();
    // }
}
