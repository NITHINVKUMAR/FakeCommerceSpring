package com.example.FakeCommerce.schema;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass // This is to tell not to make BaseEntity as table or entity instead make it as super class so that all the base classes can use
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id // to mark the field as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This is to autoincrement the value specially for mysql
    private Long id;

    @CreatedDate
    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
