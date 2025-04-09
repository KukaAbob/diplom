package com.bazarweb.bazarweb.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistDTO {
    private int id;
    private String userEmail;
    private List<WishlistItemDTO> items;
    
    public WishlistDTO(int id, String userEmail, List<WishlistItemDTO> items) {
        this.id = id;
        this.userEmail = userEmail;
        this.items = items;
    }
}
