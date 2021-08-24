package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProblemServiceImpl implements ProblemService {
    @Override
    public ServerResponse<Problem> findProblemById(String problemId) {
        return null;
    }

    @Override
    public ServerResponse<List<Indication>> findAllIndicationofProblem(String problemId, String sortBy, String direction) throws ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofProblem(String problemId, String sortBy, String direction) throws ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Content>> findAllContentofProblem(String problemId, String sortBy, String direction) throws ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofStaff(String staffId, String sortBy, String direction) throws StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofStaffByType(String staffId, String problemType, String sortBy, String direction) throws StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofStaffByTypeAndLevelofDifficulty(String staffId, String problemType, String levelofDifficulty, String sortBy, String direction) throws StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofCourseAndStaffByType(String staffId, String courseId, String problemType, String sortBy, String direction) throws StaffNotFoundException, CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofCourseAndStaffByTypeAndLevelofDifficulty(String staffId, String courseId, String problemType, String levelofDifficulty, String sortBy, String direction) throws StaffNotFoundException, CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemByType(String problemType, String sortBy, String direction) {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemByLevelofDifficulty(String levelofDifficulty, String sortBy, String direction) {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofCourse(String courseId, String sortBy, String direction) throws CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofModule(String moduleId, String sortBy, String direction) throws ModuleNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofChapter(String chapterId, String sortBy, String direction) throws ChapterNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSection(String sectionId, String sortBy, String direction) throws SectionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSubSection(String subSectionId, String sortBy, String direction) throws SubSectionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofParagraph(String paragraphId, String sortBy, String direction) throws ParagraphNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofCourseAndStaff(String courseId, String staffId, String sortBy, String direction) throws CourseNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofModuleAndStaff(String moduleId, String staffId, String sortBy, String direction) throws ModuleNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofChapterAndStaff(String chapterId, String staffId, String sortBy, String direction) throws ChapterNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSectionAndStaff(String sectionId, String staffId, String sortBy, String direction) throws SectionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSubSectionAndStaff(String subSectionId, String staffId, String sortBy, String direction) throws SubSectionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofParagraphAndStaff(String paragraphId, String staffId, String sortBy, String direction) throws ParagraphNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> saveProblem(EnumProblemType problemType, EnumLevelofDifficulty levelofDifficulty, Staff ownerStaff, String courseId, String concernedPartId) throws ConcernedPartNotBelongingToCourseException {
        return null;
    }

    @Override
    public ServerResponse<Problem> duplicateProblemForStaff(String problemId, String staffId) throws ProblemNotFoundException, StaffNotFoundException, ProblemAlreadyBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Problem> addContentToProblem(String value, String contentType, String problemId) throws ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> updateContentToProblem(String contentId, String value, String problemId) throws ContentNotBelongingToException, ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> removeContentToProblem(String contentId, String problemId) throws ContentNotBelongingToException, ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> addIndicationToProblem(String value, String contentType, String staffId, String problemId) throws ProblemNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> updateIndicationToProblem(String indicationId, String contentId, String value, String problemId) throws IndicationNotFoundException, ContentNotFoundException, ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> removeIndicationToProblem(String indicationId, String problemId) throws IndicationNotFoundException, ProblemNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> updateProblem(EnumProblemType problemType, EnumLevelofDifficulty levelofDifficulty, String courseId, String concernedPartId, String staffId) throws ConcernedPartNotBelongingToCourseException, ProblemNotBelongingToStaffException, CourseNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Problem> deleteProblem(String problemId, String staffId) throws ProblemNotFoundException, ProblemNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Problem> deleteProblem(String problemId) throws ProblemNotFoundException {
        return null;
    }
}
