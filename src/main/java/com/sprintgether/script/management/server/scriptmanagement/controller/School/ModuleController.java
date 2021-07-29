package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.form.School.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Module;
import com.sprintgether.script.management.server.scriptmanagement.service.school.ModuleService;
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

    public Pageable getModulePageable(ModuleFormList moduleFormList){

        Sort.Order order1 = new Sort.Order(moduleFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, moduleFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(moduleFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, moduleFormList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(moduleFormList.getPageNumber(), moduleFormList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/modulePageOfCourse")
    public ServerResponse<Page<Module>> getModulePageOfCourse(@Valid @RequestBody ModuleFormList moduleFormList,
                                                      BindingResult bindingResult) {
        ServerResponse<Page<Module>> srModulepage = new ServerResponse<>();
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Module>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the CourseFormList for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getModulePageable(moduleFormList);
        String schoolName = moduleFormList.getSchoolName();
        String departmentName = moduleFormList.getDepartmentName();
        String optionName = moduleFormList.getOptionName();
        String levelName = moduleFormList.getLevelName();
        String courseTitle = moduleFormList.getCourseTitle();
        String keyword = moduleFormList.getKeyword();

        if(!moduleFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            try {
                srModulepage = moduleService.findAllModuleOfCourseOutline(schoolName, departmentName,
                        optionName, levelName, courseTitle, keyword, sort);

            } catch (CourseNotFoundException e) {
                //e.printStackTrace();
                srModulepage.setErrorMessage("The course has not found in the system");
                srModulepage.setMoreDetails(e.getMessage());
                srModulepage.setResponseCode(ResponseCode.MODULE_NOT_FOUND);
            }
        }

        try {
            srModulepage =  moduleService.findAllModuleOfCourseOutline(schoolName, departmentName,
                    optionName, levelName, courseTitle, sort);
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModulepage.setErrorMessage("The course has not found in the system");
            srModulepage.setMoreDetails(e.getMessage());
            srModulepage.setResponseCode(ResponseCode.MODULE_NOT_FOUND);
        }

        return srModulepage;
    }


    @GetMapping(path = "/modulePageOfCourseByType")
    public ServerResponse<Page<Module>> getModulePageOfCourseByType(@Valid @RequestBody ModuleFormList moduleFormList,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Module>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the moduleFormList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getModulePageable(moduleFormList);
        String schoolName = moduleFormList.getSchoolName();
        String departmentName = moduleFormList.getDepartmentName();
        String optionName = moduleFormList.getOptionName();
        String levelName = moduleFormList.getLevelName();
        String courseTitle = moduleFormList.getCourseTitle();
        String moduleType = moduleFormList.getModuleType().trim();
        String keyword = moduleFormList.getKeyword();

        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();
        try {
            if(!moduleFormList.getKeyword().equalsIgnoreCase("")){
                srModulePage = moduleService.findAllModuleOfCourseOutline(schoolName,
                        departmentName, optionName, levelName, courseTitle, keyword, sort);
            }
            else if(moduleType.trim().equalsIgnoreCase("ALL")){
                srModulePage = moduleService.findAllModuleOfCourseOutline(schoolName, departmentName,
                        optionName, levelName, courseTitle, sort);
            }
            else{
                srModulePage = moduleService.findAllModuleOfCourseOutlineByType(schoolName,
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
    public ServerResponse<List<Module>> getModuleListOfCourseByType(@Valid @RequestBody ModuleFormList moduleFormList,
                                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Module>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the moduleFormList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String schoolName = moduleFormList.getSchoolName().toLowerCase().trim();
        String departmentName = moduleFormList.getDepartmentName().toLowerCase().trim();
        String optionName = moduleFormList.getOptionName().toLowerCase().trim();
        String levelName = moduleFormList.getLevelName().toLowerCase().trim();
        String courseTitle = moduleFormList.getCourseTitle().toLowerCase().trim();
        String moduleType = moduleFormList.getModuleType().trim();

        ServerResponse<List<Module>> srModuleList = new ServerResponse<>();
        try {
            if(moduleType.trim().equalsIgnoreCase("ALL")){
                srModuleList = moduleService.findAllModuleOfCourseOutline(schoolName, departmentName,
                        optionName, levelName, courseTitle);
            }
            else{
                srModuleList = moduleService.findAllModuleOfCourseOutlineByType(schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleType);
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
    public ServerResponse<Module> getModule(@Valid @RequestBody ModuleFormList moduleFormList){
        ServerResponse<Module> srModule = new ServerResponse<>();
        String schoolName = moduleFormList.getSchoolName();
        String departmentName = moduleFormList.getDepartmentName();
        String optionName = moduleFormList.getOptionName();
        String levelName = moduleFormList.getLevelName();
        String courseTitle = moduleFormList.getCourseTitle();
        String moduleTitle = moduleFormList.getModuleTitle();
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
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModule.setMoreDetails(e.getMessage());
        }
        return srModule;
    }

    @PostMapping(path = "/moduleSaved")
    public ServerResponse<Module> postModuleSaved(@Valid @RequestBody ModuleForm moduleForm,
                                                  BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = moduleForm.getTitle();
        int moduleOrder = moduleForm.getModuleOrder();
        String moduleType = moduleForm.getModuleType();
        String courseTitle = moduleForm.getCourseTitle();
        String levelName = moduleForm.getOwnerLevel();
        String optionName = moduleForm.getOwnerOption();
        String departmentName = moduleForm.getOwnerDepartment();
        String schoolName = moduleForm.getOwnerSchool();


        try {

            srModule = moduleService.saveModule(title, moduleOrder, moduleType, courseTitle, levelName, optionName, departmentName, schoolName);
            srModule.setErrorMessage("The course has beenh successfully created");
        }  catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The level name does not match any level in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (DuplicateModuleInCourseOutlineException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The level name does not match any level in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MOUDULE_DUPLICATED);
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }


    @PutMapping(path = "/moduleUpdated")
    public ServerResponse<Module> postModuleUpdated(@Valid @RequestBody ModuleForm moduleForm,
                                                  BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String moduleId = moduleForm.getModuleId();
        String title = moduleForm.getTitle();
        int moduleOrder = moduleForm.getModuleOrder();
        String moduleType = moduleForm.getModuleType();
        String courseTitle = moduleForm.getCourseTitle();
        String levelName = moduleForm.getOwnerLevel();
        String optionName = moduleForm.getOwnerOption();
        String departmentName = moduleForm.getOwnerDepartment();
        String schoolName = moduleForm.getOwnerSchool();

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
            srModule.setResponseCode(ResponseCode.EXCEPTION_MOUDULE_DUPLICATED);
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }


    @PutMapping(path = "/moduleTitleUpdated")
    public ServerResponse<Module> postModuleTitleUpdated(@Valid @RequestBody ModuleForm moduleForm,
                                                    BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String moduleId = moduleForm.getModuleId();
        String newTitle = moduleForm.getTitle();

        try {
            srModule = moduleService.updateModuleTitle(moduleId, newTitle);
            srModule.setErrorMessage("The module has been successfully updated");
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The module title does not match any module in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (DuplicateModuleInCourseOutlineException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The module title already exist in the course outline of the course specified");
            srModule.setResponseCode(ResponseCode.EXCEPTION_MOUDULE_DUPLICATED);
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }

    @PostMapping(path = "/addContentToModule")
    public ServerResponse<Module> postAddContentToModule(@Valid @RequestBody ModuleContentForm moduleContentForm,
                                                         BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleContentForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = moduleContentForm.getValue();
        String contentType = moduleContentForm.getContentType();
        String schoolName = moduleContentForm.getOwnerSchool();
        String departmentName = moduleContentForm.getOwnerDepartment();
        String optionName = moduleContentForm.getOwnerOption();
        String levelName = moduleContentForm.getOwnerLevel();
        String courseTitle = moduleContentForm.getCourseTitle();
        String moduleTitle = moduleContentForm.getModuleTitle();

        try {
            srModule = moduleService.addContentToModule(value, contentType, schoolName,
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
    public ServerResponse<Module> putUpdateContentToModule(@Valid @RequestBody ModuleContentForm moduleContentForm,
                                                         BindingResult bindingResult) {
        ServerResponse<Module> srModule = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Module>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleContentForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = moduleContentForm.getValue();
        String contentId = moduleContentForm.getContentId();
        String schoolName = moduleContentForm.getOwnerSchool();
        String departmentName = moduleContentForm.getOwnerDepartment();
        String optionName = moduleContentForm.getOwnerOption();
        String levelName = moduleContentForm.getOwnerLevel();
        String courseTitle = moduleContentForm.getCourseTitle();
        String moduleTitle = moduleContentForm.getModuleTitle();

        try {
            srModule = moduleService.updateContentToCourse(contentId, value, schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle);
            srModule.setErrorMessage("The content has been successfully added to the module");
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srModule.setErrorMessage("There is proble during the creation of content");
            srModule.setMoreDetails(e.getMessage());
        } catch (ContentNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srModule.setErrorMessage("There is proble during the creation of content");
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }


    @PostMapping(path = "/removeContentToModule")
    public ServerResponse<Module> postRemoveContentToModule(@Valid @RequestBody ModuleContentForm moduleContentForm,
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

        String contentId = moduleContentForm.getContentId();
        String schoolName = moduleContentForm.getOwnerSchool();
        String departmentName = moduleContentForm.getOwnerDepartment();
        String optionName = moduleContentForm.getOwnerOption();
        String levelName = moduleContentForm.getOwnerLevel();
        String courseTitle = moduleContentForm.getCourseTitle();
        String moduleTitle = moduleContentForm.getModuleTitle();

        try {
            srModule = moduleService.removeContentToModule(contentId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle);
            srModule.setErrorMessage("The content has been successfully removed to the module");
        } catch (ContentNotFoundException e) {
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
