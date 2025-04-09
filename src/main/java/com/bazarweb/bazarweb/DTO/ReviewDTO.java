package com.bazarweb.bazarweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private int userId;
    private int rating;
    private String comment;
}
