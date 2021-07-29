package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DepartmentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateDepartmentInSchoolException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.SchoolNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    ServerResponse<Department> findDepartmentOfSchoolByName(String schoolName, String departmentName) throws SchoolNotFoundException;
    ServerResponse<List<Department>> findAllDepartment();
    ServerResponse<Page<Department>> findAllDepartment(Pageable pageable);
    ServerResponse<Page<Department>> findAllDepartment(String keyword, Pageable pageable);
    ServerResponse<Page<Department>> findAllDepartmentOfSchool(String schoolName, String keyword, Pageable pageable) throws SchoolNotFoundException;
    ServerResponse<Page<Department>> findAllDepartmentOfSchool(String schoolName, Pageable pageable) throws SchoolNotFoundException;
    ServerResponse<List<Department>> findAllDepartmentOfSchool(String schoolName) throws SchoolNotFoundException;
    Department saveDepartment(Department department);
    ServerResponse<Department> saveDepartment(String name, String acronym, String description,
                                              String ownerSchoolName)
            throws DuplicateDepartmentInSchoolException, SchoolNotFoundException;
    ServerResponse<Department> updateDepartment(String departmentId, String name, String acronym, String description,
                                                String ownerSchoolName)
            throws DuplicateDepartmentInSchoolException, DepartmentNotFoundException;
    ServerResponse<Department> updateDepartmentName(String departmentId, String departmentName)
            throws DepartmentNotFoundException, DuplicateDepartmentInSchoolException;
    ServerResponse<Department> deleteDepartmentOfSchoolByName(String schoolName,
                                                              String departmentName)
            throws SchoolNotFoundException, DepartmentNotFoundException;
    ServerResponse<Department> deleteDepartmentOfSchool(String departmentID)
            throws DepartmentNotFoundException;
}
