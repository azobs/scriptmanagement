package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.CourseOutline;
import com.sprintgether.script.management.server.scriptmanagement.model.school.EnumCoursePartType;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends MongoRepository<Module, String> {
    Optional<Module> findById(String moduleId);
    Optional<Module> findByOwnerCourseOutlineAndTitle(CourseOutline courseOutline,
                                                      String moduleTitle);
    Page<Module> findAllByOwnerCourseOutlineAndTitleContaining(CourseOutline courseOutline,
                                                               String keyword, Pageable pageable);
    Page<Module> findAllByOwnerCourseOutline(CourseOutline courseOutline, Pageable pageable);

    Page<Module> findAllByOwnerCourseOutlineAndModuleType(CourseOutline courseOutline,
                                                          EnumCoursePartType moduleType,
                                                          Pageable pageable);
    List<Module> findAllByOwnerCourseOutlineAndModuleTypeOrderByTitleAsc(CourseOutline courseOutline,
                                                          EnumCoursePartType moduleType);
    List<Module> findAllByOwnerCourseOutlineAndModuleTypeOrderByModuleOrderAsc(
                                                                    CourseOutline courseOutline,
                                                                    EnumCoursePartType moduleType);
    List<Module> findAllByOwnerCourseOutlineOrderByTitleAsc(CourseOutline courseOutline);
    List<Module> findAllByOwnerCourseOutlineOrderByModuleOrderAsc(CourseOutline courseOutline);

    List<Module> findAllByOwnerCourseOutlineAndModuleTypeOrderByTitleDesc(CourseOutline courseOutline,
                                                                         EnumCoursePartType moduleType);
    List<Module> findAllByOwnerCourseOutlineAndModuleTypeOrderByModuleOrderDesc(
            CourseOutline courseOutline,
            EnumCoursePartType moduleType);
    List<Module> findAllByOwnerCourseOutlineOrderByTitleDesc(CourseOutline courseOutline);
    List<Module> findAllByOwnerCourseOutlineOrderByModuleOrderDesc(CourseOutline courseOutline);


}
