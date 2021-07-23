package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.School.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.school.CourseOutline;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Level;
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

    public Pageable getCoursePageable(CourseFormList courseFormList){

        Sort.Order order1 = new Sort.Order(courseFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, courseFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(courseFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, courseFormList.getSortBy2());

        Sort.Order order3 = new Sort.Order(courseFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, courseFormList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        Pageable sort = PageRequest.of(courseFormList.getPageNumber(), courseFormList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/coursePage")
    public ServerResponse<Page<Course>> getCoursePage(@Valid @RequestBody CourseFormList courseFormList,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Course>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the CourseFormList for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getCoursePageable(courseFormList);

        if(!courseFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return courseService.findAllCourse(courseFormList.getKeyword(), sort);
        }

        return courseService.findAllCourse(sort);
    }

    @GetMapping(path = "/coursePageOfLevel")
    public ServerResponse<Page<Course>> getCoursePageOfLevel(@Valid @RequestBody CourseFormList courseFormList,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Course>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        Pageable sort = this.getCoursePageable(courseFormList);
        String schoolName = courseFormList.getSchoolName();
        String departmentName = courseFormList.getDepartmentName();
        String optionName = courseFormList.getOptionName();
        String levelName = courseFormList.getOptionName();

        ServerResponse<Page<Course>> srCoursePage = new ServerResponse<>();
        try {
            if(!courseFormList.getKeyword().equalsIgnoreCase("")){
                srCoursePage = courseService.findAllCourseOfLevel(schoolName, departmentName,
                        optionName, levelName, courseFormList.getKeyword(), sort);
            }
            else{
                srCoursePage = courseService.findAllCourseOfLevel(schoolName, departmentName,
                        optionName, levelName, sort);
            }
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCoursePage.setErrorMessage("The associated level has not found");
            srCoursePage.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCoursePage.setMoreDetails(e.getMessage());
        }

        return srCoursePage;
    }

    @GetMapping(path = "/courseListOfLevel")
    public ServerResponse<List<Course>> getCourseListOfLevel(@Valid @RequestBody CourseFormList courseFormList,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Course>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the CourseFormList for search",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        String schoolName = courseFormList.getSchoolName();
        String departmentName = courseFormList.getDepartmentName();
        String optionName = courseFormList.getOptionName();
        String levelName = courseFormList.getOptionName();

        ServerResponse<List<Course>> srCourseList = new ServerResponse<>();
        try {
            srCourseList = courseService.findAllCourseOfLevel(schoolName, departmentName, optionName, levelName);
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourseList.setErrorMessage("The associated level has not found");
            srCourseList.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCourseList.setMoreDetails(e.getMessage());
        }
        return srCourseList;
    }


    @GetMapping(path = "/course")
    public ServerResponse<Course> getCourse(@Valid @RequestBody CourseFormList courseFormList){
        ServerResponse<Course> srCourse = new ServerResponse<>();
        try {
            srCourse = courseService.findCourseOfLevelByTitle(courseFormList.getSchoolName(),
                    courseFormList.getDepartmentName(), courseFormList.getOptionName(),
                    courseFormList.getLevelName(), courseFormList.getCourseTitle());
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
    public ServerResponse<Course> postCourseSaved(@Valid @RequestBody CourseForm courseForm,
                                                BindingResult bindingResult) {
        ServerResponse<Course> srCourse = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Course>(error.getDefaultMessage(),
                        "Some entry are not well filled in the courseForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srCourse = courseService.saveCourse(courseForm.getTitle(), courseForm.getCourseCode(),
                    courseForm.getNbreCredit(), courseForm.getLevelName(),
                    courseForm.getOptionName(), courseForm.getDepartmentName(),
                    courseForm.getSchoolName(), courseForm.getCourseOutlineTitle());
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
    public ServerResponse<Course> postCourseUpdated(@Valid @RequestBody CourseForm courseForm,
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
            srCourse = courseService.updateCourse(courseForm.getTitle(), courseForm.getCourseCode(),
                    courseForm.getNbreCredit(), courseForm.getLevelName(), courseForm.getOptionName(),
                    courseForm.getDepartmentName(), courseForm.getSchoolName());
            srCourse.setErrorMessage("The Course has been successfully updated");
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The level name does not match any level in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srCourse.setErrorMessage("The course name does not match any course in the system");
            srCourse.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srCourse.setMoreDetails(e.getMessage());
        }

        return srCourse;
    }

    @PutMapping(path = "/courseOutlineUpdated")
    public ServerResponse<CourseOutline> postCourseOutlineUpdated(@Valid @RequestBody CourseForm courseForm,
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
            srCourseOutline = courseService.updateCourseOutlineTitle(courseForm.getCourseOutlineTitle(),
                    courseForm.getSchoolName(), courseForm.getDepartmentName(), courseForm.getOptionName(),
                    courseForm.getLevelName(), courseForm.getTitle());
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
    public ServerResponse<Course> postSetLecturerToCourse(@Valid @RequestBody CourseStaffForm courseStaffForm,
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
            srCourse = courseService.setLecturerToCourse(courseStaffForm.getLecturerEmail(), courseStaffForm.getSchoolName(), courseStaffForm.getDepartmentName(), courseStaffForm.getOptionName(), courseStaffForm.getLevelName(), courseStaffForm.getCourseTitle());
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
    public ServerResponse<Course> postRemoveLecturerToCourse(@Valid @RequestBody CourseStaffForm courseStaffForm,
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
            srCourse = courseService.removeLecturerToCourse(courseStaffForm.getLecturerEmail(), courseStaffForm.getSchoolName(), courseStaffForm.getDepartmentName(), courseStaffForm.getOptionName(), courseStaffForm.getLevelName(), courseStaffForm.getCourseTitle());
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


}
