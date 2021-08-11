package com.sprintgether.script.management.server.scriptmanagement.service.commonused;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.form.program.course.CourseContentDeleted;
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
    public boolean isConcernedPartBelongingToCourse(String concernedPartId, Course course)
            throws CourseNotFoundException, ModuleNotFoundException, ChapterNotFoundException,
            SectionNotFoundException, SubSectionNotFoundException, ParagraphNotFoundException {
        boolean isPartof = false;
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

        return isPartof;
    }
}
