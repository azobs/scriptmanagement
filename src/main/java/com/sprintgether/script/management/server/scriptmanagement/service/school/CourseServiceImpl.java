package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.CourseOutlineRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.CourseRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    CourseOutlineRepository courseOutlineRepository;
    LevelService levelService;
    OptionService optionService;
    DepartmentService departmentService;
    SchoolService schoolService;
    StaffService staffService;

    public CourseServiceImpl(CourseRepository courseRepository, LevelService levelService,
                             OptionService optionService, DepartmentService departmentService,
                             SchoolService schoolService, StaffService staffService,
                             CourseOutlineRepository courseOutlineRepository) {
        this.courseRepository = courseRepository;
        this.levelService = levelService;
        this.optionService = optionService;
        this.departmentService = departmentService;
        this.schoolService = schoolService;
        this.staffService = staffService;
        this.courseOutlineRepository = courseOutlineRepository;
    }

    @Override
    public ServerResponse<Course> findCourseOfLevelByTitle(String schoolName, String departmentName,
                                                           String optionName, String levelName,
                                                           String courseTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();

        ServerResponse<School> srSchool = schoolService.findSchoolByName(schoolName);
        if(srSchool.getResponseCode() != ResponseCode.SCHOOL_FOUND){
            throw new SchoolNotFoundException("The school name specified does not match any school in the system");
        }
        School concernedSchool = srSchool.getAssociatedObject();

        ServerResponse<Department> srDepartment = departmentService.findDepartmentOfSchoolByName(
                concernedSchool.getName(), departmentName);
        if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
            throw new DepartmentNotFoundException("The department name specified does not match any department in the school specified");
        }
        Department concernedDepartment = srDepartment.getAssociatedObject();

        ServerResponse<Option> srOption = optionService.findOptionOfDepartmentByName(
                concernedSchool.getName(), concernedDepartment.getName(), optionName);
        if(srOption.getResponseCode() != ResponseCode.OPTION_FOUND){
            throw new OptionNotFoundException("The option name specified does not match any option in the department of school specified");
        }
        Option concernedOption = srOption.getAssociatedObject();

        ServerResponse<Level> srLevel = levelService.findLevelOfOptionByName(concernedSchool.getName(),
                concernedDepartment.getName(), concernedOption.getName(), levelName);
        if(srLevel.getResponseCode() != ResponseCode.LEVEL_FOUND){
            throw new LevelNotFoundException("The level name specified does not match any level in the option specified");
        }
        Level concernedLevel = srLevel.getAssociatedObject();

        Optional<Course> optionalCourse = courseRepository.findByOwnerLevelAndTitle(concernedLevel, courseTitle);
        if(!optionalCourse.isPresent()){
            srCourse.setErrorMessage("The course name specified does not match any course in the inndicated level");
            srCourse.setResponseCode(ResponseCode.COURSE_NOT_FOUND);
        }
        else{
            srCourse.setResponseCode(ResponseCode.COURSE_FOUND);
            srCourse.setErrorMessage("The course has been successfully found in the system");
            srCourse.setAssociatedObject(optionalCourse.get());
        }
        return srCourse;
    }

    @Override
    public ServerResponse<List<Course>> findAllCourse() {
        ServerResponse<List<Course>> srCourseList = new ServerResponse<>();
        List<Course> listOfCourse = courseRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "title")));
        srCourseList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srCourseList.setErrorMessage("The level list has been successfully made");
        srCourseList.setAssociatedObject(listOfCourse);
        return srCourseList;
    }

    @Override
    public ServerResponse<Page<Course>> findAllCourse(Pageable pageable) {
        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();
        Page<Course> pageOfCourse = courseRepository.findAll(pageable);
        srCoursePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srCoursePage.setErrorMessage("The course page has been successfully made");
        srCoursePage.setAssociatedObject(pageOfCourse);
        return srCoursePage;
    }

    @Override
    public ServerResponse<Page<Course>> findAllCourse(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase().trim();
        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();
        Page<Course> pageOfCourse = courseRepository.findAllByTitleContaining(keyword, pageable);
        srCoursePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srCoursePage.setErrorMessage("The course page has been successfully made");
        srCoursePage.setAssociatedObject(pageOfCourse);
        return srCoursePage;
    }

    @Override
    public ServerResponse<Page<Course>> findAllCourseOfLevel(String schoolName, String departmentName,
                                                             String optionName, String levelName,
                                                             Pageable pageable)
            throws LevelNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();

        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();
        try {
            ServerResponse<Level> srLevel = levelService.findLevelOfOptionByName(schoolName,
                    departmentName, optionName, levelName);
            if(srLevel.getResponseCode() != ResponseCode.LEVEL_FOUND){
                throw new LevelNotFoundException("The level name specified does not match any level in the option specified");
            }
            Level concernedLevel = srLevel.getAssociatedObject();
            Page<Course> pageOfCourse = courseRepository.findAllByOwnerLevel(concernedLevel, pageable);
            srCoursePage.setErrorMessage("The level page of option has been successfully made");
            srCoursePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srCoursePage.setAssociatedObject(pageOfCourse);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The school name specified does not match any school in the system");
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srCoursePage.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The department name specified does not match any department in the school specified");
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srCoursePage.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The option name specified does not match any option in the department specified");
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srCoursePage.setMoreDetails(e.getMessage());
        }

        return srCoursePage;
    }

    @Override
    public ServerResponse<Page<Course>> findAllCourseOfLevel(String schoolName, String departmentName,
                                                             String optionName, String levelName,
                                                             String keyword, Pageable pageable)
            throws LevelNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        keyword = keyword.toLowerCase().trim();

        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();
        try {
            ServerResponse<Level> srLevel = levelService.findLevelOfOptionByName(schoolName, departmentName,
                    optionName, levelName);
            if(srLevel.getResponseCode() != ResponseCode.LEVEL_FOUND){
                throw new LevelNotFoundException("The level name specified does not match any level in the option specified");
            }
            Level concernedLevel = srLevel.getAssociatedObject();
            Page<Course> pageOfCourse = courseRepository.findAllByOwnerLevelAndTitleContaining(
                    concernedLevel, keyword, pageable);
            srCoursePage.setErrorMessage("The course page of level has been successfully made");
            srCoursePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srCoursePage.setAssociatedObject(pageOfCourse);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The school name specified does not match any school in the system");
            srCoursePage.setMoreDetails(e.getMessage());
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The department name specified does not match any department in the school specified");
            srCoursePage.setMoreDetails(e.getMessage());
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The option name specified does not match any option in the department specified");
            srCoursePage.setMoreDetails(e.getMessage());
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
        }
        return srCoursePage;
    }

    @Override
    public ServerResponse<List<Course>> findAllCourseOfLevel(String schoolName, String departmentName,
                                                             String optionName, String levelName)
            throws LevelNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();

        ServerResponse<List<Course>> srCourseList = new ServerResponse<>();

        try {
            ServerResponse<Level> srLevel = levelService.findLevelOfOptionByName(schoolName,
                    departmentName, optionName,levelName);
            if(srLevel.getResponseCode() !=  ResponseCode.LEVEL_FOUND){
                throw new LevelNotFoundException("The level name specified does not match any level in the option specified");
            }
            Level associatedLevel = srLevel.getAssociatedObject();
            List<Course> listOfCourse = courseRepository.findAllByOwnerLevelOrderByTitle(associatedLevel);
            srCourseList.setErrorMessage("The course list of level has been successfully made");
            srCourseList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srCourseList.setAssociatedObject(listOfCourse);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourseList.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srCourseList.setMoreDetails(e.getMessage());
            srCourseList.setErrorMessage("The school name specified does not match any school in the system");
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourseList.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srCourseList.setMoreDetails(e.getMessage());
            srCourseList.setErrorMessage("The department name specified does not match any department in the school specified");
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCourseList.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srCourseList.setMoreDetails(e.getMessage());
            srCourseList.setErrorMessage("The option name specified does not match any option in the department specified");
        }

        return srCourseList;
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public ServerResponse<Course> saveCourse(String title, String courseCode, int nbreCredit,
                                             String levelName, String optionName,
                                             String departmentName, String schoolName,
                                             String courseOutlineTitle)
            throws LevelNotFoundException, DuplicateCourseInLevelException {

        title = title.toLowerCase().trim();
        courseCode = courseCode.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();

        try {
            ServerResponse<Level> srLevel = levelService.findLevelOfOptionByName(schoolName, departmentName,
                    optionName,levelName);
            if(srLevel.getResponseCode() != ResponseCode.LEVEL_FOUND){
                throw new LevelNotFoundException("The level name does not match any level in the optionn specified");
            }

            Level concernedLevel = srLevel.getAssociatedObject();
            ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                    optionName, levelName, title);
            if(srCourse1.getResponseCode() == ResponseCode.COURSE_FOUND){
                throw new DuplicateCourseInLevelException("The course title specified already exist in the level specified");
            }

            //We must initialise the lecturer list of a course with empty list during creation
            List<Staff> listofStaff = new ArrayList<>();

            //We must automatically add a course title to the new created course
            CourseOutline courseOutline = new CourseOutline(courseOutlineTitle);
            CourseOutline courseOutlineSaved = courseOutlineRepository.save(courseOutline);

            Course course = new Course();
            course.setCourseCode(courseCode);
            course.setNbreCredit(nbreCredit);
            course.setTitle(title);
            course.setListofLecturer(listofStaff);
            course.setOwnerLevel(concernedLevel);
            course.setCourseOutline(courseOutlineSaved);

            Course courseSaved = courseRepository.save(course);
            srCourse.setResponseCode(ResponseCode.COURSE_CREATED);
            srCourse.setErrorMessage("The course has been successfully created");
            srCourse.setAssociatedObject(courseSaved);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srCourse.setErrorMessage("The school name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srCourse.setErrorMessage("The department name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            e.printStackTrace();
        }

        return srCourse;
    }

    @Override
    public ServerResponse<Course> updateCourse(String title, String courseCode, int nbreCredit,
                                               String levelName, String optionName,
                                               String departmentName, String schoolName)
            throws LevelNotFoundException, CourseNotFoundException {

        title = title.toLowerCase().trim();
        courseCode = courseCode.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();

        try {
            ServerResponse<Level> srLevel = levelService.findLevelOfOptionByName(schoolName, departmentName,
                    optionName,levelName);
            if(srLevel.getResponseCode() != ResponseCode.LEVEL_FOUND){
                throw new LevelNotFoundException("The level name does not match any level in the optionn specified");
            }

            Level concernedLevel = srLevel.getAssociatedObject();
            ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                    optionName, levelName, title);
            if(srCourse1.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course title specified does not match any course in the level specified");
            }
            Course concernedCourse = srCourse1.getAssociatedObject();

            concernedCourse.setCourseCode(courseCode);
            concernedCourse.setNbreCredit(nbreCredit);
            concernedCourse.setTitle(title);
            concernedCourse.setOwnerLevel(concernedLevel);

            Course courseUpdated = courseRepository.save(concernedCourse);
            srCourse.setResponseCode(ResponseCode.COURSE_UPDATED);
            srCourse.setErrorMessage("The course has been successfully updated");
            srCourse.setAssociatedObject(courseUpdated);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srCourse.setErrorMessage("The school name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srCourse.setErrorMessage("The department name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            e.printStackTrace();
        }

        return srCourse;
    }

    @Override
    public ServerResponse<Course> setLecturerToCourse(String lecturerEmail, String schoolName,
                                                      String departmentName, String optionName,
                                                      String levelName, String courseTitle)
            throws StaffNotFoundException, CourseNotFoundException, StaffAlreadySetToCourseException{
        lecturerEmail = lecturerEmail.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();
        try {
            ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle);
            if(srCourse1.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course name specified does not match any " +
                        "course in the level");
            }
            Course concernedCourse = srCourse1.getAssociatedObject();
            ServerResponse<Staff> srStaff = staffService.findStaffByEmail(lecturerEmail);
            if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
                throw new StaffNotFoundException("The lecturer email specified does not match any lecturer in the system");
            }
            Staff lecturerToAdd = srStaff.getAssociatedObject();

            for(Staff staff : concernedCourse.getListofLecturer()){
                if(staff.getEmail().equals(lecturerEmail)){
                    throw new StaffAlreadySetToCourseException("The staff specified is already set to that course");
                }
            }
            /****
             *We can only be there if and only if StaffAlreadySetToCourseException
             * has not been lunched
             */
            concernedCourse.getListofLecturer().add(lecturerToAdd);
            Course courseUpdated = courseRepository.save(concernedCourse);

            srCourse.setErrorMessage("The lecturer has been successfully add to the course");
            srCourse.setResponseCode(ResponseCode.COURSE_UPDATED);
            srCourse.setAssociatedObject(courseUpdated);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The school name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The department name specified does not match any department in the school");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The option name specified does not match any Option in the department");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The Level name specified does not match any levem in the Option");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
        }
        return srCourse;
    }

    @Override
    public ServerResponse<Course> removeLecturerToCourse(String lecturerEmail, String schoolName,
                                                         String departmentName, String optionName,
                                                         String levelName, String courseTitle)
            throws StaffNotFoundException, CourseNotFoundException {
        lecturerEmail = lecturerEmail.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();
        try {
            ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle);
            if(srCourse1.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course name specified does not match any " +
                        "course in the level");
            }
            Course concernedCourse = srCourse1.getAssociatedObject();
            Staff lecturerToRemove = null;
            for(Staff staff : concernedCourse.getListofLecturer()){
                if(staff.getEmail().equals(lecturerEmail)){
                    lecturerToRemove = staff;
                }
            }

            if(lecturerToRemove == null){
                throw new StaffNotFoundException("The lecturer email does not match any staff associated with the course");
            }
            /****
             *We can only be there if and only if StaffNotFoundException
             * has not been lunched
             */
            concernedCourse.getListofLecturer().remove(lecturerToRemove);
            Course courseUpdated = courseRepository.save(concernedCourse);

            srCourse.setErrorMessage("The lecturer has been successfully add to the course");
            srCourse.setResponseCode(ResponseCode.COURSE_UPDATED);
            srCourse.setAssociatedObject(courseUpdated);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The school name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The department name specified does not match any department in the school");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The option name specified does not match any Option in the department");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The Level name specified does not match any levem in the Option");
            srCourse.setMoreDetails(e.getMessage());
            srCourse.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
        }
        return srCourse;
    }

    @Override
    public ServerResponse<CourseOutline> updateCourseOutlineTitle(String courseOutlineTitle, String schoolName,
                                                        String departmentName, String optionName,
                                                        String levelName, String courseTitle)
            throws CourseNotFoundException {

        courseOutlineTitle = courseOutlineTitle.toUpperCase().trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        ServerResponse<CourseOutline> srCourseOutline = new ServerResponse<>();

        try {
            ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle);
            if(srCourse1.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course name specified does not match any " +
                        "course in the level");
            }
            Course concernedCourse = srCourse1.getAssociatedObject();
            CourseOutline courseOutline = concernedCourse.getCourseOutline();
            courseOutline.setTitle(courseOutlineTitle);
            CourseOutline courseOutlineUpdated = courseOutlineRepository.save(courseOutline);

            srCourseOutline.setErrorMessage("The course outline title has been successfully updated");
            srCourseOutline.setResponseCode(ResponseCode.COURSEOUTLINE_UPDATED);
            srCourseOutline.setAssociatedObject(courseOutlineUpdated);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourseOutline.setErrorMessage("The school name specified does not match any school in the system");
            srCourseOutline.setMoreDetails(e.getMessage());
            srCourseOutline.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourseOutline.setErrorMessage("The department name specified does not match any department in the school");
            srCourseOutline.setMoreDetails(e.getMessage());
            srCourseOutline.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCourseOutline.setErrorMessage("The option name specified does not match any Option in the department");
            srCourseOutline.setMoreDetails(e.getMessage());
            srCourseOutline.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourseOutline.setErrorMessage("The Level name specified does not match any levem in the Option");
            srCourseOutline.setMoreDetails(e.getMessage());
            srCourseOutline.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
        }
        return srCourseOutline;
    }

    @Override
    public ServerResponse<Course> deleteCourse(String schoolName, String departmentName,
                                               String optionName, String levelName, String courseTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException, CourseNotFoundException {
        return null;
    }
}
