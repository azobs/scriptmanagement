package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    ServerResponse<Question> findQuestionById(String questionId);

    ServerResponse<List<Proposition>> findAllPropositionofQuestion(String questionId)
            throws QuestionNotFoundException;


    ServerResponse<List<Indication>> findAllIndicationofQuestion(String questionId)
            throws QuestionNotFoundException;


    ServerResponse<List<Content>> findAllContentofQuestion(String questionId)
            throws QuestionNotFoundException;


    ServerResponse<List<Question>> findAllQuestionofStaffBy(String staffId,
                                                                    String questionType,
                                                                    String questionScope,
                                                                    String levelofDifficulty,
                                                                    String sortBy,
                                                                    String direction)
            throws StaffNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofStaffBy(String staffId,
                                                            String questionType,
                                                            String questionScope,
                                                            String levelofDifficulty,
                                                            Pageable pageable)
            throws StaffNotFoundException;


    ServerResponse<List<Question>> findAllQuestionofCourseAndStaffBy(String staffId,
                                                                     String courseId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     String sortBy,
                                                                     String direction)
            throws StaffNotFoundException, CourseNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofCourseAndStaffBy(String staffId,
                                                                     String courseId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     Pageable pageable)
            throws StaffNotFoundException, CourseNotFoundException;



    ServerResponse<List<Question>> findAllQuestionofCourseBy(String courseId,
                                                           String questionType,
                                                           String questionScope,
                                                           String levelofDifficulty,
                                                           String sortBy,
                                                           String direction)
            throws CourseNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofCourseBy(String courseId,
                                                             String questionType,
                                                             String questionScope,
                                                             String levelofDifficulty,
                                                             Pageable pageable)
            throws CourseNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofModuleBy(String moduleId,
                                                           String questionType,
                                                           String questionScope,
                                                           String levelofDifficulty,
                                                           String sortBy,
                                                           String direction)
            throws ModuleNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofModuleBy(String moduleId,
                                                             String questionType,
                                                             String questionScope,
                                                             String levelofDifficulty,
                                                             Pageable pageable)
            throws ModuleNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofChapterBy(String chapterId,
                                                            String questionType,
                                                            String questionScope,
                                                            String levelofDifficulty,
                                                            String sortBy,
                                                            String direction)
            throws ChapterNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofChapterBy(String chapterId,
                                                              String questionType,
                                                              String questionScope,
                                                              String levelofDifficulty,
                                                              Pageable pageable)
            throws ChapterNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSectionBy(String sectionId,
                                                            String questionType,
                                                            String questionScope,
                                                            String levelofDifficulty,
                                                            String sortBy,
                                                            String direction)
            throws SectionNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofSectionBy(String sectionId,
                                                              String questionType,
                                                              String questionScope,
                                                              String levelofDifficulty,
                                                              Pageable pageable)
            throws SectionNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSubSectionBy(String subSectionId,
                                                               String questionType,
                                                               String questionScope,
                                                               String levelofDifficulty,
                                                               String sortBy,
                                                               String direction)
            throws SubSectionNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofSubSectionBy(String subSectionId,
                                                                 String questionType,
                                                                 String questionScope,
                                                                 String levelofDifficulty,
                                                                 Pageable pageable)
            throws SubSectionNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofParagraphBy(String paragraphId,
                                                              String questionType,
                                                              String questionScope,
                                                              String levelofDifficulty,
                                                              String sortBy,
                                                              String direction)
            throws ParagraphNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofParagraphBy(String paragraphId,
                                                                String questionType,
                                                                String questionScope,
                                                                String levelofDifficulty,
                                                                Pageable pageable)
            throws ParagraphNotFoundException;

    /////

    ServerResponse<List<Question>> findAllQuestionofModuleAndStaffBy(String moduleId,
                                                                     String staffId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     String sortBy,
                                                                     String direction)
            throws ModuleNotFoundException, StaffNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofModuleAndStaffBy(String moduleId,
                                                                     String staffId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     Pageable pageable)
            throws ModuleNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofChapterAndStaffBy(String chapterId,
                                                                      String staffId,
                                                                      String questionType,
                                                                      String questionScope,
                                                                    String levelofDifficulty,
                                                                    String sortBy,
                                                                    String direction)
            throws ChapterNotFoundException, StaffNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofChapterAndStaffBy(String chapterId,
                                                                      String staffId,
                                                                      String questionType,
                                                                      String questionScope,
                                                                      String levelofDifficulty,
                                                                      Pageable pageable)
            throws ChapterNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSectionAndStaffBy(String sectionId,
                                                                  String staffId,
                                                                      String questionType,
                                                                      String questionScope,
                                                                      String levelofDifficulty,
                                                                  String sortBy,
                                                                  String direction)
            throws SectionNotFoundException, StaffNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofSectionAndStaffBy(String sectionId,
                                                                      String staffId,
                                                                      String questionType,
                                                                      String questionScope,
                                                                      String levelofDifficulty,
                                                                      Pageable pageable)
            throws SectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSubSectionAndStaffBy(String subSectionId,
                                                                         String staffId,
                                                                         String questionType,
                                                                         String questionScope,
                                                                         String levelofDifficulty,
                                                                         String sortBy,
                                                                         String direction)
            throws SubSectionNotFoundException, StaffNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofSubSectionAndStaffBy(String subSectionId,
                                                                         String staffId,
                                                                         String questionType,
                                                                         String questionScope,
                                                                         String levelofDifficulty,
                                                                         Pageable pageable)
            throws SubSectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofParagraphAndStaffBy(String paragraphId,
                                                                        String staffId,
                                                                        String questionType,
                                                                        String questionScope,
                                                                        String levelofDifficulty,
                                                                        String sortBy,
                                                                        String direction)
            throws ParagraphNotFoundException, StaffNotFoundException;

    ServerResponse<Page<Question>> findAllQuestionofParagraphAndStaffBy(String paragraphId,
                                                                        String staffId,
                                                                        String questionType,
                                                                        String questionScope,
                                                                        String levelofDifficulty,
                                                                        Pageable pageable)
            throws ParagraphNotFoundException, StaffNotFoundException;

    ServerResponse<List<Answer>> findAllAnswerofQuestionBy(String questionId, String answerScope,
                                                           String sortBy, String direction)
            throws QuestionNotFoundException;

    ServerResponse<Page<Answer>> findAllAnswerofQuestion(String questionId, String answerScope,
                                                         Pageable pageable)
            throws QuestionNotFoundException;

    ServerResponse<List<Answer>> findAllAnswerofQuestionAndStaffBy(String questionId, String staffId,
                                                                   String answerScope, String sortBy,
                                                                   String direction)
            throws QuestionNotFoundException, StaffNotFoundException;

    ServerResponse<Page<Answer>> findAllAnswerofQuestionAndStaffBy(String questionId, String staffId,
                                                                   String answerScope, Pageable pageable)
            throws QuestionNotFoundException, StaffNotFoundException;


    Question saveQuestion(Question question);
    /***************************************************************************
     * The concernedPartId parameter identify the program part of the course associated
     *   to the question. It can be module, chapter, section, subsection or paragraph.
     * If concernedPartId  designate a Module, it must be a module in the courseOutline
     * associated to the course specified.
     * The question can be for a problem or not. if not the problem will be null.
     * @param title
     * @param questionType
     * @param levelofDifficulty
     * @param questionScope
     * @param ownerStaffId
     * @param courseId
     * @param concernedPartId
     * @param problemId
     * @return
     */
    ServerResponse<Question> saveQuestion(String title,
                                          String questionType,
                                          String levelofDifficulty,
                                          String questionScope,
                                          String ownerStaffId,
                                          String courseId,
                                          String concernedPartId,
                                          String problemId)
            throws ConcernedPartNotBelongingToCourseException, StaffNotFoundException,
            CourseNotFoundException;

    ServerResponse<Question> duplicateQuestionForStaff(String questionId, String staffId)
            throws QuestionNotFoundException, StaffNotFoundException;

    ServerResponse<Question> addContentToQuestion(String value, String contentType,
                                                  String questionId)
            throws QuestionNotFoundException;

    ServerResponse<Question> updateContentToQuestion(String contentId, String value,
                                                     String questionId)
            throws ContentNotBelongingToException, QuestionNotFoundException;

    ServerResponse<Question> removeContentToQuestion(String contentId, String questionId)
            throws ContentNotBelongingToException, QuestionNotFoundException;

    ServerResponse<Question> addIndicationToQuestion(String value, String contentType,
                                                     String staffId, String questionId)
            throws QuestionNotFoundException, StaffNotFoundException;

    ServerResponse<Question> updateIndicationToQuestion(String indicationId, String contentId,
                                                        String value, String questionId,
                                                        String staffId)
            throws IndicationNotFoundException, QuestionNotFoundException,
            IndicationNotBelongingToStaffException;

    ServerResponse<Question> removeIndicationToQuestion(String indicationId, String questionId,
                                                        String staffId)
            throws IndicationNotFoundException, QuestionNotFoundException,
            IndicationNotBelongingToStaffException, StaffNotFoundException;

    ServerResponse<Question> addPropositionToQuestion(boolean valid, String value,
                                                      String contentType, String staffId,
                                                      String questionId)
            throws QuestionNotFoundException, StaffNotFoundException;

    ServerResponse<Question> updatePropositionToQuestion(String propositionId, boolean valid,
                                                         String contentId, String value,
                                                         String questionId, String staffId)
            throws PropositionNotFoundException,
            QuestionNotFoundException, PropositionNotBelongingToStaffException;

    ServerResponse<Question> removePropositionToQuestion(String propositionId,
                                                         String questionId, String staffId)
            throws PropositionNotFoundException, QuestionNotFoundException,
            PropositionNotBelongingToStaffException, StaffNotFoundException;

    ServerResponse<Answer> addAnswerToQuestion(String title, String answerScope,
                                                 String value, String contentType,
                                                 String questionId,
                                                 String staffId)
            throws QuestionNotFoundException, StaffNotFoundException;

    ServerResponse<Answer> duplicateAnswerForStaff(String answerId, String staffId)
            throws AnswerNotFoundException, StaffNotFoundException;

    ServerResponse<Answer> updateAnswerToQuestion(String answerId, String title,
                                                  String answerScope, String value,
                                                  String contentType, String questionId,
                                                  String staffId)
            throws AnswerNotFoundException, QuestionNotFoundException, StaffNotFoundException,
            AnswerNotBelongingToStaffException;

    ServerResponse<Answer> removeAnswerToQuestion(String answerId, String questionId,
                                                    String staffId)
            throws AnswerNotFoundException, QuestionNotFoundException, StaffNotFoundException,
            AnswerNotBelongingToStaffException;

    ServerResponse<Question> updateQuestion(String questionId,
                                            String newTitle,
                                            String questionType,
                                            String levelofDifficulty,
                                            String questionScope,
                                            String courseId,
                                            String concernedPartId,
                                            String staffId)
            throws QuestionNotFoundException, ConcernedPartNotBelongingToCourseException,
            QuestionNotBelongingToStaffException, StaffNotFoundException,
            CourseNotFoundException;

    ServerResponse<Question> deleteQuestion(String questionId, String staffId)
            throws QuestionNotFoundException, QuestionNotBelongingToStaffException,
            IndicationNotFoundException, PropositionNotFoundException;

    ServerResponse<Question> deleteQuestion(String questionId)
            throws QuestionNotFoundException, IndicationNotFoundException, PropositionNotFoundException;



}
