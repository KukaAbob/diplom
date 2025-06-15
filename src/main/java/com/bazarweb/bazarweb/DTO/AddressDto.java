package com.bazarweb.bazarweb.dto;

import com.bazarweb.bazarweb.model.User.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private int id;
    private String street;
    private String city;
    private int zipCode;
    private String country;
    private int userId;

    public static AddressDto fromEntity(Address address){
        return AddressDto.builder()
            .id(address.getId())
            .street(address.getStreet())
            .city(address.getCity())
            .zipCode(address.getZipCode())
            .country(address.getCountry())
            .userId(address.getUser().getId())
            .build();
    }
}
