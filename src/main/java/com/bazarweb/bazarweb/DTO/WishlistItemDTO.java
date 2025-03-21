package com.bazarweb.bazarweb.DTO;

import java.math.BigDecimal;

import com.bazarweb.bazarweb.enums.ProductStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistItemDTO {
    private int id;
    private int productId;
    private String productName;
    private BigDecimal price;
    private ProductStatus productStatus;
    
    public WishlistItemDTO(int id, int productId, String productName, BigDecimal price, 
                          ProductStatus productStatus) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.productStatus = productStatus;
    }
}
