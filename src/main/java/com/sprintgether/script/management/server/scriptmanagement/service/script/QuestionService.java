package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;

import java.util.List;

public interface QuestionService {
    ServerResponse<Question> findQuestionById(String questionId);

    ServerResponse<List<Proposition>> findAllPropositionofQuestion(String questionId,
                                                                   String sortBy,
                                                                   String direction)
            throws QuestionNotFoundException;

    ServerResponse<List<Indication>> findAllIndicationofQuestion(String questionId, String sortBy,
                                                                  String direction)
            throws QuestionNotFoundException;

    ServerResponse<List<Content>> findAllContentofQuestion(String questionId, String sortBy,
                                                                   String direction)
            throws QuestionNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofStaff(String staffId, String sortBy,
                                                          String direction)
            throws StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofStaffByType(String staffId,
                                                              String questionType,
                                                              String sortBy,
                                                              String direction)
            throws StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofStaffByTypeAndLevelofDifficulty(
            String staffId,
            String questionType,
            String levelofDifficulty,
            String sortBy,
            String direction)
            throws StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofCourseAndStaffByType(String staffId,
                                                                       String courseId,
                                                                       String questionType,
                                                                       String sortBy,
                                                                       String direction)
            throws StaffNotFoundException, CourseNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofCourseAndStaffByTypeAndLevelofDifficulty(
                            String staffId,
                            String courseId,
                            String questionType,
                            String levelofDifficulty,
                            String sortBy,
                            String direction)
            throws StaffNotFoundException, CourseNotFoundException;

    ServerResponse<List<Question>> findAllQuestionByType(String questionType, String sortBy,
                                                                 String direction);

    ServerResponse<List<Question>> findAllQuestionByLevelofDifficulty(String levelofDifficulty,
                                                                      String sortBy,
                                                                      String direction);

    ServerResponse<List<Question>> findAllQuestionofCourse(String courseId, String sortBy,
                                                          String direction)
            throws CourseNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofModule(String moduleId, String sortBy,
                                                           String direction)
            throws ModuleNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofChapter(String chapterId, String sortBy,
                                                           String direction)
            throws ChapterNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSection(String sectionId, String sortBy,
                                                           String direction)
            throws SectionNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSubSection(String subSectionId,
                                                               String sortBy,
                                                               String direction)
            throws SubSectionNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofParagraph(String paragraphId,
                                                              String sortBy,
                                                              String direction)
            throws ParagraphNotFoundException;

    /////
    ServerResponse<List<Question>> findAllQuestionofCourseAndStaff(String courseId,
                                                                 String staffId,
                                                                 String sortBy,
                                                                 String direction)
            throws CourseNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofModuleAndStaff(String moduleId,
                                                                 String staffId,
                                                                 String sortBy,
                                                                 String direction)
            throws ModuleNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofChapterAndStaff(String chapterId,
                                                                  String staffId,
                                                                  String sortBy,
                                                                  String direction)
            throws ChapterNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSectionAndStaff(String sectionId,
                                                                  String staffId,
                                                                  String sortBy,
                                                                  String direction)
            throws SectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofSubSectionAndStaff(String subSectionId,
                                                                     String staffId,
                                                                     String sortBy,
                                                                     String direction)
            throws SubSectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofParagraphAndStaff(String paragraphId,
                                                                    String staffId,
                                                                    String sortBy,
                                                                    String direction)
            throws ParagraphNotFoundException, StaffNotFoundException;
    /////


    /***************************************************************************
     * The concernedPartId parameter identify the program part of the course associated
     *   to the question. It can be module, chapter, section, subsection or paragraph.
     * If concernedPartId  designate a Module, it must be a module in the courseOutline
     * associated to the course specified.
     * @param questionType
     * @param levelofDifficulty
     * @param ownerStaff
     * @param courseId
     * @param concernedPartId
     * @return
     */
    ServerResponse<Question> saveQuestion(EnumQuestionType questionType,
                                          EnumLevelofDifficulty levelofDifficulty,
                                          Staff ownerStaff,
                                          String courseId,
                                          String concernedPartId)
            throws ConcernedPartNotBelongingToCourseException;

    ServerResponse<Question> duplicateQuestionForStaff(String questionId, Staff staffId)
            throws QuestionNotFoundException, StaffNotFoundException,
            QuestionAlreadyBelongingToStaffException;

    ServerResponse<Question> addContentToQuestion(String value, String contentType,
                                                  String questionId)
            throws QuestionNotFoundException;

    ServerResponse<Question> updateContentToQuestion(String contentId, String value,
                                                     String questionId)
            throws ContentNotFoundException, QuestionNotFoundException;

    ServerResponse<Question> removeContentToQuestion(String contentId, String questionId)
            throws ContentNotFoundException, QuestionNotFoundException;

    ServerResponse<Question> addIndicationToQuestion(String value, String contentType,
                                                     String staffId, String questionId)
            throws QuestionNotFoundException, StaffNotFoundException;

    ServerResponse<Question> updateIndicationToQuestion(String indicationId, String contentId,
                                                        String value, String questionId,
                                                        String staffId)
            throws IndicationNotFoundException, ContentNotFoundException,
            QuestionNotFoundException, IndicationNotBelongingToStaffException;

    ServerResponse<Question> removeIndicationToQuestion(String indicationId, String questionId,
                                                        String staffId)
            throws IndicationNotFoundException, QuestionNotFoundException,
            IndicationNotBelongingToStaffException;

    ServerResponse<Question> addPropositionToQuestion(boolean valid, String value,
                                                      String contentType, String staffId,
                                                      String questionId)
            throws QuestionNotFoundException, StaffNotFoundException;

    ServerResponse<Question> updatePropositionToQuestion(String propositionId, boolean valid,
                                                         String contentId, String value,
                                                         String questionId, String staffId)
            throws PropositionNotFoundException, ContentNotFoundException,
            QuestionNotFoundException, PropositionNotBelongingToStaffException;

    ServerResponse<Question> removePropositionToQuestion(String propositionId,
                                                         String questionId, String staffId)
            throws PropositionNotFoundException, QuestionNotFoundException,
            PropositionNotBelongingToStaffException;

    ServerResponse<Question> updateQuestion(EnumQuestionType questionType,
                                            EnumLevelofDifficulty levelofDifficulty,
                                            String courseId,
                                            String concernedPartId,
                                            String staffId)
            throws ConcernedPartNotBelongingToCourseException,
            QuestionNotBelongingToStaffException, StaffNotFoundException,
            CourseNotFoundException;

    ServerResponse<Question> deleteQuestion(String questionId, String staffId)
            throws QuestionNotFoundException, QuestionNotBelongingToStaffException;

    ServerResponse<Question> deleteQuestion(String questionId)
            throws QuestionNotFoundException;



}
