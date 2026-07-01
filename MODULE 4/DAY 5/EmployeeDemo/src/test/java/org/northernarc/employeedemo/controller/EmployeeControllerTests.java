package org.northernarc.employeedemo.controller;

import org.junit.jupiter.api.Test;
import org.northernarc.employeedemo.dto.EmployeeResponseDTO;
import org.northernarc.employeedemo.exception.EmployeeNotFound;
import org.northernarc.employeedemo.exception.GlobalExceptionHandler;
import org.northernarc.employeedemo.model.Employee;
import org.northernarc.employeedemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.northernarc.employeedemo.dto.EmployeeRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTests {

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @MockitoBean
    EmployeeService service;

    @Test
    @WithMockUser
    void testGetEmployeeSuccess() throws Exception {
        EmployeeResponseDTO emp = new EmployeeResponseDTO(1, "yuva", 100000);

        when(service.getEmployeeById(1)).thenReturn(emp);

        mockMvc.perform(
                        get("/api/employees/1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("yuva"))
                .andExpect(jsonPath("$.salary").value(100000));
    }

    @Test
    @WithMockUser
    void testGetEmployeeNotFoundException() throws Exception {
        when(service.getEmployeeById(999))
                .thenThrow(new EmployeeNotFound("Employee not found with id: 999"));

        mockMvc.perform(
                        get("/api/employees/999")
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"));
    }

    @Test
    @WithMockUser
    void testDeleteEmployeeById() throws Exception {
        doNothing().when(service).deleteEmployee(1);

        mockMvc.perform(
                        delete("/api/employees/1")
                ).andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));
    }

    @Test
    @WithMockUser
    void testAddEmployee() throws Exception {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("John Doe", 50000);
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(1, "John Doe", 50000);

        when(service.createEmployee(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(
                        post("/api/employees")
                                .contentType("application/json")
                                .content(mapper.writeValueAsString(requestDTO))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.salary").value(50000));
    }

    @Test
    @WithMockUser
    void testUpdateEmployee() throws Exception {
        int id = 1;
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("Renjitha", 30000);
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(id, "Renjitha", 30000);
        when(service.updateEmployee(id, requestDTO)).thenReturn(responseDTO);
        mockMvc.perform(
                        put("/api/employees/{id}", id)
                                .contentType("application/json")
                                .content(mapper.writeValueAsString(requestDTO))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Renjitha"))
                .andExpect(jsonPath("$.salary").value(30000));
    }






}
