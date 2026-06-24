package org.northernarc.projectemployee.service;

import org.northernarc.projectemployee.dto.EmployeeRequestDTO;
import org.northernarc.projectemployee.dto.EmployeeResponseDTO;
import org.northernarc.projectemployee.dto.EmployeeUpdateDTO;
import org.northernarc.projectemployee.model.Employee;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO saveEmployee(EmployeeRequestDTO employee);
    List<EmployeeResponseDTO>  getAllEmployees();
    EmployeeResponseDTO getEmployeeById(Long id);
    EmployeeResponseDTO updateEmployee(Long id, EmployeeUpdateDTO employee);
    void deleteEmployee(Long id);
    public List<EmployeeResponseDTO> getAllSorted();
    public List<EmployeeResponseDTO> getAllByPage(int pageno,int pagesize);
    public List<EmployeeResponseDTO> getAllEmpByCustom(String dept);
    public List<Employee> getEmpByDept();
    public int UpdateEmployeeEmailByName(String email,String name);

}
