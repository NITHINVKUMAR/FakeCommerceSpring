package com.example.FakeCommerce.schema;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass // This is to tell not to make BaseEntity as table or entity instead make it as super class so that all the base classes can use
public class BaseEntity {
    @Id // to mark the field as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This is to autoincrement the value specially for mysql
    private Long id;
}
