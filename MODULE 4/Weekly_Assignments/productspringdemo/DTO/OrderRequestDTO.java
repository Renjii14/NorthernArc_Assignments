package org.northernarc.productspringdemo.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {

    private Integer orderId;

//    @NotNull(message = "Customer Id cannot be null")
//    private Integer customerId;

    @NotNull(message = "Product Id cannot be null")
    private Integer productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}