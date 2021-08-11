package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.CourseNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Paper;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;

import java.util.List;

public interface PaperService {
    ServerResponse<Paper> findPaperById(String paperId);
    ServerResponse<List<Paper>> findAllPaperofStaff(String staffId, String sortBy,
                                                    String direction);
    ServerResponse<List<Paper>> findAllPaperByType(String paperType, String sortBy,
                                                   String direction);
    ServerResponse<List<Paper>> findAllPaperByTitle(String title, String sortBy,
                                                   String direction);
    ServerResponse<List<Paper>> findAllPaperofCourse(String courseId, String sortBy,
                                                   String direction)
            throws CourseNotFoundException;
    ServerResponse<List<Paper>> findAllPaperofCourseByPaperType(String courseId,
                                                                String paperType,
                                                                String sortBy,
                                                                String direction)
            throws CourseNotFoundException;
    ServerResponse<List<Paper>> findAllPaperofCourseAndStaff(String courseId,
                                                             String staffId,
                                                             String sortBy,
                                                             String direction)
            throws CourseNotFoundException, StaffNotFoundException;

    ServerResponse<Paper> savePaper(String title, String paperType, String  concernedCourseId,
                                    String ownerStaffId)
            throws CourseNotFoundException, StaffNotFoundException;

    ServerResponse<Paper> duplicatePaperForStaff(String paperId, String ownerStaffId)
            throws PaperNotFoundException, StaffNotFoundException;

    ServerResponse<Paper> updatePaper(String paperId, String title, String paperType,
                                      String  concernedCourseId, String ownerStaffId)
            throws PaperNotFoundException, CourseNotFoundException, StaffNotFoundException;

    ServerResponse<Paper> addQuestionToPaper(String questionId, String paperId)
            throws QuestionNotFoundException, PaperNotFoundException;

    ServerResponse<Paper> removeQuestionToPaper(String questionId, String paperId)
            throws QuestionNotFoundException, PaperNotFoundException, QuestionNotBelongingToPaperException;

    ServerResponse<Paper> addIndicationToPaper(String indicationId, String paperId)
            throws IndicationNotFoundException, PaperNotFoundException;

    ServerResponse<Paper> removeIndicationToPaper(String indicationId, String paperId)
            throws IndicationNotFoundException, PaperNotFoundException,
            IndicationNotBelongingToPaperException;

    ServerResponse<Paper> updateIndicationToPaper(String indicationId, String contentId,
                                                      String value, String paperId,
                                                      String staffId)
            throws IndicationNotFoundException, ContentNotFoundException,
            PaperNotFoundException, IndicationNotBelongingToStaffException;

    ServerResponse<Paper> addProblemToPaper(String problemId, String paperId)
            throws ProblemNotFoundException, PaperNotFoundException;

    ServerResponse<Paper> removeProblemToPaper(String problemId, String paperId)
            throws QuestionNotFoundException, PaperNotFoundException,
            ProblemNotBelongingToPaperException;

    ServerResponse<Paper> deletePaper(String paperId, String staffId)
            throws PaperNotFoundException,  StaffNotFoundException, PaperNotBelongingToStaffException;
}
