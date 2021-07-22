package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Department;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Option;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends MongoRepository<Option, String> {
    Optional<Option> findByOwnerDepartmentAndName(Department department, String optionName);
    Page<Option> findAllByNameContaining(String keyword, Pageable pageable);
    Page<Option> findAllByOwnerDepartmentAndNameContaining(Department department, String keyword, Pageable pageable);
    Page<Option> findAllByOwnerDepartment(Department department, Pageable pageable);
    List<Option> findAllByOwnerDepartmentOrderByName(Department department);
}
