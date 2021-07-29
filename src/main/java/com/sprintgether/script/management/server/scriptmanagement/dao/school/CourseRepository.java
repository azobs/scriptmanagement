package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.school.CourseOutline;
import com.sprintgether.script.management.server.scriptmanagement.model.school.EnumCoursePartType;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository  extends MongoRepository<Course, String> {
    Optional<Course> findByOwnerLevelAndTitle(Level ownerLevel, String title);
    Optional<Course> findByCourseOutline(CourseOutline courseOutline);
    Page<Course> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Course> findAllByCourseType(EnumCoursePartType courseType, Pageable pageable);
    Page<Course> findAllByOwnerLevelAndTitleContaining(Level ownerLevel, String keyword, Pageable pageable);
    Page<Course> findAllByOwnerLevel(Level ownerLevel, Pageable pageable);
    Page<Course> findAllByOwnerLevelAndCourseType(Level ownerLevel, EnumCoursePartType courseType,
                                                  Pageable pageable);
    List<Course> findAllByOwnerLevelAndCourseType(Level ownerLevel, EnumCoursePartType courseType);
    List<Course> findAllByOwnerLevelOrderByTitle(Level ownerLevel);
}
