package org.northernarc.productspringdemo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateDTO {

    private Integer customerId;

    private List<Integer> orderItemIds;

    @NotBlank(message = "Status cannot be empty")
    private String status;
}