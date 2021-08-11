package com.sprintgether.script.management.server.scriptmanagement.controller.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.form.program.module.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.service.program.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/school")
public class ModuleController {
    ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    public Pageable getModulePageable(ModuleList moduleList){

        Sort.Order order1 = new Sort.Order(moduleList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, moduleList.getSortBy1());

        Sort.Order order2 = new Sort.Order(moduleList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, moduleList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(moduleList.getPageNumber(), moduleList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/modulePageOfCourse")
    public ServerResponse<Page<Module>> getModulePageOfCourse(@Valid @RequestBody ModuleList moduleList,
                                                              BindingResult bindingResult) {
        ServerResponse<Page<Module>> srModulepage = new ServerResponse<>();
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Module>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the CourseList for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getModulePageable(moduleList);
        String courseId = moduleList.getCourseId();
        String schoolName = moduleList.getSchoolName();
        String departmentName = moduleList.getDepartmentName();
        String optionName = moduleList.getOptionName();
        String levelName = moduleList.getLevelName();
        String courseTitle = moduleList.getCourseTitle();
        String keyword = moduleList.getKeyword();

        if(!moduleList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            try {
                srModulepage = moduleService.findAllModuleOfCourseOutline(courseId, schoolName,
                        departmentName, optionName, levelName, courseTitle, keyword, sort);

            } catch (CourseNotFoundException e) {
                //e.printStackTrace();
                srModulepage.setErrorMessage("The course has not found in the system");
                srModulepage.setMoreDetails(e.getMessage());
                srModulepage.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            }
        }
        else {
            try {
                srModulepage = moduleService.findAllModuleOfCourseOutline(courseId, schoolName,
                        departmentName, optionName, levelName, courseTitle, sort);
            } catch (CourseNotFoundException e) {
                //e.printStackTrace();
                srModulepage.setErrorMessage("The course has not found in the system");
                srModulepage.setMoreDetails(e.getMessage());
                srModulepage.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            }
        }

        return srModulepage;
    }


    @GetMapping(path = "/modulePageOfCourseByType")
    public ServerResponse<Page<Module>> getModulePageOfCourseByType(@Valid @RequestBody ModuleList moduleList,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Module>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the moduleList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getModulePageable(moduleList);
        String courseId = moduleList.getCourseId();
        String schoolName = moduleList.getSchoolName();
        String departmentName = moduleList.getDepartmentName();
        String optionName = moduleList.getOptionName();
        String levelName = moduleList.getLevelName();
        String courseTitle = moduleList.getCourseTitle();
        String moduleType = moduleList.getModuleType().trim();
        String keyword = moduleList.getKeyword();

        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();
        try {
            if(!moduleList.getKeyword().equalsIgnoreCase("")){
                srModulePage = moduleService.findAllModuleOfCourseOutline(schoolName,
                        departmentName, optionName, levelName, courseTitle, keyword, sort);
            }
            else if(moduleType.trim().equalsIgnoreCase("ALL")){
                srModulePage = moduleService.findAllModuleOfCourseOutline(courseId, schoolName,
                        departmentName, optionName, levelName, courseTitle, sort);
            }
            else{
                srModulePage = moduleService.findAllModuleOfCourseOutlineByType(courseId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleType, sort);
            }
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The associated level has not found");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        }

        return srModulePage;
    }

    @GetMapping(path = "/moduleListOfCourseByType")
    public ServerResponse<List<Module>> getModuleListOfCourseByType(@Valid @RequestBody ModuleList moduleList,
                                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Module>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the moduleList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String courseId = moduleList.getCourseId();
        String schoolName = moduleList.getSchoolName().toLowerCase().trim();
        String departmentName = moduleList.getDepartmentName().toLowerCase().trim();
        String optionName = moduleList.getOptionName().toLowerCase().trim();
        String levelName = moduleList.getLevelName().toLowerCase().trim();
        String courseTitle = moduleList.getCourseTitle().toLowerCase().trim();
        String moduleType = moduleList.getModuleType().trim();

        ServerResponse<List<Module>> srModuleList = new ServerResponse<>();
        try {
            if(moduleType.trim().equalsIgnoreCase("ALL")){
                srModuleList = moduleService.findAllModuleOfCourseOutline(courseId, schoolName,
                        departmentName, optionName, levelName, courseTitle,
                        moduleList.getSortBy1(), moduleList.getDirection1());
            }
            else{
                srModuleList = moduleService.findAllModuleOfCourseOutlineByType(courseId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleType,
                        moduleList.getSortBy1(), moduleList.getDirection1());
            }
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The associated level has not found");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        }

        return srModuleList;
    }

    @GetMapping(path = "/module")
    public ServerResponse<Module> getModule(@Valid @RequestBody ModuleList moduleList){
        ServerResponse<Module> srModule = new ServerResponse<>();
        String schoolName = moduleList.getSchoolName();
        String departmentName = moduleList.getDepartmentName();
        String optionName = moduleList.getOptionName();
        String levelName = moduleList.getLevelName();
        String courseTitle = moduleList.getCourseTitle();
        String moduleTitle = moduleList.getModuleTitle();
        try {
            srModule = moduleService.findModuleOfCourseOutlineByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The associated school has not found");
            srModule.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The associated department has not found");
            srModule.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The associated option has not found");
            srModule.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The associated level has not found");
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The associated course has not found");
            srModule.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srModule.setMoreDetails(e.getMessage());
        }
        return srModule;
    }

    @PostMapping(path = "/moduleSaved")
    public ServerResponse<Module> postModuleSaved(@Valid @RequestBody ModuleSaved moduleSaved,
                                                  BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = moduleSaved.getTitle();
        int moduleOrder = moduleSaved.getModuleOrder();
        String moduleType = moduleSaved.getModuleType();
        String courseId = moduleSaved.getCourseId();
        String courseTitle = moduleSaved.getCourseTitle();
        String levelName = moduleSaved.getOwnerLevel();
        String optionName = moduleSaved.getOwnerOption();
        String departmentName = moduleSaved.getOwnerDepartment();
        String schoolName = moduleSaved.getOwnerSchool();


        try {

            srModule = moduleService.saveModule(title, moduleOrder, moduleType, courseId, courseTitle,
                    levelName, optionName, departmentName, schoolName);
            srModule.setErrorMessage("The module has been successfully created");
        }  catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The course title does not match any course in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (DuplicateModuleInCourseOutlineException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The module already exist in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_DUPLICATED);
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }


    @PutMapping(path = "/moduleUpdated")
    public ServerResponse<Module> postModuleUpdated(@Valid @RequestBody ModuleUpdated moduleUpdated,
                                                  BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String moduleId = moduleUpdated.getModuleId();
        String title = moduleUpdated.getTitle();
        int moduleOrder = moduleUpdated.getModuleOrder();
        String moduleType = moduleUpdated.getModuleType();
        String courseTitle = moduleUpdated.getCourseTitle();
        String levelName = moduleUpdated.getOwnerLevel();
        String optionName = moduleUpdated.getOwnerOption();
        String departmentName = moduleUpdated.getOwnerDepartment();
        String schoolName = moduleUpdated.getOwnerSchool();

        try {
            srModule = moduleService.updateModule(moduleId, title, moduleOrder, moduleType,
                    courseTitle, levelName, optionName, departmentName, schoolName);
            srModule.setErrorMessage("The module has been successfully updated");
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The module title does not match any module in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (DuplicateModuleInCourseOutlineException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The module title already exist in the course outline of the course specified");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_DUPLICATED);
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }


    @PutMapping(path = "/moduleTitleUpdated")
    public ServerResponse<Module> postModuleTitleUpdated(@Valid @RequestBody ModuleTitleUpdated moduleTitleUpdated,
                                                    BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String moduleId = moduleTitleUpdated.getModuleId();
        String newModuleTitle = moduleTitleUpdated.getNewModuleTitle();

        try {
            srModule = moduleService.updateModuleTitle(moduleId, newModuleTitle);
            srModule.setErrorMessage("The module has been successfully updated");
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The module title does not match any module in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (DuplicateModuleInCourseOutlineException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The module title already exist in the course outline of the course specified");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_DUPLICATED);
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }

    @PostMapping(path = "/addContentToModule")
    public ServerResponse<Module> postAddContentToModule(@Valid @RequestBody ModuleContentSaved moduleContentSaved,
                                                         BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = moduleContentSaved.getValue();
        String contentType = moduleContentSaved.getContentType();
        String moduleId = moduleContentSaved.getModuleId();
        String schoolName = moduleContentSaved.getOwnerSchool();
        String departmentName = moduleContentSaved.getOwnerDepartment();
        String optionName = moduleContentSaved.getOwnerOption();
        String levelName = moduleContentSaved.getOwnerLevel();
        String courseTitle = moduleContentSaved.getCourseTitle();
        String moduleTitle = moduleContentSaved.getModuleTitle();

        try {
            srModule = moduleService.addContentToModule(value, contentType, moduleId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle);
            srModule.setErrorMessage("The content has been successfully added to the module");
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_CONTENT_ADDED);
            srModule.setErrorMessage("There is proble during the creation of content");
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }

    @PutMapping(path = "/updateContentToModule")
    public ServerResponse<Module> putUpdateContentToModule(@Valid @RequestBody ModuleContentUpdated moduleContentUpdated,
                                                         BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = moduleContentUpdated.getValue();
        String contentId = moduleContentUpdated.getContentId();
        String moduleId = moduleContentUpdated.getModuleId();
        String schoolName = moduleContentUpdated.getOwnerSchool();
        String departmentName = moduleContentUpdated.getOwnerDepartment();
        String optionName = moduleContentUpdated.getOwnerOption();
        String levelName = moduleContentUpdated.getOwnerLevel();
        String courseTitle = moduleContentUpdated.getCourseTitle();
        String moduleTitle = moduleContentUpdated.getModuleTitle();

        try {
            srModule = moduleService.updateContentToModule(contentId, value, moduleId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle);
            srModule.setErrorMessage("The content has been successfully added to the module");
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srModule.setErrorMessage("There is proble during the creation of content");
            srModule.setMoreDetails(e.getMessage());
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srModule.setErrorMessage("There is proble during the creation of content");
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }


    @PostMapping(path = "/removeContentToModule")
    public ServerResponse<Module> postRemoveContentToModule(@Valid @RequestBody ModuleContentDeleted moduleContentDeleted,
                                                            BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String contentId = moduleContentDeleted.getContentId();
        String moduleId = moduleContentDeleted.getModuleId();
        String schoolName = moduleContentDeleted.getOwnerSchool();
        String departmentName = moduleContentDeleted.getOwnerDepartment();
        String optionName = moduleContentDeleted.getOwnerOption();
        String levelName = moduleContentDeleted.getOwnerLevel();
        String courseTitle = moduleContentDeleted.getCourseTitle();
        String moduleTitle = moduleContentDeleted.getModuleTitle();

        try {
            srModule = moduleService.removeContentToModule(contentId, moduleId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle);
            srModule.setErrorMessage("The content has been successfully removed to the module");
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srModule.setErrorMessage("The content id does not match any content in the system");
            srModule.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srModule.setErrorMessage("The module does not found in the system");
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }





}
