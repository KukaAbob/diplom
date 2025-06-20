package com.bazarweb.bazarweb.dto.Requests.admin;

import java.math.BigDecimal;

import com.bazarweb.bazarweb.enums.Gender;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCreateRequest {
        @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("collection")
    private String collection;
    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("productStatus")
    private ProductStatus productStatus = ProductStatus.IN_STOCK;
    @JsonProperty("gender")
    private Gender gender;

        @Override
    public String toString() {
        return "ProductCreateRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", collection='" + collection + '\'' +
                ", categoryId=" + categoryId +
                ", productStatus=" + productStatus +
                ", gender=" + gender +
                '}';
    }
}