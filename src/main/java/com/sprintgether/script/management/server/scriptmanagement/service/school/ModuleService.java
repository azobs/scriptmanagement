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
                                                    String optionName, String levelName,
                                                            String courseTitle, String moduleTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException;

    ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String schoolName, String departmentName,
                                                      String optionName, String levelName,
                                                              String courseTitle, Pageable pageable)
            throws CourseNotFoundException;

    ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String schoolName, String departmentName,
                                                      String optionName, String levelName,
                                                              String courseTitle, String keyword,
                                                              Pageable pageable)
            throws CourseNotFoundException;

    ServerResponse<List<Module>> findAllModuleOfCourseOutline(String schoolName, String departmentName,
                                                      String optionName, String levelName,
                                                              String courseTitle)
            throws CourseNotFoundException;

    ServerResponse<Page<Module>> findAllModuleOfCourseOutlineByType(String schoolName, String departmentName, String optionName,
                                                            String levelName, String courseTitle,
                                                                    String moduleType,
                                                            Pageable pageable)
            throws CourseNotFoundException;
    ServerResponse<List<Module>> findAllModuleOfCourseOutlineByType(String schoolName, String departmentName,
                                                            String optionName, String levelName,
                                                                    String courseTitle, String moduleType)
            throws CourseNotFoundException;

    Module saveModule(Module module);
    ServerResponse<Module> saveModule(String title, int moduleOrder, String moduleType, String courseTitle,
                                      String levelName, String optionName, String departmentName,
                                      String schoolName)
            throws CourseNotFoundException, DuplicateModuleInCourseOutlineException;

    ServerResponse<Module> updateModule(String moduleId, String title, int moduleOrder, String moduleType, String courseTitle,
                                        String levelName, String optionName, String departmentName,
                                        String schoolName)
            throws ModuleNotFoundException, DuplicateModuleInCourseOutlineException;

    ServerResponse<Module> updateModuleTitle(String moduleId, String newModuleTitle)
            throws ModuleNotFoundException, DuplicateModuleInCourseOutlineException;

    ServerResponse<Module> addContentToModule(String value, String contentType, String schoolName,
                                              String departmentName, String optionName,
                                              String levelName, String courseTitle, String moduleTitle)
            throws ModuleNotFoundException;

    ServerResponse<Module> removeContentToModule(String contentId, String schoolName,
                                                 String departmentName, String optionName,
                                                 String levelName, String courseTitle, String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException;

    ServerResponse<Module> updateContentToCourse(String contentId, String value, String schoolName,
                                                 String departmentName, String optionName,
                                                 String levelName, String courseTitle, String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException;

    ServerResponse<Module> deleteModule(String schoolName, String departmentName, String optionName,
                                        String levelName, String courseTitle, String moduleTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException;

    ServerResponse<Module> deleteModule(String moduleId) throws ModuleNotFoundException;

}
