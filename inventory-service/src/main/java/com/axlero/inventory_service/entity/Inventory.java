package com.axlero.inventory_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String skuCode;
    @Column(nullable = false)
    private Integer quantity;
}
