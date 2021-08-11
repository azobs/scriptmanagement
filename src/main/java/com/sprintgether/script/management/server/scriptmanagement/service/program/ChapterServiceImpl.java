package com.sprintgether.script.management.server.scriptmanagement.service.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.program.ChapterRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumContentType;
import com.sprintgether.script.management.server.scriptmanagement.service.school.*;
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
public class ChapterServiceImpl implements ChapterService {

    ChapterRepository chapterRepository;
    ContentRepository contentRepository;
    ModuleService moduleService;
    CourseService courseService;

    public ChapterServiceImpl(ChapterRepository chapterRepository, ContentRepository contentRepository,
                              ModuleService moduleService, CourseService courseService) {
        this.chapterRepository = chapterRepository;
        this.contentRepository = contentRepository;
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @Override
    public ServerResponse<Chapter> findChapterOfModuleById(String chapterId) {
        chapterId = chapterId.trim();
        ServerResponse<Chapter> srChapter = new ServerResponse<>();
        Optional<Chapter> optionalChapter = chapterRepository.findById(chapterId);
        if(!optionalChapter.isPresent()){
            srChapter.setErrorMessage("The chapter id does not match any chapter in the " +
                    "module associated to the course specified");
            srChapter.setResponseCode(ResponseCode.CHAPTER_NOT_FOUND);
        }
        else{
            srChapter.setErrorMessage("The chapter has been successfully found in the system");
            srChapter.setResponseCode(ResponseCode.CHAPTER_FOUND);
            srChapter.setAssociatedObject(optionalChapter.get());
        }
        return srChapter;
    }

    @Override
    public ServerResponse<Chapter> findChapterOfModuleByTitle(String schoolName, String departmentName,
                                                              String optionName, String levelName,
                                                              String courseTitle, String moduleTitle,
                                                              String chapterTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException {
        ServerResponse<Chapter> srChapter = new ServerResponse<>();

        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        ServerResponse<Module> srModule = moduleService.findModuleOfCourseOutlineByTitle(schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle);

        if(srModule.getResponseCode() != ResponseCode.MODULE_FOUND){
            throw new ModuleNotFoundException("The module title does not match any module in the " +
                    "system");
        }

        Module concernedModule = srModule.getAssociatedObject();

        Optional<Chapter> optionalChapter = chapterRepository.findByOwnerModuleAndTitle(concernedModule,
                chapterTitle);

        if(!optionalChapter.isPresent()){
            srChapter.setErrorMessage("The chapter title does not match any Chapter in the " +
                    "module associated to the course specified");
            srChapter.setResponseCode(ResponseCode.CHAPTER_NOT_FOUND);
        }
        else{
            srChapter.setErrorMessage("The module has been successfully found in the system");
            srChapter.setResponseCode(ResponseCode.CHAPTER_FOUND);
            srChapter.setAssociatedObject(optionalChapter.get());
        }

        return srChapter;
    }

    public Optional<Module> findConcernedModule(String moduleId, String schoolName,
                                                String departmentName, String optionName,
                                                String levelName, String courseTitle,
                                                String moduleTitle) {

        moduleId = moduleId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();

        Optional<Module> optionalModule = Optional.empty();

        Module concernedModule = null;
        ServerResponse<Module> srMouduleFoundById = null;

        srMouduleFoundById = moduleService.findModuleOfCourseOutlineById(moduleId);

        if(srMouduleFoundById.getResponseCode() != ResponseCode.MODULE_FOUND){
            ServerResponse<Module> srModule = null;

            try {
                srModule = moduleService.findModuleOfCourseOutlineByTitle(schoolName, departmentName,
                        optionName, levelName, courseTitle, moduleTitle);
                concernedModule = srModule.getAssociatedObject();
            } catch (SchoolNotFoundException e) {
                e.printStackTrace();
            } catch (DepartmentNotFoundException e) {
                e.printStackTrace();
            } catch (OptionNotFoundException e) {
                e.printStackTrace();
            } catch (LevelNotFoundException e) {
                e.printStackTrace();
            } catch (CourseNotFoundException e) {
                e.printStackTrace();
            }

        }
        else{
            concernedModule = srMouduleFoundById.getAssociatedObject();
        }

        optionalModule = Optional.ofNullable(concernedModule);

        return optionalModule;
    }

    @Override
    public ServerResponse<Page<Chapter>> findAllChapterOfModule(String moduleId, String schoolName,
                                                                String departmentName, String optionName,
                                                                String levelName, String courseTitle,
                                                                String moduleTitle, Pageable pageable)
            throws ModuleNotFoundException {
        ServerResponse<Page<Chapter>> srChapterPage = new ServerResponse<>();

        moduleId = moduleId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();

        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The specified module does not found on the system");
        }
        Module concernedModule = optionalModule.get();

        Page<Chapter> pageofChapter = chapterRepository.findAllByOwnerModule(concernedModule, pageable);
        srChapterPage.setErrorMessage("The chapter page of module has been successfully listed");
        srChapterPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srChapterPage.setAssociatedObject(pageofChapter);

        return srChapterPage;
    }

    @Override
    public ServerResponse<Page<Chapter>> findAllChapterOfModule(String moduleId, String schoolName,
                                                                String departmentName, String optionName,
                                                                String levelName, String courseTitle,
                                                                String moduleTitle, String keyword, Pageable pageable)
            throws ModuleNotFoundException {

        ServerResponse<Page<Chapter>> srChapterPage = new ServerResponse<>();

        moduleId = moduleId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        keyword = keyword.trim();

        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The specified module does not found on the system");
        }

        Module concernedModule = optionalModule.get();

        Page<Chapter> pageofChapter = chapterRepository.findAllByOwnerModuleAndTitleContaining(concernedModule,
                keyword, pageable);
        srChapterPage.setErrorMessage("The chapter page of module has been successfully listed");
        srChapterPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srChapterPage.setAssociatedObject(pageofChapter);

        return srChapterPage;
    }

    @Override
    public ServerResponse<Page<Chapter>> findAllChapterOfModuleByType(String moduleId, String schoolName,
                                                                      String departmentName, String optionName,
                                                                      String levelName, String courseTitle,
                                                                      String moduleTitle, String chapterType,
                                                                      Pageable pageable)
            throws ModuleNotFoundException {

        ServerResponse<Page<Chapter>> srChapterPage = new ServerResponse<>();

        moduleId = moduleId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterType = chapterType.trim();

        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The specified module does not found on the system");
        }

        Module concernedModule = optionalModule.get();

        try{
            EnumCoursePartType enumChapterType = EnumCoursePartType.valueOf(
                    chapterType.toUpperCase());

            Page<Chapter> pageofChapter = chapterRepository.findAllByOwnerModuleAndChapterType(concernedModule,
                    enumChapterType, pageable);
            srChapterPage.setErrorMessage("The chapter page of module has been successfully listed");
            srChapterPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srChapterPage.setAssociatedObject(pageofChapter);
        } catch (IllegalArgumentException e) {
            srChapterPage.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srChapterPage.setErrorMessage("IllegalArgumentException");
            srChapterPage.setMoreDetails(e.getMessage());
        }

        return srChapterPage;
    }

    @Override
    public ServerResponse<List<Chapter>> findAllChapterOfModule(String moduleId, String schoolName,
                                                                String departmentName, String optionName,
                                                                String levelName, String courseTitle,
                                                                String moduleTitle, String sortBy, String direction)
            throws ModuleNotFoundException {

        ServerResponse<List<Chapter>> srChapterList = new ServerResponse<>();

        moduleId = moduleId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();

        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The specified module does not found on the system");
        }

        Module concernedModule = optionalModule.get();

        List<Chapter> listofChapter = new ArrayList<>();
        if (sortBy.equalsIgnoreCase("title")) {
            if (direction.equalsIgnoreCase("ASC")) {
                listofChapter = chapterRepository.findAllByOwnerModuleOrderByTitleAsc(concernedModule);
            }else{
                listofChapter = chapterRepository.findAllByOwnerModuleOrderByTitleDesc(concernedModule);
            }
        }else{
            if (direction.equalsIgnoreCase("ASC")) {
                listofChapter = chapterRepository.findAllByOwnerModuleOrderByChapterOrderAsc(concernedModule);
            }
            else{
                listofChapter = chapterRepository.findAllByOwnerModuleOrderByChapterOrderDesc(concernedModule);
            }
        }

        srChapterList.setErrorMessage("The chapter list of module has been successfully listed");
        srChapterList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srChapterList.setAssociatedObject(listofChapter);

        return srChapterList;
    }

    @Override
    public boolean isChapterofModule(String chapterId, String moduleId)
            throws ModuleNotFoundException {

        ServerResponse<List<Chapter>> srListofChapterofModule =
                this.findAllChapterOfModule(moduleId, null, null,
                        null, null, null, null,
                        "chapterOrder", "ASC");
        if(srListofChapterofModule.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
            List<Chapter> listofChapterofModule = srListofChapterofModule.getAssociatedObject();
            for(Chapter chapter : listofChapterofModule){
                if(chapter.getId().equalsIgnoreCase(chapterId)) return true;
            }
        }
        return false;
    }

    @Override
    public ServerResponse<List<Chapter>> findAllChapterOfModuleByType(String moduleId, String schoolName,
                                                                      String departmentName, String optionName,
                                                                      String levelName, String courseTitle,
                                                                      String moduleTitle, String chapterType,
                                                                      String sortBy, String direction)
            throws ModuleNotFoundException {


        ServerResponse<List<Chapter>> srChapterList = new ServerResponse<>();

        moduleId = moduleId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();

        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);

        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The specified module does not found on the system");
        }

        Module concernedModule = optionalModule.get();

        try{
            EnumCoursePartType enumChapterType = EnumCoursePartType.valueOf(
                    chapterType.toUpperCase());

            List<Chapter> listofChapter = new ArrayList<>();
            if (sortBy.equalsIgnoreCase("title")) {
                if (direction.equalsIgnoreCase("ASC")) {
                    listofChapter = chapterRepository.findAllByOwnerModuleAndChapterTypeOrderByTitleAsc(concernedModule,
                            enumChapterType);
                }else{
                    listofChapter = chapterRepository.findAllByOwnerModuleAndChapterTypeOrderByTitleDesc(concernedModule,
                            enumChapterType);
                }
            }else{
                if (direction.equalsIgnoreCase("ASC")) {
                    listofChapter = chapterRepository.findAllByOwnerModuleAndChapterTypeOrderByChapterOrderAsc(
                            concernedModule, enumChapterType);
                }
                else{
                    listofChapter = chapterRepository.findAllByOwnerModuleAndChapterTypeOrderByChapterOrderDesc(
                            concernedModule, enumChapterType);
                }
            }

            srChapterList.setErrorMessage("The chapter list of module has been successfully listed");
            srChapterList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srChapterList.setAssociatedObject(listofChapter);
        } catch (IllegalArgumentException e) {
            srChapterList.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srChapterList.setErrorMessage("IllegalArgumentException");
            srChapterList.setMoreDetails(e.getMessage());
        }


        return srChapterList;
    }

    @Override
    public Chapter saveChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    @Override
    public ServerResponse<Chapter> saveChapter(String title,
                                               int chapterOrder,
                                               String chapterType,
                                               String moduleId,
                                               String moduleTitle,
                                               String courseTitle,
                                               String levelName,
                                               String optionName,
                                               String departmentName,
                                               String schoolName)
            throws ModuleNotFoundException, DuplicateChapterInModuleException {
        ServerResponse<Chapter> srChapter = new ServerResponse<>();

        title = title.toLowerCase().trim();
        chapterType = chapterType.trim();
        moduleId = moduleId.trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        Module concernedModule = null;
        Optional<Module> optionalModule = this.findConcernedModule(moduleId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle);
        if(!optionalModule.isPresent()){
            throw new ModuleNotFoundException("The specified module does not found in the system");
        }
        concernedModule = optionalModule.get();

        try {
            ServerResponse<Chapter> srChapterDuplicated = this.findChapterOfModuleByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle, title);
            if(srChapterDuplicated.getResponseCode() == ResponseCode.CHAPTER_FOUND){
                throw new DuplicateChapterInModuleException("There is another chapter with the same title in " +
                        "the module specified");
            }
            EnumCoursePartType enumChapterPartType = EnumCoursePartType.valueOf(
                    chapterType.toUpperCase());
            List<Content> listofContent = new ArrayList<>();

            Chapter chapterToSaved = new Chapter();
            chapterToSaved.setChapterOrder(chapterOrder);
            chapterToSaved.setChapterType(enumChapterPartType);
            chapterToSaved.setListofContent(listofContent);
            chapterToSaved.setOwnerModule(concernedModule);
            chapterToSaved.setTitle(title);

            Chapter chapterSaved = chapterRepository.save(chapterToSaved);

            srChapter.setErrorMessage("The new chater has been successfully created in the system");
            srChapter.setResponseCode(ResponseCode.CHAPTER_CREATED);
            srChapter.setAssociatedObject(chapterSaved);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The specified school does not found in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The specified department does not found in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The specified option does not found in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The specified level does not found in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The specified course does not found in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        }


        return srChapter;
    }

    @Override
    public ServerResponse<Chapter> updateChapter(String chapterId,
                                                 String title,
                                                 int chapterOrder,
                                                 String chapterType,
                                                 String moduleTitle,
                                                 String courseTitle,
                                                 String levelName,
                                                 String optionName,
                                                 String departmentName,
                                                 String schoolName)
            throws ChapterNotFoundException, DuplicateChapterInModuleException {
        ServerResponse<Chapter> srChapter = new ServerResponse<>();

        title = title.toLowerCase().trim();
        chapterType = chapterType.trim();
        chapterId = chapterId.trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        Optional<Chapter> optionalChapter = chapterRepository.findById(chapterId);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found in the system");
        }
        Chapter chapterToUpdate1 = optionalChapter.get();

        try {
            ServerResponse<Chapter> srChapterDuplicated = this.findChapterOfModuleByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle, title);
            if(srChapterDuplicated.getResponseCode() == ResponseCode.CHAPTER_FOUND){
                Chapter chapterToUpdate2 = srChapterDuplicated.getAssociatedObject();
                if(!chapterToUpdate1.getId().equalsIgnoreCase(chapterToUpdate2.getId())) {
                    throw new DuplicateChapterInModuleException("There is another chapter with the same title in " +
                            "the module specified");
                }
            }
            else{
                chapterToUpdate1.setTitle(title);
            }
            EnumCoursePartType enumChapterPartType = EnumCoursePartType.valueOf(
                    chapterType.toUpperCase());

            chapterToUpdate1.setChapterOrder(chapterOrder);
            chapterToUpdate1.setChapterType(enumChapterPartType);

            Chapter chapterUpdated = chapterRepository.save(chapterToUpdate1);

            srChapter.setErrorMessage("The chapter has been successfully updated");
            srChapter.setResponseCode(ResponseCode.CHAPTER_UPDATED);
            srChapter.setAssociatedObject(chapterUpdated);

        } catch (IllegalArgumentException e){
            srChapter.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srChapter.setErrorMessage("There is problem during conversion of string to enumcourseparttype");
            srChapter.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srChapter.setErrorMessage("The specified school does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srChapter.setErrorMessage("The specified department does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srChapter.setErrorMessage("The specified option does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srChapter.setErrorMessage("The specified level does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srChapter.setErrorMessage("The specified course does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srChapter.setErrorMessage("The specified module does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        }


        return srChapter;
    }

    @Override
    public ServerResponse<Chapter> updateChapterTitle(String chapterId, String newChapterTitle)
            throws ChapterNotFoundException, DuplicateChapterInModuleException {
        ServerResponse<Chapter> srChapter = new ServerResponse<>();
        chapterId = chapterId.trim();
        newChapterTitle = newChapterTitle.toLowerCase().trim();

        Optional<Chapter> optionalChapter = chapterRepository.findById(chapterId);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found in the system");
        }
        Chapter chapterToUpdate = optionalChapter.get();

        Module concernedModule = chapterToUpdate.getOwnerModule();
        CourseOutline associatedCourseOutline = concernedModule.getOwnerCourseOutline();
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
            String moduleTitle = concernedModule.getTitle();

            ServerResponse<Chapter> srChapter1 = this.findChapterOfModuleByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, newChapterTitle);
            if(srChapter1.getResponseCode() == ResponseCode.CHAPTER_FOUND){
                throw new DuplicateChapterInModuleException("The new chapter name already designed a " +
                        "chapter in the module specified");
            }
            chapterToUpdate.setTitle(newChapterTitle);

            Chapter chapterUpdated = chapterRepository.save(chapterToUpdate);

            srChapter.setErrorMessage("The chapter title has been successfully updated");
            srChapter.setResponseCode(ResponseCode.CHAPTER_UPDATED);
            srChapter.setAssociatedObject(chapterUpdated);

        } catch (CourseOutlineNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The course outline associated is not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_COURSEOUTLINE_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The course  associated is not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The department associated is not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The school associated is not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The level associated is not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The option associated is not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The module associated is not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        }


        return srChapter;
    }

    public Optional<Chapter> findConcernedChapter(String chapterId, String schoolName,
                                                  String departmentName, String optionName,
                                                  String levelName, String courseTitle,
                                                  String moduleTitle, String chapterTitle){
        chapterId = chapterId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        Chapter concernedChapter = null;

        ServerResponse<Chapter> srChapterFoundById = this.findChapterOfModuleById(chapterId);
        if(srChapterFoundById.getResponseCode() != ResponseCode.CHAPTER_FOUND){
            try {
                ServerResponse<Chapter> srChapter = this.findChapterOfModuleByTitle(schoolName, departmentName, optionName,
                        levelName, courseTitle, moduleTitle, chapterTitle);
                concernedChapter = srChapter.getAssociatedObject();
            } catch (SchoolNotFoundException e) {
                e.printStackTrace();
            } catch (DepartmentNotFoundException e) {
                e.printStackTrace();
            } catch (OptionNotFoundException e) {
                e.printStackTrace();
            } catch (LevelNotFoundException e) {
                e.printStackTrace();
            } catch (CourseNotFoundException e) {
                e.printStackTrace();
            } catch (ModuleNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            concernedChapter = srChapterFoundById.getAssociatedObject();
        }
        return Optional.ofNullable(concernedChapter);
    }

    @Override
    public ServerResponse<Chapter> addContentToChapter(String value, String contentType,
                                                       String chapterId, String schoolName,
                                                       String departmentName, String optionName,
                                                       String levelName, String courseTitle,
                                                       String moduleTitle, String chapterTitle)
            throws ChapterNotFoundException {
        ServerResponse<Chapter> srChapter = new ServerResponse<>();

        value = value.trim();
        contentType = contentType.trim();
        chapterId = chapterId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        Chapter concernedChapter = null;
        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found in the system");
        }
        concernedChapter = optionalChapter.get();

        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);
            concernedChapter.getListofContent().add(contentSaved);

            Chapter chapterSavedWithContent = this.saveChapter(concernedChapter);
            srChapter.setErrorMessage("A content has been added in the list of content of " +
                    "the chapter");
            srChapter.setResponseCode(ResponseCode.CONTENT_ADDED);
            srChapter.setAssociatedObject(chapterSavedWithContent);
        } catch (IllegalArgumentException e) {
            srChapter.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srChapter.setErrorMessage("IllegalArgumentException");
            srChapter.setMoreDetails(e.getMessage());
        }

        return srChapter;
    }

    @Override
    public ServerResponse<Chapter> removeContentToChapter(String contentId, String chapterId, String schoolName,
                                                          String departmentName, String optionName, String levelName,
                                                          String courseTitle, String moduleTitle, String chapterTitle)
            throws ChapterNotFoundException, ContentNotFoundException {
        ServerResponse<Chapter> srChapter = new ServerResponse<>();

        contentId = contentId.trim();
        chapterId = chapterId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any " +
                    "content in the system");
        }
        contentRepository.deleteById(contentId);

        Chapter concernedChapter = null;
        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found in the system");
        }
        concernedChapter = optionalChapter.get();

        srChapter.setErrorMessage("A content has been removed in the list of content of " +
                "the chapter");
        srChapter.setResponseCode(ResponseCode.CONTENT_DELETED);
        srChapter.setAssociatedObject(concernedChapter);

        return srChapter;
    }

    @Override
    public ServerResponse<Chapter> updateContentToChapter(String contentId, String value,
                                                          String chapterId, String schoolName,
                                                          String departmentName, String optionName,
                                                          String levelName, String courseTitle,
                                                          String moduleTitle, String chapterTitle)
            throws ChapterNotFoundException, ContentNotFoundException {
        ServerResponse<Chapter> srChapter = new ServerResponse<>();
        contentId = contentId.trim();
        value = value.trim();
        chapterId = chapterId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any " +
                    "content in the system");
        }

        Content content = optionalContent.get();
        content.setValue(value);
        contentRepository.save(content);

        Chapter concernedChapter = null;

        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName, departmentName, optionName,
                levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found in the system");
        }
        concernedChapter = optionalChapter.get();

        srChapter.setErrorMessage("A content has been updated in the list of content of " +
                "the chapter");
        srChapter.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srChapter.setAssociatedObject(concernedChapter);

        return srChapter;
    }

    @Override
    public ServerResponse<Chapter> deleteChapter(String chapterId, String schoolName, String departmentName,
                                                 String optionName, String levelName, String courseTitle,
                                                 String moduleTitle, String chapterTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException, ChapterNotFoundException {
        return null;
    }
}
