package com.sprintgether.script.management.server.scriptmanagement.controller.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.form.program.chapter.*;
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
public class ChapterController {
    ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    public Pageable getChapterPageable(ChapterList chapterList){

        Sort.Order order1 = new Sort.Order(chapterList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, chapterList.getSortBy1());

        Sort.Order order2 = new Sort.Order(chapterList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, chapterList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(chapterList.getPageNumber(), chapterList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/chapterPageOfModule")
    public ServerResponse<Page<Chapter>> getChapterPageOfModule(
            @Valid @RequestBody ChapterList chapterList,
            BindingResult bindingResult) {
        ServerResponse<Page<Chapter>> srChapterpage = new ServerResponse<>();
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Chapter>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the ChapterList for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getChapterPageable(chapterList);
        String moduleId = chapterList.getModuleId();
        String moduleTitle = chapterList.getModuleTitle();
        String schoolName = chapterList.getSchoolName();
        String departmentName = chapterList.getDepartmentName();
        String optionName = chapterList.getOptionName();
        String levelName = chapterList.getLevelName();
        String courseTitle = chapterList.getCourseTitle();
        String keyword = chapterList.getKeyword();

        if(!chapterList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            try {
                srChapterpage = chapterService.findAllChapterOfModule(moduleId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, keyword, sort);
            } catch (ModuleNotFoundException e) {
                //e.printStackTrace();
                srChapterpage.setErrorMessage("The module has not found in the system");
                srChapterpage.setMoreDetails(e.getMessage());
                srChapterpage.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            }
        }
        else{
            try {
                srChapterpage = chapterService.findAllChapterOfModule(moduleId,  schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, sort);
            } catch (ModuleNotFoundException e) {
                //e.printStackTrace();
                srChapterpage.setErrorMessage("The module has not found in the system");
                srChapterpage.setMoreDetails(e.getMessage());
                srChapterpage.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            }
        }

        return  srChapterpage;
    }


    @GetMapping(path = "/chapterPageOfModuleByType")
    public ServerResponse<Page<Chapter>> getChapterPageOfModuleByType(
            @Valid @RequestBody ChapterList chapterList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Chapter>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the moduleList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getChapterPageable(chapterList);
        String moduleId = chapterList.getModuleId();
        String moduleTitle = chapterList.getModuleTitle();
        String schoolName = chapterList.getSchoolName();
        String departmentName = chapterList.getDepartmentName();
        String optionName = chapterList.getOptionName();
        String levelName = chapterList.getLevelName();
        String courseTitle = chapterList.getCourseTitle();
        String chapterType = chapterList.getChapterType().trim();
        String keyword = chapterList.getKeyword();

        ServerResponse<Page<Chapter>> srChapterPage = new ServerResponse<>();

        try {
            if(!chapterList.getKeyword().equalsIgnoreCase("")){
                srChapterPage = chapterService.findAllChapterOfModule(schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, keyword, sort);
            }
            else if(chapterType.trim().equalsIgnoreCase("ALL")){
                srChapterPage = chapterService.findAllChapterOfModule(moduleId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, sort);
            }
            else{
                srChapterPage = chapterService.findAllChapterOfModuleByType(moduleId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, chapterType, sort);
            }
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srChapterPage.setErrorMessage("The associated module has not found");
            srChapterPage.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srChapterPage.setMoreDetails(e.getMessage());
        }

        return srChapterPage;
    }

    @GetMapping(path = "/chapterListOfModuleByType")
    public ServerResponse<List<Chapter>> getChapterListOfModuleByType(
            @Valid @RequestBody ChapterList chapterList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Chapter>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the moduleList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String moduleId = chapterList.getModuleId();
        String moduleTitle = chapterList.getModuleTitle();
        String schoolName = chapterList.getSchoolName().toLowerCase().trim();
        String departmentName = chapterList.getDepartmentName().toLowerCase().trim();
        String optionName = chapterList.getOptionName().toLowerCase().trim();
        String levelName = chapterList.getLevelName().toLowerCase().trim();
        String courseTitle = chapterList.getCourseTitle().toLowerCase().trim();
        String chapterType = chapterList.getChapterType().trim();

        ServerResponse<List<Chapter>> srChapterList = new ServerResponse<>();
        try {
            if(chapterType.trim().equalsIgnoreCase("ALL")){
                srChapterList = chapterService.findAllChapterOfModule(moduleId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterList.getSortBy1(), chapterList.getDirection1());
            }
            else{
                srChapterList = chapterService.findAllChapterOfModuleByType(moduleId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, chapterType,
                        chapterList.getSortBy1(), chapterList.getDirection1());
            }
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srChapterList.setErrorMessage("The associated module has not found");
            srChapterList.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srChapterList.setMoreDetails(e.getMessage());
        }
        return  srChapterList;
    }

    @GetMapping(path = "/chapter")
    public ServerResponse<Chapter> getChapter(@Valid @RequestBody ChapterList chapterList){
        ServerResponse<Chapter> srChapter = new ServerResponse<>();
        String schoolName = chapterList.getSchoolName();
        String departmentName = chapterList.getDepartmentName();
        String optionName = chapterList.getOptionName();
        String levelName = chapterList.getLevelName();
        String courseTitle = chapterList.getCourseTitle();
        String moduleTitle = chapterList.getModuleTitle();
        String chapterTitle = chapterList.getChapterTitle();
        try {
            srChapter = chapterService.findChapterOfModuleByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The associated school has not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The associated department has not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The associated option has not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The associated level has not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The associated course has not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The associated module has not found");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        }
        return srChapter;
    }

    @PostMapping(path = "/chapterSaved")
    public ServerResponse<Chapter> postChapterSaved(@Valid @RequestBody ChapterSaved chapterSaved,
                                                  BindingResult bindingResult) {
        ServerResponse<Chapter> srChapter = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Chapter>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = chapterSaved.getTitle();
        int chapterOrder = chapterSaved.getChapterOrder();
        String chapterType = chapterSaved.getChapterType();
        String moduleId = chapterSaved.getModuleId();
        String moduleTitle = chapterSaved.getModuleTitle();
        String courseTitle = chapterSaved.getCourseTitle();
        String levelName = chapterSaved.getOwnerLevel();
        String optionName = chapterSaved.getOwnerOption();
        String departmentName = chapterSaved.getOwnerDepartment();
        String schoolName = chapterSaved.getOwnerSchool();

        try {
            srChapter = chapterService.saveChapter(title, chapterOrder, chapterType, moduleId, moduleTitle, courseTitle,
                    levelName, optionName, departmentName, schoolName);
            srChapter.setErrorMessage("The chapter has been successfully created");
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The module title does not match any module in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (DuplicateChapterInModuleException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The chapter title already exist in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_DUPLICATED);
            srChapter.setMoreDetails(e.getMessage());
        }

        return srChapter;
    }

    @PutMapping(path = "/chapterUpdated")
    public ServerResponse<Chapter> postChapterUpdated(@Valid @RequestBody ChapterUpdated chapterUpdated,
                                                    BindingResult bindingResult) {
        ServerResponse<Chapter> srChapter = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Chapter>(error.getDefaultMessage(),
                        "Some entry are not well filled in the moduleSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String chapterId = chapterUpdated.getChapterId();
        String title = chapterUpdated.getTitle();
        int chapterOrder = chapterUpdated.getChapterOrder();
        String chapterType = chapterUpdated.getChapterType();
        String moduleTitle = chapterUpdated.getModuleTitle();
        String courseTitle = chapterUpdated.getCourseTitle();
        String levelName = chapterUpdated.getOwnerLevel();
        String optionName = chapterUpdated.getOwnerOption();
        String departmentName = chapterUpdated.getOwnerDepartment();
        String schoolName = chapterUpdated.getOwnerSchool();

        try {
            srChapter = chapterService.updateChapter(chapterId, title, chapterOrder, chapterType,
                    moduleTitle, courseTitle, levelName, optionName, departmentName, schoolName);
            srChapter.setErrorMessage("The chapter has been successfully updated");
        }  catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The chapter title does not match any chapter in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (DuplicateChapterInModuleException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The chapter already exist in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_DUPLICATED);
            srChapter.setMoreDetails(e.getMessage());
        }

        return srChapter;
    }

    @PutMapping(path = "/chapterTitleUpdated")
    public ServerResponse<Chapter> postChapterTitleUpdated(@Valid @RequestBody ChapterTitleUpdated chapterTitleUpdated,
                                                         BindingResult bindingResult) {
        ServerResponse<Chapter> srChapter = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Chapter>(error.getDefaultMessage(),
                        "Some entry are not well filled in the chapterSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String chapterId = chapterTitleUpdated.getChapterId();
        String newChapterTitle = chapterTitleUpdated.getNewChapterTitle();

        try {
            srChapter = chapterService.updateChapterTitle(chapterId, newChapterTitle);
            srChapter.setErrorMessage("The chapter has been successfully updated");
        }  catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The chapter title does not match any chapter in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srChapter.setMoreDetails(e.getMessage());
        } catch (DuplicateChapterInModuleException e) {
            //e.printStackTrace();
            srChapter.setErrorMessage("The chapter already exists in the system");
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_DUPLICATED);
            srChapter.setMoreDetails(e.getMessage());
        }

        return srChapter;
    }

    @PostMapping(path = "/addContentToChapter")
    public ServerResponse<Chapter> postAddContentToChapter(@Valid @RequestBody ChapterContentSaved chapterContentSaved,
                                                         BindingResult bindingResult) {
        ServerResponse<Chapter> srChapter = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Chapter>(error.getDefaultMessage(),
                        "Some entry are not well filled in the chapterContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = chapterContentSaved.getValue();
        String contentType = chapterContentSaved.getContentType();
        String chapterId = chapterContentSaved.getChapterId();
        String schoolName = chapterContentSaved.getOwnerSchool();
        String departmentName = chapterContentSaved.getOwnerDepartment();
        String optionName = chapterContentSaved.getOwnerOption();
        String levelName = chapterContentSaved.getOwnerLevel();
        String courseTitle = chapterContentSaved.getCourseTitle();
        String moduleTitle = chapterContentSaved.getModuleTitle();
        String chapterTitle = chapterContentSaved.getChapterTitle();

        try {
            srChapter = chapterService.addContentToChapter(value, contentType, chapterId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
            srChapter.setErrorMessage("The content has been successfully added to the chapter");
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CONTENT_ADDED);
            srChapter.setErrorMessage("There is problem during the creation of content");
            srChapter.setMoreDetails(e.getMessage());
        }

        return srChapter;
    }

    @PutMapping(path = "/updateContentToChapter")
    public ServerResponse<Chapter> putUpdateContentToChapter(@Valid @RequestBody ChapterContentUpdated chapterContentUpdated,
                                                           BindingResult bindingResult) {
        ServerResponse<Chapter> srChapter = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Chapter>(error.getDefaultMessage(),
                        "Some entry are not well filled in the chapterContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = chapterContentUpdated.getValue();
        String contentId = chapterContentUpdated.getContentId();
        String chapterId = chapterContentUpdated.getChapterId();
        String schoolName = chapterContentUpdated.getOwnerSchool();
        String departmentName = chapterContentUpdated.getOwnerDepartment();
        String optionName = chapterContentUpdated.getOwnerOption();
        String levelName = chapterContentUpdated.getOwnerLevel();
        String courseTitle = chapterContentUpdated.getCourseTitle();
        String moduleTitle = chapterContentUpdated.getModuleTitle();
        String chapterTitle = chapterContentUpdated.getChapterTitle();

        try {
            srChapter = chapterService.updateContentToChapter(contentId, value, chapterId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
            srChapter.setErrorMessage("The content has been successfully added to the chapter");
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srChapter.setErrorMessage("The chapter does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srChapter.setErrorMessage("The content does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        }

        return srChapter;
    }

    @PostMapping(path = "/removeContentToChapter")
    public ServerResponse<Chapter> postRemoveContentToChapter(@Valid @RequestBody ChapterContentDeleted chapterContentDeleted,
                                                            BindingResult bindingResult) {
        ServerResponse<Chapter> srChapter = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Chapter>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String contentId = chapterContentDeleted.getContentId();
        String chapterId = chapterContentDeleted.getChapterId();
        String schoolName = chapterContentDeleted.getOwnerSchool();
        String departmentName = chapterContentDeleted.getOwnerDepartment();
        String optionName = chapterContentDeleted.getOwnerOption();
        String levelName = chapterContentDeleted.getOwnerLevel();
        String courseTitle = chapterContentDeleted.getCourseTitle();
        String moduleTitle = chapterContentDeleted.getModuleTitle();
        String chapterTitle = chapterContentDeleted.getChapterTitle();

        try {
            srChapter = chapterService.removeContentToChapter(contentId, chapterId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
            srChapter.setErrorMessage("The content has been successfully removed to the chapter");
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srChapter.setErrorMessage("The content id does not match any content in the system");
            srChapter.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srChapter.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srChapter.setErrorMessage("The chapter does not found in the system");
            srChapter.setMoreDetails(e.getMessage());
        }

        return srChapter;
    }

}
