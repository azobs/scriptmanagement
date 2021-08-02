package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.SubSectionRepository;
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
public class SubSectionServiceImpl implements SubSectionService{

    SubSectionRepository subSectionRepository;
    ContentRepository contentRepository;
    SectionService sectionService;
    CourseService courseService;

    public SubSectionServiceImpl(SubSectionRepository subSectionRepository,
                                 ContentRepository contentRepository, SectionService sectionService,
                                 CourseService courseService) {
        this.subSectionRepository = subSectionRepository;
        this.contentRepository = contentRepository;
        this.sectionService = sectionService;
        this.courseService = courseService;
    }

    @Override
    public ServerResponse<SubSection> findSubSectionOfSectionById(String subSectionId) {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        subSectionId = subSectionId.trim();
        Optional<SubSection> optionalSubSection = subSectionRepository.findById(subSectionId);
        if(!optionalSubSection.isPresent()){
            srSubSection.setErrorMessage("The subsection id does not match any subsection in the " +
                    "section associated to the chapter specified");
            srSubSection.setResponseCode(ResponseCode.SUBSECTION_NOT_FOUND);
        }
        else{
            srSubSection.setErrorMessage("The section has been successfully found in the system");
            srSubSection.setResponseCode(ResponseCode.SUBSECTION_FOUND);
            srSubSection.setAssociatedObject(optionalSubSection.get());
        }
        return srSubSection;
    }

    @Override
    public ServerResponse<SubSection> findSubSectionOfSectionByTitle(String schoolName,
                                                                     String departmentName,
                                                                     String optionName,
                                                                     String levelName,
                                                                     String courseTitle,
                                                                     String moduleTitle,
                                                                     String chapterTitle,
                                                                     String sectionTitle,
                                                                     String subSectionTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException,
            ChapterNotFoundException, SectionNotFoundException {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        subSectionTitle = subSectionTitle.toLowerCase().trim();

        ServerResponse<Section> srSection = sectionService.findSectionOfChapterByTitle(schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                sectionTitle);
        if(srSection.getResponseCode() != ResponseCode.SECTION_FOUND){
            throw  new SectionNotFoundException("The section specified does not found on the system");
        }
        Section concernedSection = srSection.getAssociatedObject();

        Optional<SubSection> optionalSubSection = subSectionRepository.
                findByOwnerSectionAndTitle(concernedSection, subSectionTitle);

        if(!optionalSubSection.isPresent()){
            srSubSection.setErrorMessage("The subsection title does not match any subsection in the " +
                    "section associated to the chapter specified");
            srSubSection.setResponseCode(ResponseCode.SUBSECTION_NOT_FOUND);
        }
        else{
            srSubSection.setErrorMessage("The subsection has been successfully found in the system");
            srSubSection.setResponseCode(ResponseCode.SUBSECTION_FOUND);
            srSubSection.setAssociatedObject(optionalSubSection.get());
        }

        return srSubSection;
    }

    public Optional<Section> findConcernedSection(String sectionId,
                                                  String schoolName,
                                                  String departmentName,
                                                  String optionName,
                                                  String levelName,
                                                  String courseTitle,
                                                  String moduleTitle,
                                                  String chapterTitle,
                                                  String sectionTitle){
        Section concernedSection = null;

        sectionId = sectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();

        ServerResponse<Section> srSectionFoundById = sectionService.findSectionOfChapterById(sectionId);
        if(srSectionFoundById.getResponseCode() != ResponseCode.SECTION_FOUND){
            try {
                ServerResponse<Section> srSection = sectionService.findSectionOfChapterByTitle(schoolName, departmentName,
                        optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);
                concernedSection = srSection.getAssociatedObject();
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
            }
        }
        else{
            concernedSection = srSectionFoundById.getAssociatedObject();
        }
        return Optional.ofNullable(concernedSection);
    }

    @Override
    public ServerResponse<Page<SubSection>> findAllSubSectionOfSection(String sectionId,
                                                                       String schoolName,
                                                                       String departmentName,
                                                                       String optionName,
                                                                       String levelName,
                                                                       String courseTitle,
                                                                       String moduleTitle,
                                                                       String chapterTitle,
                                                                       String sectionTitle,
                                                                       Pageable pageable)
            throws SectionNotFoundException {
        ServerResponse<Page<SubSection>> srSubSectionPage = new ServerResponse<>();

        sectionId = sectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();

        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName, departmentName,
                optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }

        Section concernedSection = optionalSection.get();
        Page<SubSection> pageofSubSection = subSectionRepository.findAllByOwnerSection(concernedSection,
                pageable);
        srSubSectionPage.setErrorMessage("The sub section page of section has been successfully listed");
        srSubSectionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srSubSectionPage.setAssociatedObject(pageofSubSection);

        return srSubSectionPage;
    }

    @Override
    public ServerResponse<Page<SubSection>> findAllSubSectionOfSection(String sectionId, String schoolName,
                                                                       String departmentName, String optionName,
                                                                       String levelName, String courseTitle,
                                                                       String moduleTitle, String chapterTitle,
                                                                       String sectionTitle, String keyword,
                                                                       Pageable pageable)
            throws SectionNotFoundException {
        ServerResponse<Page<SubSection>> srSubSectionPage = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionId = sectionId.toLowerCase().trim();
        keyword = keyword.trim();

        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName, departmentName,
                optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }

        Section concernedSection = optionalSection.get();
        Page<SubSection> pageofSubSection = subSectionRepository.
                findAllByOwnerSectionAndTitleContaining(concernedSection, keyword, pageable);
        srSubSectionPage.setErrorMessage("The sub section page of section has been successfully listed");
        srSubSectionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srSubSectionPage.setAssociatedObject(pageofSubSection);

        return srSubSectionPage;
    }

    @Override
    public ServerResponse<Page<SubSection>> findAllSubSectionOfSectionByType(String sectionId, String schoolName,
                                                                             String departmentName, String optionName,
                                                                             String levelName, String courseTitle,
                                                                             String moduleTitle, String chapterTitle,
                                                                             String sectionTitle, String subSectionType,
                                                                             Pageable pageable)
            throws SectionNotFoundException {
        ServerResponse<Page<SubSection>> srSubSectionPage = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionId = sectionId.toLowerCase().trim();
        subSectionType = subSectionType.trim();

        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName, departmentName,
                optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }

        Section concernedSection = optionalSection.get();
        try {
            EnumCoursePartType enumSubSectionType = EnumCoursePartType.valueOf(
                    subSectionType.toUpperCase());
            Page<SubSection> pageofSubSection = subSectionRepository.findAllByOwnerSectionAndSubsectionType(
                    concernedSection, enumSubSectionType, pageable);

            srSubSectionPage.setErrorMessage("The sub section page of section has been successfully listed");
            srSubSectionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srSubSectionPage.setAssociatedObject(pageofSubSection);
        } catch (IllegalArgumentException e) {
            srSubSectionPage.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSubSectionPage.setErrorMessage("IllegalArgumentException");
            srSubSectionPage.setMoreDetails(e.getMessage());
        }

        return srSubSectionPage;
    }

    @Override
    public ServerResponse<List<SubSection>> findAllSubSectionOfSection(String sectionId,
                                                                       String schoolName,
                                                                       String departmentName,
                                                                       String optionName,
                                                                       String levelName,
                                                                       String courseTitle,
                                                                       String moduleTitle,
                                                                       String chapterTitle,
                                                                       String sectionTitle,
                                                                       String sortBy,
                                                                       String direction)
            throws SectionNotFoundException {
        ServerResponse<List<SubSection>> srSubSectionList = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionId = sectionId.toLowerCase().trim();

        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName, departmentName,
                optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }

        Section concernedSection = optionalSection.get();

        List<SubSection> listofSubSection = new ArrayList<>();

        if (sortBy.equalsIgnoreCase("title")) {
            if (direction.equalsIgnoreCase("ASC")) {
                listofSubSection = subSectionRepository.
                        findAllByOwnerSectionOrderByTitleAsc(concernedSection);
            }else{
                listofSubSection = subSectionRepository.
                        findAllByOwnerSectionOrderByTitleDesc(concernedSection);
            }
        }else{
            if (direction.equalsIgnoreCase("ASC")) {
                listofSubSection = subSectionRepository.
                        findAllByOwnerSectionOrderBySubSectionOrderAsc(concernedSection);
            }
            else{
                listofSubSection = subSectionRepository.
                        findAllByOwnerSectionOrderBySubSectionOrderDesc(concernedSection);
            }
        }

        srSubSectionList.setErrorMessage("The sub section list of section has been successfully listed");
        srSubSectionList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srSubSectionList.setAssociatedObject(listofSubSection);

        return srSubSectionList;
    }

    @Override
    public ServerResponse<List<SubSection>> findAllSubSectionOfSectionByType(String sectionId,
                                                                             String schoolName,
                                                                             String departmentName,
                                                                             String optionName,
                                                                             String levelName,
                                                                             String courseTitle,
                                                                             String moduleTitle,
                                                                             String chapterTitle,
                                                                             String sectionTitle,
                                                                             String subSectionType,
                                                                             String sortBy,
                                                                             String direction)
            throws SectionNotFoundException {
        ServerResponse<List<SubSection>> srSubSectionList = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionId = sectionId.toLowerCase().trim();
        subSectionType = subSectionType.trim();

        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName, departmentName,
                optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }

        Section concernedSection = optionalSection.get();

        List<SubSection> listOfSubSection = new ArrayList<>();

        try {
            EnumCoursePartType enumSubSectionType = EnumCoursePartType.valueOf(
                    subSectionType.toUpperCase());
            if (sortBy.equalsIgnoreCase("title")) {
                if (direction.equalsIgnoreCase("ASC")) {
                    listOfSubSection = subSectionRepository.
                            findAllByOwnerSectionAndSubSectionTypeOrderByTitleAsc(
                                    concernedSection, enumSubSectionType);
                }else{
                    listOfSubSection = subSectionRepository.
                            findAllByOwnerSectionAndSubSectionTypeOrderByTitleDesc(
                                    concernedSection, enumSubSectionType);
                }
            }else{
                if (direction.equalsIgnoreCase("ASC")) {
                    listOfSubSection = subSectionRepository.
                            findAllByOwnerSectionAndSubSectionTypeOrderBySubSectionOrderAsc(
                                    concernedSection, enumSubSectionType);
                }
                else{
                    listOfSubSection = subSectionRepository.
                            findAllByOwnerSectionAndSubSectionTypeOrderBySubSectionOrderDesc(
                                    concernedSection, enumSubSectionType);
                }
            }

            srSubSectionList.setErrorMessage("The section list of chapter has been successfully listed");
            srSubSectionList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srSubSectionList.setAssociatedObject(listOfSubSection);
        } catch (IllegalArgumentException e) {
            srSubSectionList.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSubSectionList.setErrorMessage("IllegalArgumentException");
            srSubSectionList.setMoreDetails(e.getMessage());
        }

        return srSubSectionList;
    }

    @Override
    public SubSection saveSubSection(SubSection subSection) {
        return subSectionRepository.save(subSection);
    }

    @Override
    public ServerResponse<SubSection> saveSubSection(String title, int subSectionOrder, String subSectionType,
                                                     String sectionId, String sectionTitle, String chapterTitle,
                                                     String moduleTitle, String courseTitle, String levelName,
                                                     String optionName, String departmentName, String schoolName)
            throws SectionNotFoundException, DuplicateSubSectionInSectionException {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        title = title.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionId = sectionId.trim();
        subSectionType = subSectionType.trim();

        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName, departmentName,
                optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }

        Section concernedSection = optionalSection.get();

        try {
            ServerResponse<SubSection> srSubSectionDuplicated = this.findSubSectionOfSectionByTitle(
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, title);
            if(srSubSectionDuplicated.getResponseCode() == ResponseCode.SUBSECTION_FOUND){
                throw new DuplicateSubSectionInSectionException("The subsection already exist in the system with " +
                        "the same title");
            }
            EnumCoursePartType enumSubSectionPartType = EnumCoursePartType.valueOf(
                    subSectionType.toUpperCase());
            List<Content> listofContent = new ArrayList<>();

            SubSection subSectionToSaved = new SubSection();
            subSectionToSaved.setListofContent(listofContent);
            subSectionToSaved.setOwnerSection(concernedSection);
            subSectionToSaved.setSubSectionOrder(subSectionOrder);
            subSectionToSaved.setSubSectionType(enumSubSectionPartType);
            subSectionToSaved.setTitle(title);

            SubSection subSectionSaved = subSectionRepository.save(subSectionToSaved);

            srSubSection.setErrorMessage("The sub section has been successfully created in the system");
            srSubSection.setResponseCode(ResponseCode.SUBSECTION_CREATED);
            srSubSection.setAssociatedObject(subSectionSaved);

        } catch (IllegalArgumentException e) {
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSubSection.setErrorMessage("IllegalArgumentException");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The school does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The department does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The option does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The level does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The course does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The module does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The chapter does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        }


        return srSubSection;
    }

    @Override
    public ServerResponse<SubSection> updateSubSection(String subSectionId, String title, int subSectionOrder,
                                                       String subSectionType, String sectionTitle, String chapterTitle,
                                                       String moduleTitle, String courseTitle, String levelName,
                                                       String optionName, String departmentName, String schoolName)
            throws SubSectionNotFoundException, DuplicateSubSectionInSectionException {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        title = title.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        subSectionId = subSectionId.trim();
        subSectionType = subSectionType.trim();

        Optional<SubSection> optionalSubSection = subSectionRepository.findById(subSectionId);
        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The sub section specified does not found in the system");
        }
        SubSection subSectionToUpdated1 = optionalSubSection.get();

        try {
            ServerResponse<SubSection> srSubSectionDuplicated = this.findSubSectionOfSectionByTitle(
                    schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                    chapterTitle, sectionTitle, title);
            if(srSubSectionDuplicated.getResponseCode() == ResponseCode.SUBSECTION_FOUND){
                SubSection subSectionToUpdate2 = srSubSectionDuplicated.getAssociatedObject();
                if(!subSectionToUpdated1.getId().equalsIgnoreCase(subSectionToUpdate2.getId())){
                    throw new DuplicateSubSectionInSectionException("A sub section with the same name " +
                            "already exist in the system");
                }
            }
            else{
                subSectionToUpdated1.setTitle(title);
            }
            EnumCoursePartType enumSubSectionPartType = EnumCoursePartType.valueOf(
                    subSectionType.toUpperCase());
            subSectionToUpdated1.setSubSectionType(enumSubSectionPartType);
            subSectionToUpdated1.setSubSectionOrder(subSectionOrder);

            SubSection subSectionUpdated = subSectionRepository.save(subSectionToUpdated1);

            srSubSection.setErrorMessage("The sub section has been successfully updated");
            srSubSection.setResponseCode(ResponseCode.SUBSECTION_UPDATED);
            srSubSection.setAssociatedObject(subSectionUpdated);

        } catch (IllegalArgumentException e){
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSubSection.setErrorMessage("There is problem during conversion of string " +
                    "to enumcourseparttype");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSubSection.setErrorMessage("The school does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSubSection.setErrorMessage("The department does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSubSection.setErrorMessage("The option does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSubSection.setErrorMessage("The level does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSubSection.setErrorMessage("The course does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSubSection.setErrorMessage("The module does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSubSection.setErrorMessage("The chapter does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSubSection.setErrorMessage("The section does not found in the system");
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }

    @Override
    public ServerResponse<SubSection> updateSubSectionTitle(String subSectionId, String newSubSectionTitle)
            throws SubSectionNotFoundException, DuplicateSubSectionInSectionException {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        subSectionId = subSectionId.trim();
        newSubSectionTitle = newSubSectionTitle.toLowerCase().trim();

        Optional<SubSection> optionalSubSection = subSectionRepository.findById(subSectionId);
        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The sub section specified does not found in the system");
        }
        SubSection subSectionToUpdated = optionalSubSection.get();

        Section concernedSection = subSectionToUpdated.getOwnerSection();
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

            ServerResponse<SubSection> srSubSectionDuplicated = this.findSubSectionOfSectionByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle,
                    sectionTitle);
            if(srSubSectionDuplicated.getResponseCode() == ResponseCode.SUBSECTION_FOUND){
                throw new DuplicateSubSectionInSectionException("The new sub section Title already exist " +
                        "in the system");
            }

            subSectionToUpdated.setTitle(newSubSectionTitle);
            SubSection subSectionUpdated = subSectionRepository.save(subSectionToUpdated);

            srSubSection.setErrorMessage("The sub section title has been successfully updated");
            srSubSection.setResponseCode(ResponseCode.SUBSECTION_UPDATED);
            srSubSection.setAssociatedObject(subSectionUpdated);

        } catch (CourseOutlineNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The course outline title does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_COURSEOUTLINE_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The course title does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The level name does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The department name does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The school name does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (SectionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The section does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_SECTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The module does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The option does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSubSection.setErrorMessage("The chapter does not found in the system");
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
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
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        subSectionId = subSectionId.trim();
        subSectionTitle = subSectionTitle.trim();

        SubSection concernedSubSection = null;

        ServerResponse<SubSection> srSubSectionFoundById = this.findSubSectionOfSectionById(
                subSectionId);
        if(srSubSectionFoundById.getResponseCode() != ResponseCode.SUBSECTION_FOUND){
            try {
                ServerResponse<SubSection> srSubSection = this.findSubSectionOfSectionByTitle(schoolName, departmentName,
                        optionName, levelName, courseTitle, moduleTitle, chapterTitle, sectionTitle,
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
    public ServerResponse<SubSection> addContentToSubSection(String value, String contentType, String subSectionId,
                                                             String schoolName, String departmentName,
                                                             String optionName, String levelName, String courseTitle,
                                                             String moduleTitle, String chapterTitle,
                                                             String sectionTitle, String subSectionTitle)
            throws SubSectionNotFoundException {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        value = value.trim();
        contentType = contentType.trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        subSectionId = subSectionId.trim();
        subSectionTitle = subSectionTitle.trim();

        SubSection concernedSubSection = null;
        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                sectionTitle, subSectionTitle);
        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The sub section title does not found in the system");
        }
        concernedSubSection = optionalSubSection.get();

        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);

            concernedSubSection.getListofContent().add(contentSaved);
            SubSection subSectionSavedWithContent = this.saveSubSection(concernedSubSection);

            srSubSection.setErrorMessage("A content has been added in the list of content of " +
                    "the sub section");
            srSubSection.setResponseCode(ResponseCode.CONTENT_ADDED);
            srSubSection.setAssociatedObject(subSectionSavedWithContent);
        }
        catch (IllegalArgumentException e) {
            srSubSection.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSubSection.setErrorMessage("IllegalArgumentException");
            srSubSection.setMoreDetails(e.getMessage());
        }

        return srSubSection;
    }

    @Override
    public ServerResponse<SubSection> removeContentToSubSection(String contentId, String subSectionId,
                                                                String schoolName, String departmentName,
                                                                String optionName, String levelName,
                                                                String courseTitle, String moduleTitle,
                                                                String chapterTitle, String sectionTitle,
                                                                String subSectionTitle)
            throws SubSectionNotFoundException, ContentNotFoundException {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();

        contentId = contentId.trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        subSectionId = subSectionId.trim();
        subSectionTitle = subSectionTitle.trim();

        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any " +
                    "content in the system");
        }
        contentRepository.deleteById(contentId);

        SubSection concernedSubSection = null;
        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                sectionTitle, subSectionTitle);
        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The sub section title does not found in the system");
        }
        concernedSubSection = optionalSubSection.get();

        srSubSection.setErrorMessage("A content has been removed in the list of content of " +
                "the section");
        srSubSection.setResponseCode(ResponseCode.CONTENT_DELETED);
        srSubSection.setAssociatedObject(concernedSubSection);


        return srSubSection;
    }

    @Override
    public ServerResponse<SubSection> updateContentToSubSection(String contentId, String value,
                                                                String subSectionId, String schoolName,
                                                                String departmentName, String optionName,
                                                                String levelName, String courseTitle,
                                                                String moduleTitle, String chapterTitle,
                                                                String sectionTitle, String subSectionTitle)
            throws SubSectionNotFoundException, ContentNotFoundException {
        ServerResponse<SubSection> srSubSection = new ServerResponse<>();
        value = value.trim();
        sectionTitle = sectionTitle.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        subSectionId = subSectionId.trim();
        subSectionTitle = subSectionTitle.trim();

        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if(!optionalContent.isPresent()){
            throw new ContentNotFoundException("The content id specified does not match any " +
                    "content in the system");
        }

        Content content = optionalContent.get();
        content.setValue(value);
        contentRepository.save(content);

        SubSection concernedSubSection = null;
        Optional<SubSection> optionalSubSection = this.findConcernedSubSection(subSectionId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                sectionTitle, subSectionTitle);
        if(!optionalSubSection.isPresent()){
            throw new SubSectionNotFoundException("The sub section title does not found in the system");
        }
        concernedSubSection = optionalSubSection.get();

        srSubSection.setErrorMessage("A content has been removed in the list of content of " +
                "the section");
        srSubSection.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srSubSection.setAssociatedObject(concernedSubSection);

        return srSubSection;
    }

    @Override
    public ServerResponse<SubSection> deleteSubSection(String subSectionId, String schoolName, String departmentName,
                                                       String optionName, String levelName, String courseTitle,
                                                       String moduleTitle, String chapterTitle, String sectionTitle,
                                                       String subSectionTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException, ChapterNotFoundException,
            SectionNotFoundException, SubSectionNotFoundException {
        return null;
    }
}
