package org.northernarc.productrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO{
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email;
        @NotBlank(message = "Password is required")
        String password;

}
