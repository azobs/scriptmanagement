package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;

import java.util.List;

public interface ProblemService {
    ServerResponse<Problem> findProblemById(String problemId);

    ServerResponse<List<Indication>> findAllIndicationofProblem(String problemId)
            throws ProblemNotFoundException;

    ServerResponse<List<Content>> findAllContentofProblem(String problemId)
            throws ProblemNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofStaffBy(String staffId,
                                                          String problemType,
                                                          String problemScope,
                                                          String levelofDifficulty,
                                                          String sortBy,
                                                          String direction)
            throws StaffNotFoundException;



    ServerResponse<List<Problem>> findAllProblemofCourseAndStaffBy(String courseId,
                                                              String staffId,
                                                              String problemType,
                                                                   String problemScope,
                                                                   String levelofDifficulty,
                                                              String sortBy,
                                                              String direction)
            throws StaffNotFoundException, CourseNotFoundException;


    ServerResponse<List<Problem>> findAllProblemofCourseBy(String courseId,
                                                           String problemType,
                                                           String problemScope,
                                                           String levelofDifficulty,
                                                           String sortBy,
                                                           String direction)
            throws CourseNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofModuleBy(String moduleId,
                                                           String problemType,
                                                           String problemScope,
                                                           String levelofDifficulty,
                                                           String sortBy,
                                                           String direction)
            throws ModuleNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofChapterBy(String chapterId,
                                                            String problemType,
                                                            String problemScope,
                                                            String levelofDifficulty,
                                                            String sortBy,
                                                            String direction)
            throws ChapterNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSectionBy(String sectionId,
                                                            String problemType,
                                                            String problemScope,
                                                            String levelofDifficulty,
                                                            String sortBy,
                                                            String direction)
            throws SectionNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSubSectionBy(String subSectionId,
                                                               String problemType,
                                                               String problemScope,
                                                               String levelofDifficulty,
                                                               String sortBy,
                                                               String direction)
            throws SubSectionNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofParagraphBy(String paragraphId,
                                                              String problemType,
                                                              String problemScope,
                                                              String levelofDifficulty,
                                                              String sortBy,
                                                              String direction)
            throws ParagraphNotFoundException;

    ////

    ServerResponse<List<Problem>> findAllProblemofModuleAndStaff(String moduleId,
                                                                 String staffId,
                                                                 String problemType,
                                                                 String problemScope,
                                                                 String levelofDifficulty,
                                                                 String sortBy,
                                                                 String direction)
            throws ModuleNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofChapterAndStaff(String chapterId,
                                                                  String staffId,
                                                                  String problemType,
                                                                  String problemScope,
                                                                  String levelofDifficulty,
                                                                  String sortBy,
                                                                  String direction)
            throws ChapterNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSectionAndStaff(String sectionId,
                                                                  String staffId,
                                                                  String problemType,
                                                                  String problemScope,
                                                                  String levelofDifficulty,
                                                                  String sortBy,
                                                                  String direction)
            throws SectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSubSectionAndStaff(String subSectionId,
                                                                     String staffId,
                                                                     String problemType,
                                                                     String problemScope,
                                                                     String levelofDifficulty,
                                                                     String sortBy,
                                                                     String direction)
            throws SubSectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofParagraphAndStaff(String paragraphId,
                                                                    String staffId,
                                                                    String problemType,
                                                                    String problemScope,
                                                                    String levelofDifficulty,
                                                                    String sortBy,
                                                                    String direction)
            throws ParagraphNotFoundException, StaffNotFoundException;
    ////

    Problem saveProblem(Problem problem);

    /***************************************************************************
     * The concernedPartId parameter identify the program part of the course associated
     *   to the question. It can be module, chapter, section, subsection or paragraph.
     * If concernedPartId  designate a Module, it must be a module in the courseOutline
     * associated to the course specified.
     * @param title
     * @param problemType
     * @param levelofDifficulty
     * @param problemScope
     * @param ownerStaffId
     * @param courseId
     * @param concernedPartId
     * @return
     */
    ServerResponse<Problem> saveProblem(String title,
                                        String problemType,
                                        String levelofDifficulty,
                                        String problemScope,
                                        String ownerStaffId,
                                        String courseId,
                                        String concernedPartId)
            throws ConcernedPartNotBelongingToCourseException, StaffNotFoundException, CourseNotFoundException;

    ServerResponse<Problem> duplicateProblemForStaff(String problemId, String staffId)
            throws ProblemNotFoundException, StaffNotFoundException;

    ServerResponse<Problem> addContentToProblem(String value, String contentType,
                                                  String problemId)
            throws ProblemNotFoundException;

    ServerResponse<Problem> updateContentToProblem(String contentId, String value,
                                                     String problemId)
            throws ContentNotBelongingToException, ProblemNotFoundException;

    ServerResponse<Problem> removeContentToProblem(String contentId, String problemId)
            throws ContentNotBelongingToException, ProblemNotFoundException;

    ServerResponse<Problem> addIndicationToProblem(String value, String contentType,
                                                     String staffId, String problemId)
            throws ProblemNotFoundException, StaffNotFoundException;

    ServerResponse<Problem> updateIndicationToProblem(String indicationId, String contentId,
                                                        String value, String problemId, String staffId)
            throws IndicationNotFoundException, ContentNotFoundException,
            ProblemNotFoundException, IndicationNotBelongingToStaffException;

    ServerResponse<Problem> removeIndicationToProblem(String indicationId, String problemId, String staffId)
            throws IndicationNotFoundException, ProblemNotFoundException,
            IndicationNotBelongingToStaffException, StaffNotFoundException;

    ServerResponse<Problem> addQuestionToProblem(String questionId, String problemId, String staffId)
            throws QuestionNotFoundException, ProblemNotFoundException, StaffNotFoundException,
            QuestionAlreadyBelongingToProblemException, ProblemNotBelongingToStaffException;

    ServerResponse<Problem> removeQuestionToProblem(String questionId, String problemId, String staffId)
            throws QuestionNotFoundException, ProblemNotFoundException, StaffNotFoundException,
            QuestionNotBelongingToProblemException, ProblemNotBelongingToStaffException;

    ServerResponse<Problem> updateProblem(String problemId,
                                          String newTitle,
                                          String problemType,
                                          String levelofDifficulty,
                                          String problemScope,
                                          String courseId,
                                          String concernedPartId,
                                          String staffId)
            throws ConcernedPartNotBelongingToCourseException, ProblemNotFoundException,
            ProblemNotBelongingToStaffException, CourseNotFoundException, StaffNotFoundException;

    ServerResponse<Problem> deleteProblem(String problemId, String staffId)
            throws ProblemNotFoundException, ProblemNotBelongingToStaffException,
            ProblemNotBelongingToStaffException, IndicationNotFoundException;

    ServerResponse<Problem> deleteProblem(String problemId)
            throws ProblemNotFoundException, IndicationNotFoundException;

}
