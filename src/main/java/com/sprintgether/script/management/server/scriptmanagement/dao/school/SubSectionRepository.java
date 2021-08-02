package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.EnumCoursePartType;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Section;
import com.sprintgether.script.management.server.scriptmanagement.model.school.SubSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubSectionRepository extends MongoRepository<SubSection, String> {
    Optional<SubSection> findById(String subSectionTitle);

    Optional<SubSection> findByOwnerSectionAndTitle(Section ownerSection, String subSectionTitle);

    Page<SubSection> findAllByOwnerSectionAndTitleContaining(Section ownerSection, String keyword,
                                                             Pageable pageable);

    Page<SubSection> findAllByOwnerSection(Section ownerSection, Pageable pageable);

    Page<SubSection> findAllByOwnerSectionAndSubsectionType(Section ownerSection,
                                                            EnumCoursePartType subSectionType,
                                                            Pageable pageable);

    List<SubSection> findAllByOwnerSectionAndSubSectionTypeOrderByTitleAsc(Section ownerSection,
                                                                           EnumCoursePartType subSectionType);

    List<SubSection> findAllByOwnerSectionAndSubSectionTypeOrderByTitleDesc(Section ownerSection,
                                                                            EnumCoursePartType sectionType);

    List<SubSection> findAllByOwnerSectionAndSubSectionTypeOrderBySubSectionOrderAsc(Section ownerSection,
                                                                           EnumCoursePartType subSectionType);

    List<SubSection> findAllByOwnerSectionAndSubSectionTypeOrderBySubSectionOrderDesc(Section ownerSection,
                                                                                     EnumCoursePartType subSectionType);

    List<SubSection> findAllByOwnerSectionOrderByTitleAsc(Section ownerSection);

    List<SubSection> findAllByOwnerSectionOrderByTitleDesc(Section ownerSection);

    List<SubSection> findAllByOwnerSectionOrderBySubSectionOrderAsc(Section ownerSection);

    List<SubSection> findAllByOwnerSectionOrderBySubSectionOrderDesc(Section ownerSection);
}
