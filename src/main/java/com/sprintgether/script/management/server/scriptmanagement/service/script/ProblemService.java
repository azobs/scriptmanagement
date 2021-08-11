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

    ServerResponse<List<Indication>> findAllIndicationofProblem(String problemId, String sortBy,
                                                                 String direction)
            throws ProblemNotFoundException;

    ServerResponse<List<Question>> findAllQuestionofProblem(String problemId, String sortBy,
                                                                String direction)
            throws ProblemNotFoundException;

    ServerResponse<List<Content>> findAllContentofProblem(String problemId, String sortBy,
                                                           String direction)
            throws ProblemNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofStaff(String staffId, String sortBy,
                                                          String direction)
            throws StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofStaffByType(String staffId,
                                                              String problemType,
                                                              String sortBy,
                                                        String direction)
            throws StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofStaffByTypeAndLevelofDifficulty(String staffId,
                                                              String problemType,
                                                              String levelofDifficulty,
                                                              String sortBy,
                                                              String direction)
            throws StaffNotFoundException;


    ServerResponse<List<Problem>> findAllProblemofCourseAndStaffByType(String staffId,
                                                              String courseId,
                                                              String problemType,
                                                              String sortBy,
                                                              String direction)
            throws StaffNotFoundException, CourseNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofCourseAndStaffByTypeAndLevelofDifficulty(
                                        String staffId,
                                        String courseId,
                                        String problemType,
                                        String levelofDifficulty,
                                        String sortBy,
                                        String direction)
            throws StaffNotFoundException, CourseNotFoundException;

    ServerResponse<List<Problem>> findAllProblemByType(String problemType, String sortBy,
                                                         String direction);

    ServerResponse<List<Problem>> findAllProblemByLevelofDifficulty(String levelofDifficulty,
                                                                      String sortBy,
                                                                      String direction);

    ServerResponse<List<Problem>> findAllProblemofCourse(String courseId, String sortBy,
                                                           String direction)
            throws CourseNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofModule(String moduleId, String sortBy,
                                                         String direction)
            throws ModuleNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofChapter(String chapterId, String sortBy,
                                                            String direction)
            throws ChapterNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSection(String sectionId, String sortBy,
                                                            String direction)
            throws SectionNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSubSection(String subSectionId,
                                                               String sortBy,
                                                               String direction)
            throws SubSectionNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofParagraph(String paragraphId,
                                                              String sortBy,
                                                              String direction)
            throws ParagraphNotFoundException;

    ////

    ServerResponse<List<Problem>> findAllProblemofCourseAndStaff(String courseId,
                                                                 String staffId,
                                                                 String sortBy,
                                                                 String direction)
            throws CourseNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofModuleAndStaff(String moduleId,
                                                                 String staffId,
                                                                 String sortBy,
                                                                 String direction)
            throws ModuleNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofChapterAndStaff(String chapterId,
                                                                  String staffId,
                                                                  String sortBy,
                                                                  String direction)
            throws ChapterNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSectionAndStaff(String sectionId,
                                                                  String staffId,
                                                                  String sortBy,
                                                                  String direction)
            throws SectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofSubSectionAndStaff(String subSectionId,
                                                                     String staffId,
                                                                     String sortBy,
                                                                     String direction)
            throws SubSectionNotFoundException, StaffNotFoundException;

    ServerResponse<List<Problem>> findAllProblemofParagraphAndStaff(String paragraphId,
                                                                    String staffId,
                                                                    String sortBy,
                                                                    String direction)
            throws ParagraphNotFoundException, StaffNotFoundException;
    ////

    /***************************************************************************
     * The concernedPartId parameter identify the program part of the course associated
     *   to the question. It can be module, chapter, section, subsection or paragraph.
     * If concernedPartId  designate a Module, it must be a module in the courseOutline
     * associated to the course specified.
     * @param problemType
     * @param levelofDifficulty
     * @param ownerStaff
     * @param courseId
     * @param concernedPartId
     * @return
     */
    ServerResponse<Problem> saveProblem(EnumProblemType problemType,
                                        EnumLevelofDifficulty levelofDifficulty,
                                        Staff ownerStaff,
                                        String courseId,
                                        String concernedPartId)
            throws ConcernedPartNotBelongingToCourseException;

    ServerResponse<Problem> duplicateProblemForStaff(String problemId, String staffId)
            throws ProblemNotFoundException, StaffNotFoundException,
            ProblemAlreadyBelongingToStaffException;

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
                                                        String value, String problemId)
            throws IndicationNotFoundException, ContentNotFoundException,
            ProblemNotFoundException;

    ServerResponse<Problem> removeIndicationToProblem(String indicationId, String problemId)
            throws IndicationNotFoundException, ProblemNotFoundException;

    ServerResponse<Problem> updateProblem(EnumProblemType problemType,
                                            EnumLevelofDifficulty levelofDifficulty,
                                            String courseId,
                                            String concernedPartId,
                                            String staffId)
            throws ConcernedPartNotBelongingToCourseException,
            ProblemNotBelongingToStaffException, CourseNotFoundException, StaffNotFoundException;

    ServerResponse<Problem> deleteProblem(String problemId, String staffId)
            throws ProblemNotFoundException, ProblemNotBelongingToStaffException;

    ServerResponse<Problem> deleteProblem(String problemId)
            throws ProblemNotFoundException;

}
