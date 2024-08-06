package com.revature.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "groceries")
public class Grocery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", updatable = false)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String category;

    @Column(columnDefinition = "int CHECK (quantity>=0)")
    private Integer quantity;

    @Column(columnDefinition = "decimal(20,2) CHECK (price>0)")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @JsonBackReference
    private Account owner;
}
