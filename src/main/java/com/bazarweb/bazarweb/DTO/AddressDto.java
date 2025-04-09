package com.bazarweb.bazarweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private int id;
    private String street;
    private String city;
    private int zipCode;
    private String country;
    private int userId;
}
