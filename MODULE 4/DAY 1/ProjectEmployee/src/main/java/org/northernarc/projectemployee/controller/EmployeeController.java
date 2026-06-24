package org.northernarc.projectemployee.controller;

import jakarta.validation.Valid;
import org.northernarc.projectemployee.dto.EmployeeRequestDTO;
import org.northernarc.projectemployee.dto.EmployeeResponseDTO;
import org.northernarc.projectemployee.dto.EmployeeUpdateDTO;
import org.northernarc.projectemployee.model.Employee;
import org.northernarc.projectemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mtm/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/sorted")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllSorted() {
        return ResponseEntity.ok(employeeService.getAllSorted());
    }
    @GetMapping("/page/{pno}/{psize}")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllByPage(@PathVariable int pno, @PathVariable int psize) {
        return ResponseEntity.ok(employeeService.getAllByPage(pno,psize));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> findAll() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/custom")
    public ResponseEntity<List<Employee>> findAll2() {
        return ResponseEntity.ok(employeeService.getEmpByDept());
    }


    @GetMapping("/dept/{dept}")
    public ResponseEntity<List<EmployeeResponseDTO>> getAll(@PathVariable String dept) {
        return ResponseEntity.ok(employeeService.getAllEmpByCustom(dept));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return ResponseEntity.status(201)
                .body(employeeService.saveEmployee(employeeRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateDTO dto) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{email}/{name}")
    public int updateemployeeemailbyname(@PathVariable String email,@PathVariable String name){
        return employeeService.UpdateEmployeeEmailByName(email,name);

    }


}