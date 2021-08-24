package com.sprintgether.script.management.server.scriptmanagement.service.commonused;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ConcernedPartNotBelongingToCourseException;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.service.program.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
    CourseService courseService;
    ModuleService moduleService;
    ChapterService chapterService;
    SectionService sectionService;
    SubSectionService subSectionService;
    ParagraphService paragraphService;

    public CommonServiceImpl(CourseService courseService,
                             ModuleService moduleService,
                             ChapterService chapterService,
                             SectionService sectionService,
                             SubSectionService subSectionService,
                             ParagraphService paragraphService) {
        this.courseService = courseService;
        this.moduleService = moduleService;
        this.chapterService = chapterService;
        this.sectionService = sectionService;
        this.subSectionService = subSectionService;
        this.paragraphService = paragraphService;
    }

    @Override
    public Optional<String> concernedPartName(String objectId) {
        ServerResponse<Module> isModule = moduleService.findModuleOfCourseOutlineById(objectId);
        if(isModule.getResponseCode() == ResponseCode.MODULE_FOUND){
            return Optional.ofNullable("Module");
        }
        ServerResponse<Chapter> isChapter = chapterService.findChapterOfModuleById(objectId);
        if(isChapter.getResponseCode() == ResponseCode.CHAPTER_FOUND){
            return Optional.ofNullable("Chapter");
        }
        ServerResponse<Section> isSection = sectionService.findSectionOfChapterById(objectId);
        if(isSection.getResponseCode() == ResponseCode.SECTION_FOUND){
            return Optional.ofNullable("Section");
        }
        ServerResponse<SubSection> isSubSection = subSectionService.
                findSubSectionOfSectionById(objectId);
        if(isSubSection.getResponseCode() == ResponseCode.SUBSECTION_FOUND){
            return Optional.ofNullable("SubSection");
        }
        ServerResponse<Paragraph> isParagraph = paragraphService.
                findParagraphOfSubSectionById(objectId);
        if(isParagraph.getResponseCode() == ResponseCode.PARAGRAPH_FOUND){
            return Optional.ofNullable("Paragraph");
        }
        return Optional.ofNullable("NotAPart");
    }

    @Override
    public boolean isConcernedPartBelongingToCourse(String concernedPartId, Course course) {
        boolean isPartof = false;
        concernedPartId = concernedPartId.trim();
        try{
            if(moduleService.isModuleofCourse(concernedPartId, course.getId())) return true;
            ServerResponse<List<Module>> srListModuleofCourse =
                    moduleService.findAllModuleOfCourseOutline(course.getId(), null,
                            null, null, null, null,
                            "moduleOrder", "ASC" );
            if(srListModuleofCourse.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
                for(Module module: srListModuleofCourse.getAssociatedObject()){
                    if(chapterService.isChapterofModule(concernedPartId, module.getId())) return true;
                    ServerResponse<List<Chapter>> srListChapterofModule =
                            chapterService.findAllChapterOfModule(module.getId(), null,
                                    null, null, null,
                                    null, null, "chapterOrder",
                                    "ASC");

                    if(srListChapterofModule.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
                        for(Chapter chapter: srListChapterofModule.getAssociatedObject()){
                            if(sectionService.isSectionofChapter(concernedPartId, chapter.getId()))
                                return true;
                            ServerResponse<List<Section>> srListSectionofChapter =
                                    sectionService.findAllSectionOfChapter(chapter.getId(),
                                            null, null, null,
                                            null, null, null,
                                            null, "sectionOrder", "ASC");
                            if(srListSectionofChapter.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
                                for(Section section : srListSectionofChapter.getAssociatedObject()){
                                    if(subSectionService.isSubSectionofSection(concernedPartId, section.getId()))
                                        return true;
                                    ServerResponse<List<SubSection>> srListofSubSectionofSection =
                                            subSectionService.findAllSubSectionOfSection(
                                                    section.getId(), null,
                                                    null, null,
                                                    null, null, null,
                                                    null, null,
                                                    "subSectionOrder", "ASC");
                                    if(srListofSubSectionofSection.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
                                        for(SubSection subSection: srListofSubSectionofSection.getAssociatedObject()){
                                            if(paragraphService.isParagraphofSubSection(concernedPartId, subSection.getId()))
                                                return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ChapterNotFoundException e) {
            e.printStackTrace();
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
        } catch (SubSectionNotFoundException e) {
            e.printStackTrace();
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        } catch (SectionNotFoundException e) {
            e.printStackTrace();
        }
        return isPartof;
    }

    @Override
    public Optional<Module> findModuleofCourse(String moduleId, String courseId)
            throws CourseNotFoundException, ConcernedPartNotBelongingToCourseException {
        Module associatedModule = null;
        if(!moduleService.isModuleofCourse(moduleId, courseId)){
            throw new ConcernedPartNotBelongingToCourseException("The module id does not match any " +
                    "module in the course specified");
        }
        ServerResponse<List<Module>> srListofModuleofCourse =
                moduleService.findAllModuleOfCourseOutline(courseId, null,
                        null, null, null, null,
                        "moduleOrder", "ASC");
        if(srListofModuleofCourse.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
            List<Module> listofModuleofCourse = srListofModuleofCourse.getAssociatedObject();
            for(Module module : listofModuleofCourse){
                if(module.getId().equalsIgnoreCase(moduleId)) {
                    associatedModule = module;
                    break;
                }
            }
        }
        return Optional.ofNullable(associatedModule);
    }

    @Override
    public Optional<Chapter> findChapterOfCourse(String chapterId, String courseId)
            throws CourseNotFoundException, ConcernedPartNotBelongingToCourseException {
        chapterId = chapterId.trim();
        courseId = courseId.trim();
        Chapter associatedChapter = null;
        boolean isChapterBelongingTo = false;
        ServerResponse<List<Module>> srListofModuleofCourse =
                moduleService.findAllModuleOfCourseOutline(courseId, null,
                        null, null, null, null,
                        "moduleOrder", "ASC");
        try {
            if (srListofModuleofCourse.getResponseCode() == ResponseCode.NORMAL_RESPONSE) {
                List<Module> listofModuleofCourse = srListofModuleofCourse.getAssociatedObject();
                for (Module module : listofModuleofCourse) {
                    isChapterBelongingTo = chapterService.isChapterofModule(chapterId, module.getId());
                    boolean chapterFound = false;
                    if(isChapterBelongingTo){
                        ServerResponse<List<Chapter>> srListofChapterofModule =
                                chapterService.findAllChapterOfModule(module.getId(),
                                        null, null, null,
                                        null, null, null,
                                        "chapterOrder", "ASC");
                        if(srListofChapterofModule.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
                            List<Chapter> listofChapter = srListofChapterofModule.getAssociatedObject();
                            for(Chapter chapter : listofChapter){
                                if(chapter.getId().equalsIgnoreCase(chapterId)){
                                    associatedChapter = chapter;
                                    chapterFound = true;
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        throw new ConcernedPartNotBelongingToCourseException("The chapter id precised " +
                                "does " +
                                "not match any chapter in the precised course");
                    }
                    if(chapterFound) break;
                }
            }
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(associatedChapter);
    }

    @Override
    public Optional<Section> findSectionOfCourse(String sectionId, String courseId)
            throws CourseNotFoundException, ConcernedPartNotBelongingToCourseException {
        sectionId = sectionId.trim();
        courseId = courseId.trim();
        Section associatedSection = null;
        boolean isSectionBelongingTo = false;

        ServerResponse<List<Module>> srListofModuleofCourse =
                moduleService.findAllModuleOfCourseOutline(courseId, null,
                        null, null, null, null,
                        "moduleOrder", "ASC");

        try{
            if (srListofModuleofCourse.getResponseCode() == ResponseCode.NORMAL_RESPONSE) {
                List<Module> listofModuleofCourse = srListofModuleofCourse.getAssociatedObject();
                for (Module module : listofModuleofCourse) {
                    boolean sectionFound = false;
                    ServerResponse<List<Chapter>> srListofChapterofModule =
                            chapterService.findAllChapterOfModule(module.getId(),
                                    null, null, null,
                                    null, null, null,
                                    "chapterOrder", "ASC");
                    if(srListofChapterofModule.getResponseCode() == ResponseCode.NORMAL_RESPONSE){
                        List<Chapter> listofChapter = srListofChapterofModule.getAssociatedObject();
                        for(Chapter chapter : listofChapter){
                            isSectionBelongingTo = sectionService.isSectionofChapter(sectionId,
                                    chapter.getId());
                            if(isSectionBelongingTo){
                                ServerResponse<List<Section>> srListofSectionofChapter =
                                        sectionService.findAllSectionOfChapter(chapter.getId(),
                                                null, null, null,
                                                null, null, null,
                                                null, "sectionOrder", "ASC");
                                if(srListofSectionofChapter.getResponseCode() ==
                                        ResponseCode.NORMAL_RESPONSE){
                                    List<Section> listofSection =
                                            srListofSectionofChapter.getAssociatedObject();
                                    for(Section section : listofSection){
                                        if(section.getId().equalsIgnoreCase(sectionId)){
                                            sectionFound = true;
                                            associatedSection = section;
                                            break;
                                        }
                                    }
                                }
                            }
                            else{
                                throw new ConcernedPartNotBelongingToCourseException("The section id " +
                                        "precised does " +
                                        "not match any section in the precised course");
                            }
                            if(sectionFound) break;
                        }
                        if(sectionFound) break;
                    }
                }
            }
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        } catch (ChapterNotFoundException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(associatedSection);
    }

    @Override
    public Optional<SubSection> findSubSectionOfCourse(String subSectionId, String courseId)
            throws CourseNotFoundException, ConcernedPartNotBelongingToCourseException {
        subSectionId = subSectionId.trim();
        courseId = courseId.trim();
        SubSection associatedSubSection = null;
        boolean isSubSectionBelongingTo = false;

        ServerResponse<List<Module>> srListofModuleofCourse =
                moduleService.findAllModuleOfCourseOutline(courseId, null,
                        null, null, null, null,
                        "moduleOrder", "ASC");

        try{
            if (srListofModuleofCourse.getResponseCode() == ResponseCode.NORMAL_RESPONSE) {
                List<Module> listofModuleofCourse = srListofModuleofCourse.getAssociatedObject();
                for (Module module : listofModuleofCourse) {
                    boolean subSectionFound = false;
                    ServerResponse<List<Chapter>> srListofChapterofModule =
                            chapterService.findAllChapterOfModule(module.getId(),
                                    null, null, null,
                                    null, null, null,
                                    "chapterOrder", "ASC");
                    if (srListofChapterofModule.getResponseCode() == ResponseCode.NORMAL_RESPONSE) {
                        List<Chapter> listofChapter = srListofChapterofModule.getAssociatedObject();
                        for (Chapter chapter : listofChapter) {
                            ServerResponse<List<Section>> srListofSectionofChapter =
                                    sectionService.findAllSectionOfChapter(chapter.getId(),
                                            null, null, null,
                                            null, null, null,
                                            null, "sectionOrder", "ASC");
                            if(srListofSectionofChapter.getResponseCode() ==
                                    ResponseCode.NORMAL_RESPONSE){
                                List<Section> listofSection =
                                        srListofSectionofChapter.getAssociatedObject();
                                for(Section section : listofSection){
                                    isSubSectionBelongingTo =
                                            subSectionService.isSubSectionofSection(subSectionId,
                                                    section.getId());
                                    if(isSubSectionBelongingTo){
                                        ServerResponse<List<SubSection>> srListofSubSectionofSection =
                                                subSectionService.findAllSubSectionOfSection(
                                                        section.getId(), null,
                                                        null, null,
                                                        null, null, null,
                                                        null, null, "subSectionOrder",
                                                        "ASC");
                                        if(srListofSubSectionofSection.getResponseCode() ==
                                                ResponseCode.NORMAL_RESPONSE){
                                            List<SubSection> listofSubSection =
                                                    srListofSubSectionofSection.getAssociatedObject();
                                            for(SubSection subSection : listofSubSection){
                                                if(subSection.getId().equalsIgnoreCase(subSectionId)){
                                                    subSectionFound = true;
                                                    associatedSubSection = subSection;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else{
                                        throw new ConcernedPartNotBelongingToCourseException("" +
                                                "The subsection id " +
                                                "precised does " +
                                                "not match any subsection in the precised course");
                                    }
                                    if(subSectionFound) break;
                                }
                                if(subSectionFound) break;
                            }
                        }
                        if(subSectionFound) break;
                    }
                }
            }
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        } catch (ChapterNotFoundException e) {
            e.printStackTrace();
        } catch (SectionNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(associatedSubSection);
    }

    @Override
    public Optional<Paragraph> findParagraphOfCourse(String paragraphId, String courseId)
            throws CourseNotFoundException, ConcernedPartNotBelongingToCourseException {
        paragraphId = paragraphId.trim();
        courseId = courseId.trim();
        Paragraph associatedParagraph = null;
        boolean isParagraphBelongingTo = false;

        ServerResponse<List<Module>> srListofModuleofCourse =
                moduleService.findAllModuleOfCourseOutline(courseId, null,
                        null, null, null, null,
                        "moduleOrder", "ASC");

        try {
            if (srListofModuleofCourse.getResponseCode() == ResponseCode.NORMAL_RESPONSE) {
                List<Module> listofModuleofCourse = srListofModuleofCourse.getAssociatedObject();
                for (Module module : listofModuleofCourse) {
                    boolean paragraphFound = false;
                    ServerResponse<List<Chapter>> srListofChapterofModule =
                            chapterService.findAllChapterOfModule(module.getId(),
                                    null, null, null,
                                    null, null, null,
                                    "chapterOrder", "ASC");
                    if (srListofChapterofModule.getResponseCode() == ResponseCode.NORMAL_RESPONSE) {
                        List<Chapter> listofChapter = srListofChapterofModule.getAssociatedObject();
                        for (Chapter chapter : listofChapter) {
                            ServerResponse<List<Section>> srListofSectionofChapter =
                                    sectionService.findAllSectionOfChapter(chapter.getId(),
                                            null, null, null,
                                            null, null, null,
                                            null, "sectionOrder", "ASC");
                            if (srListofSectionofChapter.getResponseCode() ==
                                    ResponseCode.NORMAL_RESPONSE) {
                                List<Section> listofSection =
                                        srListofSectionofChapter.getAssociatedObject();
                                for (Section section : listofSection) {
                                    ServerResponse<List<SubSection>> srListofSubSectionofSection =
                                            subSectionService.findAllSubSectionOfSection(
                                                    section.getId(), null,
                                                    null, null,
                                                    null, null, null,
                                                    null, null, "subSectionOrder",
                                                    "ASC");
                                    if (srListofSubSectionofSection.getResponseCode() ==
                                            ResponseCode.NORMAL_RESPONSE) {
                                        List<SubSection> listofSubSection =
                                                srListofSubSectionofSection.getAssociatedObject();
                                        for(SubSection subSection : listofSubSection) {
                                            isParagraphBelongingTo =
                                                    paragraphService.isParagraphofSubSection(
                                                            paragraphId, subSection.getId());
                                            if(isParagraphBelongingTo){
                                                ServerResponse<List<Paragraph>> srListofParagraphofSubSection =
                                                        paragraphService.findAllParagraphOfSubSection(
                                                                subSection.getId(), null,
                                                                null, null,
                                                                null, null,
                                                                null, null,
                                                                null, null,
                                                                "paragraphOrder", "ASC");
                                                if (srListofParagraphofSubSection.getResponseCode() ==
                                                        ResponseCode.NORMAL_RESPONSE) {
                                                    List<Paragraph> listofParagraph =
                                                            srListofParagraphofSubSection.
                                                                    getAssociatedObject();
                                                    for (Paragraph paragraph : listofParagraph) {
                                                        if (paragraph.getId().
                                                                equalsIgnoreCase(paragraphId)) {
                                                            associatedParagraph = paragraph;
                                                            paragraphFound = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            else{
                                                throw new ConcernedPartNotBelongingToCourseException("" +
                                                        "The paragraph id " +
                                                        "precised does " +
                                                        "not match any paragraph in the precised course");
                                            }
                                        }
                                        if(paragraphFound) break;;
                                    }
                                }
                                if(paragraphFound) break;
                            }
                        }
                        if(paragraphFound) break;
                    }
                }
            }
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        } catch (ChapterNotFoundException e) {
            e.printStackTrace();
        } catch (SectionNotFoundException e) {
            e.printStackTrace();
        } catch (SubSectionNotFoundException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(associatedParagraph);
    }
}
