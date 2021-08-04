package com.sprintgether.script.management.server.scriptmanagement.controller.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.service.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.form.program.section.*;
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
public class SectionController {
    SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    public Pageable getSectionPageable(SectionList sectionList){

        Sort.Order order1 = new Sort.Order(sectionList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, sectionList.getSortBy1());

        Sort.Order order2 = new Sort.Order(sectionList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, sectionList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(sectionList.getPageNumber(), sectionList.getPageSize(),
                Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/sectionPageOfChapter")
    public ServerResponse<Page<Section>> getSectionPageOfChapter(
            @Valid @RequestBody SectionList sectionList,
            BindingResult bindingResult) {
        ServerResponse<Page<Section>> srSectionpage = new ServerResponse<>();
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Section>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the SectionList " +
                                "for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getSectionPageable(sectionList);
        String schoolName = sectionList.getSchoolName();
        String departmentName = sectionList.getDepartmentName();
        String optionName = sectionList.getOptionName();
        String levelName = sectionList.getLevelName();
        String courseTitle = sectionList.getCourseTitle();
        String moduleTitle = sectionList.getModuleTitle();
        String chapterId = sectionList.getChapterId();
        String chapterTitle = sectionList.getChapterTitle();
        String keyword = sectionList.getKeyword();

        if(!sectionList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            try {
                srSectionpage = sectionService.findAllSectionOfChapter(chapterId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle,  keyword, sort);
            } catch (ChapterNotFoundException e) {
                //e.printStackTrace();
                srSectionpage.setErrorMessage("The chapter has not found in the system");
                srSectionpage.setMoreDetails(e.getMessage());
                srSectionpage.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            }
        }
        else{
            try {
                srSectionpage = sectionService.findAllSectionOfChapter(chapterId, schoolName,
                        departmentName, optionName, levelName, courseTitle,
                        moduleTitle, chapterTitle, sort);
            } catch (ChapterNotFoundException e) {
                //e.printStackTrace();
                srSectionpage.setErrorMessage("The chapter has not found in the system");
                srSectionpage.setMoreDetails(e.getMessage());
                srSectionpage.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            }
        }

        return  srSectionpage;
    }

    @GetMapping(path = "/sectionPageOfChapterByType")
    public ServerResponse<Page<Section>> getSectionPageOfChapterByType(
            @Valid @RequestBody SectionList sectionList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Section>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the sectionList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getSectionPageable(sectionList);
        String schoolName = sectionList.getSchoolName();
        String departmentName = sectionList.getDepartmentName();
        String optionName = sectionList.getOptionName();
        String levelName = sectionList.getLevelName();
        String courseTitle = sectionList.getCourseTitle();
        String sectionType = sectionList.getSectionType().trim();
        String moduleTitle = sectionList.getModuleTitle();
        String chapterId = sectionList.getChapterId();
        String chapterTitle = sectionList.getChapterTitle();
        String keyword = sectionList.getKeyword();

        ServerResponse<Page<Section>> srSectionPage = new ServerResponse<>();

        try {
            if(!sectionList.getKeyword().equalsIgnoreCase("")){
                srSectionPage = sectionService.findAllSectionOfChapter(schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, keyword, sort);
            }
            else if(sectionType.trim().equalsIgnoreCase("ALL")){
                srSectionPage = sectionService.findAllSectionOfChapter(chapterId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sort);
            }
            else{
                srSectionPage = sectionService.findAllSectionOfChapterByType(chapterId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionType, sort);
            }
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSectionPage.setErrorMessage("The associated chapter has not found");
            srSectionPage.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSectionPage.setMoreDetails(e.getMessage());
        }

        return srSectionPage;
    }

    @GetMapping(path = "/sectionListOfChapterByType")
    public ServerResponse<List<Section>> getSectionListOfChapterByType(
            @Valid @RequestBody SectionList sectionList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Section>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the sectionList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String schoolName = sectionList.getSchoolName().toLowerCase().trim();
        String departmentName = sectionList.getDepartmentName().toLowerCase().trim();
        String optionName = sectionList.getOptionName().toLowerCase().trim();
        String levelName = sectionList.getLevelName().toLowerCase().trim();
        String courseTitle = sectionList.getCourseTitle().toLowerCase().trim();
        String moduleTitle = sectionList.getModuleTitle();
        String chapterId = sectionList.getChapterId();
        String chapterTitle = sectionList.getChapterTitle();
        String sectionType = sectionList.getSectionType().trim();

        ServerResponse<List<Section>> srSectionList = new ServerResponse<>();

        try {
            if(sectionType.trim().equalsIgnoreCase("ALL")){
                srSectionList = sectionService.findAllSectionOfChapter(chapterId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                        sectionList.getSortBy1(), sectionList.getDirection1());
            }
            else{
                srSectionList = sectionService.findAllSectionOfChapterByType(chapterId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                        sectionType, sectionList.getSortBy1(), sectionList.getDirection1());
            }
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSectionList.setErrorMessage("The associated chapter has not found");
            srSectionList.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSectionList.setMoreDetails(e.getMessage());
        }

        return srSectionList;
    }

    @GetMapping(path = "/section")
    public ServerResponse<Section> getSection(@Valid @RequestBody SectionList sectionList) {
        ServerResponse<Section> srSection = new ServerResponse<>();
        String schoolName = sectionList.getSchoolName();
        String departmentName = sectionList.getDepartmentName();
        String optionName = sectionList.getOptionName();
        String levelName = sectionList.getLevelName();
        String courseTitle = sectionList.getCourseTitle();
        String moduleTitle = sectionList.getModuleTitle();
        String chapterTitle = sectionList.getChapterTitle();
        String sectionTitle = sectionList.getSectionTitle();

        try {
            srSection = sectionService.findSectionOfChapterByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The associated school has not found");
            srSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The associated department has not found");
            srSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The associated option has not found");
            srSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The associated level has not found");
            srSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The associated course has not found");
            srSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The associated module has not found");
            srSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The associated chapter has not found");
            srSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @PostMapping(path = "/sectionSaved")
    public ServerResponse<Section> postSectionSaved(@Valid @RequestBody SectionSaved sectionSaved,
                                                    BindingResult bindingResult) {
        ServerResponse<Section> srSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Section>(error.getDefaultMessage(),
                        "Some entry are not well filled in the sectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = sectionSaved.getTitle();
        int sectionOrder = sectionSaved.getSectionOrder();
        String sectionType = sectionSaved.getSectionType();
        String chapterId = sectionSaved.getChapterId();
        String chapterTitle = sectionSaved.getChapterTitle();
        String moduleTitle = sectionSaved.getModuleTitle();
        String courseTitle = sectionSaved.getCourseTitle();
        String levelName = sectionSaved.getOwnerLevel();
        String optionName = sectionSaved.getOwnerOption();
        String departmentName = sectionSaved.getOwnerDepartment();
        String schoolName = sectionSaved.getOwnerSchool();

        try {
            srSection = sectionService.saveSection(title, sectionOrder, sectionType, chapterId,
                    chapterTitle, moduleTitle, courseTitle, levelName, optionName, departmentName,
                    schoolName);
            srSection.setErrorMessage("The section has been successfully created");
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The chapter title does not match any section in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (DuplicateSectionInChapterException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The section title already exist in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_DUPLICATED);
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @PutMapping(path = "/sectionUpdated")
    public ServerResponse<Section> postSectionUpdated(
            @Valid @RequestBody SectionUpdated sectionUpdated,
            BindingResult bindingResult) {
        ServerResponse<Section> srSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Section>(error.getDefaultMessage(),
                        "Some entry are not well filled in the sectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = sectionUpdated.getTitle();
        int sectionOrder = sectionUpdated.getSectionOrder();
        String sectionType = sectionUpdated.getSectionType();
        String sectionId = sectionUpdated.getSectionId();
        String chapterTitle = sectionUpdated.getChapterTitle();
        String moduleTitle = sectionUpdated.getModuleTitle();
        String courseTitle = sectionUpdated.getCourseTitle();
        String levelName = sectionUpdated.getOwnerLevel();
        String optionName = sectionUpdated.getOwnerOption();
        String departmentName = sectionUpdated.getOwnerDepartment();
        String schoolName = sectionUpdated.getOwnerSchool();

        try {
            srSection = sectionService.updateSection(sectionId, title, sectionOrder, sectionType,
                    moduleTitle, courseTitle, chapterTitle, levelName, optionName, departmentName,
                    schoolName);
            srSection.setErrorMessage("The section has been successfully updated");
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The section title does not match any section in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (DuplicateSectionInChapterException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The section title already exist in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_DUPLICATED);
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @PutMapping(path = "/sectionTitleUpdated")
    public ServerResponse<Section> postSectionTitleUpdated(
            @Valid @RequestBody SectionTitleUpdated sectionTitleUpdated,
            BindingResult bindingResult) {
        ServerResponse<Section> srSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Section>(error.getDefaultMessage(),
                        "Some entry are not well filled in the sectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String sectionId = sectionTitleUpdated.getSectionId();
        String newSectionTitle = sectionTitleUpdated.getNewSectionTitle();

        try {
            srSection = sectionService.updateSectionTitle(sectionId, newSectionTitle);
            srSection.setErrorMessage("The section has been successfully updated");
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The section title does not match any section in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (DuplicateSectionInChapterException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The section title already exist in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_DUPLICATED);
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @PostMapping(path = "/addContentToSection")
    public ServerResponse<Section> postAddContentToSection(@Valid @RequestBody SectionContentSaved sectionContentSaved,
                                                           BindingResult bindingResult) {
        ServerResponse<Section> srSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Section>(error.getDefaultMessage(),
                        "Some entry are not well filled in the sectionContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = sectionContentSaved.getValue();
        String contentType = sectionContentSaved.getContentType();
        String sectionId = sectionContentSaved.getSectionId();
        String schoolName = sectionContentSaved.getOwnerSchool();
        String departmentName = sectionContentSaved.getOwnerDepartment();
        String optionName = sectionContentSaved.getOwnerOption();
        String levelName = sectionContentSaved.getOwnerLevel();
        String courseTitle = sectionContentSaved.getCourseTitle();
        String moduleTitle = sectionContentSaved.getModuleTitle();
        String chapterTitle = sectionContentSaved.getChapterTitle();
        String sectionTitle = sectionContentSaved.getSectionTitle();

        try {
            srSection = sectionService.addContentToSection(value, contentType, sectionId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    sectionTitle);
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSection.setErrorMessage("The section does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @PutMapping(path = "/updateContentToSection")
    public ServerResponse<Section> putUpdateContentToSection(
            @Valid @RequestBody SectionContentUpdated sectionContentUpdated,
            BindingResult bindingResult) {
        ServerResponse<Section> srSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Section>(error.getDefaultMessage(),
                        "Some entry are not well filled in the sectionContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = sectionContentUpdated.getValue();
        String contentId = sectionContentUpdated.getContentId();
        String schoolName = sectionContentUpdated.getOwnerSchool();
        String departmentName = sectionContentUpdated.getOwnerDepartment();
        String optionName = sectionContentUpdated.getOwnerOption();
        String levelName = sectionContentUpdated.getOwnerLevel();
        String courseTitle = sectionContentUpdated.getCourseTitle();
        String moduleTitle = sectionContentUpdated.getModuleTitle();
        String chapterTitle = sectionContentUpdated.getChapterTitle();
        String sectionId = sectionContentUpdated.getSectionId();
        String sectionTitle = sectionContentUpdated.getSectionTitle();

        try {
            srSection = sectionService.updateContentToSection(contentId, value, sectionId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    sectionTitle);
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSection.setErrorMessage("The section does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (ContentNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srSection.setErrorMessage("There is problem during the modification of content");
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @PostMapping(path = "/removeContentToSection")
    public ServerResponse<Section> postRemoveContentToSection(@Valid @RequestBody SectionContentDeleted sectionContentDeleted,
                                                              BindingResult bindingResult) {
        ServerResponse<Section> srSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Section>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String contentId = sectionContentDeleted.getContentId();
        String schoolName = sectionContentDeleted.getOwnerSchool();
        String departmentName = sectionContentDeleted.getOwnerDepartment();
        String optionName = sectionContentDeleted.getOwnerOption();
        String levelName = sectionContentDeleted.getOwnerLevel();
        String courseTitle = sectionContentDeleted.getCourseTitle();
        String moduleTitle = sectionContentDeleted.getModuleTitle();
        String chapterTitle = sectionContentDeleted.getChapterTitle();
        String sectionId = sectionContentDeleted.getSectionId();
        String sectionTitle = sectionContentDeleted.getSectionTitle();

        try {
            srSection = sectionService.removeContentToSection(contentId, sectionId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    sectionTitle);
            srSection.setErrorMessage("The content has been successfully removed to the section");
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSection.setErrorMessage("The section does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (ContentNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srSection.setErrorMessage("The content id does not match any content in the system");
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }



}
