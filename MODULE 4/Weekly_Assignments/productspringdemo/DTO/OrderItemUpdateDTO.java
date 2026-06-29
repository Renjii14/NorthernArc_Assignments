package org.northernarc.productspringdemo.DTO;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderItemUpdateDTO {

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    private Integer productId;

    private Integer orderId;
}