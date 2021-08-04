package com.sprintgether.script.management.server.scriptmanagement.controller.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.service.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.form.program.paragraph.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
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
public class ParagraphController {
    ParagraphService paragraphService;

    public ParagraphController(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }

    public Pageable getParagraphPageable(ParagraphList paragraphList){

        Sort.Order order1 = new Sort.Order(paragraphList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, paragraphList.getSortBy1());

        Sort.Order order2 = new Sort.Order(paragraphList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, paragraphList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(paragraphList.getPageNumber(), paragraphList.getPageSize(),
                Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/paragraphPageOfSubSection")
    public ServerResponse<Page<Paragraph>> getParagraphPageOfSubSection(
            @Valid @RequestBody ParagraphList paragraphList,
            BindingResult bindingResult) {
        ServerResponse<Page<Paragraph>> srParagraphpage = new ServerResponse<>();
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Paragraph>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the ParagraphList " +
                                "for selection",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getParagraphPageable(paragraphList);
        String schoolName = paragraphList.getSchoolName();
        String departmentName = paragraphList.getDepartmentName();
        String optionName = paragraphList.getOptionName();
        String levelName = paragraphList.getLevelName();
        String courseTitle = paragraphList.getCourseTitle();
        String moduleTitle = paragraphList.getModuleTitle();
        String chapterTitle = paragraphList.getChapterTitle();
        String sectionTitle = paragraphList.getSectionTitle();
        String subSectionId = paragraphList.getSubSectionId();
        String subSectionTitle = paragraphList.getSubSectionTitle();
        String keyword = paragraphList.getKeyword();

        if(!paragraphList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            try {
                srParagraphpage = paragraphService.findAllParagraphOfSubSection(subSectionId,
                        schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionTitle,  keyword, sort);
            } catch (SubSectionNotFoundException e) {
                //e.printStackTrace();
                srParagraphpage.setErrorMessage("The subSection has not found in the system");
                srParagraphpage.setMoreDetails(e.getMessage());
                srParagraphpage.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            }
        }
        else{
            try {
                srParagraphpage = paragraphService.findAllParagraphOfSubSection(subSectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle,
                        moduleTitle, chapterTitle, sectionTitle, subSectionTitle, sort);
            } catch (SubSectionNotFoundException e) {
                //e.printStackTrace();
                srParagraphpage.setErrorMessage("The subSection has not found in the system");
                srParagraphpage.setMoreDetails(e.getMessage());
                srParagraphpage.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            }
        }

        return srParagraphpage;
    }

    @GetMapping(path = "/paragraphPageOfSubSectionByType")
    public ServerResponse<Page<Paragraph>> getParagraphPageOfSubSectionByType(
            @Valid @RequestBody ParagraphList paragraphList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Paragraph>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the paragraphList " +
                                "for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        Pageable sort = this.getParagraphPageable(paragraphList);
        String schoolName = paragraphList.getSchoolName();
        String departmentName = paragraphList.getDepartmentName();
        String optionName = paragraphList.getOptionName();
        String levelName = paragraphList.getLevelName();
        String courseTitle = paragraphList.getCourseTitle();
        String paragraphType = paragraphList.getParagraphType().trim();
        String moduleTitle = paragraphList.getModuleTitle();
        String chapterTitle = paragraphList.getChapterTitle();
        String sectionTitle = paragraphList.getSectionTitle();
        String subSectionId = paragraphList.getSubSectionId();
        String subSectionTitle = paragraphList.getSubSectionTitle();
        String keyword = paragraphList.getKeyword();

        ServerResponse<Page<Paragraph>> srParagraphPage = new ServerResponse<>();

        try {
            if(!paragraphList.getKeyword().equalsIgnoreCase("")){
                srParagraphPage = paragraphService.findAllParagraphOfSubSection(schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionTitle, keyword, sort);
            }
            else if(paragraphType.trim().equalsIgnoreCase("ALL")){
                srParagraphPage = paragraphService.findAllParagraphOfSubSection(subSectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionTitle, sort);
            }
            else{
                srParagraphPage = paragraphService.findAllParagraphOfSubSection(subSectionId, schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionTitle, paragraphType, sort);
            }
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srParagraphPage.setErrorMessage("The associated subSection has not found");
            srParagraphPage.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srParagraphPage.setMoreDetails(e.getMessage());
        }

        return  srParagraphPage;
    }

    @GetMapping(path = "/paragraphListOfSubSectionByType")
    public ServerResponse<List<Paragraph>> getParagraphListOfSubSectionByType(
            @Valid @RequestBody ParagraphList paragraphList,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Paragraph>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the subSectionList for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String schoolName = paragraphList.getSchoolName().toLowerCase().trim();
        String departmentName = paragraphList.getDepartmentName().toLowerCase().trim();
        String optionName = paragraphList.getOptionName().toLowerCase().trim();
        String levelName = paragraphList.getLevelName().toLowerCase().trim();
        String courseTitle = paragraphList.getCourseTitle().toLowerCase().trim();
        String moduleTitle = paragraphList.getModuleTitle();
        String chapterTitle = paragraphList.getChapterTitle();
        String sectionTitle = paragraphList.getSectionTitle();
        String subSectionId = paragraphList.getSubSectionId();
        String subSectionTitle = paragraphList.getSubSectionTitle();
        String paragraphType = paragraphList.getParagraphType().trim();

        ServerResponse<List<Paragraph>> srParagraphList = new ServerResponse<>();
        try {
            if(paragraphType.trim().equalsIgnoreCase("ALL")){
                srParagraphList = paragraphService.findAllParagraphOfSubSection(subSectionId,
                        schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionTitle,  paragraphList.getSortBy1(),
                        paragraphList.getDirection1());
            }
            else{
                srParagraphList = paragraphService.findAllParagraphOfSubSectionByType(subSectionId,
                        schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle, subSectionTitle,  paragraphType,
                        paragraphList.getSortBy1(), paragraphList.getDirection1());
            }
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srParagraphList.setErrorMessage("The associated subsection has not found");
            srParagraphList.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srParagraphList.setMoreDetails(e.getMessage());
        }

        return  srParagraphList;
    }

    @GetMapping(path = "/paragraph")
    public ServerResponse<Paragraph> getParagraph(@Valid @RequestBody ParagraphList paragraphList) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();
        String schoolName = paragraphList.getSchoolName();
        String departmentName = paragraphList.getDepartmentName();
        String optionName = paragraphList.getOptionName();
        String levelName = paragraphList.getLevelName();
        String courseTitle = paragraphList.getCourseTitle();
        String moduleTitle = paragraphList.getModuleTitle();
        String chapterTitle = paragraphList.getChapterTitle();
        String sectionTitle = paragraphList.getSectionTitle();
        String subSectionTitle = paragraphList.getSubSectionTitle();
        String paragraphTitle = paragraphList.getParagraphTitle();

        try {
            srParagraph = paragraphService.findParagraphOfSubSectionByTitle(schoolName, departmentName,
                    optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle,
                    subSectionTitle, paragraphTitle);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated school has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated department has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated option has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated level has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated course has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated module has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated chapter has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated section has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated subsection has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @PostMapping(path = "/paragraphSaved")
    public ServerResponse<Paragraph> postParagraphSaved(
            @Valid @RequestBody ParagraphSaved paragraphSaved,
            BindingResult bindingResult) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Paragraph>(error.getDefaultMessage(),
                        "Some entry are not well filled in the sectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = paragraphSaved.getTitle();
        int paragraphOrder = paragraphSaved.getParagraphOrder();
        String paragraphType = paragraphSaved.getParagraphType();
        String subSectionId = paragraphSaved.getSubSectionId();
        String subSectionTitle = paragraphSaved.getSubSectionTitle();
        String sectionTitle = paragraphSaved.getSectionTitle();
        String chapterTitle = paragraphSaved.getChapterTitle();
        String moduleTitle = paragraphSaved.getModuleTitle();
        String courseTitle = paragraphSaved.getCourseTitle();
        String levelName = paragraphSaved.getOwnerLevel();
        String optionName = paragraphSaved.getOwnerOption();
        String departmentName = paragraphSaved.getOwnerDepartment();
        String schoolName = paragraphSaved.getOwnerSchool();

        try {
            srParagraph = paragraphService.saveParagraph(title, paragraphOrder, paragraphType,
                    subSectionId, subSectionTitle, sectionTitle, chapterTitle, moduleTitle, courseTitle, levelName,
                    optionName, departmentName, schoolName);
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated subsection has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (DuplicateParagraphInSubSectionException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The paragraph title already exist in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_DUPLICATED);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return  srParagraph;
    }

    @PutMapping(path = "/paragraphUpdated")
    public ServerResponse<Paragraph> postParagraphUpdated(
            @Valid @RequestBody ParagraphUpdated paragraphUpdated,
            BindingResult bindingResult) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Paragraph>(error.getDefaultMessage(),
                        "Some entry are not well filled in the paragraphSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String title = paragraphUpdated.getTitle();
        int paragraphOrder = paragraphUpdated.getParagraphOrder();
        String paragraphType = paragraphUpdated.getParagraphType();
        String paragraphId = paragraphUpdated.getParagraphId();
        String subSectionTitle = paragraphUpdated.getSubSectionTitle();
        String sectionTitle = paragraphUpdated.getSectionTitle();
        String chapterTitle = paragraphUpdated.getChapterTitle();
        String moduleTitle = paragraphUpdated.getModuleTitle();
        String courseTitle = paragraphUpdated.getCourseTitle();
        String levelName = paragraphUpdated.getOwnerLevel();
        String optionName = paragraphUpdated.getOwnerOption();
        String departmentName = paragraphUpdated.getOwnerDepartment();
        String schoolName = paragraphUpdated.getOwnerSchool();

        try {
            srParagraph = paragraphService.updateParagraph(paragraphId, title, paragraphOrder,
                    paragraphType, subSectionTitle, sectionTitle, moduleTitle, courseTitle, chapterTitle, levelName,
                    optionName, departmentName, schoolName);
        } catch (ParagraphNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated paragraph has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (DuplicateParagraphInSubSectionException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The paragraph title already exist in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_DUPLICATED);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @PutMapping(path = "/paragraphTitleUpdated")
    public ServerResponse<Paragraph> postParagraphTitleUpdated(
            @Valid @RequestBody ParagraphTitleUpdated paragraphTitleUpdated,
            BindingResult bindingResult) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Paragraph>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String paragraphId = paragraphTitleUpdated.getParagraphId();
        String newParagraphTitle = paragraphTitleUpdated.getNewParagraphTitle();

        try {
            srParagraph = paragraphService.updateParagraphTitle(paragraphId, newParagraphTitle);
        } catch (ParagraphNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated paragraph has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (DuplicateParagraphInSubSectionException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The paragraph title already exist in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_DUPLICATED);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @PostMapping(path = "/addContentToParagraph")
    public ServerResponse<Paragraph> postAddContentToParagraph(
            @Valid @RequestBody ParagraphContentSaved paragraphContentSaved,
            BindingResult bindingResult) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Paragraph>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = paragraphContentSaved.getValue();
        String contentType = paragraphContentSaved.getContentType();
        String schoolName = paragraphContentSaved.getOwnerSchool();
        String departmentName = paragraphContentSaved.getOwnerDepartment();
        String optionName = paragraphContentSaved.getOwnerOption();
        String levelName = paragraphContentSaved.getOwnerLevel();
        String courseTitle = paragraphContentSaved.getCourseTitle();
        String moduleTitle = paragraphContentSaved.getModuleTitle();
        String chapterTitle = paragraphContentSaved.getChapterTitle();
        String sectionTitle = paragraphContentSaved.getSectionTitle();
        String subSectionTitle = paragraphContentSaved.getSubSectionTitle();
        String paragraphId = paragraphContentSaved.getParagraphId();
        String paragraphTitle = paragraphContentSaved.getParagraphTitle();

        try {
            srParagraph = paragraphService.addContentToParagraph(value, contentType, paragraphId,
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, subSectionTitle, paragraphTitle);
        } catch (ParagraphNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated paragraph has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @PutMapping(path = "/updateContentToParagraph")
    public ServerResponse<Paragraph> putUpdateContentToParagraph(
            @Valid @RequestBody ParagraphContentUpdated paragraphContentUpdated,
            BindingResult bindingResult) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Paragraph>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionContentSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String value = paragraphContentUpdated.getValue();
        String contentId = paragraphContentUpdated.getContentId();
        String schoolName = paragraphContentUpdated.getOwnerSchool();
        String departmentName = paragraphContentUpdated.getOwnerDepartment();
        String optionName = paragraphContentUpdated.getOwnerOption();
        String levelName = paragraphContentUpdated.getOwnerLevel();
        String courseTitle = paragraphContentUpdated.getCourseTitle();
        String moduleTitle = paragraphContentUpdated.getModuleTitle();
        String chapterTitle = paragraphContentUpdated.getChapterTitle();
        String sectionTitle = paragraphContentUpdated.getSectionTitle();
        String subSectionTitle = paragraphContentUpdated.getSubSectionTitle();
        String paragraphId = paragraphContentUpdated.getParagraphId();
        String paragraphTitle = paragraphContentUpdated.getParagraphTitle();

        try {
            srParagraph = paragraphService.updateContentToParagraph(contentId, value, paragraphId,
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, subSectionTitle, paragraphTitle);
        } catch (ParagraphNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated paragraph has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ContentNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srParagraph.setErrorMessage("There is problem during the modification of content");
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @PostMapping(path = "/removeContentToParagraph")
    public ServerResponse<Paragraph> postRemoveContentToParagraph(
            @Valid @RequestBody ParagraphContentDeleted paragraphContentDeleted,
            BindingResult bindingResult) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Paragraph>(error.getDefaultMessage(),
                        "Some entry are not well filled in the subSectionContentDeleted for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String contentId = paragraphContentDeleted.getContentId();
        String schoolName = paragraphContentDeleted.getOwnerSchool();
        String departmentName = paragraphContentDeleted.getOwnerDepartment();
        String optionName = paragraphContentDeleted.getOwnerOption();
        String levelName = paragraphContentDeleted.getOwnerLevel();
        String courseTitle = paragraphContentDeleted.getCourseTitle();
        String moduleTitle = paragraphContentDeleted.getModuleTitle();
        String chapterTitle = paragraphContentDeleted.getChapterTitle();
        String sectionTitle = paragraphContentDeleted.getSectionTitle();
        String subSectionTitle = paragraphContentDeleted.getSubSectionTitle();
        String paragraphId = paragraphContentDeleted.getParagraphId();
        String paragraphTitle = paragraphContentDeleted.getParagraphTitle();

        try {
            srParagraph = paragraphService.removeContentToParagraph(contentId, paragraphId, schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    sectionTitle, subSectionTitle, paragraphTitle);
        } catch (ParagraphNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The associated paragraph has not found");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_PARAGRAPH_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ContentNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_CONTENT_FOUND);
            srParagraph.setErrorMessage("There is problem during the modification of content");
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }


}
