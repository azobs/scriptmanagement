package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.ModuleRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ModuleServiceImpl implements ModuleService{

    ModuleRepository moduleRepository;
    ContentRepository contentRepository;
    CourseService courseService;
    LevelService levelService;
    OptionService optionService;
    DepartmentService departmentService;
    SchoolService schoolService;

    public ModuleServiceImpl(ModuleRepository moduleRepository, ContentRepository contentRepository,
                             CourseService courseService, LevelService levelService,
                             OptionService optionService, DepartmentService departmentService,
                             SchoolService schoolService) {
        this.moduleRepository = moduleRepository;
        this.contentRepository = contentRepository;
        this.courseService = courseService;
        this.levelService = levelService;
        this.optionService = optionService;
        this.departmentService = departmentService;
        this.schoolService = schoolService;
    }

    @Override
    public ServerResponse<Module> findModuleOfCourseOutlineByTitle(String schoolName,
                                                                   String departmentName,
                                                                   String optionName,
                                                                   String levelName,
                                                                   String courseTitle,
                                                                   String moduleTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();

        ServerResponse<Module> srModule = new ServerResponse<>();

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                departmentName, optionName, levelName, courseTitle);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course title does not match any course in the level defined");
        }
        Course concernedCourse = srCourse.getAssociatedObject();
        CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();

        Optional<Module> optionalModule = moduleRepository.findByOwnerCourseOutlineAndTitle(
                concernedCourseOutline, moduleTitle);

        if(!optionalModule.isPresent()){
            srModule.setErrorMessage("The module title does not match any Module in the " +
                    "courseoutline associated to the course specified");
            srModule.setResponseCode(ResponseCode.MODULE_NOT_FOUND);
        }
        else{
            srModule.setErrorMessage("The module has been successfully found in the system");
            srModule.setResponseCode(ResponseCode.MODULE_FOUND);
            srModule.setAssociatedObject(optionalModule.get());
        }

        return srModule;
    }

    @Override
    public ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String schoolName,
                                                                     String departmentName,
                                                                     String optionName,
                                                                     String levelName,
                                                                     String courseTitle,
                                                                     Pageable pageable)
            throws CourseNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();

        ServerResponse<Course> srCourse = null;
        try {
            srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle);
            if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course title does not match any course in the level defined");
            }
            Course concernedCourse = srCourse.getAssociatedObject();
            CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
            Page<Module> pageofModule = moduleRepository.findAllByOwnerCourseOutline(
                    concernedCourseOutline, pageable);
            srModulePage.setErrorMessage("The module page of courseOutline has been successfully listed");
            srModulePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srModulePage.setAssociatedObject(pageofModule);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The school specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The department specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The option specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The level specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        }


        return srModulePage;
    }

    @Override
    public ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String schoolName,
                                                                     String departmentName,
                                                                     String optionName,
                                                                     String levelName,
                                                                     String courseTitle,
                                                                     String keyword,
                                                                     Pageable pageable)
            throws CourseNotFoundException {

        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        keyword = keyword.trim();

        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();

        ServerResponse<Course> srCourse = null;
        try {
            srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle);
            if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course title does not match any course in the level defined");
            }
            Course concernedCourse = srCourse.getAssociatedObject();
            CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
            Page<Module> pageofModule = moduleRepository.findAllByOwnerCourseOutlineAndTitleContaining(
                    concernedCourseOutline, keyword, pageable);
            srModulePage.setErrorMessage("The module page of courseOutline has been successfully listed");
            srModulePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srModulePage.setAssociatedObject(pageofModule);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The school specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The department specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The option specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The level specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        }


        return srModulePage;
    }

    @Override
    public ServerResponse<List<Module>> findAllModuleOfCourseOutline(String schoolName,
                                                                     String departmentName,
                                                                     String optionName,
                                                                     String levelName,
                                                                     String courseTitle)
            throws CourseNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<List<Module>> srModuleList = new ServerResponse<>();

        ServerResponse<Course> srCourse = null;
        try {
            srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle);
            if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course title does not match any course in the level defined");
            }
            Course concernedCourse = srCourse.getAssociatedObject();
            CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
            List<Module> listofModule = moduleRepository.findAllByOwnerCourseOutlineOrderByTitle(
                    concernedCourseOutline);
            srModuleList.setErrorMessage("The module page of courseOutline has been successfully listed");
            srModuleList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srModuleList.setAssociatedObject(listofModule);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The school specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The department specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The option specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The level specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        }


        return srModuleList;
    }

    @Override
    public ServerResponse<Page<Module>> findAllModuleOfCourseOutlineByType(String schoolName,
                                                                           String departmentName,
                                                                           String optionName,
                                                                           String levelName,
                                                                           String courseTitle,
                                                                           String moduleType,
                                                                           Pageable pageable)
            throws CourseNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        EnumCoursePartType enumModuleType = EnumCoursePartType.valueOf(moduleType.toUpperCase());

        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();

        ServerResponse<Course> srCourse = null;
        try {
            srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle);
            if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course title does not match any course in the level defined");
            }
            Course concernedCourse = srCourse.getAssociatedObject();
            CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
            Page<Module> pageofModule = moduleRepository.findAllByOwnerCourseOutlineAndModuleType(
                    concernedCourseOutline, enumModuleType, pageable);
            srModulePage.setErrorMessage("The module page of courseOutline has been successfully listed");
            srModulePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srModulePage.setAssociatedObject(pageofModule);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The school specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The department specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The option specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModulePage.setErrorMessage("The level specified is not found in the system");
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModulePage.setMoreDetails(e.getMessage());
        }


        return srModulePage;
    }

    @Override
    public ServerResponse<List<Module>> findAllModuleOfCourseOutlineByType(String schoolName,
                                                                           String departmentName,
                                                                           String optionName,
                                                                           String levelName,
                                                                           String courseTitle,
                                                                           String moduleType)
            throws CourseNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        EnumCoursePartType enumModuleType = EnumCoursePartType.valueOf(moduleType.toUpperCase());

        ServerResponse<List<Module>> srModuleList = new ServerResponse<>();

        ServerResponse<Course> srCourse = null;
        try {
            srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle);
            if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("The course title does not match any course in the level defined");
            }
            Course concernedCourse = srCourse.getAssociatedObject();
            CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
            List<Module> listofModule = moduleRepository.findAllByOwnerCourseOutlineAndModuleType(
                    concernedCourseOutline, enumModuleType);
            srModuleList.setErrorMessage("The module page of courseOutline has been successfully listed");
            srModuleList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srModuleList.setAssociatedObject(listofModule);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The school specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The department specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The option specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModuleList.setErrorMessage("The level specified is not found in the system");
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModuleList.setMoreDetails(e.getMessage());
        }


        return srModuleList;
    }

    @Override
    public Module saveModule(Module module) {
        return moduleRepository.save(module);
    }

    @Override
    public ServerResponse<Module> saveModule(String title, int moduleOrder, String moduleType,
                                             String courseTitle, String levelName,
                                             String optionName, String departmentName,
                                             String schoolName)
            throws CourseNotFoundException, DuplicateModuleInCourseOutlineException {
        title = title.toLowerCase().trim();
        moduleType = moduleType.trim();
        courseTitle = courseTitle.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Module> srModule = new ServerResponse<>();

        try {
            ServerResponse<Course> srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle);
            if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
                throw new CourseNotFoundException("There is no courseOutline associated to the " +
                        "course specified. Mean the savinnd procedure had failed");
            }
            CourseOutline associatedCourseOutline = srCourse.getAssociatedObject().getCourseOutline();
            ServerResponse<Module> srModule1 = this.findModuleOfCourseOutlineByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, title);
            if(srModule1.getResponseCode() == ResponseCode.MODULE_FOUND){
                throw new DuplicateModuleInCourseOutlineException("The module title match another module title in " +
                        "the course outline of the specified course");
            }
            try {
                EnumCoursePartType enumModulePartType = EnumCoursePartType.valueOf(
                        moduleType.toUpperCase());
                List<Content> listofContent = new ArrayList<>();

                Module moduleToSaved  = new Module();
                moduleToSaved.setListofContent(listofContent);
                moduleToSaved.setModuleOrder(moduleOrder);
                moduleToSaved.setModuleType(enumModulePartType);
                moduleToSaved.setOwnerCourseOutline(associatedCourseOutline);
                moduleToSaved.setTitle(title);

                Module moduleSaved = moduleRepository.save(moduleToSaved);

                srModule.setErrorMessage("The module has been successfully created in the system");
                srModule.setResponseCode(ResponseCode.MODULE_CREATED);
                srModule.setAssociatedObject(moduleSaved);
            }
            catch (IllegalArgumentException e){
                srModule.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
                srModule.setErrorMessage("There is problem during conversion of string to enumcourseparttype");
                srModule.setMoreDetails(e.getMessage());
            }

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The school name does not match any school in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The department name does not match any school in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The option name does not match any school in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModule.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The Level name does not match any school in the system");
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }

    @Override
    public ServerResponse<Module> updateModule(String moduleId, String title, int moduleOrder,
                                               String moduleType,
                                               String courseTitle, String levelName,
                                               String optionName, String departmentName,
                                               String schoolName)
            throws DuplicateModuleInCourseOutlineException, ModuleNotFoundException {
        moduleId = moduleId.trim();
        title = title.toLowerCase().trim();
        moduleType = moduleType.trim();
        courseTitle = courseTitle.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Module> srModule = new ServerResponse<>();
        srModule.setResponseCode(ResponseCode.MODULE_NOT_UPDATED);

        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The module Id does not found any module " +
                    "in the system");
        }
        else{
            Module moduleToUpdated1 = optionalModule.get();
            try {
                EnumCoursePartType enumModulePartType = EnumCoursePartType.valueOf(
                        moduleType.toUpperCase());

                moduleToUpdated1.setModuleOrder(moduleOrder);
                moduleToUpdated1.setModuleType(enumModulePartType);

                ServerResponse<Module> srModule2 = this.findModuleOfCourseOutlineByTitle(schoolName,
                        departmentName, optionName, levelName, courseTitle, title);
                if(srModule2.getResponseCode() == ResponseCode.MODULE_FOUND){
                    Module moduleToUpdated2 = srModule2.getAssociatedObject();
                    if(!moduleToUpdated1.getId().equalsIgnoreCase(moduleToUpdated2.getId())){
                        throw new DuplicateModuleInCourseOutlineException("That module is already existed in the system");
                    }
                }
                else{
                    moduleToUpdated1.setTitle(title);
                }

                Module moduleUpdated = moduleRepository.save(moduleToUpdated1);

                srModule.setErrorMessage("The module has been successfully updated in the system");
                srModule.setResponseCode(ResponseCode.MODULE_UPDATED);
                srModule.setAssociatedObject(moduleUpdated);
            }
            catch (IllegalArgumentException e){
                srModule.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
                srModule.setErrorMessage("There is problem during conversion of string to enumcourseparttype");
                srModule.setMoreDetails(e.getMessage());
            } catch (DepartmentNotFoundException e) {
                //e.printStackTrace();
                srModule.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
                srModule.setErrorMessage("The department has not found in the system");
                srModule.setMoreDetails(e.getMessage());
            } catch (SchoolNotFoundException e) {
                //e.printStackTrace();
                srModule.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
                srModule.setErrorMessage("The school has not found in the system");
                srModule.setMoreDetails(e.getMessage());
            } catch (LevelNotFoundException e) {
                //e.printStackTrace();
                srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
                srModule.setErrorMessage("The level has not found in the system");
                srModule.setMoreDetails(e.getMessage());
            } catch (OptionNotFoundException e) {
                //e.printStackTrace();
                srModule.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
                srModule.setErrorMessage("The option has not found in the system");
                srModule.setMoreDetails(e.getMessage());
            } catch (CourseNotFoundException e) {
                //e.printStackTrace();
                srModule.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
                srModule.setErrorMessage("The course has not found in the system");
                srModule.setMoreDetails(e.getMessage());
            }
        }

        return srModule;
    }

    @Override
    public ServerResponse<Module> updateModuleTitle(String moduleId, String newModuleTitle)
            throws ModuleNotFoundException, DuplicateModuleInCourseOutlineException {

        moduleId = moduleId.trim();
        newModuleTitle = newModuleTitle.toLowerCase().trim();

        ServerResponse<Module> srModule = new ServerResponse<>();
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if(!optionalModule.isPresent()){
           throw new ModuleNotFoundException("The module ID does not match any module in the system");
        }
        Module moduleToUpdated = optionalModule.get();
        CourseOutline associatedCourseOutline = moduleToUpdated.getOwnerCourseOutline();
        try {


            ServerResponse<Course> srCourse = courseService.findCourseByCourseOutline(
                    associatedCourseOutline.getId());
            Course associatedCourse = srCourse.getAssociatedObject();
            String schoolName = associatedCourse.getOwnerLevel().getOwnerOption()
                    .getOwnerDepartment().getOwnerSchool().getName();
            String departmentName = associatedCourse.getOwnerLevel().getOwnerOption()
                    .getOwnerDepartment().getName();
            String optionName = associatedCourse.getOwnerLevel().getOwnerOption().getName();
            String levelName = associatedCourse.getOwnerLevel().getName();
            String courseTitle = associatedCourse.getTitle();

            ServerResponse<Module> srModule1 = this.findModuleOfCourseOutlineByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, newModuleTitle);

            if(srModule1.getResponseCode() == ResponseCode.MODULE_FOUND){
                throw new DuplicateModuleInCourseOutlineException("There is another module with " +
                        "the same title in the associated course outline");
            }

            moduleToUpdated.setTitle(newModuleTitle);
            Module moduleUpdated = this.saveModule(moduleToUpdated);

            srModule.setErrorMessage("The module title has been successfully updated");
            srModule.setResponseCode(ResponseCode.MODULE_UPDATED);
            srModule.setAssociatedObject(moduleUpdated);

        } catch (CourseOutlineNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_COURSEOUTLINE_FOUND);
            srModule.setErrorMessage("There is no course outline associated");
            srModule.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srModule.setErrorMessage("There is no course associated");
            srModule.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModule.setErrorMessage("The specified department does not exist");
            srModule.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModule.setErrorMessage("The specified school does not exist");
            srModule.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModule.setErrorMessage("The specified level does not exist");
            srModule.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModule.setErrorMessage("The specified option does not exist");
            srModule.setMoreDetails(e.getMessage());
        }


        return srModule;
    }

    @Override
    public ServerResponse<Module> addContentToModule(String value, String contentType,
                                                     String schoolName, String departmentName,
                                                     String optionName, String levelName,
                                                     String courseTitle, String moduleTitle)
            throws ModuleNotFoundException {
        value = value.trim();
        contentType = contentType.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        ServerResponse<Module> srModule = new ServerResponse<>();

        ServerResponse<Module> srModule1 = new ServerResponse<>();
        try {
            srModule1 = this.findModuleOfCourseOutlineByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle);
            if(srModule1.getResponseCode() != ResponseCode.MODULE_FOUND){
                throw new ModuleNotFoundException("The specified module is not found in the system");
            }
            else{
                Module concernedModule = srModule1.getAssociatedObject();
                try {
                    EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());
                    Content content = new Content();
                    content.setValue(value);
                    content.setContentType(enumContentType);
                    Content contentSaved = contentRepository.save(content);
                    concernedModule.getListofContent().add(contentSaved);

                    Module moduleSavedWithContent = this.saveModule(concernedModule);

                    srModule.setErrorMessage("A content has been added in the list of content of " +
                            "the module");
                    srModule.setResponseCode(ResponseCode.CONTENT_ADDED);
                    srModule.setAssociatedObject(moduleSavedWithContent);
                }
                catch (IllegalArgumentException e){
                    srModule.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
                    srModule.setErrorMessage("IllegalArgumentException");
                    srModule.setMoreDetails(e.getMessage());
                }
            }
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srModule.setErrorMessage("The school has not found in the system");
            srModule.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srModule.setErrorMessage("The department has not found in the system");
            srModule.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srModule.setErrorMessage("The option has not found in the system");
            srModule.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srModule.setErrorMessage("The level has not found in the system");
            srModule.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModule.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srModule.setErrorMessage("The course has not found in the system");
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }

    @Override
    public ServerResponse<Module> removeContentToModule(String contentId, String schoolName,
                                                        String departmentName, String optionName,
                                                        String levelName, String courseTitle,
                                                        String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException {

        contentId = contentId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<Module> srModule = new ServerResponse<>();

        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any " +
                    "content in the system");
        }
        contentRepository.deleteById(contentId);

        ServerResponse<Module> srModule1 = null;
        try {
            srModule1 = this.findModuleOfCourseOutlineByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle);
            if(srModule1.getResponseCode() != ResponseCode.MODULE_FOUND){
                throw new ModuleNotFoundException("The module hasn't been found in the system");
            }

            Module concernedModule = srModule1.getAssociatedObject();

            srModule.setErrorMessage("A content has been removed in the list of content of " +
                    "the module");
            srModule.setResponseCode(ResponseCode.CONTENT_DELETED);
            srModule.setAssociatedObject(concernedModule);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The school name specified does not match any school in " +
                    "the system");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The department name specified does not match any department " +
                    "in the school");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The option name specified does not match any Option in the department");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The Level name specified does not match any levem in the Option");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The course titele specified does not match any levem in the Option");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
        }

        return srModule;
    }

    @Override
    public ServerResponse<Module> updateContentToCourse(String contentId, String value,
                                                        String schoolName, String departmentName,
                                                        String optionName, String levelName,
                                                        String courseTitle, String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException {

        ServerResponse<Module> srModule = new ServerResponse<>();

        contentId = contentId.trim();
        value = value.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any " +
                    "content in the system");
        }

        Content content = optionalContent.get();
        content.setValue(value);
        contentRepository.save(content);

        try {
            ServerResponse<Module> srModule1 = this.findModuleOfCourseOutlineByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle);
            if(srModule1.getResponseCode() != ResponseCode.MODULE_FOUND){
                throw new ModuleNotFoundException("The module hasn't been found in the system");
            }
            Module concernedModule = srModule1.getAssociatedObject();

            srModule.setErrorMessage("A content has been updated in the list of content of the course");
            srModule.setResponseCode(ResponseCode.CONTENT_UPDATED);
            srModule.setAssociatedObject(concernedModule);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The school name specified does not match any school in the system");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The department name specified does not match any department in the school");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The option name specified does not match any Option in the department");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The Level name specified does not match any level in the Option");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srModule.setErrorMessage("The Course title specified does not match any course in the Level");
            srModule.setMoreDetails(e.getMessage());
            srModule.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
        }


        return srModule;
    }

    @Override
    public ServerResponse<Module> deleteModule(String schoolName, String departmentName,
                                               String optionName, String levelName,
                                               String courseTitle, String moduleTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException,  ModuleNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Module> deleteModule(String moduleId) throws ModuleNotFoundException {
        return null;
    }
}
