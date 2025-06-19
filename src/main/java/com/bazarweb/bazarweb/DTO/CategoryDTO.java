package com.bazarweb.bazarweb.dto;

import com.bazarweb.bazarweb.model.Catalog.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryDTO {
    private Integer id;
    private String name;
    
    public static CategoryDTO fromEntity(Category category) {
        return CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }
}