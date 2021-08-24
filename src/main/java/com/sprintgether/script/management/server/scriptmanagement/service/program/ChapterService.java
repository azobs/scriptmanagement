package com.sprintgether.script.management.server.scriptmanagement.service.program;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ConcernedPartNotBelongingToCourseException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.model.program.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ChapterService {

    ServerResponse<Chapter> findChapterOfModuleById(String chapterId);

    ServerResponse<Chapter> findChapterOfModuleByTitle(String schoolName,
                                                       String departmentName,
                                                       String optionName,
                                                       String levelName,
                                                       String courseTitle,
                                                       String moduleTitle,
                                                       String chapterTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException;

    ServerResponse<Page<Chapter>> findAllChapterOfModule(String moduleId,
                                                         String schoolName,
                                                         String departmentName,
                                                         String optionName,
                                                         String levelName,
                                                         String courseTitle,
                                                         String moduleTitle,
                                                         Pageable pageable)
            throws ModuleNotFoundException;

    ServerResponse<Page<Chapter>> findAllChapterOfModule(String moduleId,
                                                         String schoolName,
                                                         String departmentName,
                                                         String optionName,
                                                         String levelName,
                                                         String courseTitle,
                                                         String moduleTitle,
                                                         String keyword,
                                                         Pageable pageable)
            throws ModuleNotFoundException;

    ServerResponse<Page<Chapter>> findAllChapterOfModuleByType(String moduleId,
                                                               String schoolName,
                                                               String departmentName,
                                                               String optionName,
                                                               String levelName,
                                                               String courseTitle,
                                                               String moduleTitle,
                                                               String chapterType,
                                                               Pageable pageable)
            throws ModuleNotFoundException;

    ServerResponse<List<Chapter>> findAllChapterOfModule(String moduleId,
                                                         String schoolName,
                                                         String departmentName,
                                                         String optionName,
                                                         String levelName,
                                                         String courseTitle,
                                                         String moduleTitle,
                                                         String sortBy,
                                                         String direction)
            throws ModuleNotFoundException;

    boolean isChapterofModule(String chapterId, String moduleId) throws ModuleNotFoundException;

    ServerResponse<List<Chapter>> findAllChapterOfModuleByType(String moduleId,
                                                               String schoolName,
                                                               String departmentName,
                                                               String optionName,
                                                               String levelName,
                                                               String courseTitle,
                                                               String moduleTitle,
                                                               String chapterType,
                                                               String sortBy,
                                                               String direction)
            throws ModuleNotFoundException;

    Chapter saveChapter(Chapter chapter);

    ServerResponse<Chapter> saveChapter(String title, int chapterOrder, String chapterType,
                                        String moduleId, String moduleTitle, String courseTitle,
                                        String levelName, String optionName, String departmentName,
                                        String schoolName)
            throws ModuleNotFoundException, DuplicateChapterInModuleException;


    ServerResponse<Chapter> updateChapter(String chapterId, String title, int chapterOrder,
                                          String chapterType, String moduleTitle, String courseTitle,
                                          String levelName, String optionName, String departmentName,
                                          String schoolName)
            throws ChapterNotFoundException, DuplicateChapterInModuleException;

    ServerResponse<Chapter> updateChapterTitle(String chapterId, String newChapterTitle)
            throws ChapterNotFoundException, DuplicateChapterInModuleException;

    ServerResponse<Chapter> addContentToChapter(String value, String contentType, String chapterId,
                                                String schoolName, String departmentName,
                                                String optionName, String levelName, String courseTitle,
                                                String moduleTitle, String chapterTitle)
            throws ChapterNotFoundException;

    ServerResponse<Chapter> removeContentToChapter(String contentId, String chapterId,
                                                String schoolName, String departmentName,
                                                String optionName, String levelName, String courseTitle,
                                                String moduleTitle, String chapterTitle)
            throws ChapterNotFoundException, ContentNotBelongingToException;

    ServerResponse<Chapter> updateContentToChapter(String contentId, String value, String chapterId,
                                                String schoolName, String departmentName,
                                                String optionName, String levelName, String courseTitle,
                                                String moduleTitle, String chapterTitle)
            throws ChapterNotFoundException, ContentNotBelongingToException;

    ServerResponse<Chapter> deleteChapter(String chapterId, String schoolName,
                                          String departmentName, String optionName,
                                          String levelName, String courseTitle,
                                          String moduleTitle, String chapterTitle)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException,
            LevelNotFoundException, CourseNotFoundException, ModuleNotFoundException, ChapterNotFoundException;

}
