package com.sprintgether.script.management.server.scriptmanagement.service.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.program.ParagraphRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
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
public class ParagraphServiceImpl implements ParagraphService {

    ParagraphRepository paragraphRepository;
    ContentRepository contentRepository;
    SubSectionService subSectionService;
    CourseService courseService;

    public ParagraphServiceImpl(ParagraphRepository paragraphRepository,
                                ContentRepository contentRepository,
                                SubSectionService subSectionService,
                                CourseService courseService) {
        this.paragraphRepository = paragraphRepository;
        this.contentRepository = contentRepository;
        this.subSectionService = subSectionService;
        this.courseService = courseService;
    }

    @Override
    public ServerResponse<Paragraph> findParagraphOfSubSectionById(String paragraphId) {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();
        paragraphId = paragraphId.trim();

        Optional<Paragraph> optionalParagraph = paragraphRepository.findById(paragraphId);
        if(!optionalParagraph.isPresent()){
            srParagraph.setErrorMessage("The paragraph id does not match any paragraph in the " +
                    "sub section associated to the section specified");
            srParagraph.setResponseCode(ResponseCode.PARAGRAPH_NOT_FOUND);
        }
        else{
            srParagraph.setErrorMessage("The paragraph has been successfully found in the system");
            srParagraph.setResponseCode(ResponseCode.PARAGRAPH_FOUND);
            srParagraph.setAssociatedObject(optionalParagraph.get());
        }

        return srParagraph;
    }

    @Override
    public ServerResponse<Paragraph> findParagraphOfSubSectionByTitle(String schoolName,
                                                                      String departmentName,
                                                                      String optionName,
                                                                      String levelName,
                                                                      String courseTitle,
                                                                      String moduleTitle,
                                                                      String chapterTitle,
                                                                      String sectionTitle,
                                                                      String subSectionTitle,
                                                                      String paragraphTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException,
            ChapterNotFoundException, SectionNotFoundException, SubSectionNotFoundException {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        paragraphTitle = paragraphTitle.toLowerCase().trim();

        ServerResponse<SubSection> srSubSection = subSectionService.findSubSectionOfSectionByTitle(
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle);

        if(srSubSection.getResponseCode() != ResponseCode.SUBSECTION_FOUND){
            throw  new SubSectionNotFoundException("The sub section specified does not found " +
                    "on the system");
        }
        SubSection concernedSubSection = srSubSection.getAssociatedObject();

        Optional<Paragraph> optionalParagraph = paragraphRepository.
                findByOwnerSubSectionAndTitle(concernedSubSection, paragraphTitle);

        if(!optionalParagraph.isPresent()){
            srParagraph.setErrorMessage("The paragraph title does not match any paragraph in the " +
                    "subsection associated to the section specified");
            srParagraph.setResponseCode(ResponseCode.PARAGRAPH_NOT_FOUND);
        }
        else{
            srParagraph.setErrorMessage("The paragraph has been successfully found in the system");
            srParagraph.setResponseCode(ResponseCode.PARAGRAPH_FOUND);
            srParagraph.setAssociatedObject(optionalParagraph.get());
        }

        return srParagraph;
    }

    public Optional<SubSection> findConcernedSubSection(String subSectionId,
                                                        String schoolName,
                                                        String departmentName,
                                                        String optionName,
                                                        String levelName,
                                                        String courseTitle,
                                                        String moduleTitle,
                                                        String chapterTitle,
                                                        String sectionTitle,
                                                        String subSectionTitle){
        SubSection concernedSubSection = null;

        subSectionId = subSectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();

        ServerResponse<SubSection> srSubSectionFoundById =
                subSectionService.findSubSectionOfSectionById(subSectionId);

        if(srSubSectionFoundById.getResponseCode() == ResponseCode.SUBSECTION_FOUND){
            try {
                ServerResponse<SubSection> srSubSection = subSectionService.
                        findSubSectionOfSectionByTitle(schoolName, departmentName, optionName,
                                levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle,
                                subSectionTitle);
                concernedSubSection = srSubSection.getAssociatedObject();
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
            } catch (ChapterNotFoundException e) {
                e.printStackTrace();
            } catch (SectionNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            concernedSubSection = srSubSectionFoundById.getAssociatedObject();
        }

        return Optional.ofNullable(concernedSubSection);
    }

    @Override
    public ServerResponse<Page<Paragraph>> findAllParagraphOfSubSection(String subSectionId,
                                                                        String schoolName,
                                                                        String departmentName,
                                                                        String optionName,
                                                                        String levelName,
                                                                        String courseTitle,
                                                                        String moduleTitle,
                                                                        String chapterTitle,
                                                                        String sectionTitle,
                                                                        String subSectionTitle,
                                                                        Pageable pageable)
            throws SubSectionNotFoundException {
        ServerResponse<Page<Paragraph>> srParagraphPage = new ServerResponse<>();

        subSectionId = subSectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();

        SubSection concernedSubSection = null;

        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle);

        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The specified sub section does not found in " +
                    "the system");
        }

        concernedSubSection = optionalSubSection.get();
        Page<Paragraph> pageofParagraph = paragraphRepository.
                findAllByOwnerSubSection(concernedSubSection, pageable);

        srParagraphPage.setErrorMessage("The paragraph page of subsection has been successfully listed");
        srParagraphPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srParagraphPage.setAssociatedObject(pageofParagraph);

        return srParagraphPage;
    }

    @Override
    public ServerResponse<Page<Paragraph>> findAllParagraphOfSubSection(String subSectionId,
                                                                        String schoolName,
                                                                        String departmentName,
                                                                        String optionName,
                                                                        String levelName,
                                                                        String courseTitle,
                                                                        String moduleTitle,
                                                                        String chapterTitle,
                                                                        String sectionTitle,
                                                                        String subSectionTitle,
                                                                        String keyword,
                                                                        Pageable pageable)
            throws SubSectionNotFoundException {
        ServerResponse<Page<Paragraph>> srParagraphPage = new ServerResponse<>();

        subSectionId = subSectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        keyword = keyword.trim();

        SubSection concernedSubSection = null;

        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle);

        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The specified sub section does not found in " +
                    "the system");
        }

        concernedSubSection = optionalSubSection.get();
        Page<Paragraph> pageofParagraph = paragraphRepository.
                findAllByOwnerSubSectionAndTitleContaining(concernedSubSection, keyword, pageable);

        srParagraphPage.setErrorMessage("The paragraph page of subsection has been successfully listed");
        srParagraphPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srParagraphPage.setAssociatedObject(pageofParagraph);

        return srParagraphPage;
    }

    @Override
    public ServerResponse<Page<Paragraph>> findAllParagraphOfSubSectionByType(String subSectionId,
                                                                              String schoolName,
                                                                              String departmentName,
                                                                              String optionName,
                                                                              String levelName,
                                                                              String courseTitle,
                                                                              String moduleTitle,
                                                                              String chapterTitle,
                                                                              String sectionTitle,
                                                                              String subSectionTitle,
                                                                              String paragraphType,
                                                                              Pageable pageable)
            throws SubSectionNotFoundException {
        ServerResponse<Page<Paragraph>> srParagraphPage = new ServerResponse<>();

        subSectionId = subSectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        paragraphType = paragraphType.trim();

        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle);

        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The specified sub section does not found " +
                    "in the system");
        }

        SubSection concernedSubSection = optionalSubSection.get();

        try {
            EnumCoursePartType enumParagraphType = EnumCoursePartType.valueOf(
                    paragraphType.toUpperCase());
            Page<Paragraph> pageofParagraph = paragraphRepository.
                    findAllByOwnerSubSectionAndParagraphType(concernedSubSection, enumParagraphType,
                            pageable);

            srParagraphPage.setErrorMessage("The paragraph page of section has been successfully listed");
            srParagraphPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srParagraphPage.setAssociatedObject(pageofParagraph);
        } catch (IllegalArgumentException e) {
            srParagraphPage.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srParagraphPage.setErrorMessage("IllegalArgumentException");
            srParagraphPage.setMoreDetails(e.getMessage());
        }

        return srParagraphPage;
    }

    @Override
    public ServerResponse<List<Paragraph>> findAllParagraphOfSubSection(String subSectionId,
                                                                        String schoolName,
                                                                        String departmentName,
                                                                        String optionName,
                                                                        String levelName,
                                                                        String courseTitle,
                                                                        String moduleTitle,
                                                                        String chapterTitle,
                                                                        String sectionTitle,
                                                                        String subSectionTitle,
                                                                        String sortBy,
                                                                        String direction)
            throws SubSectionNotFoundException {
        ServerResponse<List<Paragraph>> srParagraphList = new ServerResponse<>();

        subSectionId = subSectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();

        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle);

        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The specified sub section does not found " +
                    "in the system");
        }

        SubSection concernedSubSection = optionalSubSection.get();

        List<Paragraph> listofParagraph = new ArrayList<>();

        if (sortBy.equalsIgnoreCase("title")) {
            if (direction.equalsIgnoreCase("ASC")) {
                listofParagraph = paragraphRepository.
                        findAllByOwnerSubSectionOrderByTitleAsc(concernedSubSection);
            }else{
                listofParagraph = paragraphRepository.
                        findAllByOwnerSubSectionOrderByTitleDesc(concernedSubSection);
            }
        }else{
            if (direction.equalsIgnoreCase("ASC")) {
                listofParagraph = paragraphRepository.
                        findAllByOwnerSubSectionOrderByParagraphOrderAsc(concernedSubSection);
            }
            else{
                listofParagraph = paragraphRepository.
                        findAllByOwnerSubSectionOrderByParagraphOrderDesc(concernedSubSection);
            }
        }

        srParagraphList.setErrorMessage("The paragraph list of section has been successfully listed");
        srParagraphList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srParagraphList.setAssociatedObject(listofParagraph);

        return srParagraphList;
    }

    @Override
    public boolean isParagraphofSubSection(String paragraphId, String subSectionId)
            throws SubSectionNotFoundException {
        ServerResponse<List<Paragraph>> srListofParagraphofSubSection =
                this.findAllParagraphOfSubSection(subSectionId, null,
                        null, null, null, null,
                        null, null, null, null,
                        "paragraphOrder", "ASC");
        if(srListofParagraphofSubSection.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
            List<Paragraph> listofParagraphofSubsection =
                    srListofParagraphofSubSection.getAssociatedObject();
            for(Paragraph paragraph : listofParagraphofSubsection){
                if(paragraph.getId().equalsIgnoreCase(paragraphId)) return true;
            }
        }
        return false;
    }

    @Override
    public ServerResponse<List<Paragraph>> findAllParagraphOfSubSectionByType(String subSectionId,
                                                                              String schoolName,
                                                                              String departmentName,
                                                                              String optionName,
                                                                              String levelName,
                                                                              String courseTitle,
                                                                              String moduleTitle,
                                                                              String chapterTitle,
                                                                              String sectionTitle,
                                                                              String subSectionTitle,
                                                                              String paragraphType,
                                                                              String sortBy,
                                                                              String direction)
            throws SubSectionNotFoundException {
        ServerResponse<List<Paragraph>> srParagraphList = new ServerResponse<>();

        subSectionId = subSectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        paragraphType = paragraphType.trim();

        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle);

        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The specified sub section does not found " +
                    "in the system");
        }

        SubSection concernedSubSection = optionalSubSection.get();

        List<Paragraph> listofParagraph = new ArrayList<>();
        try {
            EnumCoursePartType enumSubSectionType = EnumCoursePartType.valueOf(
                    paragraphType.toUpperCase());
            if (sortBy.equalsIgnoreCase("title")) {
                if (direction.equalsIgnoreCase("ASC")) {
                    listofParagraph = paragraphRepository.
                            findAllByOwnerSubSectionAndParagraphTypeOrderByTitleAsc(
                                    concernedSubSection, enumSubSectionType);
                }else{
                    listofParagraph = paragraphRepository.
                            findAllByOwnerSubSectionAndParagraphTypeOrderByTitleDesc(
                                    concernedSubSection, enumSubSectionType);
                }
            }else{
                if (direction.equalsIgnoreCase("ASC")) {
                    listofParagraph = paragraphRepository.
                            findAllByOwnerSubSectionAndParagraphTypeOrderByParagraphOrderAsc(
                                    concernedSubSection, enumSubSectionType);
                }
                else{
                    listofParagraph = paragraphRepository.
                            findAllByOwnerSubSectionAndParagraphTypeOrderByParagraphOrderDesc(
                                    concernedSubSection, enumSubSectionType);
                }
            }

            srParagraphList.setErrorMessage("The paragraph list of section has been successfully listed");
            srParagraphList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srParagraphList.setAssociatedObject(listofParagraph);

        } catch (IllegalArgumentException e) {
            srParagraphList.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srParagraphList.setErrorMessage("IllegalArgumentException");
            srParagraphList.setMoreDetails(e.getMessage());
        }

        return srParagraphList;
    }

    @Override
    public Paragraph saveParagraph(Paragraph paragraph) {
        return paragraphRepository.save(paragraph);
    }

    @Override
    public ServerResponse<Paragraph> saveParagraph(String title,
                                                   int paragraphOrder,
                                                   String paragraphType,
                                                   String subSectionId,
                                                   String subSectionTitle,
                                                   String sectionTitle,
                                                   String chapterTitle,
                                                   String moduleTitle,
                                                   String courseTitle,
                                                   String levelName,
                                                   String optionName,
                                                   String departmentName,
                                                   String schoolName)
            throws SubSectionNotFoundException, DuplicateParagraphInSubSectionException {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();

        title = title.trim();
        paragraphType = paragraphType.trim();
        subSectionId = subSectionId.trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle);

        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The specified sub section does not found " +
                    "in the system");
        }

        SubSection concernedSubSection = optionalSubSection.get();

        try {
            ServerResponse<Paragraph> srParagraphDuplicated = this.findParagraphOfSubSectionByTitle(
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, subSectionTitle, title);
            if(srParagraphDuplicated.getResponseCode() == ResponseCode.PARAGRAPH_FOUND){
                throw new DuplicateParagraphInSubSectionException("The paragraph already found in the " +
                        "system");
            }

            EnumCoursePartType enumParagraphPartType = EnumCoursePartType.valueOf(
                    paragraphType.toUpperCase());
            List<Content> listofContent = new ArrayList<>();

            Paragraph paragraphToSaved = new Paragraph();
            paragraphToSaved.setListofContent(listofContent);
            paragraphToSaved.setOwnerSubSection(concernedSubSection);
            paragraphToSaved.setParagraphOrder(paragraphOrder);
            paragraphToSaved.setParagraphType(enumParagraphPartType);
            paragraphToSaved.setTitle(title);

            Paragraph paragraphSaved = paragraphRepository.save(paragraphToSaved);

            srParagraph.setErrorMessage("The paragraph has been successfully created in the system");
            srParagraph.setResponseCode(ResponseCode.PARAGRAPH_CREATED);
            srParagraph.setAssociatedObject(paragraphSaved);

        } catch (IllegalArgumentException e) {
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srParagraph.setErrorMessage("IllegalArgumentException");
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The school does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The department does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The option does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The level does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The course does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The Module does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The chapter does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The section does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @Override
    public ServerResponse<Paragraph> updateParagraph(String paragraphId,
                                                     String title,
                                                     int paragraphOrder,
                                                     String paragraphType,
                                                     String subSectionTitle,
                                                     String sectionTitle,
                                                     String chapterTitle,
                                                     String moduleTitle,
                                                     String courseTitle,
                                                     String levelName,
                                                     String optionName,
                                                     String departmentName,
                                                     String schoolName)
            throws ParagraphNotFoundException, DuplicateParagraphInSubSectionException {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();

        title = title.trim();
        paragraphType = paragraphType.trim();
        paragraphId = paragraphId.trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        Optional<Paragraph> optionalParagraph = paragraphRepository.findById(paragraphId);
        if(!optionalParagraph.isPresent()){
            throw new ParagraphNotFoundException("The paragraph specified does not found in the system");
        }
        Paragraph paragraphToUpdated1 = optionalParagraph.get();

        try {
            ServerResponse<Paragraph> srParagraphDuplicated = this.findParagraphOfSubSectionByTitle(
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, subSectionTitle, title);

            if(srParagraphDuplicated.getResponseCode() == ResponseCode.PARAGRAPH_FOUND){
                Paragraph paragraphToUpdate2 = srParagraphDuplicated.getAssociatedObject();
                if(!paragraphToUpdated1.getId().equalsIgnoreCase(paragraphToUpdate2.getId())){
                    throw new DuplicateParagraphInSubSectionException("A paragraph with the same name " +
                            "already exist in the system");
                }
            }
            else{
                paragraphToUpdated1.setTitle(title);
            }

            EnumCoursePartType enumParagraphPartType = EnumCoursePartType.valueOf(
                    paragraphType.toUpperCase());
            paragraphToUpdated1.setParagraphType(enumParagraphPartType);
            paragraphToUpdated1.setParagraphOrder(paragraphOrder);

            Paragraph paragraphUpdated = paragraphRepository.save(paragraphToUpdated1);

            srParagraph.setErrorMessage("The paragraph has been successfully updated");
            srParagraph.setResponseCode(ResponseCode.PARAGRAPH_UPDATED);
            srParagraph.setAssociatedObject(paragraphUpdated);

        } catch (IllegalArgumentException e){
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srParagraph.setErrorMessage("There is problem during conversion of string " +
                    "to enumcourseparttype");
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The school does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The department does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The option does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The level does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The course does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The module does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The chapter does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The section does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The subsection does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @Override
    public ServerResponse<Paragraph> updateParagraphTitle(String paragraphId,
                                                          String newParagraphTitle)
            throws ParagraphNotFoundException, DuplicateParagraphInSubSectionException {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();
        paragraphId = paragraphId.trim();
        newParagraphTitle = newParagraphTitle.toLowerCase().trim();

        Optional<Paragraph> optionalParagraph = paragraphRepository.findById(paragraphId);
        if(!optionalParagraph.isPresent()){
            throw new ParagraphNotFoundException("The paragraph specified does not found in the system");
        }
        Paragraph paragraphToUpdated = optionalParagraph.get();

        SubSection concernedSubSection = paragraphToUpdated.getOwnerSubSection();
        Section concernedSection = concernedSubSection.getOwnerSection();
        Chapter concernedChapter = concernedSection.getOwnerChapter();
        Module concernedModule = concernedChapter.getOwnerModule();
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
            String chapterTitle = concernedChapter.getTitle();
            String sectionTitle = concernedSection.getTitle();
            String subSectionTitle = concernedSubSection.getTitle();

            ServerResponse<Paragraph> srParagraphDuplicated = this.findParagraphOfSubSectionByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle,
                    sectionTitle, subSectionTitle);
            if(srParagraphDuplicated.getResponseCode() == ResponseCode.PARAGRAPH_FOUND){
                throw new DuplicateParagraphInSubSectionException("The new paragraph Title already exist " +
                        "in the system");
            }

            paragraphToUpdated.setTitle(newParagraphTitle);
            Paragraph paragraphUpdated = paragraphRepository.save(paragraphToUpdated);

            srParagraph.setErrorMessage("The paragraph title has been successfully updated");
            srParagraph.setResponseCode(ResponseCode.PARAGRAPH_UPDATED);
            srParagraph.setAssociatedObject(paragraphUpdated);

        } catch (CourseOutlineNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The course outline title does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_COURSEOUTLINE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The course  title does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The level name does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The department name does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SubSectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The subsection title does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SUBSECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The school name does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The section title does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The module title does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The option name does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srParagraph.setErrorMessage("The chapter title does not found in the system");
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    public Optional<Paragraph> findConcernedParagraph(String paragraphId,
                                                      String schoolName,
                                                      String departmentName,
                                                      String optionName,
                                                      String levelName,
                                                      String courseTitle,
                                                      String moduleTitle,
                                                      String chapterTitle,
                                                      String sectionTitle,
                                                      String subSectionTitle,
                                                      String paragraphTitle){
        Paragraph concernedParagraph = null;

        paragraphTitle = paragraphTitle.toLowerCase().trim();
        paragraphId = paragraphId.trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        ServerResponse<Paragraph> srParagraphFoundById = this.findParagraphOfSubSectionById(
                paragraphId);
        if(srParagraphFoundById.getResponseCode() != ResponseCode.SUBSECTION_FOUND){
            try {
                ServerResponse<Paragraph> srParagraph = this.findParagraphOfSubSectionByTitle(schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                        sectionTitle, subSectionTitle, paragraphTitle);
                concernedParagraph = srParagraph.getAssociatedObject();
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
            } catch (ChapterNotFoundException e) {
                e.printStackTrace();
            } catch (SectionNotFoundException e) {
                e.printStackTrace();
            } catch (SubSectionNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            concernedParagraph = srParagraphFoundById.getAssociatedObject();
        }

        return Optional.ofNullable(concernedParagraph);
    }

    @Override
    public ServerResponse<Paragraph> addContentToParagraph(String value,
                                                           String contentType,
                                                           String paragraphId,
                                                           String schoolName,
                                                           String departmentName,
                                                           String optionName,
                                                           String levelName,
                                                           String courseTitle,
                                                           String moduleTitle,
                                                           String chapterTitle,
                                                           String sectionTitle,
                                                           String subSectionTitle,
                                                           String paragraphTitle)
            throws ParagraphNotFoundException {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();
        value = value.trim();
        contentType = contentType.trim();
        paragraphTitle = paragraphTitle.toLowerCase().trim();
        paragraphId = paragraphId.trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();

        Paragraph concernedParagraph = null;
        Optional<Paragraph> optionalParagraph = this.findConcernedParagraph(paragraphId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle, subSectionTitle, paragraphTitle);
        if(!optionalParagraph.isPresent()){
            throw new ParagraphNotFoundException("The paragraph title does not found in the system");
        }
        concernedParagraph = optionalParagraph.get();
        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);

            concernedParagraph.getListofContent().add(contentSaved);
            Paragraph paragraphSavedWithContent = this.saveParagraph(concernedParagraph);

            srParagraph.setErrorMessage("A content has been added in the list of content of " +
                    "the sub section");
            srParagraph.setResponseCode(ResponseCode.CONTENT_ADDED);
            srParagraph.setAssociatedObject(paragraphSavedWithContent);

        } catch (IllegalArgumentException e) {
            srParagraph.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srParagraph.setErrorMessage("IllegalArgumentException");
            srParagraph.setMoreDetails(e.getMessage());
        }

        return srParagraph;
    }

    @Override
    public ServerResponse<Paragraph> removeContentToParagraph(String contentId,
                                                              String paragraphId,
                                                              String schoolName,
                                                              String departmentName,
                                                              String optionName,
                                                              String levelName,
                                                              String courseTitle,
                                                              String moduleTitle,
                                                              String chapterTitle,
                                                              String sectionTitle,
                                                              String subSectionTitle,
                                                              String paragraphTitle)
            throws ParagraphNotFoundException, ContentNotFoundException {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();

        contentId = contentId.trim();
        paragraphTitle = paragraphTitle.toLowerCase().trim();
        paragraphId = paragraphId.trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
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

        Paragraph concernedParagraph = null;

        Optional<Paragraph> optionalParagraph = this.findConcernedParagraph(paragraphId,
                schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                sectionTitle, subSectionTitle, paragraphTitle);
        if(!optionalParagraph.isPresent()){
            throw new ParagraphNotFoundException("The paragraph title does not found in the system");
        }
        concernedParagraph = optionalParagraph.get();

        srParagraph.setErrorMessage("A content has been removed in the list of content of " +
                "the section");
        srParagraph.setResponseCode(ResponseCode.CONTENT_DELETED);
        srParagraph.setAssociatedObject(concernedParagraph);

        return srParagraph;
    }

    @Override
    public ServerResponse<Paragraph> updateContentToParagraph(String contentId,
                                                              String value,
                                                              String paragraphId,
                                                              String schoolName,
                                                              String departmentName,
                                                              String optionName,
                                                              String levelName,
                                                              String courseTitle,
                                                              String moduleTitle,
                                                              String chapterTitle,
                                                              String sectionTitle,
                                                              String subSectionTitle,
                                                              String paragraphTitle)
            throws ParagraphNotFoundException, ContentNotFoundException {
        ServerResponse<Paragraph> srParagraph = new ServerResponse<>();
        value = value.trim();
        contentId = contentId.trim();
        paragraphTitle = paragraphTitle.toLowerCase().trim();
        paragraphId = paragraphId.trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
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

        Paragraph concernedParagraph = null;
        Optional<Paragraph> optionalParagraph = this.findConcernedParagraph(paragraphId, schoolName, departmentName,
                optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle, subSectionTitle,
                paragraphTitle);
        if(!optionalParagraph.isPresent()){
            throw new ParagraphNotFoundException("The paragraph title does not found in the system");
        }
        concernedParagraph = optionalParagraph.get();

        srParagraph.setErrorMessage("A content has been removed in the list of content of " +
                "the section");
        srParagraph.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srParagraph.setAssociatedObject(concernedParagraph);

        return srParagraph;
    }

    @Override
    public ServerResponse<Paragraph> deleteParagraph(String paragraphId,
                                                     String schoolName,
                                                     String departmentName,
                                                     String optionName,
                                                     String levelName,
                                                     String courseTitle,
                                                     String moduleTitle,
                                                     String chapterTitle,
                                                     String sectionTitle,
                                                     String subSectionTitle,
                                                     String paragraphTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException, ChapterNotFoundException,
            SectionNotFoundException, SubSectionNotFoundException, ParagraphNotFoundException {
        return null;
    }
}
