package com.example.FakeCommerce.dto;

import com.example.FakeCommerce.schema.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequestDto {

    private String title;

    private String description;

    private BigDecimal price;

    private String image;

    private Long categoryId;

    private BigDecimal rating;
}
