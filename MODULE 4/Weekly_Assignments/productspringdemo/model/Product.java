package org.northernarc.productspringdemo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ecom_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;
    private String name;
    private String brand;
    private String category;
    private double cost;

    @OneToMany(mappedBy = "product",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<OrderItem> orderItems;
}
