package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.school.CourseOutline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    ServerResponse<Course> findCourseOfLevelByTitle(String schoolName, String departmentName,
                                                    String optionName, String levelName, String courseTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException;

    ServerResponse<Course> findCourseByCourseOutline(String courseOutlineId)
            throws CourseOutlineNotFoundException, CourseNotFoundException;

    ServerResponse<List<Course>> findAllCourse();
    ServerResponse<Page<Course>> findAllCourse(Pageable pageable);
    ServerResponse<Page<Course>> findAllCourse(String keyword, Pageable pageable);

    ServerResponse<Page<Course>> findAllCourseOfLevel(String schoolName, String departmentName,
                                                      String optionName, String levelName, Pageable pageable)
            throws LevelNotFoundException;
    ServerResponse<Page<Course>> findAllCourseOfLevel(String schoolName, String departmentName,
                                                      String optionName, String levelName, String keyword,
                                                      Pageable pageable)
            throws LevelNotFoundException;
    ServerResponse<List<Course>> findAllCourseOfLevel(String schoolName, String departmentName,
                                                      String optionName, String levelName)
            throws LevelNotFoundException;

    ServerResponse<Page<Course>> findAllCourseOfLevelByType(String schoolName, String departmentName, String optionName,
                                                            String levelName, String courseType,
                                                            Pageable pageable)
            throws LevelNotFoundException;
    ServerResponse<List<Course>> findAllCourseOfLevelByType(String schoolName, String departmentName,
                                                      String optionName, String levelName, String courseType)
            throws LevelNotFoundException;


    Course saveCourse(Course course);
    ServerResponse<Course> saveCourse(String title, String courseCode, int nbreCredit, String courseType,
                                      String levelName, String optionName, String departmentName,
                                      String schoolName, String courseOutlineTitle)
            throws LevelNotFoundException, DuplicateCourseInLevelException;

    ServerResponse<Course> updateCourse(String courseId, String title, String courseCode, int nbreCredit, String courseType,
                                        String levelName, String optionName, String departmentName,
                                        String schoolName)
            throws CourseNotFoundException, DuplicateCourseInLevelException;

    ServerResponse<Course> updateCourseTitle(String courseId, String title)
            throws CourseNotFoundException, DuplicateCourseInLevelException;

    ServerResponse<Course> setLecturerToCourse(String lecturerEmail, String schoolName,
                                               String departmentName, String optionName,
                                               String levelName, String courseTitle)
            throws StaffNotFoundException, CourseNotFoundException, StaffAlreadySetToCourseException;
    ServerResponse<Course> removeLecturerToCourse(String lecturerEmail, String schoolName,
                                               String departmentName, String optionName,
                                               String levelName, String courseTitle)
            throws StaffNotFoundException, CourseNotFoundException;
    ServerResponse<CourseOutline> updateCourseOutlineTitle(String courseOutlineTitle, String schoolName,
                                                        String departmentName, String optionName,
                                                        String levelName, String courseTitle)
            throws CourseNotFoundException;

    ServerResponse<Course> addContentToCourse(String value, String contentType, String schoolName,
                                              String departmentName, String optionName,
                                              String levelName, String courseTitle) throws CourseNotFoundException;

    ServerResponse<Course> removeContentToCourse(String contentId, String schoolName,
                                              String departmentName, String optionName,
                                              String levelName, String courseTitle)
            throws CourseNotFoundException, ContentNotFoundException;

    ServerResponse<Course> updateContentToCourse(String contentId, String value, String schoolName,
                                              String departmentName, String optionName,
                                              String levelName, String courseTitle)
            throws CourseNotFoundException, ContentNotFoundException;

    ServerResponse<Course> deleteCourse(String schoolName, String departmentName, String optionName,
                                        String levelName, String courseTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException;

    ServerResponse<Course> deleteCourse(String courseId) throws CourseNotFoundException;
}
