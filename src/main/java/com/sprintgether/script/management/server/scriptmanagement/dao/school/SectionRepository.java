package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Chapter;
import com.sprintgether.script.management.server.scriptmanagement.model.school.EnumCoursePartType;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends MongoRepository<Section, String> {
    Optional<Section> findById(String sectionId);

    Optional<Section> findByOwnerChapterAndTitle(Chapter ownerChapter, String sectionTitle);

    Page<Section> findAllByOwnerChapterAndTitleContaining(Chapter ownerChapter, String keyword, Pageable pageable);

    Page<Section> findAllByOwnerChapter(Chapter ownerChapter, Pageable pageable);

    Page<Section> findAllByOwnerChapterAndSectionType(Chapter ownerChapter, EnumCoursePartType sectionType,
                                                      Pageable pageable);

    List<Section> findAllByOwnerChapterAndSectionTypeOrderByTitleAsc(Chapter ownerChapter,
                                                                     EnumCoursePartType sectionType);

    List<Section> findAllByOwnerChapterAndSectionTypeOrderByTitleDesc(Chapter ownerChapter,
                                                                     EnumCoursePartType sectionType);

    List<Section> findAllByOwnerChapterAndSectionTypeOrderBySectionOrderAsc(Chapter ownerChapter,
                                                                     EnumCoursePartType sectionType);

    List<Section> findAllByOwnerChapterAndSectionTypeOrderBySectionOrderDesc(Chapter ownerChapter,
                                                                            EnumCoursePartType sectionType);

    List<Section> findAllByOwnerChapterOrderByTitleAsc(Chapter ownerChapter);

    List<Section> findAllByOwnerChapterOrderByTitleDesc(Chapter ownerChapter);

    List<Section> findAllByOwnerChapterOrderBySectionOrderAsc(Chapter ownerChapter);

    List<Section> findAllByOwnerChapterOrderBySectionOrderDesc(Chapter ownerChapter);

}
