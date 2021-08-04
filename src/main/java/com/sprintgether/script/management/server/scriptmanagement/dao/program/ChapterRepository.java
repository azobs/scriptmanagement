package com.sprintgether.script.management.server.scriptmanagement.dao.program;

import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository  extends MongoRepository<Chapter, String> {
    Optional<Chapter> findById(String chapterId);
    Optional<Chapter> findByOwnerModuleAndTitle(Module ownerModule, String chapterTitle);
    Page<Chapter> findAllByOwnerModuleAndTitleContaining(Module ownerModule, String keyword,
                                                         Pageable pageable);
    Page<Chapter> findAllByOwnerModule(Module module, Pageable pageable);
    Page<Chapter> findAllByOwnerModuleAndChapterType(Module module,
                                                      EnumCoursePartType chapterType,
                                                      Pageable pageable);
    List<Chapter> findAllByOwnerModuleAndChapterTypeOrderByTitleAsc(Module module,
                                                      EnumCoursePartType chapterType);
    List<Chapter> findAllByOwnerModuleAndChapterTypeOrderByTitleDesc(Module module,
                                                                 EnumCoursePartType chapterType);
    List<Chapter> findAllByOwnerModuleAndChapterTypeOrderByChapterOrderAsc(Module module,
                                                                 EnumCoursePartType chapterType);
    List<Chapter> findAllByOwnerModuleAndChapterTypeOrderByChapterOrderDesc(Module module,
                                                                        EnumCoursePartType chapterType);
    List<Chapter> findAllByOwnerModuleOrderByTitleAsc(Module module);
    List<Chapter> findAllByOwnerModuleOrderByTitleDesc(Module module);
    List<Chapter> findAllByOwnerModuleOrderByChapterOrderAsc(Module module);
    List<Chapter> findAllByOwnerModuleOrderByChapterOrderDesc(Module module);
}
