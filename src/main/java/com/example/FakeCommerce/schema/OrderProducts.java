package com.example.FakeCommerce.schema;

import com.example.FakeCommerce.repositories.ProductRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_products")
@SQLDelete(sql = "UPDATE order_products SET deleted_at = CURRENT_TIMESTAMP where id = ?")
@SQLRestriction("deleted_at IS NULL ")
public class OrderProducts extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product products;
}