package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.CourseOutlineRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.CourseRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumContentType;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    CourseOutlineRepository courseOutlineRepository;
    ContentRepository contentRepository;
    LevelService levelService;
    StaffService staffService;

    public CourseServiceImpl(CourseRepository courseRepository, LevelService levelService,
                              StaffService staffService,
                             CourseOutlineRepository courseOutlineRepository,
                             ContentRepository contentRepository) {
        this.courseRepository = courseRepository;
        this.levelService = levelService;
        this.staffService = staffService;
        this.courseOutlineRepository = courseOutlineRepository;
        this.contentRepository = contentRepository;
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

        ServerResponse<Level> srLevel = levelService.findLevelOfOptionByName(schoolName,
                departmentName, optionName, levelName);
        if(srLevel.getResponseCode() != ResponseCode.LEVEL_FOUND){
            throw new LevelNotFoundException("The level name specified does not match any level " +
                    "in the option specified");
        }
        Level concernedLevel = srLevel.getAssociatedObject();

        Optional<Course> optionalCourse = courseRepository.findByOwnerLevelAndTitle(concernedLevel,
                courseTitle);
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
    public ServerResponse<Course> findCourseOfLevelById(String courseId) {
        courseId = courseId.trim();
        ServerResponse<Course> srCourse = new ServerResponse<>();
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if(!optionalCourse.isPresent()){
            srCourse.setErrorMessage("The course id specified does not match any course in the inndicated level");
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
    public ServerResponse<Course> findCourseByCourseOutline(String courseOutlineId)
            throws CourseOutlineNotFoundException, CourseNotFoundException {
        ServerResponse<Course> srCourse = new ServerResponse<>();
        Optional<CourseOutline> optionalCourseOutline = courseOutlineRepository.findById(courseOutlineId);
        if(!optionalCourseOutline.isPresent()){
            throw new CourseOutlineNotFoundException("There is no courseOutline associated to this id");
        }
        CourseOutline courseOutline = optionalCourseOutline.get();
        Optional<Course> optionalCourse = courseRepository.findByCourseOutline(courseOutline);
        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The course associated does not found in the system");
        }
        Course course = optionalCourse.get();
        srCourse.setErrorMessage("The associated course has been successfully found");
        srCourse.setResponseCode(ResponseCode.COURSE_FOUND);
        srCourse.setAssociatedObject(course);
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

    public Optional<Level> findConcernedLevel(String levelId,
                                              String schoolName,
                                              String departmentName,
                                              String optionName,
                                              String levelName){
        Optional<Level> optionalLevel = Optional.empty();
        levelId = levelId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        Level concernedLevel = null;

        ServerResponse<Level> srLevelFoundById = levelService.findLevelOfOptionById(levelId);
        if(srLevelFoundById.getResponseCode() != ResponseCode.LEVEL_FOUND) {
            ServerResponse<Level> srLevel = null;
            try {
                srLevel = levelService.findLevelOfOptionByName(schoolName,
                        departmentName, optionName, levelName);

                concernedLevel = srLevel.getAssociatedObject();
            } catch (OptionNotFoundException e) {
                //e.printStackTrace();
            } catch (SchoolNotFoundException e) {
                //e.printStackTrace();
            } catch (DepartmentNotFoundException e) {
                //e.printStackTrace();
            }
        }
        else {
            concernedLevel = srLevelFoundById.getAssociatedObject();
        }

        optionalLevel = Optional.ofNullable(concernedLevel);

        return optionalLevel;
    }

    @Override
    public ServerResponse<Page<Course>> findAllCourseOfLevel(String levelId,
                                                             String schoolName,
                                                             String departmentName,
                                                             String optionName,
                                                             String levelName,
                                                             Pageable pageable)
            throws LevelNotFoundException {
        levelId = levelId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();

        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();

        Level concernedLevel = null;
        Optional<Level> optionalLevel = this.findConcernedLevel(levelId, schoolName, departmentName,
                optionName, levelName);
        if(!optionalLevel.isPresent()){
            throw new LevelNotFoundException("The specified level does not found on the system");
        }
        concernedLevel = optionalLevel.get();

        Page<Course> pageOfCourse = courseRepository.findAllByOwnerLevel(concernedLevel, pageable);
        srCoursePage.setErrorMessage("The level page of option has been successfully made");
        srCoursePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srCoursePage.setAssociatedObject(pageOfCourse);

        return srCoursePage;
    }



    @Override
    public ServerResponse<Page<Course>> findAllCourseOfLevel(String levelId,
                                                             String schoolName,
                                                             String departmentName,
                                                             String optionName,
                                                             String levelName,
                                                             String keyword,
                                                             Pageable pageable)
            throws LevelNotFoundException {
        levelId = levelId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        keyword = keyword.toLowerCase().trim();
        Level concernedLevel = null;
        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();

        Optional<Level> optionalLevel = this.findConcernedLevel(levelId, schoolName, departmentName,
                optionName, levelName);
        if(!optionalLevel.isPresent()){
            throw new LevelNotFoundException("The specified level does not found on the system");
        }
        concernedLevel = optionalLevel.get();

        Page<Course> pageOfCourse = courseRepository.findAllByOwnerLevelAndTitleContaining(
                concernedLevel, keyword, pageable);
        srCoursePage.setErrorMessage("The course page of level has been successfully made");
        srCoursePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srCoursePage.setAssociatedObject(pageOfCourse);
        return srCoursePage;
    }


    @Override
    public ServerResponse<List<Course>> findAllCourseOfLevel(String levelId,
                                                             String schoolName,
                                                             String departmentName,
                                                             String optionName,
                                                             String levelName,
                                                             String sortBy,
                                                             String direction)
            throws LevelNotFoundException {
        levelId = levelId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();

        ServerResponse<List<Course>> srCourseList = new ServerResponse<>();
        Level associatedLevel = null;
        Optional<Level> optionalLevel = this.findConcernedLevel(levelId, schoolName, departmentName,
                optionName, levelName);
        if(!optionalLevel.isPresent()){
            throw new LevelNotFoundException("The specified level does not found on the system");
        }
        associatedLevel = optionalLevel.get();

        List<Course> listOfCourse = null;

        if (sortBy.equalsIgnoreCase("title")) {
            if (direction.equalsIgnoreCase("ASC")) {
                listOfCourse = courseRepository.findAllByOwnerLevelOrderByTitleAsc(
                        associatedLevel);
            } else {
                listOfCourse = courseRepository.findAllByOwnerLevelOrderByTitleDesc(
                        associatedLevel);
            }
        } else if (sortBy.equalsIgnoreCase("courseCode")) {
            if (direction.equalsIgnoreCase("ASC")) {
                listOfCourse = courseRepository.findAllByOwnerLevelOrderByCourseCodeAsc(
                        associatedLevel);
            } else {
                listOfCourse = courseRepository.findAllByOwnerLevelOrderByCourseCodeDesc(
                        associatedLevel);
            }
        } else {
            if (direction.equalsIgnoreCase("ASC")) {
                listOfCourse = courseRepository.findAllByOwnerLevelOrderByNbreCreditAsc(
                        associatedLevel);
            } else {
                listOfCourse = courseRepository.findAllByOwnerLevelOrderByNbreCreditDesc(
                        associatedLevel);
            }
        }
        srCourseList.setErrorMessage("The course list of level has been successfully made");
        srCourseList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srCourseList.setAssociatedObject(listOfCourse);

        return srCourseList;
    }


    @Override
    public ServerResponse<Page<Course>> findAllCourseOfLevelByType(String levelId,
                                                                   String schoolName,
                                                                   String departmentName,
                                                                   String optionName,
                                                                   String levelName,
                                                                   String courseType,
                                                                   Pageable pageable)
            throws LevelNotFoundException {
        levelId = levelId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseType = courseType.trim();

        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();

        Level concernedLevel = null;
        Optional<Level> optionalLevel = this.findConcernedLevel(levelId, schoolName, departmentName,
                optionName, levelName);
        if(!optionalLevel.isPresent()){
            throw new LevelNotFoundException("The specified level does not found on the system");
        }
        concernedLevel = optionalLevel.get();

        try {
            EnumCoursePartType enumCoursePartType = EnumCoursePartType.valueOf(
                    courseType.toUpperCase());
            Page<Course> pageOfCourse = courseRepository.findAllByOwnerLevelAndCourseType(
                    concernedLevel, enumCoursePartType, pageable);

            srCoursePage.setErrorMessage("The course page of level by type has been " +
                    "successfully made");
            srCoursePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srCoursePage.setAssociatedObject(pageOfCourse);
        }
        catch (IllegalArgumentException e) {
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srCoursePage.setErrorMessage("IllegalArgumentException");
            srCoursePage.setMoreDetails(e.getMessage());
        }
        return srCoursePage;
    }


    @Override
    public ServerResponse<List<Course>> findAllCourseOfLevelByType(String levelId,
                                                                   String schoolName,
                                                                   String departmentName,
                                                                   String optionName,
                                                                   String levelName,
                                                                   String courseType,
                                                                   String sortBy,
                                                                   String direction)
            throws LevelNotFoundException {
        levelId = levelId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseType = courseType.trim();

        ServerResponse<List<Course>> srCourseList = new ServerResponse<>();

        Level associatedLevel = null;
        Optional<Level> optionalLevel = this.findConcernedLevel(levelId, schoolName, departmentName,
                optionName, levelName);
        if(!optionalLevel.isPresent()){
            throw new LevelNotFoundException("The specified level does not found on the system");
        }
        associatedLevel = optionalLevel.get();

        try{
            EnumCoursePartType enumCoursePartType = EnumCoursePartType.valueOf(courseType.toUpperCase());

            List<Course> listOfCourse = null;
            if (sortBy.equalsIgnoreCase("title")) {
                if (direction.equalsIgnoreCase("ASC")) {
                    listOfCourse = courseRepository.findAllByOwnerLevelAndCourseTypeOrderByTitleAsc(
                            associatedLevel, enumCoursePartType);
                } else {
                    listOfCourse = courseRepository.findAllByOwnerLevelAndCourseTypeOrderByTitleDesc(
                            associatedLevel, enumCoursePartType);
                }
            } else if (sortBy.equalsIgnoreCase("courseCode")) {
                if (direction.equalsIgnoreCase("ASC")) {
                    listOfCourse = courseRepository.findAllByOwnerLevelAndCourseTypeOrderByCourseCodeAsc(
                            associatedLevel, enumCoursePartType);
                } else {
                    listOfCourse = courseRepository.findAllByOwnerLevelAndCourseTypeOrderByCourseCodeDesc(
                            associatedLevel, enumCoursePartType);
                }
            } else {
                if (direction.equalsIgnoreCase("ASC")) {
                    listOfCourse = courseRepository.findAllByOwnerLevelAndCourseTypeOrderByNbreCreditAsc(
                            associatedLevel, enumCoursePartType);
                } else {
                    listOfCourse = courseRepository.findAllByOwnerLevelAndCourseTypeOrderByNbreCreditDesc(
                            associatedLevel, enumCoursePartType);
                }
            }

            srCourseList.setErrorMessage("The course list of level by type has been successfully made");
            srCourseList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srCourseList.setAssociatedObject(listOfCourse);
        } catch (IllegalArgumentException e) {
            srCourseList.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srCourseList.setErrorMessage("IllegalArgumentException");
            srCourseList.setMoreDetails(e.getMessage());
        }

        return srCourseList;
    }


    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public ServerResponse<Course> saveCourse(String title,
                                             String courseCode,
                                             int nbreCredit,
                                             String courseType,
                                             String levelId,
                                             String levelName,
                                             String optionName,
                                             String departmentName,
                                             String schoolName,
                                             String courseOutlineTitle)
            throws LevelNotFoundException, DuplicateCourseInLevelException {

        title = title.toLowerCase().trim();
        courseCode = courseCode.trim();
        courseType = courseType.trim();
        levelId = levelId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();


        try{
            Level concernedLevel = null;

            Optional<Level> optionalLevel = this.findConcernedLevel(levelId, schoolName, departmentName,
                    optionName, levelName);
            if(!optionalLevel.isPresent()){
                throw new LevelNotFoundException("The level specified does not match any level in the system");
            }
            concernedLevel = optionalLevel.get();

            ServerResponse<Course> srCourse1 = null;

            srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                    optionName, levelName, title);

            if (srCourse1.getResponseCode() == ResponseCode.COURSE_FOUND) {
                throw new DuplicateCourseInLevelException("The course title specified already exist in the level specified");
            }

            EnumCoursePartType enumCoursePartType = EnumCoursePartType.valueOf(courseType.toUpperCase());

            //We must initialise the lecturer list of a course with empty list during creation
            List<Staff> listofStaff = new ArrayList<>();
            List<Content> listofContent = new ArrayList<>();

            //We must automatically add a course title to the new created course
            CourseOutline courseOutline = new CourseOutline(courseOutlineTitle);
            CourseOutline courseOutlineSaved = courseOutlineRepository.save(courseOutline);

            Course course = new Course();
            course.setCourseCode(courseCode);
            course.setNbreCredit(nbreCredit);
            course.setTitle(title);
            course.setCourseType(enumCoursePartType);
            course.setListofLecturer(listofStaff);
            course.setListofContent(listofContent);
            course.setOwnerLevel(concernedLevel);
            course.setCourseOutline(courseOutlineSaved);

            Course courseSaved = courseRepository.save(course);
            srCourse.setResponseCode(ResponseCode.COURSE_CREATED);
            srCourse.setErrorMessage("The course has been successfully created");
            srCourse.setAssociatedObject(courseSaved);

        } catch (IllegalArgumentException e) {
            srCourse.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srCourse.setErrorMessage("IllegalArgumentException");
            srCourse.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srCourse.setErrorMessage("The school name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srCourse.setErrorMessage("The department name specified does not match any " +
                    "department in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srCourse.setErrorMessage("The option name specified does not match any option " +
                    "in the system");
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }



    @Override
    public ServerResponse<Course> updateCourse(String courseId,
                                               String title,
                                               String courseCode,
                                               int nbreCredit,
                                               String courseType,
                                               String levelName,
                                               String optionName,
                                               String departmentName,
                                               String schoolName)
            throws DuplicateCourseInLevelException, CourseNotFoundException {

        courseId = courseId.trim();
        title = title.toLowerCase().trim();
        courseCode = courseCode.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();
        srCourse.setResponseCode(ResponseCode.LEVEL_NOT_UPDATED);

        //////////////////////////////////////////////////////////////
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not exist in the system");
        }
        else{
            Course courseToUpdated1 = optionalCourse.get();
            try {
                EnumCoursePartType enumCoursePartType = EnumCoursePartType.valueOf(
                        courseType.toUpperCase());

                courseToUpdated1.setCourseCode(courseCode);
                courseToUpdated1.setNbreCredit(nbreCredit);
                courseToUpdated1.setTitle(title);
                courseToUpdated1.setCourseType(enumCoursePartType);

                ServerResponse<Course> srCourse2 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                        optionName, levelName, title);
                if(srCourse2.getResponseCode() == ResponseCode.COURSE_FOUND){
                    Course courseToUpdated2 = srCourse2.getAssociatedObject();
                    if(!courseToUpdated1.getId().equalsIgnoreCase(courseToUpdated2.getId())){
                        throw new DuplicateCourseInLevelException("There is another course in the " +
                                "level with the same title");
                    }
                }
                else{
                    courseToUpdated1.setTitle(title);
                }

                Course courseUpdated = courseRepository.save(courseToUpdated1);
                srCourse.setResponseCode(ResponseCode.COURSE_UPDATED);
                srCourse.setErrorMessage("The course has been successfully updated");
                srCourse.setAssociatedObject(courseUpdated);

            }catch (IllegalArgumentException e){
                srCourse.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
                srCourse.setErrorMessage("IllegalArgumentException");
                srCourse.setMoreDetails(e.getMessage());
            } catch (DepartmentNotFoundException e) {
                //e.printStackTrace();
                srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
                srCourse.setErrorMessage("The department name specified does not match any department in the system");
                srCourse.setMoreDetails(e.getMessage());
            } catch (SchoolNotFoundException e) {
                //e.printStackTrace();
                srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
                srCourse.setErrorMessage("The school name specified does not match any school in the system");
                srCourse.setMoreDetails(e.getMessage());
            } catch (LevelNotFoundException e) {
                //e.printStackTrace();
                srCourse.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
                srCourse.setErrorMessage("The level name specified does not match any level in the system");
                srCourse.setMoreDetails(e.getMessage());
            } catch (OptionNotFoundException e) {
                //e.printStackTrace();
                srCourse.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
                srCourse.setErrorMessage("The option name specified does not match any option in the system");
                srCourse.setMoreDetails(e.getMessage());
            }


        }
        //////////////////////////////////////////////////////////////

        return srCourse;
    }

    @Override
    public ServerResponse<Course> updateCourseTitle(String courseId, String title)
            throws CourseNotFoundException, DuplicateCourseInLevelException {
        courseId = courseId.trim();
        title = title.toLowerCase().trim();
        ServerResponse<Course> srCourse = new ServerResponse<>();

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("There is no course in the system identify by this courseId");
        }
        Course courseToUpdate = optionalCourse.get();
        String levelName = courseToUpdate.getOwnerLevel().getName();
        String optionName  = courseToUpdate.getOwnerLevel().getOwnerOption().getName();
        String departmentName = courseToUpdate.getOwnerLevel().getOwnerOption().
                getOwnerDepartment().getName();
        String schoolName = courseToUpdate.getOwnerLevel().getOwnerOption().
                getOwnerDepartment().getOwnerSchool().getName();

        try {
            ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName,
                    departmentName, optionName, levelName, title);
            if(srCourse1.getResponseCode() == ResponseCode.COURSE_FOUND){
                throw new DuplicateCourseInLevelException("The course title designate another course " +
                        "in the level defined");
            }
            courseToUpdate.setTitle(title);
            Course courseUpdate = this.saveCourse(courseToUpdate);

            srCourse.setResponseCode(ResponseCode.COURSE_UPDATED);
            srCourse.setErrorMessage("The course  title has been well updated");
            srCourse.setAssociatedObject(courseUpdate);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srCourse.setErrorMessage("The school name specified does not match any school in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srCourse.setErrorMessage("The department name specified does not match any department in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srCourse.setErrorMessage("The option name specified does not match any option in the system");
            srCourse.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCourse.setErrorMessage("The level name specified does not match any level in the system");
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }

    @Override
    public ServerResponse<Course> setLecturerToCourse(String lecturerEmail, String courseId,
                                                      String schoolName, String departmentName,
                                                      String optionName, String levelName,
                                                      String courseTitle)
            throws StaffNotFoundException, CourseNotFoundException, StaffAlreadySetToCourseException{
        lecturerEmail = lecturerEmail.trim();
        courseId = courseId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();

        Course concernedCourse = null;
        ServerResponse<Course> srCourseFoundById = this.findCourseOfLevelById(courseId);
        if(srCourseFoundById.getResponseCode() != ResponseCode.COURSE_FOUND) {

            try {
                ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                        optionName, levelName, courseTitle);
                if (srCourse1.getResponseCode() != ResponseCode.COURSE_FOUND) {
                    throw new CourseNotFoundException("The course name specified does not match any " +
                            "course in the level");
                }
                concernedCourse = srCourse1.getAssociatedObject();

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
        }
        else{
            concernedCourse = srCourseFoundById.getAssociatedObject();
        }

        ServerResponse<Staff> srStaff = staffService.findStaffByEmail(lecturerEmail);
        if (srStaff.getResponseCode() != ResponseCode.STAFF_FOUND) {
            throw new StaffNotFoundException("The lecturer email specified does not match any " +
                    "lecturer in the system");
        }
        Staff lecturerToAdd = srStaff.getAssociatedObject();

        for (Staff staff : concernedCourse.getListofLecturer()) {
            if (staff.getEmail().equals(lecturerEmail)) {
                throw new StaffAlreadySetToCourseException("The staff specified is already set to " +
                        "that course");
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

        return srCourse;
    }

    @Override
    public ServerResponse<Course> removeLecturerToCourse(String lecturerEmail, String courseId,
                                                         String schoolName, String departmentName,
                                                         String optionName, String levelName,
                                                         String courseTitle)
            throws StaffNotFoundException, CourseNotFoundException {
        lecturerEmail = lecturerEmail.trim();
        courseId = courseId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();

        Course concernedCourse = null;
        ServerResponse<Course> srCourseFoundById = null;
        srCourseFoundById = this.findCourseOfLevelById(courseId);
        if(srCourseFoundById.getResponseCode() != ResponseCode.COURSE_FOUND) {
            try {
                ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                        optionName, levelName, courseTitle);
                if (srCourse1.getResponseCode() != ResponseCode.COURSE_FOUND) {
                    throw new CourseNotFoundException("The course name specified does not match any " +
                            "course in the level");
                }

                concernedCourse = srCourse1.getAssociatedObject();

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
        }
        else{
            concernedCourse = srCourseFoundById.getAssociatedObject();
        }

        Staff lecturerToRemove = null;
        for (Staff staff : concernedCourse.getListofLecturer()) {
            if (staff.getEmail().equals(lecturerEmail)) {
                lecturerToRemove = staff;
            }
        }

        if (lecturerToRemove == null) {
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

        return srCourse;
    }

    @Override
    public ServerResponse<CourseOutline> updateCourseOutlineTitle(String courseOutlineTitle,
                                                                  String courseId,
                                                                  String schoolName,
                                                                  String departmentName,
                                                                  String optionName,
                                                                  String levelName,
                                                                  String courseTitle)
            throws CourseNotFoundException {

        courseOutlineTitle = courseOutlineTitle.toUpperCase().trim();
        courseId = courseId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        ServerResponse<CourseOutline> srCourseOutline = new ServerResponse<>();

        Course concernedCourse = null;
        ServerResponse<Course> srCourseFoundById = null;
        srCourseFoundById = this.findCourseOfLevelById(courseId);
        if(srCourseFoundById.getResponseCode() != ResponseCode.COURSE_FOUND) {
            try {
                ServerResponse<Course> srCourse1 = this.findCourseOfLevelByTitle(schoolName,
                        departmentName, optionName, levelName, courseTitle);
                if (srCourse1.getResponseCode() != ResponseCode.COURSE_FOUND) {
                    throw new CourseNotFoundException("The course name specified does not match any " +
                            "course in the level");
                }
                concernedCourse = srCourse1.getAssociatedObject();

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
        }
        else{
            concernedCourse = srCourseFoundById.getAssociatedObject();
        }

        CourseOutline courseOutline = concernedCourse.getCourseOutline();
        courseOutline.setTitle(courseOutlineTitle);
        CourseOutline courseOutlineUpdated = courseOutlineRepository.save(courseOutline);

        srCourseOutline.setErrorMessage("The course outline title has been successfully updated");
        srCourseOutline.setResponseCode(ResponseCode.COURSEOUTLINE_UPDATED);
        srCourseOutline.setAssociatedObject(courseOutlineUpdated);

        return srCourseOutline;
    }

    public Optional<Course> findConcernedCourse(String courseId,
                                                String schoolName, String departmentName,
                                                String optionName, String levelName,
                                                String courseTitle){
        courseId = courseId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourseFoundById = null;
        Course concernedCourse = null;
        srCourseFoundById = this.findCourseOfLevelById(courseId);
        if(srCourseFoundById.getResponseCode() != ResponseCode.COURSE_FOUND) {
            ServerResponse<Course> srCourse1 = null;
            try {
                srCourse1 = this.findCourseOfLevelByTitle(schoolName, departmentName,
                        optionName, levelName, courseTitle);

                concernedCourse = srCourse1.getAssociatedObject();

            } catch (DepartmentNotFoundException e) {
                e.printStackTrace();
            } catch (SchoolNotFoundException e) {
                e.printStackTrace();
            } catch (LevelNotFoundException e) {
                e.printStackTrace();
            } catch (OptionNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            concernedCourse = srCourseFoundById.getAssociatedObject();
        }
        return Optional.ofNullable(concernedCourse);
    }

    @Override
    public ServerResponse<Course> addContentToCourse(String value, String contentType, String courseId,
                                                     String schoolName, String departmentName,
                                                     String optionName, String levelName,
                                                     String courseTitle)
            throws CourseNotFoundException{
        value = value.trim();
        contentType = contentType.trim();
        courseId = courseId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();

        Course concernedCourse = null;
        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found in the system");
        }
        concernedCourse = optionalCourse.get();
        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());
            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            /******
             * We must save the date and hour of saving this content
             * This date must include hours
             */
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);
            concernedCourse.getListofContent().add(contentSaved);
            Course courseSavedWithContent = this.saveCourse(concernedCourse);

            srCourse.setErrorMessage("A content has been added in the list of content of " +
                    "the course");
            srCourse.setResponseCode(ResponseCode.CONTENT_ADDED);
            srCourse.setAssociatedObject(courseSavedWithContent);
        } catch (IllegalArgumentException e) {
            srCourse.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srCourse.setErrorMessage("IllegalArgumentException");
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }

    @Override
    public ServerResponse<Course> removeContentToCourse(String contentId, String courseId,
                                                        String schoolName, String departmentName,
                                                        String optionName, String levelName,
                                                        String courseTitle)
            throws CourseNotFoundException, ContentNotFoundException{
        contentId = contentId.trim();
        courseId = courseId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Course> srCourse = new ServerResponse<>();

        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any content in the system");
        }
        contentRepository.deleteById(contentId);

        Course concernedCourse = null;
        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found in the system");
        }
        concernedCourse = optionalCourse.get();

        srCourse.setErrorMessage("A content has been removed in the list of content of the course");
        srCourse.setResponseCode(ResponseCode.CONTENT_DELETED);
        srCourse.setAssociatedObject(concernedCourse);

        return srCourse;
    }

    @Override
    public ServerResponse<Course> updateContentToCourse(String contentId, String value,
                                                        String courseId, String schoolName,
                                                        String departmentName, String optionName,
                                                        String levelName, String courseTitle)
            throws CourseNotFoundException, ContentNotFoundException{

        ServerResponse<Course> srCourse = new ServerResponse<>();
        contentId = contentId.trim();
        value = value.trim();
        courseId = contentId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any content in the system");
        }

        Content content = optionalContent.get();
        content.setValue(value);
        contentRepository.save(content);

        Course concernedCourse = null;
        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found in the system");
        }
        concernedCourse = optionalCourse.get();

        srCourse.setErrorMessage("A content has been updated in the list of content of the course");
        srCourse.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srCourse.setAssociatedObject(concernedCourse);

        return srCourse;
    }

    @Override
    public ServerResponse<Course> deleteCourse(String courseId, String schoolName,
                                               String departmentName, String optionName,
                                               String levelName, String courseTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException, CourseNotFoundException {
        return null;
    }


}
