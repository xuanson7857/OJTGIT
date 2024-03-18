package com.ojt.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "storeId")
    private Store store;
    @OneToMany(mappedBy = "orders")
    private Set<OrderDetails> orderDetails;
    private Timestamp createDate;
    private String phoneNumber;
}
