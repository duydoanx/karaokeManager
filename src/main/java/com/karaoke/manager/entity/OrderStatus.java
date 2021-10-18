package com.karaoke.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "order_status")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class OrderStatus extends BaseEntity{

    @Column(name = "status_code")
    private String statusCode;

    private String description;

    @OneToMany(mappedBy = "status")
    private List<Order> orders;
}
