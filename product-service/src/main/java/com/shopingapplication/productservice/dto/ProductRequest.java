package com.shopingapplication.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;

    /*here used the annotation of lombok such as @AllArgsConstructor, @NoArgsConstructor, @Builder, @Data
    -to Create the getters,setters and constructor automatically instead of creating manually, */
}
