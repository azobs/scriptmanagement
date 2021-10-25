package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.CourseNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Paper;

import java.util.List;

public interface PaperService {
    ServerResponse<Paper> findPaperById(String paperId);
    ServerResponse<List<Paper>> findAllPaperofStaffBy(String staffId,
                                                    String paperType,
                                                    String paperScope,
                                                    String levelofDifficulty,
                                                    String sortBy,
                                                    String direction)
            throws StaffNotFoundException;

    ServerResponse<List<Paper>> findAllPaperofCourseBy(String courseId,
                                                       String paperType,
                                                       String paperScope,
                                                       String levelofDifficulty,
                                                       String sortBy,
                                                       String direction)
            throws CourseNotFoundException;
    ServerResponse<List<Paper>> findAllPaperofCourseAndStaffBy(String courseId,
                                                               String staffId,
                                                               String paperType,
                                                               String paperScope,
                                                               String levelofDifficulty,
                                                               String sortBy,
                                                               String direction)
            throws CourseNotFoundException, StaffNotFoundException;

    Paper savePaper(Paper paper);

    ServerResponse<Paper> savePaper(String title, String paperType, String levelofDifficulty,
                                    String paperScope, String  concernedCourseId, String ownerStaffId)
            throws CourseNotFoundException, StaffNotFoundException;

    ServerResponse<Paper> duplicatePaperForStaff(String paperId, String staffId)
            throws PaperNotFoundException, StaffNotFoundException;

    ServerResponse<Paper> updatePaper(String paperId, String title, String paperType,
                                      String levelofDifficulty, String paperScope,
                                      String  courseId, String staffId)
            throws PaperNotFoundException, CourseNotFoundException, StaffNotFoundException,
            PaperNotBelongingToStaffException;

    ServerResponse<Paper> addQuestionToPaper(String questionId, String paperId, String staffId)
            throws QuestionNotFoundException, PaperNotFoundException, StaffNotFoundException,
            QuestionAlreadyBelongingToPaperException, PaperNotBelongingToStaffException;

    ServerResponse<Paper> removeQuestionToPaper(String questionId, String paperId, String staffId)
            throws QuestionNotFoundException, PaperNotFoundException, StaffNotFoundException,
            QuestionNotBelongingToPaperException, PaperNotBelongingToStaffException;

    ServerResponse<Paper> addIndicationToPaper(String value, String contentType, String paperId, String staffId)
            throws PaperNotFoundException, StaffNotFoundException;

    ServerResponse<Paper> updateIndicationToPaper(String indicationId, String contentId,
                                                  String value, String paperId,
                                                  String staffId)
            throws IndicationNotFoundException,
            PaperNotFoundException, IndicationNotBelongingToStaffException;

    ServerResponse<Paper> removeIndicationToPaper(String indicationId, String paperId, String staffId)
            throws IndicationNotFoundException, PaperNotFoundException,
            IndicationNotBelongingToStaffException, StaffNotFoundException;

    ServerResponse<Paper> addProblemToPaper(String problemId, String paperId, String staffId)
            throws ProblemNotFoundException, PaperNotFoundException, StaffNotFoundException,
            ProblemAlreadyBelongingToPaperException, PaperNotBelongingToStaffException;

    ServerResponse<Paper> removeProblemToPaper(String problemId, String paperId, String staffId)
            throws ProblemNotFoundException, PaperNotFoundException, StaffNotFoundException,
            ProblemNotBelongingToPaperException, PaperNotBelongingToStaffException;

    ServerResponse<Paper> deletePaper(String paperId, String staffId)
            throws PaperNotFoundException,  StaffNotFoundException, PaperNotBelongingToStaffException;
    ServerResponse<Paper> deletePaper(String paperId) throws PaperNotFoundException;
}
