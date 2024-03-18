package com.ojt.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn( name="orders_id", referencedColumnName = "id")
    private Orders orders;
    @ManyToOne
    @JoinColumn( name="product_id", referencedColumnName = "productId")
    private Product product;
    private Integer quantity;
    private Double price;
}
