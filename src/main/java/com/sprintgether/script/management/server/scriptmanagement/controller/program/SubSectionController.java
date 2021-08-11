package com.sprintgether.script.management.server.scriptmanagement.controller.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.service.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.form.program.subsection.*;
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
public class SubSectionController {
    SubSectionService subSectionService;

    public SubSectionController(SubSectionService subSectionService) {
        this.subSectionService = subSectionService;
    }

    public Pageable getSubSectionPageable(SubSectionList subSectionList){

        Sort.Order order1 = new Sort.Order(subSectionList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, subSectionList.getSortBy1());

        Sort.Order order2 = new Sort.Order(subSectionList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, subSectionList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(subSectionList.getPageNumber(), subSectionList.getPageSize(),
                Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/subSectionPageOfSection")
    public ServerResponse<Page<SubSection>> getSubSectionPageOfSection(
            @Valid @RequestBody SubSectionList subSectionList,
            BindingResult bindingResult) {
        ServerResponse<Page<SubSection>> srSubSectionpage = new ServerResponse<>();
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<SubSection>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the SubSectionList " +
                                "for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getSubSectionPageable(subSectionList);
        String schoolName = subSectionList.getSchoolName();
        String departmentName = subSectionList.getDepartmentName();
        String optionName = subSectionList.getOptionName();
        String levelName = subSectionList.getLevelName();
        String courseTitle = subSectionList.getCourseTitle();
        String moduleTitle = subSectionList.getModuleTitle();
        String chapterTitle = subSectionList.getChapterTitle();
        String sectionId = subSectionList.getSectionId();
        String sectionTitle = subSectionList.getSectionTitle();
        String keyword = subSectionList.getKeyword();

        if(!subSectionList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            try {
                srSubSectionpage = subSectionService.findAllSubSectionOfSection(sectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle,  keyword, sort);
            } catch (SectionNotFoundException e) {
                //e.printStackTrace();
                srSubSectionpage.setErrorMessage("The section has not found in the system");
                srSubSectionpage.setMoreDetails(e.getMessage());
                srSubSectionpage.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            }
        }
        else{
            try {
                srSubSectionpage = subSectionService.findAllSubSectionOfSection(sectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle,
                        moduleTitle, chapterTitle, sectionTitle, sort);
            } catch (SectionNotFoundException e) {
                //e.printStackTrace();
                srSubSectionpage.setErrorMessage("The section has not found in the system");
                srSubSectionpage.setMoreDetails(e.getMessage());
                srSubSectionpage.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            }
        }

        return srSubSectionpage;
    }

    @GetMapping(path = "/subSectionPageOfSectionByType")
    public ServerResponse<Page<SubSection>> getSubSectionPageOfSectionByType(
            @Valid @RequestBody SubSectionList subSectionList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<SubSection>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the subSectionList " +
                                "for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getSubSectionPageable(subSectionList);
        String schoolName = subSectionList.getSchoolName();
        String departmentName = subSectionList.getDepartmentName();
        String optionName = subSectionList.getOptionName();
        String levelName = subSectionList.getLevelName();
        String courseTitle = subSectionList.getCourseTitle();
        String subSectionType = subSectionList.getSubSectionType().trim();
        String moduleTitle = subSectionList.getModuleTitle();
        String chapterTitle = subSectionList.getChapterTitle();
        String sectionId = subSectionList.getSectionId();
        String sectionTitle = subSectionList.getSectionTitle();
        String keyword = subSectionList.getKeyword();

        ServerResponse<Page<SubSection>> srSubSectionPage = new ServerResponse<>();

        try {
            if(!subSectionList.getKeyword().equalsIgnoreCase("")){
                srSubSectionPage = subSectionService.findAllSubSectionOfSection(schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, keyword, sort);
            }
            else if(subSectionType.trim().equalsIgnoreCase("ALL")){
                srSubSectionPage = subSectionService.findAllSubSectionOfSection(sectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, sort);
            }
            else{
                srSubSectionPage = subSectionService.findAllSubSectionOfSection(sectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionType, sort);
            }
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSubSectionPage.setErrorMessage("The associated section has not found");
            srSubSectionPage.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSubSectionPage.setMoreDetails(e.getMessage());
        }

        return srSubSectionPage;
    }

    @GetMapping(path = "/subSectionListOfSectionByType")
    public ServerResponse<List<SubSection>> getSubSectionListOfSectionByType(
            @Valid @RequestBody SubSectionList subSectionList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<SubSection>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the subSectionList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String schoolName = subSectionList.getSchoolName().toLowerCase().trim();
        String departmentName = subSectionList.getDepartmentName().toLowerCase().trim();
        String optionName = subSectionList.getOptionName().toLowerCase().trim();
        String levelName = subSectionList.getLevelName().toLowerCase().trim();
        String courseTitle = subSectionList.getCourseTitle().toLowerCase().trim();
        String moduleTitle = subSectionList.getModuleTitle();
        String chapterTitle = subSectionList.getChapterTitle();
        String sectionId = subSectionList.getSectionId();
        String sectionTitle = subSectionList.getSectionTitle();
        String subSectionType = subSectionList.getSubSectionType().trim();

        ServerResponse<List<SubSection>> srSubSectionList = new ServerResponse<>();

        try {
            if(subSectionType.trim().equalsIgnoreCase("ALL")){
                srSubSectionList = subSectionService.findAllSubSectionOfSection(sectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                        sectionTitle, subSectionList.getSortBy1(), subSectionList.getDirection1());
            }
            else{
                srSubSectionList = subSectionService.findAllSubSectionOfSectionByType(sectionId,
                        schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionType, subSectionList.getSortBy1(),
                        subSectionList.getDirection1());
            }
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSubSectionList.setErrorMessage("The associated section has not found");
            srSubSectionList.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSubSectionList.setMoreDetails(e.getMessage());
        }

        return srSubSectionList;
    }

    @GetMapping(path = "/subSection")
    public ServerResponse<SubSection> getSubSection(@Valid @RequestBody SubSectionList subSectionList) {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        String schoolName = subSectionList.getSchoolName();
        String departmentName = subSectionList.getDepartmentName();
        String optionName = subSectionList.getOptionName();
        String levelName = subSectionList.getLevelName();
        String courseTitle = subSectionList.getCourseTitle();
        String moduleTitle = subSectionList.getModuleTitle();
        String chapterTitle = subSectionList.getChapterTitle();
        String sectionTitle = subSectionList.getSectionTitle();
        String subSectionTitle = subSectionList.getSubSectionTitle();

        try {
            srSubSection = subSectionService.findSubSectionOfSectionByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle,
                    subSectionTitle);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated school has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated department has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated option has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated level has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated course has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated module has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated chapter has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated section has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }

    @PostMapping(path = "/subSectionSaved")
    public ServerResponse<SubSection> postSubSectionSaved(
            @Valid @RequestBody SubSectionSaved subSectionSaved,
            BindingResult bindingResult) {
        ServerResponse<SubSection> srSubSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<SubSection>(error.getDefaultMessage(),
                        "Some entry are not well filled in the sectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = subSectionSaved.getTitle();
        int subSectionOrder = subSectionSaved.getSubSectionOrder();
        String subSectionType = subSectionSaved.getSubSectionType();
        String sectionId = subSectionSaved.getSectionId();
        String sectionTitle = subSectionSaved.getSectionTitle();
        String chapterTitle = subSectionSaved.getChapterTitle();
        String moduleTitle = subSectionSaved.getModuleTitle();
        String courseTitle = subSectionSaved.getCourseTitle();
        String levelName = subSectionSaved.getOwnerLevel();
        String optionName = subSectionSaved.getOwnerOption();
        String departmentName = subSectionSaved.getOwnerDepartment();
        String schoolName = subSectionSaved.getOwnerSchool();

        try {
            srSubSection = subSectionService.saveSubSection(title, subSectionOrder, subSectionType,
                    sectionId, sectionTitle, chapterTitle, moduleTitle, courseTitle, levelName,
                    optionName, departmentName, schoolName);
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated section has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (DuplicateSubSectionInSectionException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The subsection title already exist in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_DUPLICATED);
            srSubSection.setMoreDetails(e.getMessage());
        }

        return  srSubSection;
    }

    @PutMapping(path = "/subSectionUpdated")
    public ServerResponse<SubSection> postSubSectionUpdated(
            @Valid @RequestBody SubSectionUpdated subSectionUpdated,
            BindingResult bindingResult) {
        ServerResponse<SubSection> srSubSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<SubSection>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = subSectionUpdated.getTitle();
        int subSectionOrder = subSectionUpdated.getSubSectionOrder();
        String subSectionType = subSectionUpdated.getSubSectionType();
        String subSectionId = subSectionUpdated.getSubSectionId();
        String sectionTitle = subSectionUpdated.getSectionTitle();
        String chapterTitle = subSectionUpdated.getChapterTitle();
        String moduleTitle = subSectionUpdated.getModuleTitle();
        String courseTitle = subSectionUpdated.getCourseTitle();
        String levelName = subSectionUpdated.getOwnerLevel();
        String optionName = subSectionUpdated.getOwnerOption();
        String departmentName = subSectionUpdated.getOwnerDepartment();
        String schoolName = subSectionUpdated.getOwnerSchool();

        try {
            srSubSection = subSectionService.updateSubSection(subSectionId, title, subSectionOrder,
                    subSectionType, sectionTitle, moduleTitle, courseTitle, chapterTitle, levelName,
                    optionName, departmentName, schoolName);
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated subsection has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (DuplicateSubSectionInSectionException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The subsection title already exist in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_DUPLICATED);
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }


    @PutMapping(path = "/subSectionTitleUpdated")
    public ServerResponse<SubSection> postSubSectionTitleUpdated(
            @Valid @RequestBody SubSectionTitleUpdated subSectionTitleUpdated,
            BindingResult bindingResult) {
        ServerResponse<SubSection> srSubSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<SubSection>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String subSectionId = subSectionTitleUpdated.getSubSectionId();
        String newSubSectionTitle = subSectionTitleUpdated.getNewSubSectionTitle();

        try {
            srSubSection = subSectionService.updateSubSectionTitle(subSectionId, newSubSectionTitle);
            srSubSection.setErrorMessage("The subSection has been successfully updated");
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated subsection has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (DuplicateSubSectionInSectionException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The subsection title already exist in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_DUPLICATED);
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }

    @PostMapping(path = "/addContentToSubSection")
    public ServerResponse<SubSection> postAddContentToSubSection(
            @Valid @RequestBody SubSectionContentSaved subSectionContentSaved,
            BindingResult bindingResult) {
        ServerResponse<SubSection> srSubSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<SubSection>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = subSectionContentSaved.getValue();
        String contentType = subSectionContentSaved.getContentType();
        String schoolName = subSectionContentSaved.getOwnerSchool();
        String departmentName = subSectionContentSaved.getOwnerDepartment();
        String optionName = subSectionContentSaved.getOwnerOption();
        String levelName = subSectionContentSaved.getOwnerLevel();
        String courseTitle = subSectionContentSaved.getCourseTitle();
        String moduleTitle = subSectionContentSaved.getModuleTitle();
        String chapterTitle = subSectionContentSaved.getChapterTitle();
        String sectionTitle = subSectionContentSaved.getSectionTitle();
        String subSectionId = subSectionContentSaved.getSubSectionId();
        String subSectionTitle = subSectionContentSaved.getSubSectionTitle();

        try {
            srSubSection = subSectionService.addContentToSubSection(value, contentType, subSectionId,
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, subSectionTitle);
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated subsection has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }

    @PutMapping(path = "/updateContentToSubSection")
    public ServerResponse<SubSection> putUpdateContentToSubSection(
            @Valid @RequestBody SubSectionContentUpdated subSectionContentUpdated,
            BindingResult bindingResult) {
        ServerResponse<SubSection> srSubSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<SubSection>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = subSectionContentUpdated.getValue();
        String contentId = subSectionContentUpdated.getContentId();
        String schoolName = subSectionContentUpdated.getOwnerSchool();
        String departmentName = subSectionContentUpdated.getOwnerDepartment();
        String optionName = subSectionContentUpdated.getOwnerOption();
        String levelName = subSectionContentUpdated.getOwnerLevel();
        String courseTitle = subSectionContentUpdated.getCourseTitle();
        String moduleTitle = subSectionContentUpdated.getModuleTitle();
        String chapterTitle = subSectionContentUpdated.getChapterTitle();
        String sectionTitle = subSectionContentUpdated.getSectionTitle();
        String subSectionId = subSectionContentUpdated.getSubSectionId();
        String subSectionTitle = subSectionContentUpdated.getSubSectionTitle();

        try {
            srSubSection = subSectionService.updateContentToSubSection(contentId, value, subSectionId,
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, subSectionTitle);
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated subsection has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srSubSection.setErrorMessage("There is problem during the modification of content");
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }


    @PostMapping(path = "/removeContentToSubSection")
    public ServerResponse<SubSection> postRemoveContentToSubSection(
            @Valid @RequestBody SubSectionContentDeleted subSectionContentDeleted,
            BindingResult bindingResult) {
        ServerResponse<SubSection> srSubSection = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<SubSection>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionContentDeleted for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String contentId = subSectionContentDeleted.getContentId();
        String schoolName = subSectionContentDeleted.getOwnerSchool();
        String departmentName = subSectionContentDeleted.getOwnerDepartment();
        String optionName = subSectionContentDeleted.getOwnerOption();
        String levelName = subSectionContentDeleted.getOwnerLevel();
        String courseTitle = subSectionContentDeleted.getCourseTitle();
        String moduleTitle = subSectionContentDeleted.getModuleTitle();
        String chapterTitle = subSectionContentDeleted.getChapterTitle();
        String sectionTitle = subSectionContentDeleted.getSectionTitle();
        String subSectionId = subSectionContentDeleted.getSubSectionId();
        String subSectionTitle = subSectionContentDeleted.getSubSectionTitle();

        try {
            srSubSection = subSectionService.removeContentToSubSection(contentId, subSectionId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    sectionTitle, subSectionTitle);
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The associated subsection has not found");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srSubSection.setErrorMessage("There is problem during the modification of content");
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }

}
