package org.northernarc.projectemployee.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectResponseDTO {
    private Integer id;
    @Column(unique = true)
    private String name;
}
