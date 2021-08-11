package com.sprintgether.script.management.server.scriptmanagement.service.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubSectionService {
    ServerResponse<SubSection> findSubSectionOfSectionById(String subSectionId);

    ServerResponse<SubSection> findSubSectionOfSectionByTitle(String schoolName,
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
            ChapterNotFoundException, SectionNotFoundException;

    ServerResponse<Page<SubSection>> findAllSubSectionOfSection(String sectionId,
                                                                String schoolName,
                                                                String departmentName,
                                                                String optionName,
                                                                String levelName,
                                                                String courseTitle,
                                                                String moduleTitle,
                                                                String chapterTitle,
                                                                String sectionTitle,
                                                                Pageable pageable)
            throws SectionNotFoundException;

    ServerResponse<Page<SubSection>> findAllSubSectionOfSection(String sectionId,
                                                                String schoolName,
                                                                String departmentName,
                                                                String optionName,
                                                                String levelName,
                                                                String courseTitle,
                                                                String moduleTitle,
                                                                String chapterTitle,
                                                                String sectionTitle,
                                                                String keyword,
                                                                Pageable pageable)
            throws SectionNotFoundException;

    ServerResponse<Page<SubSection>> findAllSubSectionOfSectionByType(String sectionId,
                                                                      String schoolName,
                                                                      String departmentName,
                                                                      String optionName,
                                                                      String levelName,
                                                                      String courseTitle,
                                                                      String moduleTitle,
                                                                      String chapterTitle,
                                                                      String sectionTitle,
                                                                      String subSectionType,
                                                                      Pageable pageable)
            throws SectionNotFoundException;

    ServerResponse<List<SubSection>> findAllSubSectionOfSection(String sectionId,
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
            throws SectionNotFoundException;

    boolean isSubSectionofSection(String subSectionId, String sectionId)
            throws SectionNotFoundException;

    ServerResponse<List<SubSection>> findAllSubSectionOfSectionByType(String sectionId,
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
            throws SectionNotFoundException;

    SubSection saveSubSection(SubSection subSection);

    ServerResponse<SubSection> saveSubSection(String title, int subSectionOrder, String subSectionType,
                                              String sectionId, String sectionTitle,
                                              String chapterTitle, String moduleTitle,
                                              String courseTitle, String levelName,
                                              String optionName, String departmentName,
                                              String schoolName)
            throws SectionNotFoundException, DuplicateSubSectionInSectionException;

    ServerResponse<SubSection> updateSubSection(String subSectionId, String title, int subSectionOrder,
                                                String subSectionType, String sectionTitle,
                                                String chapterTitle, String moduleTitle,
                                                String courseTitle, String levelName,
                                                String optionName, String departmentName,
                                                String schoolName)
            throws SubSectionNotFoundException, DuplicateSubSectionInSectionException;

    ServerResponse<SubSection> updateSubSectionTitle(String subSectionId, String newSubSectionTitle)
            throws SubSectionNotFoundException, DuplicateSubSectionInSectionException;

    ServerResponse<SubSection> addContentToSubSection(String value, String contentType, String subSectionId,
                                                      String schoolName, String departmentName,
                                                      String optionName, String levelName, String courseTitle,
                                                      String moduleTitle, String chapterTitle, String sectionTitle,
                                                      String subSectionTitle)
            throws SubSectionNotFoundException;

    ServerResponse<SubSection> removeContentToSubSection(String contentId, String subSectionId,
                                                         String schoolName, String departmentName,
                                                         String optionName, String levelName,
                                                         String courseTitle, String moduleTitle,
                                                         String chapterTitle, String sectionTitle,
                                                         String subSectionTitle)
            throws SubSectionNotFoundException, ContentNotFoundException;

    ServerResponse<SubSection> updateContentToSubSection(String contentId, String value,
                                                         String subSectionId, String schoolName,
                                                         String departmentName, String optionName,
                                                         String levelName, String courseTitle,
                                                         String moduleTitle, String chapterTitle,
                                                         String sectionTitle, String subSectionTitle)
            throws SubSectionNotFoundException, ContentNotFoundException;

    ServerResponse<SubSection> deleteSubSection(String subSectionId, String schoolName,
                                                String departmentName, String optionName,
                                                String levelName, String courseTitle,
                                                String moduleTitle, String chapterTitle,
                                                String sectionTitle, String subSectionTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException,
            ChapterNotFoundException, SectionNotFoundException, SubSectionNotFoundException;

}
