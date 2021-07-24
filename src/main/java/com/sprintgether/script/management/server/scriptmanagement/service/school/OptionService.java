package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DepartmentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateOptionInDepartmentException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.OptionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.SchoolNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OptionService {
    ServerResponse<Option> findOptionOfDepartmentByName(String schoolName, String departmentName,
                                                        String optionName) throws SchoolNotFoundException, DepartmentNotFoundException;
    ServerResponse<List<Option>> findAllOption();
    ServerResponse<Page<Option>> findAllOption(Pageable pageable);
    ServerResponse<Page<Option>> findAllOption(String keyword, Pageable pageable);
    ServerResponse<Page<Option>> findAllOptionOfDepartment(String schoolName, String departmentName, String keyword, Pageable pageable) throws DepartmentNotFoundException;
    ServerResponse<Page<Option>> findAllOptionOfDepartment(String schoolName, String departmentName, Pageable pageable) throws DepartmentNotFoundException;
    ServerResponse<List<Option>> findAllOptionOfDepartment(String schoolName, String departmentName) throws DepartmentNotFoundException;
    Option saveOption(Option option);
    ServerResponse<Option> saveOption(String name, String acronym, String description,
                                      String departmentName, String schoolName)
            throws DuplicateOptionInDepartmentException, DepartmentNotFoundException;
    ServerResponse<Option> updateOption(String name, String acronym, String description,
                                      String departmentName, String schoolName)
            throws OptionNotFoundException, DepartmentNotFoundException;
    ServerResponse<Option> updateOptionName(String optionId, String optionName) throws OptionNotFoundException;
    ServerResponse<Option> deleteOption(String schoolName, String departmentName, String optionName)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException;
}
