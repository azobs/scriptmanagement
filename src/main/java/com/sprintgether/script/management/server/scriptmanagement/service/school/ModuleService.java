package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModuleService {
    ServerResponse<Module> findModuleOfCourseOutlineByTitle(String schoolName, String departmentName,
                                                    String optionName, String levelName, String moduleTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException;

    ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String schoolName, String departmentName,
                                                      String optionName, String levelName, Pageable pageable)
            throws LevelNotFoundException;
    ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String schoolName, String departmentName,
                                                      String optionName, String levelName, String keyword,
                                                      Pageable pageable)
            throws LevelNotFoundException;
    ServerResponse<List<Module>> findAllModuleOfCourseOutline(String schoolName, String departmentName,
                                                      String optionName, String levelName)
            throws LevelNotFoundException;

    ServerResponse<Page<Module>> findAllModuleOfCourseOutlineByType(String schoolName, String departmentName, String optionName,
                                                            String levelName, String moduleType,
                                                            Pageable pageable)
            throws LevelNotFoundException;
    ServerResponse<List<Module>> findAllModuleOfCourseOutlineByType(String schoolName, String departmentName,
                                                            String optionName, String levelName, String moduleType)
            throws LevelNotFoundException;

    Module saveModule(Module module);
    ServerResponse<Module> saveModule(String title, int moduleOrder, String moduleType,
                                      String levelName, String optionName, String departmentName,
                                      String schoolName)
            throws LevelNotFoundException, DuplicateModuleInCourseOutlineException;

    ServerResponse<Module> updateModule(String title, int moduleOrder, String moduleType,
                                        String levelName, String optionName, String departmentName,
                                        String schoolName)
            throws LevelNotFoundException, ModuleNotFoundException;

    ServerResponse<Module> updateModuleTitle(String moduleId, String newModuleTitle)
            throws ModuleNotFoundException;

    ServerResponse<Module> addContentToModule(String value, String contentType, String schoolName,
                                              String departmentName, String optionName,
                                              String levelName, String moduleTitle)
            throws ModuleNotFoundException;

    ServerResponse<Module> removeContentToCourse(String contentId, String schoolName,
                                                 String departmentName, String optionName,
                                                 String levelName, String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException;

    ServerResponse<Module> updateContentToCourse(String contentId, String value, String schoolName,
                                                 String departmentName, String optionName,
                                                 String levelName, String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException;

    ServerResponse<Module> deleteModule(String schoolName, String departmentName, String optionName,
                                        String levelName, String moduleTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException, ModuleNotFoundException;

}
