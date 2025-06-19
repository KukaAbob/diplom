package com.bazarweb.bazarweb.dto.Requests.admin;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String collection;
    private String gender; // Строка, будет конвертироваться в enum
    private String productStatus; // Строка, будет конвертироваться в enum
    private Integer categoryId; // Простой ID категории
    private List<ProductVariantUpdateRequest> variants;
}
