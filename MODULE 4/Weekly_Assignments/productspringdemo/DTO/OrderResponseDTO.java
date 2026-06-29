package org.northernarc.productspringdemo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Integer id;

    private Integer customerId;
    private String customerName;

    private List<Integer> orderItemIds;

    private String status;
}