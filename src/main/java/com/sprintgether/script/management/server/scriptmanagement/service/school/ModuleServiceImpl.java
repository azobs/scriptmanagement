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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ModuleServiceImpl implements ModuleService{

    ModuleRepository moduleRepository;
    ContentRepository contentRepository;
    CourseService courseService;

    public ModuleServiceImpl(ModuleRepository moduleRepository, ContentRepository contentRepository,
                             CourseService courseService) {
        this.moduleRepository = moduleRepository;
        this.contentRepository = contentRepository;
        this.courseService = courseService;
    }

    @Override
    public ServerResponse<Module> findModuleOfCourseOutlineById(String moduleId) {
        moduleId = moduleId.trim();
        ServerResponse<Module> srModule = new ServerResponse<>();
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if(!optionalModule.isPresent()){
            srModule.setErrorMessage("The module id does not match any Module in the " +
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
            throw new CourseNotFoundException("The course title does not match any course in the " +
                    "level defined");
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

    public Optional<Course> findConcernedCourse(String courseId,
                                                String schoolName,
                                                String departmentName,
                                                String optionName,
                                                String levelName,
                                                String courseTitle){
        Optional<Course> optionalCourse = Optional.empty();

        courseId = courseId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        Course concernedCourse = null;
        ServerResponse<Course> srCourseFoundById = null;
        srCourseFoundById = courseService.findCourseOfLevelById(courseId);
        if(srCourseFoundById.getResponseCode() != ResponseCode.COURSE_FOUND) {
            ServerResponse<Course> srCourse = null;
            try {
                srCourse = courseService.findCourseOfLevelByTitle(schoolName,
                        departmentName, optionName, levelName, courseTitle);

                concernedCourse = srCourse.getAssociatedObject();

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

        optionalCourse = Optional.ofNullable(concernedCourse);

        return optionalCourse;
    }

    @Override
    public ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String courseId,
                                                                     String schoolName,
                                                                     String departmentName,
                                                                     String optionName,
                                                                     String levelName,
                                                                     String courseTitle,
                                                                     Pageable pageable)
            throws CourseNotFoundException {
        courseId = courseId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        Course concernedCourse = null;
        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();

        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found on the system");
        }
        concernedCourse = optionalCourse.get();

        CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
        Page<Module> pageofModule = moduleRepository.findAllByOwnerCourseOutline(
                concernedCourseOutline, pageable);
        srModulePage.setErrorMessage("The module page of courseOutline has been successfully listed");
        srModulePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srModulePage.setAssociatedObject(pageofModule);

        return srModulePage;
    }


    @Override
    public ServerResponse<Page<Module>> findAllModuleOfCourseOutline(String courseId,
                                                                     String schoolName,
                                                                     String departmentName,
                                                                     String optionName,
                                                                     String levelName,
                                                                     String courseTitle,
                                                                     String keyword,
                                                                     Pageable pageable)
            throws CourseNotFoundException {

        courseId = courseId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        keyword = keyword.trim();

        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();
        Course concernedCourse = null;

        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found on the system");
        }
        concernedCourse = optionalCourse.get();

        CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
        Page<Module> pageofModule = moduleRepository.findAllByOwnerCourseOutlineAndTitleContaining(
                concernedCourseOutline, keyword, pageable);
        srModulePage.setErrorMessage("The module page of courseOutline has been successfully listed");
        srModulePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srModulePage.setAssociatedObject(pageofModule);

        return srModulePage;
    }


    @Override
    public ServerResponse<List<Module>> findAllModuleOfCourseOutline(String courseId,
                                                                     String schoolName,
                                                                     String departmentName,
                                                                     String optionName,
                                                                     String levelName,
                                                                     String courseTitle,
                                                                     String sortBy,
                                                                     String direction)
            throws CourseNotFoundException {
        courseId = courseId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        ServerResponse<List<Module>> srModuleList = new ServerResponse<>();
        Course concernedCourse = null;

        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found on the system");
        }
        concernedCourse = optionalCourse.get();

        CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
        List<Module> listofModule = null;
        if (sortBy.equalsIgnoreCase("title")) {
            if (direction.equalsIgnoreCase("ASC")) {
                /*********
                 * The module are classified in title order
                 */
                listofModule = moduleRepository.findAllByOwnerCourseOutlineOrderByTitleAsc(
                        concernedCourseOutline);
            } else {
                listofModule = moduleRepository.findAllByOwnerCourseOutlineOrderByTitleDesc(
                        concernedCourseOutline);
            }
        } else {
            /**********
             * The module are classified in moduleOrder order
             */
            if (direction.equalsIgnoreCase("ASC")) {
                listofModule = moduleRepository.findAllByOwnerCourseOutlineOrderByModuleOrderAsc(
                        concernedCourseOutline);
            } else {
                listofModule = moduleRepository.findAllByOwnerCourseOutlineOrderByModuleOrderDesc(
                        concernedCourseOutline);
            }
        }

        srModuleList.setErrorMessage("The module list of courseOutline has been successfully listed");
        srModuleList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srModuleList.setAssociatedObject(listofModule);

        return srModuleList;
    }


    @Override
    public ServerResponse<Page<Module>> findAllModuleOfCourseOutlineByType(String courseId,
                                                                           String schoolName,
                                                                           String departmentName,
                                                                           String optionName,
                                                                           String levelName,
                                                                           String courseTitle,
                                                                           String moduleType,
                                                                           Pageable pageable)
            throws CourseNotFoundException {
        courseId = courseId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        Course concernedCourse = null;
        ServerResponse<Page<Module>> srModulePage = new ServerResponse<>();

        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found on the system");
        }
        concernedCourse = optionalCourse.get();

        try{
            EnumCoursePartType enumModuleType = EnumCoursePartType.valueOf(
                    moduleType.toUpperCase());

            CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();
            Page<Module> pageofModule = moduleRepository.findAllByOwnerCourseOutlineAndModuleType(
                    concernedCourseOutline, enumModuleType, pageable);
            srModulePage.setErrorMessage("The module page of courseOutline has been successfully listed");
            srModulePage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srModulePage.setAssociatedObject(pageofModule);
        } catch (IllegalArgumentException e) {
            srModulePage.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srModulePage.setErrorMessage("IllegalArgumentException");
            srModulePage.setMoreDetails(e.getMessage());
        }

        return srModulePage;
    }



    @Override
    public ServerResponse<List<Module>> findAllModuleOfCourseOutlineByType(String courseId,
                                                                           String schoolName,
                                                                           String departmentName,
                                                                           String optionName,
                                                                           String levelName,
                                                                           String courseTitle,
                                                                           String moduleType,
                                                                           String sortBy,
                                                                           String direction)
            throws CourseNotFoundException {
        courseId = courseId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();

        Course concernedCourse = null;
        ServerResponse<List<Module>> srModuleList = new ServerResponse<>();

        Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                levelName, courseTitle);

        if(!optionalCourse.isPresent()){
            throw new CourseNotFoundException("The specified course does not found on the system");
        }
        concernedCourse = optionalCourse.get();

        try{
            EnumCoursePartType enumModuleType = EnumCoursePartType.valueOf(moduleType.toUpperCase());
            CourseOutline concernedCourseOutline = concernedCourse.getCourseOutline();

            List<Module> listofModule = null;
            if (sortBy.equalsIgnoreCase("title")) {
                if (direction.equalsIgnoreCase("ASC")) {
                    listofModule = moduleRepository.findAllByOwnerCourseOutlineAndModuleTypeOrderByTitleAsc(
                            concernedCourseOutline, enumModuleType);
                } else {
                    listofModule = moduleRepository.findAllByOwnerCourseOutlineAndModuleTypeOrderByTitleDesc(
                            concernedCourseOutline, enumModuleType);
                }
            } else {
                if (direction.equalsIgnoreCase("ASC")) {
                    listofModule = moduleRepository.findAllByOwnerCourseOutlineAndModuleTypeOrderByModuleOrderAsc(
                            concernedCourseOutline, enumModuleType);
                } else {
                    listofModule = moduleRepository.findAllByOwnerCourseOutlineAndModuleTypeOrderByModuleOrderDesc(
                            concernedCourseOutline, enumModuleType);
                }
            }

            srModuleList.setErrorMessage("The module page of courseOutline has been successfully listed");
            srModuleList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srModuleList.setAssociatedObject(listofModule);
        } catch (IllegalArgumentException e) {
            srModuleList.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srModuleList.setErrorMessage("IllegalArgumentException");
            srModuleList.setMoreDetails(e.getMessage());
        }

        return srModuleList;
    }


    @Override
    public Module saveModule(Module module) {
        return moduleRepository.save(module);
    }

    @Override
    public ServerResponse<Module> saveModule(String title,
                                             int moduleOrder,
                                             String moduleType,
                                             String courseId,
                                             String courseTitle,
                                             String levelName,
                                             String optionName,
                                             String departmentName,
                                             String schoolName)
            throws CourseNotFoundException, DuplicateModuleInCourseOutlineException {
        title = title.toLowerCase().trim();
        moduleType = moduleType.trim();
        courseId = courseId.trim();
        courseTitle = courseTitle.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Module> srModule = new ServerResponse<>();

        try{
            CourseOutline associatedCourseOutline = null;

            Optional<Course> optionalCourse = this.findConcernedCourse(courseId, schoolName, departmentName, optionName,
                    levelName, courseTitle);

            if(!optionalCourse.isPresent()){
                throw new CourseNotFoundException("The specified course does not found on the system so no courseOutline");
            }
            associatedCourseOutline = optionalCourse.get().getCourseOutline();

            ServerResponse<Module> srModule1 = this.findModuleOfCourseOutlineByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, title);
            if(srModule1.getResponseCode() == ResponseCode.MODULE_FOUND){
                throw new DuplicateModuleInCourseOutlineException("The module title match another module title in " +
                        "the course outline of the specified course");
            }

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

        } catch (IllegalArgumentException e){
            srModule.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srModule.setErrorMessage("There is problem during conversion of string to enumcourseparttype");
            srModule.setMoreDetails(e.getMessage());
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
                    throw new DuplicateModuleInCourseOutlineException("That module is already " +
                            "existed in the system");
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

    public Optional<Module> findConcernedModule(String moduleId, String schoolName, String departmentName,
                                                String optionName, String levelName,
                                                String courseTitle, String moduleTitle){
        Optional<Module> optionalModule = Optional.empty();
        moduleId = moduleId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();

        Module concernedModule = null;
        ServerResponse<Module> srModuleFoundById = null;
        srModuleFoundById = this.findModuleOfCourseOutlineById(moduleId);
        if(srModuleFoundById.getResponseCode() != ResponseCode.MODULE_FOUND) {
            ServerResponse<Module> srModule1 = null;
            try {
                srModule1 = this.findModuleOfCourseOutlineByTitle(schoolName, departmentName,
                        optionName, levelName, courseTitle, moduleTitle);
                concernedModule = srModule1.getAssociatedObject();
            } catch (CourseNotFoundException e) {
                e.printStackTrace();
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
            concernedModule = srModuleFoundById.getAssociatedObject();
        }

        optionalModule = Optional.ofNullable(concernedModule);

        return optionalModule;
    }

    @Override
    public ServerResponse<Module> addContentToModule(String value, String contentType, String moduleId,
                                                     String schoolName, String departmentName,
                                                     String optionName, String levelName,
                                                     String courseTitle, String moduleTitle)
            throws ModuleNotFoundException {
        value = value.trim();
        contentType = contentType.trim();
        moduleId = moduleId.trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        ServerResponse<Module> srModule = new ServerResponse<>();

        Module concernedModule = null;
        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The module specified does not found on the system");
        }
        concernedModule = optionalModule.get();

        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());
            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);
            concernedModule.getListofContent().add(contentSaved);

            Module moduleSavedWithContent = this.saveModule(concernedModule);

            srModule.setErrorMessage("A content has been added in the list of content of " +
                    "the module");
            srModule.setResponseCode(ResponseCode.CONTENT_ADDED);
            srModule.setAssociatedObject(moduleSavedWithContent);
        } catch (IllegalArgumentException e) {
            srModule.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srModule.setErrorMessage("IllegalArgumentException");
            srModule.setMoreDetails(e.getMessage());
        }

        return srModule;
    }

    @Override
    public ServerResponse<Module> removeContentToModule(String contentId, String moduleId,
                                                        String schoolName, String departmentName,
                                                        String optionName, String levelName,
                                                        String courseTitle, String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException {

        contentId = contentId.trim();
        moduleId = moduleId.trim();
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

        Module concernedModule = null;
        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The module specified does not found on the system");
        }
        concernedModule = optionalModule.get();

        srModule.setErrorMessage("A content has been removed in the list of content of " +
                "the module");
        srModule.setResponseCode(ResponseCode.CONTENT_DELETED);
        srModule.setAssociatedObject(concernedModule);

        return srModule;
    }

    @Override
    public ServerResponse<Module> updateContentToCourse(String contentId, String value,
                                                        String moduleId, String schoolName,
                                                        String departmentName, String optionName,
                                                        String levelName, String courseTitle,
                                                        String moduleTitle)
            throws ModuleNotFoundException, ContentNotFoundException {

        ServerResponse<Module> srModule = new ServerResponse<>();

        contentId = contentId.trim();
        value = value.trim();
        moduleId = moduleId.trim();
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

        Module concernedModule = null;
        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The module specified does not found on the system");
        }
        concernedModule = optionalModule.get();

        srModule.setErrorMessage("A content has been updated in the list of content of the course");
        srModule.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srModule.setAssociatedObject(concernedModule);

        return srModule;
    }

    @Override
    public ServerResponse<Module> deleteModule(String moduleId, String schoolName,
                                               String departmentName, String optionName,
                                               String levelName, String courseTitle,
                                               String moduleTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException,  ModuleNotFoundException {
        return null;
    }

}
