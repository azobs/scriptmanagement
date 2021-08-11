package com.sprintgether.script.management.server.scriptmanagement.service.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParagraphService {
    ServerResponse<Paragraph> findParagraphOfSubSectionById(String paragraphId);

    ServerResponse<Paragraph> findParagraphOfSubSectionByTitle(String schoolName,
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
            ChapterNotFoundException, SectionNotFoundException, SubSectionNotFoundException;

    ServerResponse<Page<Paragraph>> findAllParagraphOfSubSection(String subSectionId,
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
            throws SubSectionNotFoundException;

    ServerResponse<Page<Paragraph>> findAllParagraphOfSubSection(String subSectionId,
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
            throws SubSectionNotFoundException;

    ServerResponse<Page<Paragraph>> findAllParagraphOfSubSectionByType(String subSectionId,
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
            throws SubSectionNotFoundException;

    ServerResponse<List<Paragraph>> findAllParagraphOfSubSection(String subSectionId,
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
            throws SubSectionNotFoundException;

    boolean isParagraphofSubSection(String paragraphId, String subSectionId)
            throws SubSectionNotFoundException;

    ServerResponse<List<Paragraph>> findAllParagraphOfSubSectionByType(String subSectionId,
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
            throws SubSectionNotFoundException;

    Paragraph saveParagraph(Paragraph paragraph);

    ServerResponse<Paragraph> saveParagraph(String title, int paragraphOrder,
                                            String paragraphType, String subSectionId,
                                            String subSectionTitle, String sectionTitle,
                                            String chapterTitle, String moduleTitle,
                                            String courseTitle, String levelName,
                                            String optionName, String departmentName,
                                            String schoolName)
            throws SubSectionNotFoundException, DuplicateParagraphInSubSectionException;

    ServerResponse<Paragraph> updateParagraph(String paragraphId, String title,
                                              int paragraphOrder, String paragraphType,
                                              String subSectionTitle, String sectionTitle,
                                              String chapterTitle, String moduleTitle,
                                              String courseTitle, String levelName,
                                              String optionName, String departmentName,
                                              String schoolName)
            throws ParagraphNotFoundException, DuplicateParagraphInSubSectionException;

    ServerResponse<Paragraph> updateParagraphTitle(String paragraphId, String newParagraphTitle)
            throws ParagraphNotFoundException, DuplicateParagraphInSubSectionException;

    ServerResponse<Paragraph> addContentToParagraph(String value, String contentType,
                                                    String paragraphId, String schoolName,
                                                    String departmentName, String optionName,
                                                    String levelName, String courseTitle,
                                                    String moduleTitle, String chapterTitle,
                                                    String sectionTitle, String subSectionTitle,
                                                    String paragraphTitle)
            throws ParagraphNotFoundException;

    ServerResponse<Paragraph> removeContentToParagraph(String contentId, String paragraphId,
                                                       String schoolName, String departmentName,
                                                       String optionName, String levelName,
                                                       String courseTitle, String moduleTitle,
                                                       String chapterTitle, String sectionTitle,
                                                       String subSectionTitle, String paragraphTitle)
            throws ParagraphNotFoundException, ContentNotFoundException;

    ServerResponse<Paragraph> updateContentToParagraph(String contentId, String value,
                                                       String paragraphId, String schoolName,
                                                       String departmentName, String optionName,
                                                       String levelName, String courseTitle,
                                                       String moduleTitle, String chapterTitle,
                                                       String sectionTitle, String subSectionTitle,
                                                       String paragraphTitle)
            throws ParagraphNotFoundException, ContentNotFoundException;

    ServerResponse<Paragraph> deleteParagraph(String paragraphId, String schoolName,
                                              String departmentName, String optionName,
                                              String levelName, String courseTitle,
                                              String moduleTitle, String chapterTitle,
                                              String sectionTitle, String subSectionTitle,
                                              String paragraphTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException,
            ChapterNotFoundException, SectionNotFoundException, SubSectionNotFoundException,
            ParagraphNotFoundException;
}
