package com.sprintgether.script.management.server.scriptmanagement.dao.script;

import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends MongoRepository<Question, String> {
    Optional<Question> findById(String questionId);

    ////////////// ALL CONCERNING STAFF /////////////////////////////////
    List<Question> findAllByOwnerStaffOrderByTitleAsc(Staff ownerStaff);

    List<Question> findAllByOwnerStaffOrderByTitleDesc(Staff ownerStaff);

    Page<Question> findAllByOwnerStaff(Staff ownerStaff, Pageable pageable);

    List<Question> findAllByOwnerStaffAndQuestionTypeOrderByTitleAsc(Staff ownerStaff,
                                                                     EnumQuestionType enumQuestionType);

    List<Question> findAllByOwnerStaffAndQuestionTypeOrderByTitleDesc(Staff ownerStaff,
                                                                      EnumQuestionType enumQuestionType);

    Page<Question> findAllByOwnerStaffAndQuestionType(Staff ownerStaff,
                                                      EnumQuestionType enumQuestionType,
                                                      Pageable pageable);

    List<Question> findAllByOwnerStaffAndQuestionScopeOrderByTitleAsc(Staff ownerStaff,
                                                                      EnumScope enumScope);

    List<Question> findAllByOwnerStaffAndQuestionScopeOrderByTitleDesc(Staff ownerStaff,
                                                                       EnumScope enumScope);

    Page<Question> findAllByOwnerStaffAndQuestionScope(Staff ownerStaff,
                                                       EnumScope enumScope,
                                                       Pageable pageable);

    List<Question> findAllByOwnerStaffAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, EnumLevelofDifficulty enumLevelofDifficulty);

    List<Question> findAllByOwnerStaffAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, EnumLevelofDifficulty enumLevelofDifficulty);

    Page<Question> findAllByOwnerStaffAndLevelofDifficulty(Staff ownerStaff,
                                                           EnumLevelofDifficulty enumLevelofDifficulty,
                                                           Pageable pageable);

    List<Question> findAllByOwnerStaffAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, EnumQuestionType enumQuestionType, EnumScope enumScope);

    List<Question> findAllByOwnerStaffAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, EnumQuestionType enumQuestionType, EnumScope enumScope);

    Page<Question> findAllByOwnerStaffAndQuestionTypeAndQuestionScope(
            Staff ownerStaff, EnumQuestionType enumQuestionType, EnumScope enumScope,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);

    List<Question> findAllByOwnerStaffAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);

    Page<Question> findAllByOwnerStaffAndQuestionTypeAndLevelofDifficulty(
            Staff ownerStaff, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);

    List<Question> findAllByOwnerStaffAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);

    Page<Question> findAllByOwnerStaffAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);

    List<Question> findAllByOwnerStaffAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);

    Page<Question> findAllByOwnerStaffAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);


    /////////////// ALL CONCERNING STAFF AND COURSE ////////////////////////////////////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse);
    List<Question>  findAllByOwnerStaffAndConcernedCourseOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse);
    Page<Question>  findAllByOwnerStaffAndConcernedCourse(
            Staff ownerStaff, Course concernedCourse, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule);
    List<Question> findAllByOwnerStaffAndConcernedModuleOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule);
    Page<Question> findAllByOwnerStaffAndConcernedModule(
            Staff ownerStaff, Module concernedModule, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter);
    List<Question> findAllByOwnerStaffAndConcernedChapterOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter);
    Page<Question> findAllByOwnerStaffAndConcernedChapter(
            Staff ownerStaff, Chapter concernedChapter, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection);
    List<Question> findAllByOwnerStaffAndConcernedSectionOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection);
    Page<Question> findAllByOwnerStaffAndConcernedSection(
            Staff ownerStaff, Section concernedSection, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection);
    Page<Question> findAllByOwnerStaffAndConcernedSubSection(
            Staff ownerStaff, SubSection concernedSubSection, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph);
    List<Question> findAllByOwnerStaffAndConcernedParagraphOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph);
    Page<Question> findAllByOwnerStaffAndConcernedParagraph(
            Staff ownerStaff, Paragraph concernedParagraph, Pageable pageable);
    ////////////// QuestionType ///////////////////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType);
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType);
    Page<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionType(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType);
    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType);
    Page<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionType(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType);
    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType);
    Page<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionType(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType);
    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType);
    Page<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionType(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType);
    Page<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionType(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType);
    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType);
    Page<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionType(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            Pageable pageable);

    ////////////// QuestionScope  ///////////////////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope);
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope);
    Page<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionScope(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule, EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule, EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionScope(
            Staff ownerStaff, Module concernedModule, EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter, EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter, EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionScope(
            Staff ownerStaff, Chapter concernedChapter, EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection, EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection, EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionScope(
            Staff ownerStaff, Section concernedSection, EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection, EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection, EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionScope(
            Staff ownerStaff, SubSection concernedSubSection, EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionScope(
            Staff ownerStaff, Paragraph concernedParagraph, EnumScope enumScope, Pageable pageable);

    ////////////// QuestionLevelofDifficulty ///////////////////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question>  findAllByOwnerStaffAndConcernedCourseAndLevelofDifficulty(
            Staff ownerStaff, Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedModuleAndLevelofDifficulty(
            Staff ownerStaff, Module concernedModule, EnumLevelofDifficulty enumLevelofDifficulty,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedChapterAndLevelofDifficulty(
            Staff ownerStaff, Chapter concernedChapter, EnumLevelofDifficulty enumLevelofDifficulty,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSectionAndLevelofDifficulty(
            Staff ownerStaff, Section concernedSection, EnumLevelofDifficulty enumLevelofDifficulty,
            Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficulty(
            Staff ownerStaff, SubSection concernedSubSection,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficulty(
            Staff ownerStaff, Paragraph concernedParagraph,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    ////////////// QuestionType and QuestionScope ///////////////////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    Page<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScope(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScope(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScope(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScope(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScope(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumScope enumScope);
    Page<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScope(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumScope enumScope, Pageable pageable);

    ////////////// QuestionType and QuestionLevelofDifficulty ///////////////////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndLevelofDifficulty(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndLevelofDifficulty(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndLevelofDifficulty(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndLevelofDifficulty(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndLevelofDifficulty(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndLevelofDifficulty(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    ////////////// QuestionScope and QuestionLevelofDifficulty ///////////////////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Module concernedModule, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Chapter concernedChapter, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Section concernedSection, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, SubSection concernedSubSection, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Paragraph concernedParagraph, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    ////////////// QuestionType, QuestionScope and QuestionLevelofDifficulty //////////////////
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question>  findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Module concernedModule, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Section concernedSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);

    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    Page<Question> findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
            Staff ownerStaff, Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty, Pageable pageable);




    /////////////// ALL CONCERNING COURSE////////////////////////////////////////////////
    List<Question>  findAllByConcernedCourseOrderByTitleAsc(Course concernedCourse);
    List<Question>  findAllByConcernedCourseOrderByTitleDesc(Course concernedCourse);
    List<Question>  findAllByConcernedModuleOrderByTitleAsc(Module concernedModule);
    List<Question>  findAllByConcernedModuleOrderByTitleDesc(Module concernedModule);
    List<Question>  findAllByConcernedChapterOrderByTitleAsc(Chapter concernedChapter);
    List<Question>  findAllByConcernedChapterOrderByTitleDesc(Chapter concernedChapter);
    List<Question>  findAllByConcernedSectionOrderByTitleAsc(Section concernedSection);
    List<Question>  findAllByConcernedSectionOrderByTitleDesc(Section concernedSection);
    List<Question>  findAllByConcernedSubSectionOrderByTitleAsc(SubSection concernedSubSection);
    List<Question>  findAllByConcernedSubSectionOrderByTitleDesc(SubSection concernedSubSection);
    List<Question>  findAllByConcernedParagraphOrderByTitleAsc(Paragraph concernedParagraph);
    List<Question>  findAllByConcernedParagraphOrderByTitleDesc(Paragraph concernedParagraph);
    ////////////// QuestionType ///////////////////////////////
    List<Question>  findAllByConcernedCourseAndQuestionTypeOrderByTitleAsc(
            Course concernedCourse, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedCourseAndQuestionTypeOrderByTitleDesc(
            Course concernedCourse, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedModuleAndQuestionTypeOrderByTitleAsc(
            Module concernedModule, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedModuleAndQuestionTypeOrderByTitleDesc(
            Module concernedModule, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedChapterAndQuestionTypeOrderByTitleAsc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedChapterAndQuestionTypeOrderByTitleDesc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedSectionAndQuestionTypeOrderByTitleAsc(
            Section concernedSection, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedSectionAndQuestionTypeOrderByTitleDesc(
            Section concernedSection, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeOrderByTitleAsc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeOrderByTitleDesc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeOrderByTitleAsc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeOrderByTitleDesc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType);
    ////////////// QuestionScope  ///////////////////////////////
    List<Question>  findAllByConcernedCourseAndQuestionScopeOrderByTitleAsc(
            Course concernedCourse, EnumScope enumScope);
    List<Question>  findAllByConcernedCourseAndQuestionScopeOrderByTitleDesc(
            Course concernedCourse, EnumScope enumScope);
    List<Question>  findAllByConcernedModuleAndQuestionScopeOrderByTitleAsc(
            Module concernedModule, EnumScope enumScope);
    List<Question>  findAllByConcernedModuleAndQuestionScopeOrderByTitleDesc(
            Module concernedModule, EnumScope enumScope);
    List<Question>  findAllByConcernedChapterAndQuestionScopeOrderByTitleAsc(
            Chapter concernedChapter, EnumScope enumScope);
    List<Question>  findAllByConcernedChapterAndQuestionScopeOrderByTitleDesc(
            Chapter concernedChapter, EnumScope enumScope);
    List<Question>  findAllByConcernedSectionAndQuestionScopeOrderByTitleAsc(
            Section concernedSection, EnumScope enumScope);
    List<Question>  findAllByConcernedSectionAndQuestionScopeOrderByTitleDesc(
            Section concernedSection, EnumScope enumScope);
    List<Question>  findAllByConcernedSubSectionAndQuestionScopeOrderByTitleAsc(
            SubSection concernedSubSection, EnumScope enumScope);
    List<Question>  findAllByConcernedSubSectionAndQuestionScopeOrderByTitleDesc(
            SubSection concernedSubSection, EnumScope enumScope);
    List<Question>  findAllByConcernedParagraphAndQuestionScopeOrderByTitleAsc(
            Paragraph concernedParagraph, EnumScope enumScope);
    List<Question>  findAllByConcernedParagraphAndQuestionScopeOrderByTitleDesc(
            Paragraph concernedParagraph, EnumScope enumScope);
    ////////////// QuestionLevelofDifficulty ///////////////////////////////
    List<Question>  findAllByConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndLevelofDifficultyOrderByTitleAsc(
            Module concernedModule, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndLevelofDifficultyOrderByTitleDesc(
            Module concernedModule, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndLevelofDifficultyOrderByTitleAsc(
            Chapter concernedChapter, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndLevelofDifficultyOrderByTitleDesc(
            Chapter concernedChapter, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndLevelofDifficultyOrderByTitleAsc(
            Section concernedSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndLevelofDifficultyOrderByTitleDesc(
            Section concernedSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(
            SubSection concernedSubSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(
            SubSection concernedSubSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(
            Paragraph concernedParagraph, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(
            Paragraph concernedParagraph, EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// QuestionType and QuestionScope ///////////////////////////////
    List<Question>  findAllByConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Course concernedCourse, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Course concernedCourse, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Module concernedModule, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Module concernedModule, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Section concernedSection, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Section concernedSection, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType, EnumScope enumScope);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType, EnumScope enumScope);
    ////////////// QuestionType and QuestionLevelofDifficulty ///////////////////////////////
    List<Question>  findAllByConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Module concernedModule, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Module concernedModule, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Section concernedSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Section concernedSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// QuestionScope and QuestionLevelofDifficulty ///////////////////////////////
    List<Question>  findAllByConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Module concernedModule, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Module concernedModule, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Chapter concernedChapter, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Chapter concernedChapter, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Section concernedSection, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Section concernedSection, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            SubSection concernedSubSection, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            SubSection concernedSubSection, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Paragraph concernedParagraph, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Paragraph concernedParagraph, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// QuestionType, QuestionScope and QuestionLevelofDifficulty //////////////////
    List<Question>  findAllByConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Module concernedModule, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Module concernedModule, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Chapter concernedChapter, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Section concernedSection, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Section concernedSection, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            SubSection concernedSubSection, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Question>  findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
            Paragraph concernedParagraph, EnumQuestionType enumQuestionType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);

    /////////////// ALL PAGE CONCERNING COURSE ////////////////////////////////////////////////
    Page<Question> findAllByConcernedCourseAndTitleContaining(Course concernedCourse,
                                                              String  keyword,
                                                              Pageable pageable);
    Page<Question>  findAllByConcernedModuleAndTitleContaining(Module concernedModule,
                                                               String  keyword,
                                                               Pageable pageable);
    Page<Question>  findAllByConcernedChapterAndTitleContaining(Chapter concernedChapter,
                                                                String  keyword,
                                                                Pageable pageable);
    Page<Question>  findAllByConcernedSectionAndTitleContaining(Section concernedSection,
                                                                String  keyword,
                                                                Pageable pageable);
    Page<Question>  findAllByConcernedSubSectionAndTitleContaining(SubSection concernedSubSection,
                                                                   String  keyword,
                                                                   Pageable pageable);
    Page<Question>  findAllByConcernedParagraphAndTitleContaining(Paragraph concernedParagraph,
                                                                  String  keyword,
                                                                  Pageable pageable);



}
