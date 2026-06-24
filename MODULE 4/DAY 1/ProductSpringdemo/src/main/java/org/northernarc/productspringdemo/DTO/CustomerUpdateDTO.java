package org.northernarc.productspringdemo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerUpdateDTO {

    @Size(min = 3, max = 50,
            message = "Customer name must contain 3 to 50 characters")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 5, max = 200,
            message = "Address must contain 5 to 200 characters")
    private String address;
}