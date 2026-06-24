package org.northernarc.productspringdemo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {

    private Integer id;

    private Integer quantity;

    private Integer productId;
    private String productName;

    private Integer orderId;
}