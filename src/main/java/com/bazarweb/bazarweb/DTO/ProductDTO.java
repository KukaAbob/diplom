package com.bazarweb.bazarweb.dto;

import java.math.BigDecimal;
import java.util.List;

import com.bazarweb.bazarweb.enums.Gender;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.bazarweb.bazarweb.model.Catalog.Category;
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
public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String collection;
    private Category category;
    private ProductStatus productStatus;
    private List<ProductVariantDTO> variants;
    private Gender gender;

    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .collection(product.getCollection())
            .category(product.getCategory())
            .productStatus(product.getProductStatus())
            .variants(
                product.getVariants() != null
                    ? product.getVariants().stream()
                        .map(ProductVariantDTO::fromEntity)
                        .toList()
                    : List.of()
            )
            .gender(product.getGender())
            .build();
    }

    public Product toEntity(){
        Product product = new Product();
        product.setId(this.id);
        product.setGender(gender);
        product.setName(name);
        product.setPrice(price);
        product.setProductStatus(productStatus);
        product.setDescription(description);
        product.setCategory(category);
        product.setCollection(collection);

        if (this.variants != null && !this.variants.isEmpty()) {
        List<ProductVariant> productVariants = this.variants.stream()
            .map(variantDTO -> {
                ProductVariant variant = variantDTO.toEntity();
                variant.setProduct(product);
                return variant;
            })
            .toList();
        product.setVariants(productVariants);
        }
        return product;
    }
}
