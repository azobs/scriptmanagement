package com.sprintgether.script.management.server.scriptmanagement.dao.program;

import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ParagraphRepository extends MongoRepository<Paragraph, String> {
    Optional<Paragraph> findById(String paragraphTitle);

    Optional<Paragraph> findByOwnerSubSectionAndTitle(SubSection ownerSubSection,
                                                      String paragraphTitle);

    Page<Paragraph> findAllByOwnerSubSectionAndTitleContaining(SubSection ownerSubSection, String keyword,
                                                             Pageable pageable);

    Page<Paragraph> findAllByOwnerSubSection(SubSection ownerSubSection, Pageable pageable);

    Page<Paragraph> findAllByOwnerSubSectionAndParagraphType(SubSection ownerSubSection,
                                                            EnumCoursePartType paragraphType,
                                                            Pageable pageable);

    List<Paragraph> findAllByOwnerSubSectionAndParagraphTypeOrderByTitleAsc(SubSection ownerSubSection,
                                                                           EnumCoursePartType paragraphType);

    List<Paragraph> findAllByOwnerSubSectionAndParagraphTypeOrderByTitleDesc(SubSection ownerSubSection,
                                                                            EnumCoursePartType paragraphType);

    List<Paragraph> findAllByOwnerSubSectionAndParagraphTypeOrderByParagraphOrderAsc(SubSection ownerSubSection,
                                                                                     EnumCoursePartType paragraphType);

    List<Paragraph> findAllByOwnerSubSectionAndParagraphTypeOrderByParagraphOrderDesc(SubSection ownerSection,
                                                                                      EnumCoursePartType paragraphType);

    List<Paragraph> findAllByOwnerSubSectionOrderByTitleAsc(SubSection ownerSubSection);

    List<Paragraph> findAllByOwnerSubSectionOrderByTitleDesc(SubSection ownerSubSection);

    List<Paragraph> findAllByOwnerSubSectionOrderByParagraphOrderAsc(SubSection ownerSubSection);

    List<Paragraph> findAllByOwnerSubSectionOrderByParagraphOrderDesc(SubSection ownerSubSection);
}
