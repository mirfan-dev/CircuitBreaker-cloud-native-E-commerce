package com.axlero.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    private String id;

    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    private String parentId;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products ;
}
