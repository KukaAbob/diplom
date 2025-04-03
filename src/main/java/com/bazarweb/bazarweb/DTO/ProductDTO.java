package com.bazarweb.bazarweb.DTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.bazarweb.bazarweb.enums.Gender;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.bazarweb.bazarweb.model.Product.Product;

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
public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private ProductStatus productStatus;
    private List<ProductVariantDTO> variants;
    private Gender gender;

    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .category(product.getCategory().getName())
            .productStatus(product.getProductStatus())
            .variants(
                product.getVariants() != null
                    ? product.getVariants().stream()
                        .map(ProductVariantDTO::fromEntity)
                        .collect(Collectors.toList())
                    : List.of()
            )
            .gender(product.getGender())
            .build();
    }
}
