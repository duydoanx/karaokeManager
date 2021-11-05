package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table('product_ordered_history')
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductOrderedHistory extends BaseEntity{

    private String description;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
