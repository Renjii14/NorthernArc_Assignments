package org.northernarc.productspringdemo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class jwtRequestDTO {
    private String username;
    private String password;
}