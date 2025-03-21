package com.bazarweb.bazarweb.DTO.Requests.Wishlist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveFromWishlistRequest {
    private String email;
    private int productId;
}
