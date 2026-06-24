package org.northernarc.projectemployee.service;



import org.northernarc.projectemployee.dto.EmployeeResponseDTO;
import org.northernarc.projectemployee.dto.ProjectRequestDTO;
import org.northernarc.projectemployee.dto.ProjectResponseDTO;
import org.northernarc.projectemployee.dto.ProjectUpdateDTO;

import java.util.List;

public interface ProjectService {
    ProjectResponseDTO saveProject(ProjectRequestDTO project);
    List<ProjectResponseDTO> getAllProjects();
    ProjectResponseDTO getProjectById(Integer id);
    ProjectResponseDTO updateProject(Integer id, ProjectUpdateDTO project);
    void deleteProject(Integer id);

    void assignMore(int pid, long eid);

    void unassign(int pid, long eid);

    List<EmployeeResponseDTO> findEmpByProjectName(String name);

    List<ProjectResponseDTO> findProjectByEmpId(long id);

    public List<ProjectResponseDTO> getAllSorted();
    public List<ProjectResponseDTO> getAllByPage(int pageno,int pagesize);

}
