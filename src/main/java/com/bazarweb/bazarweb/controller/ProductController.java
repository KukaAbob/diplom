package com.bazarweb.bazarweb.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bazarweb.bazarweb.DTO.ProductDetailsDto;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.model.User.Review;
import com.bazarweb.bazarweb.service.Product.ProductService;
import com.bazarweb.bazarweb.service.Product.ProductVariantService;
import com.bazarweb.bazarweb.service.User.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final ProductVariantService productVariantService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsDto> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        double averageRating = reviewService.calculateAverageRating(id);
        List<ProductVariant> variants = productVariantService.getVariantsByProductId(id);

        ProductDetailsDto productDetails = ProductDetailsDto.builder()
            .id(product.getId())
            .code(product.getCode())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .productStatus(product.getProductStatus())
            .category(product.getCategory().getName())
            .averageRating(averageRating)
            .reviews(reviews)
            .variants(variants) // Добавил размеры и цвета
            .build();

        return ResponseEntity.ok(productDetails);
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductVariant>> getProductVariants(@PathVariable int productId) {
        List<ProductVariant> variants = productVariantService.getVariantsByProductId(productId);
        return ResponseEntity.ok(variants);
    }

    @PostMapping("/variants")
    public ResponseEntity<ProductVariant> addVariant(@RequestBody ProductVariant variant) {
        Product savedProduct = productService.getProductById(variant.getProduct().getId());
        variant.setProduct(savedProduct); // Явно привязываем товар
        ProductVariant savedVariant = productVariantService.addProductVariant(variant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVariant);
    }

    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<Void> deleteVariant(@PathVariable int variantId) {
        productVariantService.deleteProductVariant(variantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.productCreate(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        Product savedProduct = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
