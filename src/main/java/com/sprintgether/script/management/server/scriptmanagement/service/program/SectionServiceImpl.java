package com.sprintgether.script.management.server.scriptmanagement.service.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.program.SectionRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
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
public class SectionServiceImpl implements SectionService {

    SectionRepository sectionRepository;
    ContentRepository contentRepository;
    ChapterService chapterService;
    CourseService courseService;

    public SectionServiceImpl(SectionRepository sectionRepository, ContentRepository contentRepository,
                              ChapterService chapterService, CourseService courseService) {
        this.sectionRepository = sectionRepository;
        this.contentRepository = contentRepository;
        this.chapterService = chapterService;
        this.courseService = courseService;
    }

    @Override
    public ServerResponse<Section> findSectionOfChapterById(String sectionId) {
        ServerResponse<Section> srSection = new ServerResponse<>();
        sectionId = sectionId.trim();
        Optional<Section> optionalSection = sectionRepository.findById(sectionId);
        if(!optionalSection.isPresent()){
            srSection.setErrorMessage("The section id does not match any section in the " +
                    "chapter associated to the module specified");
            srSection.setResponseCode(ResponseCode.SECTION_NOT_FOUND);
        }
        else{
            srSection.setErrorMessage("The section has been successfully found in the system");
            srSection.setResponseCode(ResponseCode.SECTION_FOUND);
            srSection.setAssociatedObject(optionalSection.get());
        }
        return srSection;
    }

    @Override
    public ServerResponse<Section> findSectionOfChapterByTitle(String schoolName, String departmentName,
                                                               String optionName, String levelName,
                                                               String courseTitle, String moduleTitle,
                                                               String chapterTitle, String sectionTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException,
            ChapterNotFoundException {
        ServerResponse<Section> srSection = new ServerResponse<>();

        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();

        ServerResponse<Chapter> srChapter = chapterService.findChapterOfModuleByTitle(schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        if(srChapter.getResponseCode() != ResponseCode.CHAPTER_FOUND){
            throw  new ChapterNotFoundException("The chapter specified does not found on the system");
        }
        Chapter concernedChapter = srChapter.getAssociatedObject();

        Optional<Section> optionalSection = sectionRepository.
                findByOwnerChapterAndTitle(concernedChapter, sectionTitle);

        if(!optionalSection.isPresent()){
            srSection.setErrorMessage("The section title does not match any section in the " +
                    "chapter associated to the module specified");
            srSection.setResponseCode(ResponseCode.SECTION_NOT_FOUND);
        }
        else{
            srSection.setErrorMessage("The section has been successfully found in the system");
            srSection.setResponseCode(ResponseCode.SECTION_FOUND);
            srSection.setAssociatedObject(optionalSection.get());
        }

        return srSection;
    }

    public Optional<Chapter> findConcernedChapter(String chapterId, String schoolName,
                                                  String departmentName, String optionName,
                                                  String levelName, String courseTitle,
                                                  String moduleTitle, String chapterTitle){
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        chapterId = chapterId.toLowerCase().trim();

        Chapter concernedChapter = null;
        ServerResponse<Chapter> srChapterFoundById = chapterService.findChapterOfModuleById(chapterId);
        if(srChapterFoundById.getResponseCode() != ResponseCode.CHAPTER_FOUND){
            try {
                ServerResponse<Chapter> srChpater = chapterService.findChapterOfModuleByTitle(
                        schoolName, departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle);
                concernedChapter = srChpater.getAssociatedObject();
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
    public ServerResponse<Page<Section>> findAllSectionOfChapter(String chapterId, String schoolName,
                                                                 String departmentName,
                                                                 String optionName,
                                                                 String levelName,
                                                                 String courseTitle,
                                                                 String moduleTitle,
                                                                 String chapterTitle,
                                                                 Pageable pageable)
            throws ChapterNotFoundException {
        ServerResponse<Page<Section>> srSectionPage = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        chapterId = chapterId.toLowerCase().trim();

        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found on the system");
        }
        Chapter concernedChapter = optionalChapter.get();
        Page<Section> pageofSection = sectionRepository.findAllByOwnerChapter(concernedChapter,
                pageable);
        srSectionPage.setErrorMessage("The section page of chapter has been successfully listed");
        srSectionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srSectionPage.setAssociatedObject(pageofSection);
        return srSectionPage;
    }

    @Override
    public ServerResponse<Page<Section>> findAllSectionOfChapter(String chapterId,
                                                                 String schoolName,
                                                                 String departmentName,
                                                                 String optionName,
                                                                 String levelName,
                                                                 String courseTitle,
                                                                 String moduleTitle,
                                                                 String chapterTitle,
                                                                 String keyword,
                                                                 Pageable pageable)
            throws ChapterNotFoundException {
        ServerResponse<Page<Section>> srSectionPage = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        chapterId = chapterId.toLowerCase().trim();
        keyword = keyword.trim();
        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found on the system");
        }
        Chapter concernedChapter = optionalChapter.get();
        Page<Section> pageofSection = sectionRepository.
                findAllByOwnerChapterAndTitleContaining(concernedChapter, keyword, pageable);
        srSectionPage.setErrorMessage("The section page of chapter has been successfully listed");
        srSectionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srSectionPage.setAssociatedObject(pageofSection);
        return srSectionPage;
    }

    @Override
    public ServerResponse<Page<Section>> findAllSectionOfChapterByType(String chapterId,
                                                                       String schoolName,
                                                                       String departmentName,
                                                                       String optionName,
                                                                       String levelName,
                                                                       String courseTitle,
                                                                       String moduleTitle,
                                                                       String chapterTitle,
                                                                       String sectionType,
                                                                       Pageable pageable)
            throws ChapterNotFoundException {
        ServerResponse<Page<Section>> srSectionPage = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        chapterId = chapterId.toLowerCase().trim();

        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found on the system");
        }
        Chapter concernedChapter = optionalChapter.get();

        try {
            EnumCoursePartType enumSectionType = EnumCoursePartType.valueOf(
                    sectionType.toUpperCase());
            Page<Section> pageofSection = sectionRepository.
                    findAllByOwnerChapterAndSectionType(concernedChapter, enumSectionType, pageable);

            srSectionPage.setErrorMessage("The section page of chapter has been successfully listed");
            srSectionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srSectionPage.setAssociatedObject(pageofSection);
        } catch (IllegalArgumentException e) {
            srSectionPage.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSectionPage.setErrorMessage("IllegalArgumentException");
            srSectionPage.setMoreDetails(e.getMessage());
        }

        return srSectionPage;
    }

    @Override
    public ServerResponse<List<Section>> findAllSectionOfChapter(String chapterId,
                                                                 String schoolName,
                                                                 String departmentName,
                                                                 String optionName,
                                                                 String levelName,
                                                                 String courseTitle,
                                                                 String moduleTitle,
                                                                 String chapterTitle,
                                                                 String sortBy,
                                                                 String direction)
            throws ChapterNotFoundException {
        ServerResponse<List<Section>> srSectionList = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        chapterId = chapterId.toLowerCase().trim();

        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found on the system");
        }
        Chapter concernedChapter = optionalChapter.get();

        List<Section> listofSection = new ArrayList<>();

        if (sortBy.equalsIgnoreCase("title")) {
            if (direction.equalsIgnoreCase("ASC")) {
                listofSection = sectionRepository.
                        findAllByOwnerChapterOrderByTitleAsc(concernedChapter);
            }else{
                listofSection = sectionRepository.
                        findAllByOwnerChapterOrderByTitleDesc(concernedChapter);
            }
        }else{
            if (direction.equalsIgnoreCase("ASC")) {
                listofSection = sectionRepository.
                        findAllByOwnerChapterOrderBySectionOrderAsc(concernedChapter);
            }
            else{
                listofSection = sectionRepository.
                        findAllByOwnerChapterOrderBySectionOrderDesc(concernedChapter);
            }
        }

        srSectionList.setErrorMessage("The section list of chapter has been successfully listed");
        srSectionList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srSectionList.setAssociatedObject(listofSection);

        return srSectionList;
    }

    @Override
    public boolean isSectionofChapter(String sectionId, String chapterId)
            throws ChapterNotFoundException {
        ServerResponse<List<Section>> srListofSectionofChapter =
                this.findAllSectionOfChapter(chapterId, null, null,
                        null, null, null, null,
                        null, "sectionOrder", "ASC");
        if(srListofSectionofChapter.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
            List<Section> listofSectionofChapter = srListofSectionofChapter.getAssociatedObject();
            for(Section section : listofSectionofChapter){
                if(section.getId().equalsIgnoreCase(sectionId)) return true;
            }
        }
        return false;
    }

    @Override
    public ServerResponse<List<Section>> findAllSectionOfChapterByType(String chapterId,
                                                                       String schoolName,
                                                                       String departmentName,
                                                                       String optionName,
                                                                       String levelName,
                                                                       String courseTitle,
                                                                       String moduleTitle,
                                                                       String chapterTitle,
                                                                       String sectionType,
                                                                       String sortBy,
                                                                       String direction)
            throws ChapterNotFoundException {
        ServerResponse<List<Section>> srSectionList = new ServerResponse<>();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionType = sectionType.trim();
        chapterId = chapterId.toLowerCase().trim();

        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found on the system");
        }
        Chapter concernedChapter = optionalChapter.get();

        List<Section> listofSection = new ArrayList<>();

        try {
            EnumCoursePartType enumSectionType = EnumCoursePartType.valueOf(
                    sectionType.toUpperCase());

            if (sortBy.equalsIgnoreCase("title")) {
                if (direction.equalsIgnoreCase("ASC")) {
                    listofSection = sectionRepository.
                            findAllByOwnerChapterAndSectionTypeOrderByTitleAsc(
                            concernedChapter, enumSectionType);
                }else{
                    listofSection = sectionRepository.
                            findAllByOwnerChapterAndSectionTypeOrderByTitleDesc(
                            concernedChapter, enumSectionType);
                }
            }else{
                if (direction.equalsIgnoreCase("ASC")) {
                    listofSection = sectionRepository.
                            findAllByOwnerChapterAndSectionTypeOrderBySectionOrderAsc(
                            concernedChapter, enumSectionType);
                }
                else{
                    listofSection = sectionRepository.
                            findAllByOwnerChapterAndSectionTypeOrderBySectionOrderDesc(
                            concernedChapter, enumSectionType);
                }
            }

            srSectionList.setErrorMessage("The section list of chapter has been successfully listed");
            srSectionList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srSectionList.setAssociatedObject(listofSection);
        } catch (IllegalArgumentException e) {
            srSectionList.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSectionList.setErrorMessage("IllegalArgumentException");
            srSectionList.setMoreDetails(e.getMessage());
        }



        return srSectionList;
    }

    @Override
    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

    @Override
    public ServerResponse<Section> saveSection(String title,
                                               int sectionOrder,
                                               String sectionType,
                                               String chapterId,
                                               String chapterTitle,
                                               String moduleTitle,
                                               String courseTitle,
                                               String levelName,
                                               String optionName,
                                               String departmentName,
                                               String schoolName)
            throws ChapterNotFoundException, DuplicateSectionInChapterException {
        ServerResponse<Section> srSection = new ServerResponse<>(ResponseCode.SECTION_NOT_CREATED);
        title = title.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionType = sectionType.trim();
        chapterId = chapterId.toLowerCase().trim();

        Optional<Chapter> optionalChapter = this.findConcernedChapter(chapterId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle);
        if(!optionalChapter.isPresent()){
            throw new ChapterNotFoundException("The specified chapter does not found on the system");
        }
        Chapter concernedChapter = optionalChapter.get();

        try {
            ServerResponse<Section> srSectionDuplicated = this.findSectionOfChapterByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    title);
            if(srSectionDuplicated.getResponseCode() == ResponseCode.SECTION_FOUND){
                throw new DuplicateSectionInChapterException("The section title already exist " +
                        "in the chapter");
            }
            EnumCoursePartType enumSectionPartType = EnumCoursePartType.valueOf(
                    sectionType.toUpperCase());
            List<Content> listofContent = new ArrayList<>();

            Section sectionToSaved = new Section();
            sectionToSaved.setListofContent(listofContent);
            sectionToSaved.setOwnerChapter(concernedChapter);
            sectionToSaved.setSectionOrder(sectionOrder);
            sectionToSaved.setSectionType(enumSectionPartType);
            sectionToSaved.setTitle(title);

            Section sectionSaved = sectionRepository.save(sectionToSaved);

            srSection.setErrorMessage("The section has been successfully created in the system");
            srSection.setResponseCode(ResponseCode.SECTION_CREATED);
            srSection.setAssociatedObject(sectionSaved);

        } catch (IllegalArgumentException e) {
            srSection.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSection.setErrorMessage("IllegalArgumentException");
            srSection.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSection.setErrorMessage("The school name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSection.setErrorMessage("The department name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSection.setErrorMessage("The option name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSection.setErrorMessage("The level name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSection.setErrorMessage("The course name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSection.setErrorMessage("The module name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @Override
    public ServerResponse<Section> updateSection(String sectionId,
                                                 String title,
                                                 int sectionOrder,
                                                 String sectionType,
                                                 String chapterTitle,
                                                 String moduleTitle,
                                                 String courseTitle,
                                                 String levelName,
                                                 String optionName,
                                                 String departmentName,
                                                 String schoolName)
            throws SectionNotFoundException, DuplicateSectionInChapterException {
        ServerResponse<Section> srSection = new ServerResponse<>(ResponseCode.SECTION_NOT_UPDATED);
        sectionId = sectionId.trim();
        title = title.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionType = sectionType.trim();

        Optional<Section> optionalSection = sectionRepository.findById(sectionId);
        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The section specified does not found in the system");
        }
        Section sectionToUpdated1 = optionalSection.get();

        try {
            ServerResponse<Section> srSectionDuplicated = this.findSectionOfChapterByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    title);
            if(srSectionDuplicated.getResponseCode() == ResponseCode.SECTION_FOUND){
                Section sectionToUpdate2 = srSectionDuplicated.getAssociatedObject();
                if(!sectionToUpdated1.getId().equalsIgnoreCase(sectionToUpdate2.getId())){
                    throw new DuplicateSectionInChapterException("A section with the same name " +
                            "already exist " +
                            "in the system");
                }
            }
            else{
                sectionToUpdated1.setTitle(title);
            }
            EnumCoursePartType enumSectionPartType = EnumCoursePartType.valueOf(
                    sectionType.toUpperCase());

            sectionToUpdated1.setSectionOrder(sectionOrder);
            sectionToUpdated1.setSectionType(enumSectionPartType);
            sectionToUpdated1.setSectionOrder(sectionOrder);

            Section sectionUpdated = sectionRepository.save(sectionToUpdated1);

            srSection.setErrorMessage("The chapter has been successfully updated");
            srSection.setResponseCode(ResponseCode.SECTION_UPDATED);
            srSection.setAssociatedObject(sectionUpdated);

        } catch (IllegalArgumentException e){
            srSection.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSection.setErrorMessage("There is problem during conversion of string " +
                    "to enumcourseparttype");
            srSection.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSection.setErrorMessage("The school name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSection.setErrorMessage("The department name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSection.setErrorMessage("The option name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSection.setErrorMessage("The level name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSection.setErrorMessage("The course name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSection.setErrorMessage("The module name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSection.setErrorMessage("The chapter name does not found in the system");
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    @Override
    public ServerResponse<Section> updateSectionTitle(String sectionId, String newSectionTitle)
            throws SectionNotFoundException, DuplicateSectionInChapterException {
        ServerResponse<Section> srSection = new ServerResponse<>(ResponseCode.SECTION_NOT_UPDATED);
        sectionId = sectionId.trim();
        newSectionTitle = newSectionTitle.toLowerCase().trim();

        Optional<Section> optionalSection = sectionRepository.findById(sectionId);
        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The section specified does not found in the system");
        }
        Section sectionToUpdated = optionalSection.get();

        Chapter concernedChapter = sectionToUpdated.getOwnerChapter();
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

            ServerResponse<Section> srSectionDuplicated = this.findSectionOfChapterByTitle(schoolName,
                    departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                    newSectionTitle);
            if(srSectionDuplicated.getResponseCode() == ResponseCode.SECTION_FOUND){
                throw new DuplicateSectionInChapterException("The new section Title already exist " +
                        "in the system");
            }

            sectionToUpdated.setTitle(newSectionTitle);
            Section sectionUpdated = sectionRepository.save(sectionToUpdated);

            srSection.setErrorMessage("The section title has been successfully updated");
            srSection.setResponseCode(ResponseCode.SECTION_UPDATED);
            srSection.setAssociatedObject(sectionUpdated);


        } catch (CourseNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The course title does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_COURSE_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (CourseOutlineNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The course outline title does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_COURSEOUTLINE_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (ChapterNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The chapter title does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_CHAPTER_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The department name does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The school name does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The level name does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The option name does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srSection.setMoreDetails(e.getMessage());
        } catch (ModuleNotFoundException e) {
            //e.printStackTrace();
            srSection.setErrorMessage("The module name does not found in the system");
            srSection.setResponseCode(ResponseCode.EXCEPTION_MODULE_FOUND);
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    public Optional<Section> findConcernedSection(String sectionId, String schoolName,
                                                  String departmentName, String optionName,
                                                  String levelName, String courseTitle,
                                                  String moduleTitle, String chapterTitle,
                                                  String sectionTitle){
        sectionId = sectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();

        Section concernedSection = null;
        ServerResponse<Section> srSectionFoundById = this.findSectionOfChapterById(sectionId);
        if(srSectionFoundById.getResponseCode() != ResponseCode.SECTION_FOUND){
            try {
                ServerResponse<Section> srSection = this.findSectionOfChapterByTitle(schoolName,
                        departmentName, optionName, levelName, courseTitle, moduleTitle,
                        chapterTitle, sectionTitle);
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
    public ServerResponse<Section> addContentToSection(String value, String contentType,
                                                       String sectionId, String schoolName,
                                                       String departmentName, String optionName,
                                                       String levelName, String courseTitle,
                                                       String moduleTitle, String chapterTitle,
                                                       String sectionTitle)
            throws SectionNotFoundException {
        ServerResponse<Section> srSection = new ServerResponse<>();
        value = value.trim();
        contentType = contentType.trim();
        sectionId = sectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();

        Section concernedSection = null;
        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                sectionTitle);
        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The section title does not found in the system");
        }
        concernedSection = optionalSection.get();

        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);

            concernedSection.getListofContent().add(contentSaved);
            Section sectionSavedWithContent = this.saveSection(concernedSection);

            srSection.setErrorMessage("A content has been added in the list of content of " +
                    "the section");
            srSection.setResponseCode(ResponseCode.CONTENT_ADDED);
            srSection.setAssociatedObject(sectionSavedWithContent);
        } catch (IllegalArgumentException e) {
            srSection.setResponseCode(ResponseCode.EXCEPTION_ENUM_COURSE_PART_TYPE);
            srSection.setErrorMessage("IllegalArgumentException");
            srSection.setMoreDetails(e.getMessage());
        }

        return srSection;
    }

    public Optional<Content> findContentInContentSectionList(String contentId, Section section){
        Content contentSearch = null;
        for(Content content : section.getListofContent()){
            if(content.getId().equalsIgnoreCase(contentId)){
                contentSearch = content;
                break;
            }
        }
        return Optional.ofNullable(contentSearch);
    }

    @Override
    public ServerResponse<Section> removeContentToSection(String contentId, String sectionId,
                                                          String schoolName, String departmentName,
                                                          String optionName, String levelName,
                                                          String courseTitle, String moduleTitle,
                                                          String chapterTitle, String sectionTitle)
            throws SectionNotFoundException, ContentNotBelongingToException {
        ServerResponse<Section> srSection = new ServerResponse<>();
        contentId = contentId.trim();
        sectionId = sectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();


        Section concernedSection = null;
        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle, chapterTitle,
                sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }
        concernedSection = optionalSection.get();

        Optional<Content> optionalContent = this.findContentInContentSectionList(contentId, concernedSection);

        if(!optionalContent.isPresent()){
            throw new ContentNotBelongingToException("The content id specified does not match any " +
                    "content in the system");
        }
        contentRepository.delete(optionalContent.get());

        srSection.setErrorMessage("A content has been removed in the list of content of " +
                "the section");
        srSection.setResponseCode(ResponseCode.CONTENT_DELETED);
        srSection.setAssociatedObject(concernedSection);

        return srSection;
    }

    @Override
    public ServerResponse<Section> updateContentToSection(String contentId, String value,
                                                          String sectionId, String schoolName,
                                                          String departmentName, String optionName,
                                                          String levelName, String courseTitle,
                                                          String moduleTitle, String chapterTitle,
                                                          String sectionTitle)
            throws SectionNotFoundException, ContentNotBelongingToException {
        ServerResponse<Section> srSection = new ServerResponse<>();
        value = value.trim();
        contentId = contentId.trim();
        sectionId = sectionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();
        courseTitle = courseTitle.toLowerCase().trim();
        moduleTitle = moduleTitle.toLowerCase().trim();
        chapterTitle = chapterTitle.toLowerCase().trim();
        sectionTitle = sectionTitle.toLowerCase().trim();

        Section concernedSection = null;
        Optional<Section> optionalSection = this.findConcernedSection(sectionId, schoolName,
                departmentName, optionName, levelName, courseTitle, moduleTitle,
                chapterTitle, sectionTitle);

        if(!optionalSection.isPresent()){
            throw new SectionNotFoundException("The specified section does not found in the system");
        }
        concernedSection = optionalSection.get();

        Optional<Content> optionalContent = this.findContentInContentSectionList(contentId,
                concernedSection);

        if(!optionalContent.isPresent()){
            throw new ContentNotBelongingToException("The content id specified does not match any " +
                    "content in the system");
        }

        Content content = optionalContent.get();
        content.setValue(value);
        contentRepository.save(content);

        srSection.setErrorMessage("A content has been removed in the list of content of " +
                "the section");
        srSection.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srSection.setAssociatedObject(concernedSection);

        return srSection;
    }

    @Override
    public ServerResponse<Section> deleteSection(String sectionId, String schoolName,
                                                 String departmentName, String optionName,
                                                 String levelName, String courseTitle,
                                                 String moduleTitle, String chapterTitle,
                                                 String sectionTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException, ChapterNotFoundException,
            SectionNotFoundException {
        return null;
    }
}
