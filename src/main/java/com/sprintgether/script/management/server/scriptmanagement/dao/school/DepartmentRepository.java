package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Department;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends MongoRepository<Department, String> {
    /****
     * A department is uniquely define by the department name and school
     * In fact, with a department name and a school we must obtain only one department.
     * In a school the department name must be unique.
     * @param departmentName
     * @param ownerSchool
     * @return
     */
    Optional<Department> findByOwnerSchoolAndName(School ownerSchool, String departmentName);
    Page<Department> findAllByNameContaining(String keyword, Pageable pageable);
    Page<Department> findAllByOwnerSchoolAndNameContaining(School ownerSchool, String departmentName, Pageable pageable);
    Page<Department> findAllByOwnerSchool(School school, Pageable pageable);
    List<Department> findAllByOwnerSchoolOrderByName(School ownerSchool);
}
