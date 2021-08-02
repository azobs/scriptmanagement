package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.school.course.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.school.CourseOutline;
import com.sprintgether.script.management.server.scriptmanagement.service.school.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/school")
public class CourseController {

    CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    public Pageable getCoursePageable(CourseList courseList){

        Sort.Order order1 = new Sort.Order(courseList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, courseList.getSortBy1());

        Sort.Order order2 = new Sort.Order(courseList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, courseList.getSortBy2());

        Sort.Order order3 = new Sort.Order(courseList.getDirection3().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, courseList.getSortBy3());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        Pageable sort = PageRequest.of(courseList.getPageNumber(), courseList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/coursePage")
    public ServerResponse<Page<Course>> getCoursePage(@Valid @RequestBody CourseList courseList,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Course>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the CourseList for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getCoursePageable(courseList);

        if(!courseList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return courseService.findAllCourse(courseList.getKeyword(), sort);
        }

        return courseService.findAllCourse(sort);
    }

    @GetMapping(path = "/coursePageOfLevelByType")
    public ServerResponse<Page<Course>> getCoursePageOfLevelByType(@Valid @RequestBody CourseList courseList,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Course>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the courseList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        Pageable sort = this.getCoursePageable(courseList);
        String schoolName = courseList.getSchoolName();
        String departmentName = courseList.getDepartmentName();
        String optionName = courseList.getOptionName();
        String levelName = courseList.getLevelName();
        String courseType = courseList.getCourseType().trim();
        String courseId = courseList.getCourseId();

        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();
        try {
            if(!courseList.getKeyword().equalsIgnoreCase("")){
                srCoursePage = courseService.findAllCourseOfLevel(schoolName, departmentName,
                        optionName, levelName, courseList.getKeyword().trim(), sort);
            }
            else if(courseType.trim().equalsIgnoreCase("ALL")){
                srCoursePage = courseService.findAllCourseOfLevel(courseId, schoolName, departmentName,
                        optionName, levelName, sort);
            }
            else{
                srCoursePage = courseService.findAllCourseOfLevelByType(courseId, schoolName, departmentName,
                        optionName, levelName, courseType, sort);
            }
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The associated level has not found");
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCoursePage.setMoreDetails(e.getMessage());
        }

        return srCoursePage;
    }

    @GetMapping(path = "/courseListOfLevelByType")
    public ServerResponse<List<Course>> getCourseListOfLevelByType(@Valid @RequestBody CourseList courseList,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Course>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the CourseList for search",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        String schoolName = courseList.getSchoolName().toLowerCase().trim();
        String departmentName = courseList.getDepartmentName().toLowerCase().trim();
        String optionName = courseList.getOptionName().toLowerCase().trim();
        String levelName = courseList.getOptionName().toLowerCase().trim();
        String courseType = courseList.getCourseType().trim();
        String sortBy = courseList.getSortBy1();
        String direction = courseList.getDirection1();
        String courseId = courseList.getCourseId();

        ServerResponse<List<Course>> srCourseList = new ServerResponse<>();
        try {
            if(courseType.trim().equalsIgnoreCase("ALL")) {
                srCourseList = courseService.findAllCourseOfLevel(courseId, schoolName, departmentName,
                        optionName, levelName, sortBy, direction);
            }
            else{
                srCourseList = courseService.findAllCourseOfLevelByType(courseId, schoolName, departmentName,
                        optionName, levelName, courseType, sortBy, direction);
            }
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourseList.setErrorMessage("The associated level has not found");
            srCourseList.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCourseList.setMoreDetails(e.getMessage());
        }
        return srCourseList;
    }


    @GetMapping(path = "/course")
    public ServerResponse<Course> getCourse(@Valid @RequestBody CourseList courseList){
        ServerResponse<Course> srCourse = new ServerResponse<>();
        try {
            srCourse = courseService.findCourseOfLevelByTitle(courseList.getSchoolName(),
                    courseList.getDepartmentName(), courseList.getOptionName(),
                    courseList.getLevelName(), courseList.getCourseTitle());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The associated school has not found");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The associated department has not found");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The associated option has not found");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The associated level has not found");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        }
        return srCourse;
    }

    @GetMapping(path = "/courseList")
    public ServerResponse<List<Course>> getCourseList(){
        ServerResponse<List<Course>> srListCourse = new ServerResponse<>();
        srListCourse = courseService.findAllCourse();
        List<Course> listofCourse = srListCourse.getAssociatedObject();
        Collections.sort(listofCourse, new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                if(o1.getTitle().compareToIgnoreCase(o2.getTitle())<0) return -1;
                if(o1.getTitle().compareToIgnoreCase(o2.getTitle())>0) return 1;
                return 0;
            }
        });
        srListCourse.setAssociatedObject(listofCourse);
        return srListCourse;
    }

    @PostMapping(path = "/courseSaved")
    public ServerResponse<Course> postCourseSaved(@Valid @RequestBody CourseSaved courseSaved,
                                                BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the courseSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.saveCourse(courseSaved.getTitle(), courseSaved.getCourseCode(),
                    courseSaved.getNbreCredit(), courseSaved.getCourseType(), courseSaved.getLevelId(),
                    courseSaved.getOwnerLevel(), courseSaved.getOwnerOption(),
                    courseSaved.getOwnerDepartment(), courseSaved.getOwnerSchool(),
                    courseSaved.getCourseOutlineTitle());
            srCourse.setErrorMessage("The course has been successfully created");
        } catch (DuplicateCourseInLevelException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course title already exists in the level specified");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_COURSE_SAVED);
            srCourse.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The level name does not match any level in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }


    @PutMapping(path = "/courseUpdated")
    public ServerResponse<Course> postCourseUpdated(@Valid @RequestBody CourseUpdated courseUpdated,
                                                  BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.updateCourse(courseUpdated.getCourseId(),
                    courseUpdated.getTitle(), courseUpdated.getCourseCode(),
                    courseUpdated.getNbreCredit(), courseUpdated.getCourseType(),
                    courseUpdated.getOwnerLevel(), courseUpdated.getOwnerOption(),
                    courseUpdated.getOwnerDepartment(), courseUpdated.getOwnerSchool());
            srCourse.setErrorMessage("The Course has been successfully updated");
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course name does not match any course in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (DuplicateCourseInLevelException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course name already exist in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_COURSE_DUPLICATED);
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }

    @PutMapping(path = "/courseTitleUpdated")
    public ServerResponse<Course> postCourseTitleUpdated(@Valid @RequestBody CourseTitleUpdated courseTitleUpdated,
                                                    BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String newTitle = courseTitleUpdated.getNewCourseTitle();
        String courseId = courseTitleUpdated.getCourseId();

        try {
            srCourse = courseService.updateCourseTitle(courseId, newTitle);
            srCourse.setErrorMessage("The Course title has been successfully updated");
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course name does not match any course in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (DuplicateCourseInLevelException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course name already exist in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_COURSE_DUPLICATED);
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }


    @PutMapping(path = "/courseOutlineUpdated")
    public ServerResponse<CourseOutline> postCourseOutlineUpdated(
            @Valid @RequestBody CourseOutlineUpdated courseOutlineUpdated,
                                                    BindingResult bindingResult) {
        ServerResponse<CourseOutline> srCourseOutline = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<CourseOutline>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourseOutline = courseService.updateCourseOutlineTitle(
                    courseOutlineUpdated.getCourseOutlineTitle(), courseOutlineUpdated.getCourseId(),
                    courseOutlineUpdated.getOwnerSchool(), courseOutlineUpdated.getOwnerDepartment(),
                    courseOutlineUpdated.getOwnerOption(), courseOutlineUpdated.getOwnerLevel(),
                    courseOutlineUpdated.getCourseTitle());
            srCourseOutline.setErrorMessage("The Course has been successfully updated");
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourseOutline.setErrorMessage("The course name does not match any course in the system");
            srCourseOutline.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srCourseOutline.setMoreDetails(e.getMessage());
        }

        return srCourseOutline;
    }

    @PostMapping(path = "/setLecturerToCourse")
    public ServerResponse<Course> postSetLecturerToCourse(@Valid @RequestBody CourseStaff courseStaff,
                                                    BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.setLecturerToCourse(courseStaff.getLecturerEmail(),
                    courseStaff.getCourseId(), courseStaff.getOwnerSchool(),
                    courseStaff.getOwnerDepartment(), courseStaff.getOwnerOption(),
                    courseStaff.getOwnerLevel(), courseStaff.getCourseTitle());
            srCourse.setErrorMessage("The course is well assigned to the precised staff");
        } catch (StaffNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The staff email does not match any staff in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_STAFF_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course title does not match any course in the level");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_STAFF_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (StaffAlreadySetToCourseException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course spacified is already assigned to the precised staff lecturer");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_STAFF_COURSE_EXIST);
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }

    @PostMapping(path = "/removeLecturerToCourse")
    public ServerResponse<Course> postRemoveLecturerToCourse(@Valid @RequestBody CourseStaff courseStaff,
                                                          BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.removeLecturerToCourse(courseStaff.getLecturerEmail(),
                    courseStaff.getCourseId(), courseStaff.getOwnerSchool(),
                    courseStaff.getOwnerDepartment(), courseStaff.getOwnerOption(),
                    courseStaff.getOwnerLevel(), courseStaff.getCourseTitle());
            srCourse.setErrorMessage("The course is well assigned to the precised staff");
        } catch (StaffNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The staff email does not match any staff in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_STAFF_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course title does not match any course in the level");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_STAFF_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }


    @PostMapping(path = "/addContentToCourse")
    public ServerResponse<Course> postAddContentToCourse(@Valid @RequestBody CourseContentSaved courseContentSaved,
                                                             BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.addContentToCourse(courseContentSaved.getValue(),
                    courseContentSaved.getContentType(), courseContentSaved.getCourseId(),
                    courseContentSaved.getOwnerSchool(), courseContentSaved.getOwnerDepartment(),
                    courseContentSaved.getOwnerOption(), courseContentSaved.getOwnerLevel(),
                    courseContentSaved.getCourseTitle());
            srCourse.setErrorMessage("The content has been successfully added to the course");
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_CONTENT_ADDED);
            srCourse.setErrorMessage("There is proble during the creation of content");
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }

    @PutMapping(path = "/updateContentToCourse")
    public ServerResponse<Course> putUpdateContentToCourse(@Valid @RequestBody CourseContentUpdated courseContentUpdated,
                                                         BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.updateContentToCourse(courseContentUpdated.getContentId(),
                    courseContentUpdated.getValue(), courseContentUpdated.getCourseId(),
                    courseContentUpdated.getOwnerSchool(), courseContentUpdated.getOwnerDepartment(),
                    courseContentUpdated.getOwnerOption(), courseContentUpdated.getOwnerLevel(),
                    courseContentUpdated.getCourseTitle());
            srCourse.setErrorMessage("The content has been successfully added to the course");
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srCourse.setErrorMessage("There is problem during the creation of content");
            srCourse.setMoreDetails(e.getMessage());
        } catch (ContentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srCourse.setErrorMessage("The content id does not match any content in the system");
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }


    @PostMapping(path = "/removeContentToCourse")
    public ServerResponse<Course> postRemoveContentToCourse(@Valid @RequestBody CourseContentDeleted courseContentDeleted,
                                                            BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.removeContentToCourse(courseContentDeleted.getContentId(),
                    courseContentDeleted.getCourseId(), courseContentDeleted.getOwnerSchool(),
                    courseContentDeleted.getOwnerDepartment(), courseContentDeleted.getOwnerOption(),
                    courseContentDeleted.getOwnerLevel(), courseContentDeleted.getCourseTitle());
            srCourse.setErrorMessage("The content has been successfully removed to the course");
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_CONTENT_ADDED);
            srCourse.setErrorMessage("There is proble during the creation of content");
            srCourse.setMoreDetails(e.getMessage());
        } catch (ContentNotFoundException e) {
            //e.printStackTrace();
            srCourse.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srCourse.setErrorMessage("The content id does not match any content in the system");
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }


}
