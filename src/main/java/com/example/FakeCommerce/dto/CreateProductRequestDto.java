package com.example.FakeCommerce.dto;

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

    public String title;

    public String description;

    public BigDecimal price;

    public String image;

    public String category;

    public String rating;
}
