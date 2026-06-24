package org.northernarc.productspringdemo.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 2, max = 50,
            message = "Product name must contain 2 to 50 characters")
    private String name;

    @NotBlank(message = "Brand cannot be blank")
    @Size(min = 2, max = 30,
            message = "Brand must contain 2 to 30 characters")
    private String brand;

    @NotBlank(message = "Category cannot be blank")
    @Size(min = 2, max = 30,
            message = "Category must contain 2 to 30 characters")
    private String category;

    @NotNull(message = "Cost cannot be null")
    @DecimalMin(value = "0.01",
            message = "Cost must be greater than 0")
    private Double cost;
}