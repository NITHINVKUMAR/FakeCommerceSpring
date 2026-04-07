package com.example.FakeCommerce.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity  /* Marks this class as a JPA entity (mapped to a database table) if we dont add this then we cannot
create table for this class, NOT map it to the database,NOT allow CRUD operations via repositories */
@Table(name = "products") //This is the table name
public class Product {
    @Id // to mark the field as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This is to autoincrement the value specially for mysql
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "Text")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String image;

    private String category;

    private String rating;
}
