package com.example.FakeCommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder // We cannot use builder here because we are inheriting properties from GetProductResponseDto so both will have @SuperBuilder
public class GetProductWithDetailsResponseDto extends GetProductResponseDto {
    private String category;
}
