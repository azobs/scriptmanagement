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
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException;

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

    Course saveCourse(Course course);
    ServerResponse<Course> saveCourse(String title, String courseCode, int nbreCredit,
                                      String levelName, String optionName, String departmentName,
                                      String schoolName, String courseOutlineTitle)
            throws LevelNotFoundException, DuplicateCourseInLevelException;
    ServerResponse<Course> updateCourse(String title, String courseCode, int nbreCredit,
                                        String levelName, String optionName, String departmentName,
                                        String schoolName)
            throws LevelNotFoundException, CourseNotFoundException;
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
    ServerResponse<Course> deleteCourse(String schoolName, String departmentName, String optionName,
                                        String levelName, String courseTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException, CourseNotFoundException;


}