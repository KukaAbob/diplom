package com.bazarweb.bazarweb.DTO.Requests.Wishlist;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoveToCartRequest {
    private String email;
    private int productId;
}
