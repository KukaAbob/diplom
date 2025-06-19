package com.bazarweb.bazarweb.dto.Requests.admin;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductVariantUpdateRequest {
    private Integer id; // null для новых вариантов
    private Integer colorId;
    private Integer sizeId;
    private Integer stock;
    private BigDecimal price;
}
