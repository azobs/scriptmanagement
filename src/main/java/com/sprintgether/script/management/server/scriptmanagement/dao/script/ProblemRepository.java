package com.sprintgether.script.management.server.scriptmanagement.dao.script;

import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends MongoRepository<Problem, String> {
    Optional<Problem> findById(String problemId);

    List<Problem> findAllByOwnerStaffOrderByTitleAsc(Staff ownerStaff);

    List<Problem> findAllByOwnerStaffOrderByTitleDesc(Staff ownerStaff);

    List<Problem> findAllByOwnerStaffAndProblemTypeOrderByTitleAsc(Staff ownerStaff,
                                                                     EnumProblemType enumProblemType);

    List<Problem> findAllByOwnerStaffAndProblemTypeOrderByTitleDesc(Staff ownerStaff,
                                                                      EnumProblemType enumProblemType);

    List<Problem> findAllByOwnerStaffAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                      EnumScope enumScope);

    List<Problem> findAllByOwnerStaffAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                       EnumScope enumScope);

    List<Problem> findAllByOwnerStaffAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                          EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                           EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndProblemTypeAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                     EnumProblemType enumProblemType, EnumScope enumScope);

    List<Problem> findAllByOwnerStaffAndProblemTypeAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                      EnumProblemType enumProblemType, EnumScope enumScope);

    List<Problem> findAllByOwnerStaffAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                         EnumProblemType enumProblemType, EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                          EnumProblemType enumProblemType, EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                          EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                           EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                         EnumProblemType enumProblemType, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                          EnumProblemType enumProblemType, EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);


    /////////////// ALL ////////////////////////////////////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseOrderByTitleAsc(Staff ownerStaff,
                                                                         Course concernedCourse);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseOrderByTitleDesc(Staff ownerStaff,
                                                                          Course concernedCourse);
    List<Problem> findAllByOwnerStaffAndConcernedModuleOrderByTitleAsc(Staff ownerStaff,
                                                                        Module concernedModule);
    List<Problem> findAllByOwnerStaffAndConcernedModuleOrderByTitleDesc(Staff ownerStaff,
                                                                         Module concernedModule);
    List<Problem> findAllByOwnerStaffAndConcernedChapterOrderByTitleAsc(Staff ownerStaff,
                                                                         Chapter concernedChapter);
    List<Problem> findAllByOwnerStaffAndConcernedChapterOrderByTitleDesc(Staff ownerStaff,
                                                                          Chapter concernedChapter);
    List<Problem> findAllByOwnerStaffAndConcernedSectionOrderByTitleAsc(Staff ownerStaff,
                                                                         Section concernedSection);
    List<Problem> findAllByOwnerStaffAndConcernedSectionOrderByTitleDesc(Staff ownerStaff,
                                                                          Section concernedSection);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionOrderByTitleAsc(Staff ownerStaff,
                                                                            SubSection concernedSubSection);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionOrderByTitleDesc(Staff ownerStaff,
                                                                             SubSection concernedSubSection);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphOrderByTitleAsc(Staff ownerStaff,
                                                                           Paragraph concernedParagraph);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphOrderByTitleDesc(Staff ownerStaff,
                                                                            Paragraph concernedParagraph);
    ////////////// ProblemType ///////////////////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeOrderByTitleAsc(Staff ownerStaff,
                                                                                        Course concernedCourse,
                                                                                        EnumProblemType enumProblemType);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeOrderByTitleDesc(Staff ownerStaff,
                                                                                         Course concernedCourse, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeOrderByTitleAsc(Staff ownerStaff,
                                                                                       Module concernedModule, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeOrderByTitleDesc(Staff ownerStaff,
                                                                                        Module concernedModule, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeOrderByTitleAsc(Staff ownerStaff,
                                                                                        Chapter concernedChapter, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeOrderByTitleDesc(Staff ownerStaff,
                                                                                         Chapter concernedChapter, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeOrderByTitleAsc(Staff ownerStaff,
                                                                                        Section concernedSection, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeOrderByTitleDesc(Staff ownerStaff,
                                                                                         Section concernedSection, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeOrderByTitleAsc(Staff ownerStaff,
                                                                                           SubSection concernedSubSection, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeOrderByTitleDesc(Staff ownerStaff,
                                                                                            SubSection concernedSubSection, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeOrderByTitleAsc(Staff ownerStaff,
                                                                                          Paragraph concernedParagraph, EnumProblemType enumProblemType);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeOrderByTitleDesc(Staff ownerStaff,
                                                                                           Paragraph concernedParagraph, EnumProblemType enumProblemType);
    ////////////// ProblemScope  ///////////////////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                         Course concernedCourse,
                                                                                         EnumScope enumScope);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                          Course concernedCourse, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                        Module concernedModule, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                         Module concernedModule, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                         Chapter concernedChapter, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                          Chapter concernedChapter, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                         Section concernedSection, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                          Section concernedSection, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                            SubSection concernedSubSection, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                             SubSection concernedSubSection, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                           Paragraph concernedParagraph, EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                            Paragraph concernedParagraph, EnumScope enumScope);
    ////////////// ProblemLevelofDifficulty ///////////////////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                             Course concernedCourse,
                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                              Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                            Module concernedModule, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                             Module concernedModule, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                             Chapter concernedChapter, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                              Chapter concernedChapter, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                             Section concernedSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                              Section concernedSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                SubSection concernedSubSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                 SubSection concernedSubSection, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                               Paragraph concernedParagraph, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                Paragraph concernedParagraph, EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// ProblemType and ProblemScope ///////////////////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                                        Course concernedCourse,
                                                                                                        EnumProblemType enumProblemType,
                                                                                                        EnumScope enumScope);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                                         Course concernedCourse,
                                                                                                         EnumProblemType enumProblemType,
                                                                                                         EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                                       Module concernedModule,
                                                                                                       EnumProblemType enumProblemType,
                                                                                                       EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                                        Module concernedModule,
                                                                                                        EnumProblemType enumProblemType,
                                                                                                        EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                                        Chapter concernedChapter,
                                                                                                        EnumProblemType enumProblemType,
                                                                                                        EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                                         Chapter concernedChapter,
                                                                                                         EnumProblemType enumProblemType,
                                                                                                         EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                                        Section concernedSection,
                                                                                                        EnumProblemType enumProblemType,
                                                                                                        EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                                         Section concernedSection,
                                                                                                         EnumProblemType enumProblemType,
                                                                                                         EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                                           SubSection concernedSubSection,
                                                                                                           EnumProblemType enumProblemType,
                                                                                                           EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                                            SubSection concernedSubSection,
                                                                                                            EnumProblemType enumProblemType,
                                                                                                            EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleAsc(Staff ownerStaff,
                                                                                                          Paragraph concernedParagraph,
                                                                                                          EnumProblemType enumProblemType,
                                                                                                          EnumScope enumScope);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleDesc(Staff ownerStaff,
                                                                                                           Paragraph concernedParagraph,
                                                                                                           EnumProblemType enumProblemType,
                                                                                                           EnumScope enumScope);
    ////////////// ProblemType and ProblemLevelofDifficulty ///////////////////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                            Course concernedCourse,
                                                                                                            EnumProblemType enumProblemType,
                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                             Course concernedCourse,
                                                                                                             EnumProblemType enumProblemType,
                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                           Module concernedModule,
                                                                                                           EnumProblemType enumProblemType,
                                                                                                           EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                            Module concernedModule,
                                                                                                            EnumProblemType enumProblemType,
                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                            Chapter concernedChapter,
                                                                                                            EnumProblemType enumProblemType,
                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                             Chapter concernedChapter,
                                                                                                             EnumProblemType enumProblemType,
                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                            Section concernedSection,
                                                                                                            EnumProblemType enumProblemType,
                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                             Section concernedSection,
                                                                                                             EnumProblemType enumProblemType,
                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                               SubSection concernedSubSection,
                                                                                                               EnumProblemType enumProblemType,
                                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                SubSection concernedSubSection,
                                                                                                                EnumProblemType enumProblemType,
                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                              Paragraph concernedParagraph,
                                                                                                              EnumProblemType enumProblemType,
                                                                                                              EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                               Paragraph concernedParagraph,
                                                                                                               EnumProblemType enumProblemType,
                                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// ProblemScope and ProblemLevelofDifficulty ///////////////////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                             Course concernedCourse,
                                                                                                             EnumScope enumScope,
                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                              Course concernedCourse,
                                                                                                              EnumScope enumScope,
                                                                                                              EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                            Module concernedModule,
                                                                                                            EnumScope enumScope,
                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                             Module concernedModule,
                                                                                                             EnumScope enumScope,
                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                             Chapter concernedChapter,
                                                                                                             EnumScope enumScope,
                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                              Chapter concernedChapter,
                                                                                                              EnumScope enumScope,
                                                                                                              EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                             Section concernedSection,
                                                                                                             EnumScope enumScope,
                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                              Section concernedSection,
                                                                                                              EnumScope enumScope,
                                                                                                              EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                                SubSection concernedSubSection,
                                                                                                                EnumScope enumScope,
                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                 SubSection concernedSubSection,
                                                                                                                 EnumScope enumScope,
                                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                               Paragraph concernedParagraph,
                                                                                                               EnumScope enumScope,
                                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                Paragraph concernedParagraph,
                                                                                                                EnumScope enumScope,
                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);

    ////////////// ProblemType, ProblemScope and ProblemLevelofDifficulty //////////////////
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                                            Course concernedCourse,
                                                                                                                            EnumProblemType enumProblemType,
                                                                                                                            EnumScope enumScope,
                                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                             Course concernedCourse,
                                                                                                                             EnumProblemType enumProblemType,
                                                                                                                             EnumScope enumScope,
                                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);

    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                                           Module concernedModule,
                                                                                                                           EnumProblemType enumProblemType,
                                                                                                                           EnumScope enumScope,
                                                                                                                           EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                            Module concernedModule,
                                                                                                                            EnumProblemType enumProblemType,
                                                                                                                            EnumScope enumScope,
                                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                                            Chapter concernedChapter,
                                                                                                                            EnumProblemType enumProblemType,
                                                                                                                            EnumScope enumScope,
                                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                             Chapter concernedChapter,
                                                                                                                             EnumProblemType enumProblemType,
                                                                                                                             EnumScope enumScope,
                                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                                            Section concernedSection,
                                                                                                                            EnumProblemType enumProblemType,
                                                                                                                            EnumScope enumScope,
                                                                                                                            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                             Section concernedSection,
                                                                                                                             EnumProblemType enumProblemType,
                                                                                                                             EnumScope enumScope,
                                                                                                                             EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                                               SubSection concernedSubSection,
                                                                                                                               EnumProblemType enumProblemType,
                                                                                                                               EnumScope enumScope,
                                                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                                SubSection concernedSubSection,
                                                                                                                                EnumProblemType enumProblemType,
                                                                                                                                EnumScope enumScope,
                                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                                                              Paragraph concernedParagraph,
                                                                                                                              EnumProblemType enumProblemType,
                                                                                                                              EnumScope enumScope,
                                                                                                                              EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem> findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                                                               Paragraph concernedParagraph,
                                                                                                                               EnumProblemType enumProblemType,
                                                                                                                               EnumScope enumScope,
                                                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);




    /////////////// ALL ////////////////////////////////////////////////
    List<Problem>  findAllByConcernedCourseOrderByTitleAsc(Course concernedCourse);
    List<Problem>  findAllByConcernedCourseOrderByTitleDesc(Course concernedCourse);
    List<Problem>  findAllByConcernedModuleOrderByTitleAsc(Module concernedModule);
    List<Problem>  findAllByConcernedModuleOrderByTitleDesc(Module concernedModule);
    List<Problem>  findAllByConcernedChapterOrderByTitleAsc(Chapter concernedChapter);
    List<Problem>  findAllByConcernedChapterOrderByTitleDesc(Chapter concernedChapter);
    List<Problem>  findAllByConcernedSectionOrderByTitleAsc(Section concernedSection);
    List<Problem>  findAllByConcernedSectionOrderByTitleDesc(Section concernedSection);
    List<Problem>  findAllByConcernedSubSectionOrderByTitleAsc(SubSection concernedSubSection);
    List<Problem>  findAllByConcernedSubSectionOrderByTitleDesc(SubSection concernedSubSection);
    List<Problem>  findAllByConcernedParagraphOrderByTitleAsc(Paragraph concernedParagraph);
    List<Problem>  findAllByConcernedParagraphOrderByTitleDesc(Paragraph concernedParagraph);
    ////////////// ProblemType ///////////////////////////////
    List<Problem>  findAllByConcernedCourseAndProblemTypeOrderByTitleAsc(Course concernedCourse,
                                                                           EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedCourseAndProblemTypeOrderByTitleDesc(Course concernedCourse,
                                                                            EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedModuleAndProblemTypeOrderByTitleAsc(Module concernedModule,
                                                                           EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedModuleAndProblemTypeOrderByTitleDesc(Module concernedModule,
                                                                            EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedChapterAndProblemTypeOrderByTitleAsc(Chapter concernedChapter,
                                                                            EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedChapterAndProblemTypeOrderByTitleDesc(Chapter concernedChapter,
                                                                             EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedSectionAndProblemTypeOrderByTitleAsc(Section concernedSection,
                                                                            EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedSectionAndProblemTypeOrderByTitleDesc(Section concernedSection,
                                                                             EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeOrderByTitleAsc(SubSection concernedSubSection,
                                                                               EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeOrderByTitleDesc(SubSection concernedSubSection,
                                                                                EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeOrderByTitleAsc(Paragraph concernedParagraph,
                                                                              EnumProblemType enumProblemType);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeOrderByTitleDesc(Paragraph concernedParagraph,
                                                                               EnumProblemType enumProblemType);
    ////////////// ProblemScope  ///////////////////////////////
    List<Problem>  findAllByConcernedCourseAndProblemScopeOrderByTitleAsc(Course concernedCourse,
                                                                            EnumScope enumScope);
    List<Problem>  findAllByConcernedCourseAndProblemScopeOrderByTitleDesc(Course concernedCourse,
                                                                             EnumScope enumScope);
    List<Problem>  findAllByConcernedModuleAndProblemScopeOrderByTitleAsc(Module concernedModule,
                                                                            EnumScope enumScope);
    List<Problem>  findAllByConcernedModuleAndProblemScopeOrderByTitleDesc(Module concernedModule,
                                                                             EnumScope enumScope);
    List<Problem>  findAllByConcernedChapterAndProblemScopeOrderByTitleAsc(Chapter concernedChapter,
                                                                             EnumScope enumScope);
    List<Problem>  findAllByConcernedChapterAndProblemScopeOrderByTitleDesc(Chapter concernedChapter,
                                                                              EnumScope enumScope);
    List<Problem>  findAllByConcernedSectionAndProblemScopeOrderByTitleAsc(Section concernedSection,
                                                                             EnumScope enumScope);
    List<Problem>  findAllByConcernedSectionAndProblemScopeOrderByTitleDesc(Section concernedSection,
                                                                              EnumScope enumScope);
    List<Problem>  findAllByConcernedSubSectionAndProblemScopeOrderByTitleAsc(SubSection concernedSubSection,
                                                                                EnumScope enumScope);
    List<Problem>  findAllByConcernedSubSectionAndProblemScopeOrderByTitleDesc(SubSection concernedSubSection,
                                                                                 EnumScope enumScope);
    List<Problem>  findAllByConcernedParagraphAndProblemScopeOrderByTitleAsc(Paragraph concernedParagraph,
                                                                               EnumScope enumScope);
    List<Problem>  findAllByConcernedParagraphAndProblemScopeOrderByTitleDesc(Paragraph concernedParagraph,
                                                                                EnumScope enumScope);
    ////////////// ProblemLevelofDifficulty ///////////////////////////////
    List<Problem>  findAllByConcernedCourseAndLevelofDifficultyOrderByTitleAsc(Course concernedCourse,
                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedCourseAndLevelofDifficultyOrderByTitleDesc(Course concernedCourse,
                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndLevelofDifficultyOrderByTitleAsc(Module concernedModule,
                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndLevelofDifficultyOrderByTitleDesc(Module concernedModule,
                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndLevelofDifficultyOrderByTitleAsc(Chapter concernedChapter,
                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndLevelofDifficultyOrderByTitleDesc(Chapter concernedChapter,
                                                                                  EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndLevelofDifficultyOrderByTitleAsc(Section concernedSection,
                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndLevelofDifficultyOrderByTitleDesc(Section concernedSection,
                                                                                  EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(SubSection concernedSubSection,
                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(SubSection concernedSubSection,
                                                                                     EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(Paragraph concernedParagraph,
                                                                                   EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(Paragraph concernedParagraph,
                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// ProblemType and ProblemScope ///////////////////////////////
    List<Problem>  findAllByConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleAsc(Course concernedCourse,
                                                                                           EnumProblemType enumProblemType,
                                                                                           EnumScope enumScope);
    List<Problem>  findAllByConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleDesc(Course concernedCourse,
                                                                                            EnumProblemType enumProblemType,
                                                                                            EnumScope enumScope);
    List<Problem>  findAllByConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleAsc(Module concernedModule,
                                                                                           EnumProblemType enumProblemType,
                                                                                           EnumScope enumScope);
    List<Problem>  findAllByConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleDesc(Module concernedModule,
                                                                                            EnumProblemType enumProblemType,
                                                                                            EnumScope enumScope);
    List<Problem>  findAllByConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleAsc(Chapter concernedChapter,
                                                                                            EnumProblemType enumProblemType,
                                                                                            EnumScope enumScope);
    List<Problem>  findAllByConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleDesc(Chapter concernedChapter,
                                                                                             EnumProblemType enumProblemType,
                                                                                             EnumScope enumScope);
    List<Problem>  findAllByConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(Section concernedSection,
                                                                                            EnumProblemType enumProblemType,
                                                                                            EnumScope enumScope);
    List<Problem>  findAllByConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(Section concernedSection,
                                                                                             EnumProblemType enumProblemType,
                                                                                             EnumScope enumScope);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(SubSection concernedSubSection,
                                                                                               EnumProblemType enumProblemType,
                                                                                               EnumScope enumScope);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(SubSection concernedSubSection,
                                                                                                EnumProblemType enumProblemType,
                                                                                                EnumScope enumScope);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleAsc(Paragraph concernedParagraph,
                                                                                              EnumProblemType enumProblemType,
                                                                                              EnumScope enumScope);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleDesc(Paragraph concernedParagraph,
                                                                                               EnumProblemType enumProblemType,
                                                                                               EnumScope enumScope);
    ////////////// ProblemType and ProblemLevelofDifficulty ///////////////////////////////
    List<Problem>  findAllByConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Course concernedCourse,
                                                                                               EnumProblemType enumProblemType,
                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Course concernedCourse,
                                                                                                EnumProblemType enumProblemType,
                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Module concernedModule,
                                                                                               EnumProblemType enumProblemType,
                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Module concernedModule,
                                                                                                EnumProblemType enumProblemType,
                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Chapter concernedChapter,
                                                                                                EnumProblemType enumProblemType,
                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Chapter concernedChapter,
                                                                                                 EnumProblemType enumProblemType,
                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Section concernedSection,
                                                                                                EnumProblemType enumProblemType,
                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Section concernedSection,
                                                                                                 EnumProblemType enumProblemType,
                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(SubSection concernedSubSection,
                                                                                                   EnumProblemType enumProblemType,
                                                                                                   EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(SubSection concernedSubSection,
                                                                                                    EnumProblemType enumProblemType,
                                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(Paragraph concernedParagraph,
                                                                                                  EnumProblemType enumProblemType,
                                                                                                  EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(Paragraph concernedParagraph,
                                                                                                   EnumProblemType enumProblemType,
                                                                                                   EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// ProblemScope and ProblemLevelofDifficulty ///////////////////////////////
    List<Problem>  findAllByConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Course concernedCourse,
                                                                                                EnumScope enumScope,
                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Course concernedCourse,
                                                                                                 EnumScope enumScope,
                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Module concernedModule,
                                                                                                EnumScope enumScope,
                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Module concernedModule,
                                                                                                 EnumScope enumScope,
                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Chapter concernedChapter,
                                                                                                 EnumScope enumScope,
                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Chapter concernedChapter,
                                                                                                  EnumScope enumScope,
                                                                                                  EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Section concernedSection,
                                                                                                 EnumScope enumScope,
                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Section concernedSection,
                                                                                                  EnumScope enumScope,
                                                                                                  EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(SubSection concernedSubSection,
                                                                                                    EnumScope enumScope,
                                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(SubSection concernedSubSection,
                                                                                                     EnumScope enumScope,
                                                                                                     EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Paragraph concernedParagraph,
                                                                                                   EnumScope enumScope,
                                                                                                   EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Paragraph concernedParagraph,
                                                                                                    EnumScope enumScope,
                                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// ProblemType, ProblemScope and ProblemLevelofDifficulty //////////////////
    List<Problem>  findAllByConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Course concernedCourse,
                                                                                                               EnumProblemType enumProblemType,
                                                                                                               EnumScope enumScope,
                                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Course concernedCourse,
                                                                                                                EnumProblemType enumProblemType,
                                                                                                                EnumScope enumScope,
                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Module concernedModule,
                                                                                                               EnumProblemType enumProblemType,
                                                                                                               EnumScope enumScope,
                                                                                                               EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Module concernedModule,
                                                                                                                EnumProblemType enumProblemType,
                                                                                                                EnumScope enumScope,
                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Chapter concernedChapter,
                                                                                                                EnumProblemType enumProblemType,
                                                                                                                EnumScope enumScope,
                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Chapter concernedChapter,
                                                                                                                 EnumProblemType enumProblemType,
                                                                                                                 EnumScope enumScope,
                                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Section concernedSection,
                                                                                                                EnumProblemType enumProblemType,
                                                                                                                EnumScope enumScope,
                                                                                                                EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Section concernedSection,
                                                                                                                 EnumProblemType enumProblemType,
                                                                                                                 EnumScope enumScope,
                                                                                                                 EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(SubSection concernedSubSection,
                                                                                                                   EnumProblemType enumProblemType,
                                                                                                                   EnumScope enumScope,
                                                                                                                   EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(SubSection concernedSubSection,
                                                                                                                    EnumProblemType enumProblemType,
                                                                                                                    EnumScope enumScope,
                                                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(Paragraph concernedParagraph,
                                                                                                                  EnumProblemType enumProblemType,
                                                                                                                  EnumScope enumScope,
                                                                                                                  EnumLevelofDifficulty enumLevelofDifficulty);
    List<Problem>  findAllByConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(Paragraph concernedParagraph,
                                                                                                                   EnumProblemType enumProblemType,
                                                                                                                   EnumScope enumScope,
                                                                                                                   EnumLevelofDifficulty enumLevelofDifficulty);




}
