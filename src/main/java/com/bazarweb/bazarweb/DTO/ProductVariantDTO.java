package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;

import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;

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
    private int id;
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

    public ProductVariant toEntity() {
        ProductVariant variant = new ProductVariant();
        variant.setId(this.id);
        variant.setStock(this.stock);
        variant.setPrice(this.price);

        Product product = new Product();
        product.setId(this.productId);
        variant.setProduct(product);

        return variant;
    }
}
