package org.northernarc.productspringdemo.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDTO {

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Product Id cannot be null")
    private Integer productId;

    @NotNull(message = "Order Id cannot be null")
    private Integer orderId;
}