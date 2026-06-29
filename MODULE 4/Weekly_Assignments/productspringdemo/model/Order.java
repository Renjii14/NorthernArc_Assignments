package org.northernarc.productspringdemo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "ecom_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;


    @ManyToOne
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    private String status;

}




