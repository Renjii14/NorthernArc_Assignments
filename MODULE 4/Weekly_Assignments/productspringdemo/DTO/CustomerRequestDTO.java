package org.northernarc.productspringdemo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequestDTO {

    @NotBlank(message = "Customer name cannot be blank")
    @Size(min = 3, max = 50,
            message = "Customer name must contain 3 to 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 5, max = 200,
            message = "Address must contain 5 to 200 characters")
    private String address;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50,
            message = "Username must contain 3 to 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100,
            message = "Password must contain 6 to 100 characters")
    private String password;
}