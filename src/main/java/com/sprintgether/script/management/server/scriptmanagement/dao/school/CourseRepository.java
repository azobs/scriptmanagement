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
    Optional<Course> findById(String courseId);
    Optional<Course> findByOwnerLevelAndTitle(Level ownerLevel, String title);
    Optional<Course> findByCourseOutline(CourseOutline courseOutline);
    Page<Course> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Course> findAllByCourseType(EnumCoursePartType courseType, Pageable pageable);
    Page<Course> findAllByOwnerLevelAndTitleContaining(Level ownerLevel, String keyword, Pageable pageable);
    Page<Course> findAllByOwnerLevel(Level ownerLevel, Pageable pageable);
    Page<Course> findAllByOwnerLevelAndCourseType(Level ownerLevel, EnumCoursePartType courseType,
                                                  Pageable pageable);
    List<Course> findAllByOwnerLevelAndCourseTypeOrderByTitleAsc(Level ownerLevel,
                                                              EnumCoursePartType courseType);
    List<Course> findAllByOwnerLevelAndCourseTypeOrderByTitleDesc(Level ownerLevel,
                                                                 EnumCoursePartType courseType);
    List<Course> findAllByOwnerLevelAndCourseTypeOrderByCourseCodeAsc(Level ownerLevel,
                                                              EnumCoursePartType courseType);
    List<Course> findAllByOwnerLevelAndCourseTypeOrderByCourseCodeDesc(Level ownerLevel,
                                                                      EnumCoursePartType courseType);
    List<Course> findAllByOwnerLevelAndCourseTypeOrderByNbreCreditAsc(Level ownerLevel,
                                                              EnumCoursePartType courseType);
    List<Course> findAllByOwnerLevelAndCourseTypeOrderByNbreCreditDesc(Level ownerLevel,
                                                                      EnumCoursePartType courseType);

    List<Course> findAllByOwnerLevelOrderByTitleAsc(Level ownerLevel);
    List<Course> findAllByOwnerLevelOrderByTitleDesc(Level ownerLevel);
    List<Course> findAllByOwnerLevelOrderByCourseCodeAsc(Level ownerLevel);
    List<Course> findAllByOwnerLevelOrderByCourseCodeDesc(Level ownerLevel);
    List<Course> findAllByOwnerLevelOrderByNbreCreditAsc(Level ownerLevel);
    List<Course> findAllByOwnerLevelOrderByNbreCreditDesc(Level ownerLevel);
}
