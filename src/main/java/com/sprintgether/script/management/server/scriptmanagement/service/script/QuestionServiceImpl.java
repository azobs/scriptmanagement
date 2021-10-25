package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.AnswerRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.QuestionRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.commonused.CommonService;
import com.sprintgether.script.management.server.scriptmanagement.service.program.*;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService{
    QuestionRepository questionRepository;
    ContentRepository contentRepository;
    AnswerRepository answerRepository;
    StaffService staffService;
    IndicationService indicationService;
    PropositionService propositionService;
    CommonService commonService;
    CourseService courseService;
    ModuleService moduleService;
    ChapterService chapterService;
    SectionService sectionService;
    SubSectionService subSectionService;
    ParagraphService paragraphService;
   /* ProblemService problemService;*/

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               ContentRepository contentRepository,
                               AnswerRepository answerRepository,
                               StaffService staffService,
                               IndicationService indicationService,
                               PropositionService propositionService,
                               CommonService commonService,
                               CourseService courseService,
                               ModuleService moduleService,
                               ChapterService chapterService,
                               SectionService sectionService,
                               SubSectionService subSectionService,
                               ParagraphService paragraphService
                               /*ProblemService problemService*/) {
        this.questionRepository = questionRepository;
        this.contentRepository = contentRepository;
        this.answerRepository = answerRepository;
        this.staffService = staffService;
        this.indicationService = indicationService;
        this.propositionService = propositionService;
        this.commonService = commonService;
        this.courseService = courseService;
        this.moduleService = moduleService;
        this.chapterService = chapterService;
        this.sectionService = sectionService;
        this.subSectionService = subSectionService;
        this.paragraphService = paragraphService;
        /*this.problemService = problemService;*/
    }

    @Override
    public ServerResponse<Question> findQuestionById(String questionId) {
        ServerResponse<Question> srQuestion = new ServerResponse<>();
        questionId = questionId.trim();
        srQuestion.setResponseCode(ResponseCode.QUESTION_NOT_FOUND);
        srQuestion.setErrorMessage("The question does not found in the system with that questionID");
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if(optionalQuestion.isPresent()){
            srQuestion.setResponseCode(ResponseCode.QUESTION_FOUND);
            srQuestion.setErrorMessage("The question is found in the system");
            srQuestion.setAssociatedObject(optionalQuestion.get());
        }

        return srQuestion;
    }

    @Override
    public ServerResponse<List<Proposition>> findAllPropositionofQuestion(String questionId)
            throws QuestionNotFoundException {
        ServerResponse<Question> srQuestion = this.findQuestionById(questionId);
        if(srQuestion.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }

        Question question = srQuestion.getAssociatedObject();

        ServerResponse<List<Proposition>> srListofProposition = new ServerResponse<>();
        srListofProposition.setErrorMessage("The proposition list of the precised question is " +
                "successfully found");
        srListofProposition.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srListofProposition.setAssociatedObject(question.getListofProposition());

        return srListofProposition;
    }

    @Override
    public ServerResponse<List<Indication>> findAllIndicationofQuestion(String questionId)
            throws QuestionNotFoundException {
        ServerResponse<Question> srQuestion = this.findQuestionById(questionId);
        if(srQuestion.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }

        Question question = srQuestion.getAssociatedObject();

        ServerResponse<List<Indication>> srListofIndication = new ServerResponse<>(ResponseCode.INITIALISATION);
        srListofIndication.setErrorMessage("The indication list of the precised question is " +
                "successfully found");
        srListofIndication.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srListofIndication.setAssociatedObject(question.getListofIndication());

        return srListofIndication;
    }

    @Override
    public ServerResponse<List<Content>> findAllContentofQuestion(String questionId)
            throws QuestionNotFoundException {

        ServerResponse<Question> srQuestion = this.findQuestionById(questionId);
        if(srQuestion.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }

        Question question = srQuestion.getAssociatedObject();

        ServerResponse<List<Content>> srListofContent = new ServerResponse<>();
        srListofContent.setErrorMessage("The content list of the precised question is " +
                "successfully found");
        srListofContent.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srListofContent.setAssociatedObject(question.getListofContent());

        return srListofContent;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofStaffBy(String staffId,
                                                                   String questionType,
                                                                   String questionScope,
                                                                   String levelofDifficulty,
                                                                   String sortBy,
                                                                   String direction)
            throws StaffNotFoundException {
        staffId = staffId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();
        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.findAllByOwnerStaffOrderByTitleAsc(
                                concernedStaff);
                    } else {
                        listofQuestion = questionRepository.findAllByOwnerStaffOrderByTitleDesc(
                                concernedStaff);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionScopeOrderByTitleAsc(concernedStaff,
                                        enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionScopeOrderByTitleDesc(concernedStaff,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeOrderByTitleAsc(concernedStaff,
                                        enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeOrderByTitleDesc(concernedStaff,
                                        enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }



        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofStaffBy(String staffId,
                                                                   String questionType,
                                                                   String questionScope,
                                                                   String levelofDifficulty,
                                                                   Pageable pageable)
            throws StaffNotFoundException {
        staffId = staffId.trim();
        questionType = questionType.trim();
        questionScope = questionScope.trim();
        levelofDifficulty = levelofDifficulty.trim();

        ServerResponse<Page<Question>> srPageofQuestion = new ServerResponse<>();
        srPageofQuestion.setErrorMessage("The question page of the precised staff is " +
                "successfully found");
        srPageofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        try{

            Page<Question> pageofQuestion = null;

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                pageofQuestion = questionRepository.findAllByOwnerStaff(concernedStaff, pageable);
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndLevelofDifficulty(
                                concernedStaff, enumLevelofDifficulty, pageable);
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndQuestionScope(concernedStaff,
                                enumScope, pageable);

            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndQuestionScopeAndLevelofDifficulty(
                                concernedStaff, enumScope, enumLevelofDifficulty, pageable);
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndQuestionType(concernedStaff,
                                enumQuestionType, pageable);
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndQuestionTypeAndQuestionScope(
                                concernedStaff, enumQuestionType, enumScope, pageable);
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndQuestionTypeAndLevelofDifficulty(
                                concernedStaff, enumQuestionType, enumLevelofDifficulty, pageable);
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
                                concernedStaff, enumQuestionType, enumScope,
                                enumLevelofDifficulty, pageable);
            }
            srPageofQuestion.setAssociatedObject(pageofQuestion);
        }
        catch (IllegalArgumentException e) {
            srPageofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srPageofQuestion.setErrorMessage("IllegalArgumentException");
            srPageofQuestion.setMoreDetails(e.getMessage());
        }


        return srPageofQuestion;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofCourseAndStaffBy(String staffId,
                                                                            String courseId,
                                                                            String questionType,
                                                                            String questionScope,
                                                                            String levelofDifficulty,
                                                                            String sortBy,
                                                                            String direction)
            throws StaffNotFoundException, CourseNotFoundException {
        staffId = staffId.trim();
        courseId = courseId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id precised does not match any course " +
                    "in the system");
        }
        Course concernedCourse = srCourse.getAssociatedObject();

        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseOrderByTitleAsc(
                                        concernedStaff, concernedCourse);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseOrderByTitleDesc(
                                        concernedStaff, concernedCourse);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedCourse,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }



        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofCourseAndStaffBy(String staffId,
                                                                            String courseId,
                                                                            String questionType,
                                                                            String questionScope,
                                                                            String levelofDifficulty,
                                                                            Pageable pageable)
            throws StaffNotFoundException, CourseNotFoundException {
        staffId = staffId.trim();
        courseId = courseId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();

        ServerResponse<Page<Question>> srPageofQuestion = new ServerResponse<>();
        srPageofQuestion.setErrorMessage("The page list of the precised staff is " +
                "successfully found");
        srPageofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id precised does not match any course " +
                    "in the system");
        }
        Course concernedCourse = srCourse.getAssociatedObject();

        try{

            Page<Question> pageofQuestion = null;

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourse(
                                concernedStaff, concernedCourse, pageable);

            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourseAndLevelofDifficulty(
                                concernedStaff, concernedCourse, enumLevelofDifficulty, pageable);

            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourseAndQuestionScope(
                                concernedStaff, concernedCourse, enumScope, pageable);

            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourseAndQuestionScopeAndLevelofDifficulty(
                                concernedStaff, concernedCourse, enumScope, enumLevelofDifficulty,
                                pageable);

            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourseAndQuestionType(
                                concernedStaff, concernedCourse, enumQuestionType, pageable);

            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScope(
                                concernedStaff, concernedCourse, enumQuestionType, enumScope, pageable);

            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndLevelofDifficulty(
                                concernedStaff, concernedCourse, enumQuestionType,
                                enumLevelofDifficulty, pageable);

            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                pageofQuestion = questionRepository.
                        findAllByOwnerStaffAndConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficulty(
                                concernedStaff, concernedCourse, enumQuestionType, enumScope,
                                enumLevelofDifficulty, pageable);

            }
            srPageofQuestion.setAssociatedObject(pageofQuestion);
        }
        catch (IllegalArgumentException e) {
            srPageofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srPageofQuestion.setErrorMessage("IllegalArgumentException");
            srPageofQuestion.setMoreDetails(e.getMessage());
        }



        return srPageofQuestion;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofCourseBy(String courseId,
                                                                    String questionType,
                                                                    String questionScope,
                                                                    String levelofDifficulty,
                                                                    String sortBy,
                                                                    String direction)
            throws CourseNotFoundException {
        courseId = courseId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id precised does not match any course " +
                    "in the system");
        }
        Course concernedCourse = srCourse.getAssociatedObject();


        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseOrderByTitleAsc(
                                        concernedCourse);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseOrderByTitleDesc(
                                        concernedCourse);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionScopeOrderByTitleAsc(
                                        concernedCourse, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionScopeOrderByTitleDesc(
                                        concernedCourse,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeOrderByTitleAsc(
                                        concernedCourse, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeOrderByTitleDesc(
                                        concernedCourse, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedCourse, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedCourse, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedCourseAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofCourseBy(String courseId,
                                                                    String questionType,
                                                                    String questionScope,
                                                                    String levelofDifficulty,
                                                                    Pageable pageable)
            throws CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofModuleBy(String moduleId,
                                                                    String questionType,
                                                                    String questionScope,
                                                                    String levelofDifficulty,
                                                                    String sortBy,
                                                                    String direction)
            throws ModuleNotFoundException {
        moduleId = moduleId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Module> srModule = moduleService.findModuleOfCourseOutlineById(moduleId);
        if(srModule.getResponseCode() != ResponseCode.MODULE_FOUND){
            throw new ModuleNotFoundException("The module id precised does not match any module " +
                    "in the system");
        }
        Module concernedModule = srModule.getAssociatedObject();


        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleOrderByTitleAsc(
                                        concernedModule);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleOrderByTitleDesc(
                                        concernedModule);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionScopeOrderByTitleAsc(
                                        concernedModule, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionScopeOrderByTitleDesc(
                                        concernedModule,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeOrderByTitleAsc(
                                        concernedModule, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeOrderByTitleDesc(
                                        concernedModule, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedModule, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedModule, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofModuleBy(String moduleId,
                                                                    String questionType,
                                                                    String questionScope,
                                                                    String levelofDifficulty,
                                                                    Pageable pageable)
            throws ModuleNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofChapterBy(String chapterId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     String sortBy,
                                                                     String direction)
            throws ChapterNotFoundException {
        chapterId = chapterId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Chapter> srChapter = chapterService.findChapterOfModuleById(chapterId);
        if(srChapter.getResponseCode() != ResponseCode.CHAPTER_FOUND){
            throw new ChapterNotFoundException("The chapter id precised does not match any chapter " +
                    "in the system");
        }
        Chapter concernedChapter = srChapter.getAssociatedObject();


        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterOrderByTitleAsc(
                                        concernedChapter);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterOrderByTitleDesc(
                                        concernedChapter);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionScopeOrderByTitleAsc(
                                        concernedChapter, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionScopeOrderByTitleDesc(
                                        concernedChapter,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeOrderByTitleAsc(
                                        concernedChapter, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeOrderByTitleDesc(
                                        concernedChapter, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedChapter, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedChapter, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofChapterBy(String chapterId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     Pageable pageable)
            throws ChapterNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSectionBy(String sectionId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     String sortBy,
                                                                     String direction)
            throws SectionNotFoundException {
        sectionId = sectionId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Section> srSection = sectionService.findSectionOfChapterById(sectionId);
        if(srSection.getResponseCode() != ResponseCode.SECTION_FOUND){
            throw new SectionNotFoundException("The section id precised does not match any section " +
                    "in the system");
        }
        Section concernedSection = srSection.getAssociatedObject();


        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionOrderByTitleAsc(
                                        concernedSection);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionOrderByTitleDesc(
                                        concernedSection);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionScopeOrderByTitleAsc(
                                        concernedSection, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionScopeOrderByTitleDesc(
                                        concernedSection,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeOrderByTitleAsc(
                                        concernedSection, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeOrderByTitleDesc(
                                        concernedSection, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedSection, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedSection, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofSectionBy(String sectionId,
                                                                     String questionType,
                                                                     String questionScope,
                                                                     String levelofDifficulty,
                                                                     Pageable pageable)
            throws SectionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSubSectionBy(String subSectionId,
                                                                        String questionType,
                                                                        String questionScope,
                                                                        String levelofDifficulty,
                                                                        String sortBy,
                                                                        String direction)
            throws SubSectionNotFoundException {
        subSectionId = subSectionId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<SubSection> srSubSection = subSectionService.findSubSectionOfSectionById(
                subSectionId);
        if(srSubSection.getResponseCode() != ResponseCode.SUBSECTION_FOUND){
            throw new SubSectionNotFoundException("The subSection id precised does not match " +
                    "any subSection " +
                    "in the system");
        }
        SubSection concernedSubSection = srSubSection.getAssociatedObject();


        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionOrderByTitleAsc(
                                        concernedSubSection);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionOrderByTitleDesc(
                                        concernedSubSection);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionScopeOrderByTitleAsc(
                                        concernedSubSection, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionScopeOrderByTitleDesc(
                                        concernedSubSection,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeOrderByTitleAsc(
                                        concernedSubSection, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeOrderByTitleDesc(
                                        concernedSubSection, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedSubSection, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedSubSection, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofSubSectionBy(String subSectionId,
                                                                        String questionType,
                                                                        String questionScope,
                                                                        String levelofDifficulty,
                                                                        Pageable pageable)
            throws SubSectionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofParagraphBy(String paragraphId,
                                                                       String questionType,
                                                                       String questionScope,
                                                                       String levelofDifficulty,
                                                                       String sortBy,
                                                                       String direction)
            throws ParagraphNotFoundException {
        paragraphId = paragraphId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Paragraph> srParagraph = paragraphService.findParagraphOfSubSectionById(
                paragraphId);
        if(srParagraph.getResponseCode() != ResponseCode.PARAGRAPH_FOUND){
            throw new ParagraphNotFoundException("The paragraph id precised does not match " +
                    "any paragraph " +
                    "in the system");
        }
        Paragraph concernedParagraph = srParagraph.getAssociatedObject();


        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphOrderByTitleAsc(
                                        concernedParagraph);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphOrderByTitleDesc(
                                        concernedParagraph);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionScopeOrderByTitleAsc(
                                        concernedParagraph, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionScopeOrderByTitleDesc(
                                        concernedParagraph,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeOrderByTitleAsc(
                                        concernedParagraph, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeOrderByTitleDesc(
                                        concernedParagraph, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedParagraph, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedParagraph, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofParagraphBy(String paragraphId,
                                                                       String questionType,
                                                                       String questionScope,
                                                                       String levelofDifficulty,
                                                                       Pageable pageable)
            throws ParagraphNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofModuleAndStaffBy(String moduleId,
                                                                            String staffId,
                                                                            String questionType,
                                                                            String questionScope,
                                                                            String levelofDifficulty,
                                                                            String sortBy,
                                                                            String direction)

            throws ModuleNotFoundException, StaffNotFoundException {
        moduleId = moduleId.trim();
        staffId = staffId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Module> srModule = moduleService.findModuleOfCourseOutlineById(moduleId);
        if(srModule.getResponseCode() != ResponseCode.MODULE_FOUND){
            throw new ModuleNotFoundException("The module id precised does not match any module " +
                    "in the system");
        }
        Module concernedModule = srModule.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleOrderByTitleAsc(
                                        concernedStaff,
                                        concernedModule);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleOrderByTitleDesc(
                                        concernedStaff,
                                        concernedModule);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedModule,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedModuleAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofModuleAndStaffBy(String moduleId,
                                                                            String staffId,
                                                                            String questionType,
                                                                            String questionScope,
                                                                            String levelofDifficulty,
                                                                            Pageable pageable)
            throws ModuleNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofChapterAndStaffBy(String chapterId,
                                                                             String staffId,
                                                                             String questionType,
                                                                             String questionScope,
                                                                             String levelofDifficulty,
                                                                             String sortBy,
                                                                             String direction)
            throws ChapterNotFoundException, StaffNotFoundException {
        chapterId = chapterId.trim();
        staffId = staffId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Chapter> srChapter = chapterService.findChapterOfModuleById(chapterId);
        if(srChapter.getResponseCode() != ResponseCode.CHAPTER_FOUND){
            throw new ChapterNotFoundException("The chapter id precised does not match any chapter " +
                    "in the system");
        }
        Chapter concernedChapter = srChapter.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterOrderByTitleAsc(
                                        concernedStaff,
                                        concernedChapter);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterOrderByTitleDesc(
                                        concernedStaff,
                                        concernedChapter);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedChapter,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedChapterAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofChapterAndStaffBy(String chapterId,
                                                                             String staffId,
                                                                             String questionType,
                                                                             String questionScope,
                                                                             String levelofDifficulty,
                                                                             Pageable pageable)
            throws ChapterNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSectionAndStaffBy(String sectionId,
                                                                             String staffId,
                                                                             String questionType,
                                                                             String questionScope,
                                                                             String levelofDifficulty,
                                                                             String sortBy,
                                                                             String direction)
            throws SectionNotFoundException, StaffNotFoundException {
        sectionId = sectionId.trim();
        staffId = staffId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Section> srSection = sectionService.findSectionOfChapterById(sectionId);
        if(srSection.getResponseCode() != ResponseCode.SECTION_FOUND){
            throw new SectionNotFoundException("The section id precised does not match any section " +
                    "in the system");
        }
        Section concernedSection = srSection.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionOrderByTitleAsc(
                                        concernedStaff,
                                        concernedSection);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionOrderByTitleDesc(
                                        concernedStaff,
                                        concernedSection);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedSection,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofSectionAndStaffBy(String sectionId,
                                                                             String staffId,
                                                                             String questionType,
                                                                             String questionScope,
                                                                             String levelofDifficulty,
                                                                             Pageable pageable)
            throws SectionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSubSectionAndStaffBy(String subSectionId,
                                                                                String staffId,
                                                                                String questionType,
                                                                                String questionScope,
                                                                                String levelofDifficulty,
                                                                                String sortBy,
                                                                                String direction)
            throws SubSectionNotFoundException, StaffNotFoundException {
        subSectionId = subSectionId.trim();
        staffId = staffId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<SubSection> srSubSection = subSectionService.findSubSectionOfSectionById(
                subSectionId);
        if(srSubSection.getResponseCode() != ResponseCode.SUBSECTION_FOUND){
            throw new SubSectionNotFoundException("The subSection id precised does not match any " +
                    "subSection " +
                    "in the system");
        }
        SubSection concernedSubSection = srSubSection.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionOrderByTitleAsc(
                                        concernedStaff,
                                        concernedSubSection);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionOrderByTitleDesc(
                                        concernedStaff,
                                        concernedSubSection);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedSubSection,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofSubSectionAndStaffBy(String subSectionId,
                                                                                String staffId,
                                                                                String questionType,
                                                                                String questionScope,
                                                                                String levelofDifficulty,
                                                                                Pageable pageable)
            throws SubSectionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofParagraphAndStaffBy(String paragraphId,
                                                                               String staffId,
                                                                               String questionType,
                                                                               String questionScope,
                                                                               String levelofDifficulty,
                                                                               String sortBy,
                                                                               String direction)
            throws ParagraphNotFoundException, StaffNotFoundException {
        paragraphId = paragraphId.trim();
        staffId = staffId.trim();
        questionScope = questionScope.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Question>> srListofQuestion = new ServerResponse<>();
        srListofQuestion.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofQuestion.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Paragraph> srParagraph = paragraphService.findParagraphOfSubSectionById(
                paragraphId);
        if(srParagraph.getResponseCode() != ResponseCode.PARAGRAPH_FOUND){
            throw new ParagraphNotFoundException("The paragraph id precised does not match any " +
                    "paragraph " +
                    "in the system");
        }
        Paragraph concernedParagraph = srParagraph.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        try{

            List<Question> listofQuestion = new ArrayList<>();

            if(questionType == null && questionScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphOrderByTitleAsc(
                                        concernedStaff,
                                        concernedParagraph);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphOrderByTitleDesc(
                                        concernedStaff,
                                        concernedParagraph);
                    }
                }
            }
            else if(questionType == null && questionScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedParagraph,
                                        enumScope);
                    }
                }
            }
            else if(questionType == null && questionScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumScope, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumQuestionType);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumQuestionType);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty == null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumQuestionType, enumScope);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumQuestionType, enumScope);
                    }
                }
            }
            else if(questionType != null && questionScope == null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumQuestionType, enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumQuestionType, enumLevelofDifficulty);
                    }
                }
            }
            else if(questionType != null && questionScope != null && levelofDifficulty != null){
                EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                        questionType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofQuestion = questionRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndQuestionTypeAndQuestionScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumQuestionType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofQuestion.setAssociatedObject(listofQuestion);
        }
        catch (IllegalArgumentException e) {
            srListofQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofQuestion.setErrorMessage("IllegalArgumentException");
            srListofQuestion.setMoreDetails(e.getMessage());
        }




        return srListofQuestion;
    }

    @Override
    public ServerResponse<Page<Question>> findAllQuestionofParagraphAndStaffBy(String paragraphId,
                                                                               String staffId,
                                                                               String questionType,
                                                                               String questionScope,
                                                                               String levelofDifficulty,
                                                                               Pageable pageable)
            throws ParagraphNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Answer>> findAllAnswerofQuestionBy(String questionId,
                                                                  String answerScope,
                                                                  String sortBy,
                                                                  String direction)
            throws QuestionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Page<Answer>> findAllAnswerofQuestion(String questionId,
                                                                String answerScope,
                                                                Pageable pageable)
            throws QuestionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Answer>> findAllAnswerofQuestionAndStaffBy(String questionId,
                                                                          String staffId,
                                                                          String answerScope,
                                                                          String sortBy,
                                                                          String direction)
            throws QuestionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Page<Answer>> findAllAnswerofQuestionAndStaffBy(String questionId,
                                                                          String staffId,
                                                                          String answerScope,
                                                                          Pageable pageable)
            throws QuestionNotFoundException, StaffNotFoundException {
        return null;
    }


    @Override
    public Question saveQuestion(Question question){
        return questionRepository.save(question);
    }

    @Override
    public ServerResponse<Question> saveQuestion(String title,
                                                 String questionType,
                                                 String levelofDifficulty,
                                                 String questionScope,
                                                 String ownerStaffId, String courseId,
                                                 String concernedPartId, String problemId)
            throws ConcernedPartNotBelongingToCourseException,
            StaffNotFoundException, CourseNotFoundException {
        ServerResponse<Question> srQuestion = new ServerResponse<>(ResponseCode.QUESTION_NOT_CREATED);
        title = title.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        ownerStaffId = ownerStaffId.trim();
        courseId = courseId.trim();
        concernedPartId = concernedPartId.trim();
        problemId = problemId.trim();
        questionScope = questionScope.trim();

        Staff ownerStaff = null;
        Course concernedCourse = null;
        /*Problem concernedProblem = null;*/

        Module concernedModule = null;
        Chapter concernedChapter = null;
        Section concernedSection = null;
        SubSection concernedSubSection = null;
        Paragraph concernedParagraph = null;

        ServerResponse<Staff> srStaff = staffService.findStaffById(ownerStaffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        ownerStaff = srStaff.getAssociatedObject();

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id specified does not match any course " +
                    "in the system");
        }
        concernedCourse = srCourse.getAssociatedObject();

        /*ServerResponse<Problem> srProblem = problemService.findProblemById(problemId);
        if(srProblem.getResponseCode() == ResponseCode.PROBLEM_FOUND){
            concernedProblem = srProblem.getAssociatedObject();
        }*/

        /**
         * We must check if the concerned part precised belonging to the above course precised
         */
        boolean isBelongingToCourse = commonService.isConcernedPartBelongingToCourse(concernedPartId,
                concernedCourse);
        if(!isBelongingToCourse){
            throw new ConcernedPartNotBelongingToCourseException("The concerned part id specified does " +
                    "not a part of the course specified");
        }

        try {

            EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                    questionType.toUpperCase());
            EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                    levelofDifficulty.toUpperCase());
            EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());


            Optional<String> concernedPartName = commonService.concernedPartName(concernedPartId);
            if(concernedPartName.isPresent()){
                if(concernedPartName.get().equalsIgnoreCase("Module")){
                    Optional<Module> optionalModule = commonService.findModuleofCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalModule.isPresent()){
                        concernedModule = optionalModule.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("Chapter")){
                    Optional<Chapter> optionalChapter = commonService.findChapterOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalChapter.isPresent()){
                        concernedChapter = optionalChapter.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("Section")){
                    Optional<Section> optionalSection = commonService.findSectionOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalSection.isPresent()){
                        concernedSection = optionalSection.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("SubSection")){
                    Optional<SubSection> optionalSubSection = commonService.findSubSectionOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalSubSection.isPresent()){
                        concernedSubSection = optionalSubSection.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("Paragraph")){
                    Optional<Paragraph> optionalParagraph = commonService.findParagraphOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalParagraph.isPresent()){
                        concernedParagraph = optionalParagraph.get();
                    }
                }
            }
            List<Proposition> listofProposition = new ArrayList<Proposition>();
            List<Indication> listofIndication = new ArrayList<Indication>();
            List<Content> listofContent = new ArrayList<>();

            Question questionToSaved = new Question();
            questionToSaved.setTitle(title);
            questionToSaved.setConcernedChapter(concernedChapter);
            questionToSaved.setConcernedCourse(concernedCourse);
            questionToSaved.setConcernedModule(concernedModule);
            questionToSaved.setConcernedParagraph(concernedParagraph);
            /*questionToSaved.setConcernedProblem(concernedProblem);*/
            questionToSaved.setConcernedSection(concernedSection);
            questionToSaved.setConcernedSubSection(concernedSubSection);
            questionToSaved.setLevelofDifficulty(enumLevelofDifficulty);
            questionToSaved.setQuestionScope(enumScope);
            questionToSaved.setListofContent(listofContent);
            questionToSaved.setListofIndication(listofIndication);
            questionToSaved.setListofProposition(listofProposition);
            questionToSaved.setOwnerStaff(ownerStaff);
            questionToSaved.setQuestionType(enumQuestionType);

            Question questionSaved = questionRepository.save(questionToSaved);

            srQuestion.setErrorMessage("The question has been saved successfully");
            srQuestion.setResponseCode(ResponseCode.QUESTION_CREATED);
            srQuestion.setAssociatedObject(questionSaved);

        } catch (IllegalArgumentException e) {
            srQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_QUESTION_OR_LEVELOFDIFFICULTY_TYPE);
            srQuestion.setErrorMessage("IllegalArgumentException");
            srQuestion.setMoreDetails(e.getMessage());
        }

        return srQuestion;
    }

    @Override
    public ServerResponse<Question> duplicateQuestionForStaff(String questionId, String staffId)
            throws QuestionNotFoundException, StaffNotFoundException{
        ServerResponse<Question> srQuestion = new ServerResponse<>(ResponseCode.QUESTION_NOT_CREATED);
        questionId = questionId.trim();
        staffId = staffId.trim();

        ServerResponse<Question> srQuestionToDuplicate = this.findQuestionById(questionId);
        if(srQuestionToDuplicate.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id does not match any staff in the " +
                    "system");
        }
        Staff newOwnerStaff = srStaff.getAssociatedObject();
        Question questionToDuplicate = srQuestionToDuplicate.getAssociatedObject();

        Question questionCopy = new Question();
        questionCopy.setTitle(questionToDuplicate.getTitle());
        questionCopy.setConcernedChapter(questionToDuplicate.getConcernedChapter());
        questionCopy.setConcernedCourse(questionToDuplicate.getConcernedCourse());
        questionCopy.setConcernedModule(questionToDuplicate.getConcernedModule());
        questionCopy.setConcernedParagraph(questionToDuplicate.getConcernedParagraph());
        /*questionCopy.setConcernedProblem(questionToDuplicate.getConcernedProblem());*/
        questionCopy.setConcernedSection(questionToDuplicate.getConcernedSection());
        questionCopy.setConcernedSubSection(questionToDuplicate.getConcernedSubSection());
        questionCopy.setLevelofDifficulty(questionToDuplicate.getLevelofDifficulty());
        questionCopy.setListofContent(questionToDuplicate.getListofContent());
        questionCopy.setListofIndication(questionToDuplicate.getListofIndication());
        questionCopy.setListofProposition(questionToDuplicate.getListofProposition());
        questionCopy.setOwnerStaff(newOwnerStaff);
        questionCopy.setQuestionType(questionToDuplicate.getQuestionType());

        Question questionDuplicated = questionRepository.save(questionCopy);

        srQuestion.setErrorMessage("The question has been duplicated successfully");
        srQuestion.setResponseCode(ResponseCode.QUESTION_CREATED);
        srQuestion.setAssociatedObject(questionDuplicated);


        return srQuestion;
    }

    @Override
    public ServerResponse<Question> addContentToQuestion(String value, String contentType,
                                                         String questionId)
            throws QuestionNotFoundException {
        ServerResponse<Question> srQuestion = new ServerResponse<>();
        value = value.trim();
        contentType = contentType.trim();
        questionId = questionId.trim();
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question concernedQuestion = srQuestion1.getAssociatedObject();

        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);
            concernedQuestion.getListofContent().add(contentSaved);

            Question questionSaved = this.saveQuestion(concernedQuestion);
            srQuestion.setResponseCode(ResponseCode.CONTENT_ADDED);
            srQuestion.setErrorMessage("The content has been successfully added to the question");
            srQuestion.setAssociatedObject(questionSaved);

        } catch (IllegalArgumentException e) {
            srQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_CONTENT_TYPE);
            srQuestion.setErrorMessage("IllegalArgumentException");
            srQuestion.setMoreDetails(e.getMessage());
        }

        return srQuestion;
    }

    public Optional<Content> findContentInContentQuestionList(String contentId,
                                                                 Question question){
        Content contentSearch = null;
        for(Content content : question.getListofContent()){
            if(content.getId().equalsIgnoreCase(contentId)){
                contentSearch = content;
                break;
            }
        }
        return Optional.ofNullable(contentSearch);
    }

    @Override
    public ServerResponse<Question> updateContentToQuestion(String contentId, String value,
                                                            String questionId)
            throws ContentNotBelongingToException, QuestionNotFoundException {
        ServerResponse<Question> srQuestion = new ServerResponse<>(ResponseCode.CONTENT_NOT_UPDATED);
        contentId = contentId.trim();
        value = value.trim();
        questionId = questionId.trim();
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToUpdateContent = srQuestion1.getAssociatedObject();

        Optional<Content> optionalContentInQuestion = this.findContentInContentQuestionList(contentId,
                questionToUpdateContent);
        if(!optionalContentInQuestion.isPresent()){
            throw new ContentNotBelongingToException("The content id does not exist in the question");
        }
        Content contentToUpdate = optionalContentInQuestion.get();
        contentToUpdate.setValue(value);
        contentRepository.save(contentToUpdate);

        srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system after deleting his content");
        }
        Question questionUpdateContent = srQuestion1.getAssociatedObject();

        srQuestion.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srQuestion.setErrorMessage("The content has been successfully updated");
        srQuestion.setAssociatedObject(questionUpdateContent);

        return srQuestion;
    }

    @Override
    public ServerResponse<Question> removeContentToQuestion(String contentId, String questionId)
            throws ContentNotBelongingToException, QuestionNotFoundException {
        ServerResponse<Question> srQuestion = new ServerResponse<>(ResponseCode.CONTENT_NOT_DELETED);
        contentId = contentId.trim();
        questionId = questionId.trim();
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToDeleteContent = srQuestion1.getAssociatedObject();

        Optional<Content> optionalContentInQuestion = this.findContentInContentQuestionList(contentId,
                questionToDeleteContent);
        if(!optionalContentInQuestion.isPresent()){
            throw new ContentNotBelongingToException("The content id does not exist in the question");
        }

        contentRepository.delete(optionalContentInQuestion.get());

        ServerResponse<Question> srQuestionToDeleteContent = this.findQuestionById(questionId);
        if(srQuestionToDeleteContent.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system after deleting his content");
        }
        Question questionDeleteContent = srQuestion1.getAssociatedObject();

        srQuestion.setResponseCode(ResponseCode.CONTENT_DELETED);
        srQuestion.setErrorMessage("The content has been deleted in the question list successfully");
        srQuestion.setAssociatedObject(questionDeleteContent);

        return srQuestion;
    }

    @Override
    public ServerResponse<Question> addIndicationToQuestion(String value, String contentType,
                                                            String staffId, String questionId)
            throws QuestionNotFoundException, StaffNotFoundException {
        value = value.trim();
        contentType = contentType.trim();
        staffId = staffId.trim();
        questionId = questionId.trim();
        ServerResponse<Question> srQuestion = new ServerResponse<>();
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question concernedQuestion = srQuestion1.getAssociatedObject();

        ServerResponse<Indication> srIndicationAdded = indicationService.saveIndication(staffId,
                value, contentType);
        if(srIndicationAdded.getResponseCode() == ResponseCode.INDICATION_CREATED){
            Indication indicationToAddInQuestion = srIndicationAdded.getAssociatedObject();
            concernedQuestion.getListofIndication().add(indicationToAddInQuestion);

            Question questionSaved = this.saveQuestion(concernedQuestion);
            srQuestion.setResponseCode(ResponseCode.INDICATION_ADDED_TO_QUESTION);
            srQuestion.setErrorMessage("The indication has been successfully added to the question");
            srQuestion.setAssociatedObject(questionSaved);
        }

        return srQuestion;
    }

    public Optional<Indication> findIndicationInIndicationQuestionList(String indicationId,
                                                              Question question){
        Indication indicationSearch = null;
        for(Indication indication : question.getListofIndication()){
            if(indication.getId().equalsIgnoreCase(indicationId)){
                indicationSearch = indication;
                break;
            }
        }
        return Optional.ofNullable(indicationSearch);
    }

    @Override
    public ServerResponse<Question> updateIndicationToQuestion(String indicationId, String contentId,
                                                               String value, String questionId,
                                                               String staffId)
            throws IndicationNotFoundException, QuestionNotFoundException,
            IndicationNotBelongingToStaffException {
        ServerResponse<Question> srQuestion = new ServerResponse<>(
                ResponseCode.CONTENT_OF_INDICATION_NOT_UPDATED);
        indicationId = indicationId.trim();
        contentId = contentId.trim();
        value = value.trim();
        questionId = questionId.trim();
        staffId = staffId.trim();

        /****
         * We must find question in the system
         */
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToUpdateIndication = srQuestion1.getAssociatedObject();

        /****
         * We must check if the indication belonging to staff
         */
        Optional<Indication> optionalIndication =
                this.findIndicationInIndicationQuestionList(indicationId, questionToUpdateIndication);
        if(!optionalIndication.isPresent()){
            throw new IndicationNotFoundException("The indication id does not match any indication " +
                    "in the system");
        }
        Indication concernedIndication = optionalIndication.get();
        if(!concernedIndication.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new IndicationNotBelongingToStaffException("The staff precised is not granted to " +
                    "update this indication. he is not the owner. He can duplicate it before");
        }

        try {
            ServerResponse<Indication> srIndicationUpdate =
                    indicationService.updateContentToIndication(contentId, value, indicationId);
            if(srIndicationUpdate.getResponseCode() == ResponseCode.CONTENT_UPDATED){
                ServerResponse<Question> srQuestionIndicationUpdated =
                        this.findQuestionById(questionId);
                if(srQuestionIndicationUpdated.getResponseCode() != ResponseCode.QUESTION_FOUND){
                    throw new QuestionNotFoundException("The question id does not match any question in the " +
                            "system");
                }
                Question questionUpdateIndication = srQuestionIndicationUpdated.getAssociatedObject();

                srQuestion.setResponseCode(ResponseCode.CONTENT_OF_INDICATION_UPDATED);
                srQuestion.setErrorMessage("The indication (his content) has been updated successfully.");
                srQuestion.setAssociatedObject(questionUpdateIndication);
            }
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srQuestion.setErrorMessage("The content associated to the indication does not belonging " +
                    "to that indication precised");
            srQuestion.setResponseCode(ResponseCode.CONTENT_OF_INDICATION_EXCEPTION);
            srQuestion.setMoreDetails(e.getMessage());
        }

        return srQuestion;
    }

    @Override
    public ServerResponse<Question> removeIndicationToQuestion(String indicationId,
                                                               String questionId,
                                                               String staffId)
            throws IndicationNotFoundException, QuestionNotFoundException,
            IndicationNotBelongingToStaffException, StaffNotFoundException {
        indicationId = indicationId.trim();
        questionId = questionId.trim();
        staffId = staffId.trim();
        ServerResponse<Question> srQuestion = new ServerResponse<>(
                ResponseCode.INDICATION_OF_QUESTION_NOT_DELETED);

        /****
         * We must find question in the system
         */
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToRemoveIndication = srQuestion1.getAssociatedObject();

        /****
         * We must check if the indication belonging to staff
         */
        Optional<Indication> optionalIndication =
                this.findIndicationInIndicationQuestionList(indicationId, questionToRemoveIndication);
        if(!optionalIndication.isPresent()){
            throw new IndicationNotFoundException("The indication id does not match any indication " +
                    "in the system");
        }
        Indication concernedIndication = optionalIndication.get();

        /*****
         * We can call the function removeIndication(String indicationId, String staffId)
         * to remove the indication if that indication belonging to the precised staff in
         * parameter
         */
        ServerResponse<Indication> srIndicationRemove = indicationService.removeIndication(
                concernedIndication.getId(), staffId);
        if(srIndicationRemove.getResponseCode() == ResponseCode.INDICATION_DELETED){
            srQuestion.setResponseCode(ResponseCode.INDICATION_OF_QUESTION_DELETED);
            srQuestion.setErrorMessage("The indication has been remove to the question successfully");
        }
        else{
            srQuestion.setResponseCode(ResponseCode.INDICATION_OF_QUESTION_NOT_DELETED);
            srQuestion.setErrorMessage("The indication has not been remove to the question");
        }

        /****
         * We must find question again in the system to actualize
         */
        srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToRemoveIndicationActualize = srQuestion1.getAssociatedObject();

        srQuestion.setAssociatedObject(questionToRemoveIndicationActualize);

        return srQuestion;
    }

    @Override
    public ServerResponse<Question> addPropositionToQuestion(boolean valid, String value,
                                                             String contentType, String staffId,
                                                             String questionId)
            throws QuestionNotFoundException, StaffNotFoundException {
        value = value.trim();
        contentType = contentType.trim();
        staffId = staffId.trim();
        questionId = questionId.trim();

        ServerResponse<Question> srQuestion = new ServerResponse<>(
                ResponseCode.PROPOSITION_NOT_ADDED_TO_QUESTION);

        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question concernedQuestion = srQuestion1.getAssociatedObject();
        ////////////////
        ServerResponse<Proposition> srPropositionAdded = propositionService.saveProposition(staffId,
                value, contentType, valid);
        if(srPropositionAdded.getResponseCode() == ResponseCode.PARAGRAPH_CREATED){
            Proposition propositionToAddInQuestion = srPropositionAdded.getAssociatedObject();
            concernedQuestion.getListofProposition().add(propositionToAddInQuestion);

            Question questionSaved = this.saveQuestion(concernedQuestion);
            srQuestion.setResponseCode(ResponseCode.PROPOSITION_ADDED_TO_QUESTION);
            srQuestion.setErrorMessage("The proposition has been successfully added to the question");
            srQuestion.setAssociatedObject(questionSaved);
        }

        ////////////////
        return srQuestion;
    }

    public Optional<Proposition> findPropositionInPropositionQuestionList(String propositionId,
                                                                       Question question){
        Proposition propositionSearch = null;
        for(Proposition proposition : question.getListofProposition()){
            if(proposition.getId().equalsIgnoreCase(propositionId)){
                propositionSearch = proposition;
                break;
            }
        }
        return Optional.ofNullable(propositionSearch);
    }

    @Override
    public ServerResponse<Question> updatePropositionToQuestion(String propositionId,
                                                                boolean valid, String contentId,
                                                                String value, String questionId,
                                                                String staffId)
            throws PropositionNotFoundException, QuestionNotFoundException,
            PropositionNotBelongingToStaffException {

        ServerResponse<Question> srQuestion = new ServerResponse<>(ResponseCode.CONTENT_OF_PROPOSITION_NOT_UPDATED);

        propositionId = propositionId.trim();
        contentId = contentId.trim();
        value = value.trim();
        questionId = questionId.trim();
        staffId = staffId.trim();

        /****
         * We must find question in the system
         */
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToUpdateProposition = srQuestion1.getAssociatedObject();

        /****
         * We must check if the proposition belonging to staff
         */
        Optional<Proposition> optionalProposition =
                this.findPropositionInPropositionQuestionList(propositionId, questionToUpdateProposition);
        if(!optionalProposition.isPresent()){
            throw new PropositionNotFoundException("The proposition id does not match any proposition " +
                    "in the system");
        }
        Proposition concernedProposition = optionalProposition.get();
        if(!concernedProposition.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new PropositionNotBelongingToStaffException("The staff precised is not granted to " +
                    "update this proposition. he is not the owner. He can duplicate it before");
        }

        //////////////////////
        try {
            ServerResponse<Proposition> srPropositionUpdate =
                    propositionService.updateContentToProposition(contentId, value, propositionId);
            if(srPropositionUpdate.getResponseCode() == ResponseCode.CONTENT_UPDATED){
                ServerResponse<Question> srQuestionPropositionUpdated =
                        this.findQuestionById(questionId);
                if(srQuestionPropositionUpdated.getResponseCode() != ResponseCode.QUESTION_FOUND){
                    throw new QuestionNotFoundException("The question id does not match any question in the " +
                            "system");
                }
                Question questionUpdateProposition = srQuestionPropositionUpdated.getAssociatedObject();

                srQuestion.setResponseCode(ResponseCode.CONTENT_OF_PROPOSITION_UPDATED);
                srQuestion.setErrorMessage("The proposition (his content) has been updated successfully.");
                srQuestion.setAssociatedObject(questionUpdateProposition);
            }
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srQuestion.setErrorMessage("The content associated to the proposition does not belonging " +
                    "to that proposition precised");
            srQuestion.setResponseCode(ResponseCode.CONTENT_OF_PROPOSITION_EXCEPTION);
            srQuestion.setMoreDetails(e.getMessage());
        }
        //////////////////////

        return srQuestion;
    }

    @Override
    public ServerResponse<Question> removePropositionToQuestion(String propositionId,
                                                                String questionId,
                                                                String staffId)
            throws PropositionNotFoundException, QuestionNotFoundException,
            PropositionNotBelongingToStaffException, StaffNotFoundException {
        propositionId = propositionId.trim();
        questionId = questionId.trim();
        staffId = staffId.trim();
        ServerResponse<Question> srQuestion = new ServerResponse<>(
                ResponseCode.PROPOSITION_OF_QUESTION_NOT_DELETED);

        /****
         * We must find question in the system
         */
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToRemoveProposition = srQuestion1.getAssociatedObject();

        /****
         * We must check if the proposition belonging to staff
         */
        Optional<Proposition> optionalProposition =
                this.findPropositionInPropositionQuestionList(propositionId, questionToRemoveProposition);
        if(!optionalProposition.isPresent()){
            throw new PropositionNotFoundException("The proposition id does not match any proposition " +
                    "in the system");
        }
        Proposition concernedProposition = optionalProposition.get();

        /*****
         * We can call the function removeProposition(String propositionId, String staffId)
         * to remove the proposition if that proposition belonging to the precised staff in
         * parameter
         */
        ServerResponse<Proposition> srPropositionRemove = propositionService.removeProposition(
                concernedProposition.getId(), staffId);
        if(srPropositionRemove.getResponseCode() == ResponseCode.PROPOSITION_DELETED){
            srQuestion.setResponseCode(ResponseCode.PROPOSITION_OF_QUESTION_DELETED);
            srQuestion.setErrorMessage("The proposition has been remove to the question successfully");
        }
        else{
            srQuestion.setResponseCode(ResponseCode.PROPOSITION_OF_QUESTION_NOT_DELETED);
            srQuestion.setErrorMessage("The proposition has not been remove to the question");
        }

        /****
         * We must find question again in the system to actualize it
         */
        srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToRemovePropositionActualize = srQuestion1.getAssociatedObject();
        srQuestion.setAssociatedObject(questionToRemovePropositionActualize);

        return srQuestion;
    }

    @Override
    public ServerResponse<Answer> addAnswerToQuestion(String title,
                                                      String answerScope,
                                                      String value,
                                                      String contentType,
                                                      String questionId,
                                                      String staffId)
            throws QuestionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Answer> duplicateAnswerForStaff(String answerId,
                                                          String staffId)
            throws AnswerNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Answer> updateAnswerToQuestion(String answerId,
                                                         String title,
                                                         String answerScope,
                                                         String value,
                                                         String contentType,
                                                         String questionId,
                                                         String staffId)
            throws AnswerNotFoundException, QuestionNotFoundException,
            StaffNotFoundException, AnswerNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Answer> removeAnswerToQuestion(String answerId,
                                                         String questionId,
                                                         String staffId)
            throws AnswerNotFoundException, QuestionNotFoundException,
            StaffNotFoundException, AnswerNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Question> updateQuestion(String questionId,
                                                   String newTitle,
                                                   String questionType,
                                                   String levelofDifficulty,
                                                   String questionScope,
                                                   String courseId,
                                                   String concernedPartId,
                                                   String staffId)
            throws QuestionNotFoundException, ConcernedPartNotBelongingToCourseException,
            QuestionNotBelongingToStaffException, StaffNotFoundException,
            CourseNotFoundException {

        ServerResponse<Question> srQuestion = new ServerResponse<>(ResponseCode.QUESTION_NOT_UPDATED);
        newTitle = newTitle.trim();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        staffId = staffId.trim();
        courseId = courseId.trim();
        concernedPartId = concernedPartId.trim();
        questionScope = questionScope.trim();

        Staff ownerStaff = null;
        Course concernedCourse = null;
        Problem concernedProblem = null;

        Module concernedModule = null;
        Chapter concernedChapter = null;
        Section concernedSection = null;
        SubSection concernedSubSection = null;
        Paragraph concernedParagraph = null;

        /****
         * We must find question in the system
         */
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToUpdate = srQuestion1.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        ownerStaff = srStaff.getAssociatedObject();

        if(questionToUpdate.getOwnerStaff().getId() != ownerStaff.getId()){
            throw new QuestionNotBelongingToStaffException("The precised staff can not updated the " +
                    "question because he does not have permission. he can duplicated it first ");
        }

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id specified does not match any course " +
                    "in the system");
        }
        concernedCourse = srCourse.getAssociatedObject();

        /**
         * We must check if the concerned part precised belonging to the above course precised
         */
        boolean isBelongingToCourse = commonService.isConcernedPartBelongingToCourse(concernedPartId,
                concernedCourse);
        if(!isBelongingToCourse){
            throw new ConcernedPartNotBelongingToCourseException("The concerned part id specified does " +
                    "not a part of the course specified");
        }

        ////////
        try {

            EnumQuestionType enumQuestionType = EnumQuestionType.valueOf(
                    questionType.toUpperCase());
            EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                    levelofDifficulty.toUpperCase());
            EnumScope enumScope = EnumScope.valueOf(questionScope.toUpperCase());


            Optional<String> concernedPartName = commonService.concernedPartName(concernedPartId);
            if(concernedPartName.isPresent()){
                if(concernedPartName.get().equalsIgnoreCase("Module")){
                    Optional<Module> optionalModule = commonService.findModuleofCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalModule.isPresent()){
                        concernedModule = optionalModule.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("Chapter")){
                    Optional<Chapter> optionalChapter = commonService.findChapterOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalChapter.isPresent()){
                        concernedChapter = optionalChapter.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("Section")){
                    Optional<Section> optionalSection = commonService.findSectionOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalSection.isPresent()){
                        concernedSection = optionalSection.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("SubSection")){
                    Optional<SubSection> optionalSubSection = commonService.findSubSectionOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalSubSection.isPresent()){
                        concernedSubSection = optionalSubSection.get();
                    }
                } else if(concernedPartName.get().equalsIgnoreCase("Paragraph")){
                    Optional<Paragraph> optionalParagraph = commonService.findParagraphOfCourse(concernedPartId,
                            concernedCourse.getId());
                    if(optionalParagraph.isPresent()){
                        concernedParagraph = optionalParagraph.get();
                    }
                }
            }

            questionToUpdate.setTitle(newTitle);
            questionToUpdate.setConcernedChapter(concernedChapter);
            questionToUpdate.setConcernedCourse(concernedCourse);
            questionToUpdate.setConcernedModule(concernedModule);
            questionToUpdate.setConcernedParagraph(concernedParagraph);
            /*questionToUpdate.setConcernedProblem(concernedProblem);*/
            questionToUpdate.setConcernedSection(concernedSection);
            questionToUpdate.setConcernedSubSection(concernedSubSection);
            questionToUpdate.setLevelofDifficulty(enumLevelofDifficulty);
            questionToUpdate.setQuestionScope(enumScope);
            questionToUpdate.setQuestionType(enumQuestionType);

            Question questionUpdated = questionRepository.save(questionToUpdate);

            srQuestion.setErrorMessage("The question has been updated successfully");
            srQuestion.setResponseCode(ResponseCode.QUESTION_UPDATED);
            srQuestion.setAssociatedObject(questionUpdated);

        } catch (IllegalArgumentException e) {
            srQuestion.setResponseCode(ResponseCode.EXCEPTION_ENUM_QUESTION_OR_LEVELOFDIFFICULTY_TYPE);
            srQuestion.setErrorMessage("IllegalArgumentException");
            srQuestion.setMoreDetails(e.getMessage());
        }
        ////////

        return srQuestion;
    }

    @Override
    public ServerResponse<Question> deleteQuestion(String questionId, String staffId)
            throws QuestionNotFoundException, QuestionNotBelongingToStaffException,
            IndicationNotFoundException, PropositionNotFoundException {

        questionId = questionId.trim();
        staffId = staffId.trim();
        /****
         * We must find question in the system
         */
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToDelete = srQuestion1.getAssociatedObject();

        if(questionToDelete.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new QuestionNotBelongingToStaffException("The precised question does not " +
                    "belonging to the precised staff and then the cancelation can not been done " +
                    "by the precised staff");
        }
        /***
         * if we are here then all the conditions are filled to delete the question
         */
        return this.deleteQuestion(questionId);
    }

    @Override
    public ServerResponse<Question> deleteQuestion(String questionId)
            throws QuestionNotFoundException, IndicationNotFoundException,
            PropositionNotFoundException {
        ServerResponse<Question> srQuestion = new ServerResponse<>(ResponseCode.QUESTION_NOT_DELETED);
        questionId = questionId.trim();
        /****
         * We must find question in the system
         */
        ServerResponse<Question> srQuestion1 = this.findQuestionById(questionId);
        if(srQuestion1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToDelete = srQuestion1.getAssociatedObject();
        /****
         * To delete a question all the associated object must be deleted means
         * the indication list, the proposition list and the content list
         */
        for(Content content : questionToDelete.getListofContent()){
            contentRepository.delete(content);
        }
        for (Indication indication: questionToDelete.getListofIndication()){
            indicationService.removeIndication(indication.getId());
        }
        for (Proposition proposition: questionToDelete.getListofProposition()){
            propositionService.removeProposition(proposition.getId());
        }

        questionRepository.delete(questionToDelete);

        srQuestion.setResponseCode(ResponseCode.QUESTION_DELETED);
        srQuestion.setErrorMessage("The question has been deleted successfully");
        srQuestion.setAssociatedObject(questionToDelete);
        return srQuestion;
    }
}
