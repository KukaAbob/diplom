package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;

import com.bazarweb.bazarweb.model.Product.Color;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.model.Product.Size;
import com.bazarweb.bazarweb.repository.Product.ColorRepository;
import com.bazarweb.bazarweb.repository.Product.SizeRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {
    private Integer id;
    private String color;
    private String size;
    private int stock;
    private BigDecimal price;
    private int productId;
    
    public static ProductVariantDTO fromEntity(ProductVariant variant) {
        return ProductVariantDTO.builder()
            .id(variant.getId())
            .color(variant.getColor().getName())
            .size(variant.getSize().getName())
            .stock(variant.getStock())
            .price(variant.getPrice())
            .productId(variant.getProduct().getId())
            .build();
    }
    
    // Этот метод нужно изменить или не использовать
    public ProductVariant toEntity() {
        ProductVariant variant = new ProductVariant();
        variant.setStock(this.stock);
        variant.setPrice(this.price);
        // НЕ устанавливаем color и size здесь, так как нужны репозитории
        return variant;
    }
    
    // Добавьте новый метод, который принимает репозитории
    public ProductVariant toEntity(ColorRepository colorRepository, SizeRepository sizeRepository) {
        ProductVariant variant = new ProductVariant();
        variant.setStock(this.stock);
        variant.setPrice(this.price);
        
        if (this.color != null) {
            Color color = colorRepository.findByName(this.color)
                .orElseThrow(() -> new RuntimeException("Цвет не найден: " + this.color));
            variant.setColor(color);
        }
        
        if (this.size != null) {
            Size size = sizeRepository.findByName(this.size)
                .orElseThrow(() -> new RuntimeException("Размер не найден: " + this.size));
            variant.setSize(size);
        }
        
        return variant;
    }
}