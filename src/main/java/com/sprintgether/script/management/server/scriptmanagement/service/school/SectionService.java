package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SectionService {
    ServerResponse<Section> findSectionOfChapterById(String sectionId);

    ServerResponse<Section> findSectionOfChapterByTitle(String schoolName,
                                                        String departmentName,
                                                        String optionName,
                                                        String levelName,
                                                        String courseTitle,
                                                        String moduleTitle,
                                                        String chapterTitle,
                                                        String sectionTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException,
            ChapterNotFoundException;

    ServerResponse<Page<Section>> findAllSectionOfChapter(String chapterId,
                                                         String schoolName,
                                                         String departmentName,
                                                         String optionName,
                                                         String levelName,
                                                         String courseTitle,
                                                         String moduleTitle,
                                                         String chapterTitle,
                                                         Pageable pageable)
            throws ChapterNotFoundException;

    ServerResponse<Page<Section>> findAllSectionOfChapter(String chapterId,
                                                         String schoolName,
                                                         String departmentName,
                                                         String optionName,
                                                         String levelName,
                                                         String courseTitle,
                                                         String moduleTitle,
                                                         String chapterTitle,
                                                         String keyword,
                                                         Pageable pageable)
            throws ChapterNotFoundException;

    ServerResponse<Page<Section>> findAllSectionOfChapterByType(String chapterId,
                                                               String schoolName,
                                                               String departmentName,
                                                               String optionName,
                                                               String levelName,
                                                               String courseTitle,
                                                               String moduleTitle,
                                                               String chapterTitle,
                                                               String sectionType,
                                                               Pageable pageable)
            throws ChapterNotFoundException;

    ServerResponse<List<Section>> findAllSectionOfChapter(String chapterId,
                                                         String schoolName,
                                                         String departmentName,
                                                         String optionName,
                                                         String levelName,
                                                         String courseTitle,
                                                         String moduleTitle,
                                                         String chapterTitle,
                                                         String sortBy,
                                                         String direction)
            throws ChapterNotFoundException;

    ServerResponse<List<Section>> findAllSectionOfChapterByType(String chapterId,
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
            throws ChapterNotFoundException;

    Section saveSection(Section section);

    ServerResponse<Section> saveSection(String title, int sectionOrder, String sectionType,
                                        String chapterId, String chapterTitle, String moduleTitle,
                                        String courseTitle, String levelName, String optionName,
                                        String departmentName, String schoolName)
            throws ChapterNotFoundException, DuplicateSectionInChapterException;

    ServerResponse<Section> updateSection(String sectionId, String title, int sectionOrder,
                                          String sectionType, String chapterTitle, String moduleTitle,
                                          String courseTitle, String levelName, String optionName,
                                          String departmentName, String schoolName)
            throws SectionNotFoundException, DuplicateSectionInChapterException;

    ServerResponse<Section> updateSectionTitle(String sectionId, String newSectionTitle)
            throws SectionNotFoundException, DuplicateSectionInChapterException;

    ServerResponse<Section> addContentToSection(String value, String contentType, String sectionId,
                                                String schoolName, String departmentName,
                                                String optionName, String levelName, String courseTitle,
                                                String moduleTitle, String chapterTitle, String sectionTitle)
            throws SectionNotFoundException;

    ServerResponse<Section> removeContentToSection(String contentId, String sectionId,
                                                   String schoolName, String departmentName,
                                                   String optionName, String levelName,
                                                   String courseTitle, String moduleTitle,
                                                   String chapterTitle, String sectionTitle)
            throws SectionNotFoundException, ContentNotFoundException;

    ServerResponse<Section> updateContentToSection(String contentId, String value, String sectionId,
                                                   String schoolName, String departmentName,
                                                   String optionName, String levelName,
                                                   String courseTitle, String moduleTitle,
                                                   String chapterTitle, String sectionTitle)
            throws SectionNotFoundException, ContentNotFoundException;

    ServerResponse<Section> deleteSection(String sectionId, String schoolName,
                                          String departmentName, String optionName,
                                          String levelName, String courseTitle,
                                          String moduleTitle, String chapterTitle, String sectionTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException,
            ChapterNotFoundException, SectionNotFoundException;



}
