package com.example.FakeCommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetProductResponseDto {
    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private String image;

    private BigDecimal rating;
}
