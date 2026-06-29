package org.northernarc.productrental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "rental_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @NotNull(message = "Rent date is required")
    @Column(nullable = false)
    private LocalDate rentDate;

    @NotNull(message = "Expected return date is required")
    @Column(nullable = false)
    private LocalDate expectedReturnDate;

    @Column
    private LocalDate actualReturnDate;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonIgnore
    @OneToMany(mappedBy = "rentalRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentPayment> rentPayments = new ArrayList<>();


    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null && !customer.getRentalRecords().contains(this)) {
            customer.getRentalRecords().add(this);
        }
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null && !product.getRentalRecords().contains(this)) {
            product.getRentalRecords().add(this);
        }
    }


    public RentalRecord(Long rentalId,
                        LocalDate rentDate,
                        LocalDate expectedReturnDate,
                        LocalDate actualReturnDate,
                        String status,
                        Customer customer,
                        Product product) {

        this.rentalId = rentalId;
        this.rentDate = rentDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
        this.customer = customer;
        this.product = product;
        this.rentPayments = new ArrayList<>();
    }
}
