package com.bazarweb.bazarweb.dto.Requests.Cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFromCartRequest {
    private String email;
    private Long productId;
}
