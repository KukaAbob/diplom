package com.bazarweb.bazarweb.dto.Requests.Wishlist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToWishlistRequest {
    private String email;
    private int productId;
}
