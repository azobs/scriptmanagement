package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ProblemRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.commonused.CommonService;
import com.sprintgether.script.management.server.scriptmanagement.service.program.*;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProblemServiceImpl implements ProblemService {

    ProblemRepository problemRepository;
    ContentRepository contentRepository;
    StaffService staffService;
    IndicationService indicationService;
    CommonService commonService;
    CourseService courseService;
    ModuleService moduleService;
    ChapterService chapterService;
    SectionService sectionService;
    SubSectionService subSectionService;
    ParagraphService paragraphService;
    QuestionService questionService;

    public ProblemServiceImpl(ProblemRepository problemRepository,
                              ContentRepository contentRepository,
                              StaffService staffService,
                              IndicationService indicationService,
                              CommonService commonService,
                              CourseService courseService,
                              ModuleService moduleService,
                              ChapterService chapterService,
                              SectionService sectionService,
                              SubSectionService subSectionService,
                              ParagraphService paragraphService,
                              QuestionService questionService) {
        this.problemRepository = problemRepository;
        this.contentRepository = contentRepository;
        this.staffService = staffService;
        this.indicationService = indicationService;
        this.commonService = commonService;
        this.courseService = courseService;
        this.moduleService = moduleService;
        this.chapterService = chapterService;
        this.sectionService = sectionService;
        this.subSectionService = subSectionService;
        this.paragraphService = paragraphService;
        this.questionService = questionService;
    }

    @Override
    public ServerResponse<Problem> findProblemById(String problemId) {
        problemId = problemId.trim();
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.PROBLEM_NOT_FOUND);
        srProblem.setErrorMessage("The problem does not found in the system with that problemID");
        Optional<Problem> optionalProblem = problemRepository.findById(problemId);
        if(optionalProblem.isPresent()){
            srProblem.setResponseCode(ResponseCode.PROBLEM_FOUND);
            srProblem.setErrorMessage("The problem is found in the system");
            srProblem.setAssociatedObject(optionalProblem.get());
        }

        return srProblem;
    }

    @Override
    public ServerResponse<List<Indication>> findAllIndicationofProblem(String problemId)
            throws ProblemNotFoundException {
        problemId = problemId.trim();
        ServerResponse<Problem> srProblem = this.findProblemById(problemId);
        if(srProblem.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problem = srProblem.getAssociatedObject();

        ServerResponse<List<Indication>> srListofIndication = new ServerResponse<>(ResponseCode.INITIALISATION);
        srListofIndication.setErrorMessage("The indication list of the precised problem is " +
                "successfully found");
        srListofIndication.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srListofIndication.setAssociatedObject(problem.getListofIndication());

        return srListofIndication;
    }


    @Override
    public ServerResponse<List<Content>> findAllContentofProblem(String problemId)
            throws ProblemNotFoundException {
        problemId = problemId.trim();
        ServerResponse<Problem> srProblem = this.findProblemById(problemId);
        if(srProblem.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problem = srProblem.getAssociatedObject();

        ServerResponse<List<Content>> srListofContent = new ServerResponse<>();
        srListofContent.setErrorMessage("The content list of the precised problem is " +
                "successfully found");
        srListofContent.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srListofContent.setAssociatedObject(problem.getListofContent());

        return srListofContent;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofStaffBy(String staffId,
                                                                 String problemType,
                                                                 String problemScope,
                                                                 String levelofDifficulty,
                                                                 String sortBy,
                                                                 String direction)
            throws StaffNotFoundException {
        staffId = staffId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();

        try{

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.findAllByOwnerStaffOrderByTitleAsc(
                                concernedStaff);
                    } else {
                        listofProblem = problemRepository.findAllByOwnerStaffOrderByTitleDesc(
                                concernedStaff);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemScopeOrderByTitleAsc(concernedStaff,
                                        enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemScopeOrderByTitleDesc(concernedStaff,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeOrderByTitleAsc(concernedStaff,
                                        enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeOrderByTitleDesc(concernedStaff,
                                        enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }



        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofCourseAndStaffBy(String courseId,
                                                                          String staffId,
                                                                          String problemType,
                                                                          String problemScope,
                                                                          String levelofDifficulty,
                                                                          String sortBy,
                                                                          String direction)
            throws StaffNotFoundException, CourseNotFoundException {

        staffId = staffId.trim();
        courseId = courseId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

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

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseOrderByTitleAsc(
                                        concernedStaff, concernedCourse);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseOrderByTitleDesc(
                                        concernedStaff, concernedCourse);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedCourse,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }



        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofCourseBy(String courseId,
                                                                  String problemType,
                                                                  String problemScope,
                                                                  String levelofDifficulty,
                                                                  String sortBy,
                                                                  String direction)
            throws CourseNotFoundException {
        courseId = courseId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id precised does not match any course " +
                    "in the system");
        }
        Course concernedCourse = srCourse.getAssociatedObject();


        try{

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseOrderByTitleAsc(
                                        concernedCourse);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseOrderByTitleDesc(
                                        concernedCourse);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemScopeOrderByTitleAsc(
                                        concernedCourse, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemScopeOrderByTitleDesc(
                                        concernedCourse,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeOrderByTitleAsc(
                                        concernedCourse, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeOrderByTitleDesc(
                                        concernedCourse, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedCourse, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedCourse, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedCourseAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofModuleBy(String moduleId,
                                                                  String problemType,
                                                                  String problemScope,
                                                                  String levelofDifficulty,
                                                                  String sortBy,
                                                                  String direction)
            throws ModuleNotFoundException {
        moduleId = moduleId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Module> srModule = moduleService.findModuleOfCourseOutlineById(moduleId);
        if(srModule.getResponseCode() != ResponseCode.MODULE_FOUND){
            throw new ModuleNotFoundException("The module id precised does not match any module " +
                    "in the system");
        }
        Module concernedModule = srModule.getAssociatedObject();


        try{

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleOrderByTitleAsc(
                                        concernedModule);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleOrderByTitleDesc(
                                        concernedModule);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemScopeOrderByTitleAsc(
                                        concernedModule, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemScopeOrderByTitleDesc(
                                        concernedModule,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeOrderByTitleAsc(
                                        concernedModule, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeOrderByTitleDesc(
                                        concernedModule, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedModule, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedModule, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedModule, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedModule, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofChapterBy(String chapterId,
                                                                   String problemType,
                                                                   String problemScope,
                                                                   String levelofDifficulty,
                                                                   String sortBy,
                                                                   String direction)
            throws ChapterNotFoundException {
        chapterId = chapterId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Chapter> srChapter = chapterService.findChapterOfModuleById(chapterId);
        if(srChapter.getResponseCode() != ResponseCode.CHAPTER_FOUND){
            throw new ChapterNotFoundException("The chapter id precised does not match any chapter " +
                    "in the system");
        }
        Chapter concernedChapter = srChapter.getAssociatedObject();


        try{

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterOrderByTitleAsc(
                                        concernedChapter);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterOrderByTitleDesc(
                                        concernedChapter);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemScopeOrderByTitleAsc(
                                        concernedChapter, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemScopeOrderByTitleDesc(
                                        concernedChapter,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeOrderByTitleAsc(
                                        concernedChapter, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeOrderByTitleDesc(
                                        concernedChapter, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedChapter, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedChapter, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedChapter, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedChapter, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSectionBy(String sectionId,
                                                                   String problemType,
                                                                   String problemScope,
                                                                   String levelofDifficulty,
                                                                   String sortBy,
                                                                   String direction)
            throws SectionNotFoundException {
        sectionId = sectionId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The Problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Section> srSection = sectionService.findSectionOfChapterById(sectionId);
        if(srSection.getResponseCode() != ResponseCode.SECTION_FOUND){
            throw new SectionNotFoundException("The section id precised does not match any section " +
                    "in the system");
        }
        Section concernedSection = srSection.getAssociatedObject();


        try{

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionOrderByTitleAsc(
                                        concernedSection);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionOrderByTitleDesc(
                                        concernedSection);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemScopeOrderByTitleAsc(
                                        concernedSection, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemScopeOrderByTitleDesc(
                                        concernedSection,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeOrderByTitleAsc(
                                        concernedSection, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeOrderByTitleDesc(
                                        concernedSection, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedSection, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedSection, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSubSectionBy(String subSectionId,
                                                                      String problemType,
                                                                      String problemScope,
                                                                      String levelofDifficulty,
                                                                      String sortBy,
                                                                      String direction)
            throws SubSectionNotFoundException {
        subSectionId = subSectionId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<SubSection> srSubSection = subSectionService.findSubSectionOfSectionById(
                subSectionId);
        if(srSubSection.getResponseCode() != ResponseCode.SUBSECTION_FOUND){
            throw new SubSectionNotFoundException("The subSection id precised does not match " +
                    "any subSection " +
                    "in the system");
        }
        SubSection concernedSubSection = srSubSection.getAssociatedObject();


        try{

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionOrderByTitleAsc(
                                        concernedSubSection);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionOrderByTitleDesc(
                                        concernedSubSection);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemScopeOrderByTitleAsc(
                                        concernedSubSection, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemScopeOrderByTitleDesc(
                                        concernedSubSection,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeOrderByTitleAsc(
                                        concernedSubSection, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeOrderByTitleDesc(
                                        concernedSubSection, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedSubSection, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedSubSection, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedSubSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedSubSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofParagraphBy(String paragraphId,
                                                                     String problemType,
                                                                     String problemScope,
                                                                     String levelofDifficulty,
                                                                     String sortBy,
                                                                     String direction)
            throws ParagraphNotFoundException {
        paragraphId = paragraphId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Paragraph> srParagraph = paragraphService.findParagraphOfSubSectionById(
                paragraphId);
        if(srParagraph.getResponseCode() != ResponseCode.PARAGRAPH_FOUND){
            throw new ParagraphNotFoundException("The paragraph id precised does not match " +
                    "any paragraph " +
                    "in the system");
        }
        Paragraph concernedParagraph = srParagraph.getAssociatedObject();


        try{

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphOrderByTitleAsc(
                                        concernedParagraph);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphOrderByTitleDesc(
                                        concernedParagraph);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemScopeOrderByTitleAsc(
                                        concernedParagraph, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemScopeOrderByTitleDesc(
                                        concernedParagraph,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeOrderByTitleAsc(
                                        concernedParagraph, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeOrderByTitleDesc(
                                        concernedParagraph, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedParagraph, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedParagraph, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedParagraph, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedParagraph, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofModuleAndStaff(String moduleId,
                                                                        String staffId,
                                                                        String problemType,
                                                                        String problemScope,
                                                                        String levelofDifficulty,
                                                                        String sortBy,
                                                                        String direction)
            throws ModuleNotFoundException, StaffNotFoundException {
        moduleId = moduleId.trim();
        staffId = staffId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

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

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleOrderByTitleAsc(
                                        concernedStaff,
                                        concernedModule);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleOrderByTitleDesc(
                                        concernedStaff,
                                        concernedModule);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedModule,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedModule, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedModuleAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedModule, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofChapterAndStaff(String chapterId,
                                                                         String staffId,
                                                                         String problemType,
                                                                         String problemScope,
                                                                         String levelofDifficulty,
                                                                         String sortBy,
                                                                         String direction)
            throws ChapterNotFoundException, StaffNotFoundException {
        chapterId = chapterId.trim();
        staffId = staffId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

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

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterOrderByTitleAsc(
                                        concernedStaff,
                                        concernedChapter);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterOrderByTitleDesc(
                                        concernedStaff,
                                        concernedChapter);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedChapter,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedChapter, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedChapterAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedChapter, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSectionAndStaff(String sectionId,
                                                                         String staffId,
                                                                         String problemType,
                                                                         String problemScope,
                                                                         String levelofDifficulty,
                                                                         String sortBy,
                                                                         String direction)
            throws SectionNotFoundException, StaffNotFoundException {
        sectionId = sectionId.trim();
        staffId = staffId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

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

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionOrderByTitleAsc(
                                        concernedStaff,
                                        concernedSection);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionOrderByTitleDesc(
                                        concernedStaff,
                                        concernedSection);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedSection,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofSubSectionAndStaff(String subSectionId,
                                                                            String staffId,
                                                                            String problemType,
                                                                            String problemScope,
                                                                            String levelofDifficulty,
                                                                            String sortBy,
                                                                            String direction)
            throws SubSectionNotFoundException, StaffNotFoundException {
        subSectionId = subSectionId.trim();
        staffId = staffId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

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

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionOrderByTitleAsc(
                                        concernedStaff,
                                        concernedSubSection);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionOrderByTitleDesc(
                                        concernedStaff,
                                        concernedSubSection);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedSubSection,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedSubSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedSubSectionAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedSubSection, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public ServerResponse<List<Problem>> findAllProblemofParagraphAndStaff(String paragraphId,
                                                                           String staffId,
                                                                           String problemType,
                                                                           String problemScope,
                                                                           String levelofDifficulty,
                                                                           String sortBy,
                                                                           String direction)
            throws ParagraphNotFoundException, StaffNotFoundException {
        paragraphId = paragraphId.trim();
        staffId = staffId.trim();
        problemScope = problemScope.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Problem>> srListofProblem = new ServerResponse<>();
        srListofProblem.setErrorMessage("The problem list of the precised staff is " +
                "successfully found");
        srListofProblem.setResponseCode(ResponseCode.NORMAL_RESPONSE);

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

            List<Problem> listofProblem = new ArrayList<>();

            if(problemType == null && problemScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphOrderByTitleAsc(
                                        concernedStaff,
                                        concernedParagraph);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphOrderByTitleDesc(
                                        concernedStaff,
                                        concernedParagraph);
                    }
                }
            }
            else if(problemType == null && problemScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedParagraph,
                                        enumScope);
                    }
                }
            }
            else if(problemType == null && problemScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumScope, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumProblemType);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumProblemType);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty == null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumProblemType, enumScope);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumProblemType, enumScope);
                    }
                }
            }
            else if(problemType != null && problemScope == null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumProblemType, enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumProblemType, enumLevelofDifficulty);
                    }
                }
            }
            else if(problemType != null && problemScope != null && levelofDifficulty != null){
                EnumProblemType enumProblemType = EnumProblemType.valueOf(
                        problemType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedParagraph, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofProblem = problemRepository.
                                findAllByOwnerStaffAndConcernedParagraphAndProblemTypeAndProblemScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedParagraph, enumProblemType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofProblem.setAssociatedObject(listofProblem);
        }
        catch (IllegalArgumentException e) {
            srListofProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofProblem.setErrorMessage("IllegalArgumentException");
            srListofProblem.setMoreDetails(e.getMessage());
        }




        return srListofProblem;
    }

    @Override
    public Problem saveProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    @Override
    public ServerResponse<Problem> saveProblem(String title,
                                               String problemType,
                                               String levelofDifficulty,
                                               String problemScope,
                                               String ownerStaffId,
                                               String courseId,
                                               String concernedPartId)
            throws ConcernedPartNotBelongingToCourseException, StaffNotFoundException,
            CourseNotFoundException {
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.PROBLEM_NOT_CREATED);
        title = title.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        ownerStaffId = ownerStaffId.trim();
        courseId = courseId.trim();
        concernedPartId = concernedPartId.trim();
        problemScope = problemScope.trim();

        Staff ownerStaff = null;
        Course concernedCourse = null;

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

            EnumProblemType enumProblemType = EnumProblemType.valueOf(
                    problemType.toUpperCase());
            EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                    levelofDifficulty.toUpperCase());
            EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());

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


            List<Indication> listofIndication = new ArrayList<Indication>();
            List<Content> listofContent = new ArrayList<>();
            List<Question> listofQuestion = new ArrayList<>();

            Problem problemToSaved = new Problem();
            problemToSaved.setTitle(title);
            problemToSaved.setConcernedChapter(concernedChapter);
            problemToSaved.setConcernedCourse(concernedCourse);
            problemToSaved.setConcernedModule(concernedModule);
            problemToSaved.setConcernedParagraph(concernedParagraph);
            problemToSaved.setConcernedSection(concernedSection);
            problemToSaved.setConcernedSubSection(concernedSubSection);
            problemToSaved.setLevelofDifficulty(enumLevelofDifficulty);
            problemToSaved.setProblemScope(enumScope);
            problemToSaved.setListofContent(listofContent);
            problemToSaved.setListofIndication(listofIndication);
            problemToSaved.setListofQuestion(listofQuestion);
            problemToSaved.setOwnerStaff(ownerStaff);
            problemToSaved.setProblemType(enumProblemType);

            Problem problemSaved = problemRepository.save(problemToSaved);

            srProblem.setErrorMessage("The problem has been saved successfully");
            srProblem.setResponseCode(ResponseCode.PROBLEM_CREATED);
            srProblem.setAssociatedObject(problemSaved);


        } catch (IllegalArgumentException e) {
            srProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_PROBLEM_OR_LEVELOFDIFFICULTY_TYPE);
            srProblem.setErrorMessage("IllegalArgumentException");
            srProblem.setMoreDetails(e.getMessage());
        }


        return srProblem;
    }

    @Override
    public ServerResponse<Problem> duplicateProblemForStaff(String problemId,
                                                            String staffId)
            throws ProblemNotFoundException, StaffNotFoundException{
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.PROBLEM_NOT_CREATED);
        problemId = problemId.trim();
        staffId = staffId.trim();

        ServerResponse<Problem> srProblemToDuplicate = this.findProblemById(problemId);
        if(srProblemToDuplicate.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id does not match any staff in the " +
                    "system");
        }
        Staff newOwnerStaff = srStaff.getAssociatedObject();
        Problem problemToDuplicate = srProblemToDuplicate.getAssociatedObject();

        Problem problemCopy = new Problem();
        problemCopy.setTitle(problemToDuplicate.getTitle());
        problemCopy.setConcernedChapter(problemToDuplicate.getConcernedChapter());
        problemCopy.setConcernedCourse(problemToDuplicate.getConcernedCourse());
        problemCopy.setConcernedModule(problemToDuplicate.getConcernedModule());
        problemCopy.setConcernedParagraph(problemToDuplicate.getConcernedParagraph());
        problemCopy.setConcernedSection(problemToDuplicate.getConcernedSection());
        problemCopy.setConcernedSubSection(problemToDuplicate.getConcernedSubSection());
        problemCopy.setLevelofDifficulty(problemToDuplicate.getLevelofDifficulty());
        problemCopy.setListofContent(problemToDuplicate.getListofContent());
        problemCopy.setListofIndication(problemToDuplicate.getListofIndication());
        problemCopy.setListofQuestion(problemToDuplicate.getListofQuestion());
        problemCopy.setOwnerStaff(newOwnerStaff);
        problemCopy.setProblemType(problemToDuplicate.getProblemType());

        Problem problemDuplicated = problemRepository.save(problemCopy);

        srProblem.setErrorMessage("The problem has been duplicated successfully");
        srProblem.setResponseCode(ResponseCode.PROBLEM_CREATED);
        srProblem.setAssociatedObject(problemDuplicated);


        return srProblem;
    }

    @Override
    public ServerResponse<Problem> addContentToProblem(String value,
                                                       String contentType,
                                                       String problemId)
            throws ProblemNotFoundException {
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.CONTENT_NOT_ADDED);
        value = value.trim();
        contentType = contentType.trim();
        problemId = problemId.trim();
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem concernedProblem = srProblem1.getAssociatedObject();

        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);
            concernedProblem.getListofContent().add(contentSaved);

            Problem problemSaved = this.saveProblem(concernedProblem);
            srProblem.setResponseCode(ResponseCode.CONTENT_ADDED);
            srProblem.setErrorMessage("The content has been successfully added to the problem");
            srProblem.setAssociatedObject(problemSaved);

        } catch (IllegalArgumentException e) {
            srProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_CONTENT_TYPE);
            srProblem.setErrorMessage("IllegalArgumentException");
            srProblem.setMoreDetails(e.getMessage());
        }

        return srProblem;
    }

    public Optional<Content> findContentInContentProblemList(String contentId,
                                                              Problem problem){
        Content contentSearch = null;
        for(Content content : problem.getListofContent()){
            if(content.getId().equalsIgnoreCase(contentId)){
                contentSearch = content;
                break;
            }
        }
        return Optional.ofNullable(contentSearch);
    }

    @Override
    public ServerResponse<Problem> updateContentToProblem(String contentId,
                                                          String value,
                                                          String problemId)
            throws ContentNotBelongingToException, ProblemNotFoundException {
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.CONTENT_NOT_UPDATED);
        contentId = contentId.trim();
        value = value.trim();
        problemId = problemId.trim();
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToUpdateContent = srProblem1.getAssociatedObject();

        Optional<Content> optionalContentInProblem = this.findContentInContentProblemList(contentId,
                problemToUpdateContent);
        if(!optionalContentInProblem.isPresent()){
            throw new ContentNotBelongingToException("The content id does not exist in the problem");
        }
        Content contentToUpdate = optionalContentInProblem.get();
        contentToUpdate.setValue(value);
        contentRepository.save(contentToUpdate);

        srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system after deleting his content");
        }
        Problem problemUpdateContent = srProblem1.getAssociatedObject();

        srProblem.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srProblem.setErrorMessage("The content has been successfully updated");
        srProblem.setAssociatedObject(problemUpdateContent);

        return srProblem;
    }

    @Override
    public ServerResponse<Problem> removeContentToProblem(String contentId,
                                                          String problemId)
            throws ContentNotBelongingToException, ProblemNotFoundException {
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.CONTENT_NOT_DELETED);
        contentId = contentId.trim();
        problemId = problemId.trim();
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToDeleteContent = srProblem1.getAssociatedObject();

        Optional<Content> optionalContentInProblem = this.findContentInContentProblemList(contentId,
                problemToDeleteContent);
        if(!optionalContentInProblem.isPresent()){
            throw new ContentNotBelongingToException("The content id does not exist in the problem");
        }

        contentRepository.delete(optionalContentInProblem.get());

        ServerResponse<Problem> srProblemToDeleteContent = this.findProblemById(problemId);
        if(srProblemToDeleteContent.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system after deleting his content");
        }
        Problem problemDeleteContent = srProblem1.getAssociatedObject();

        srProblem.setResponseCode(ResponseCode.CONTENT_DELETED);
        srProblem.setErrorMessage("The content has been deleted in the problem list successfully");
        srProblem.setAssociatedObject(problemDeleteContent);

        return srProblem;
    }

    @Override
    public ServerResponse<Problem> addIndicationToProblem(String value,
                                                          String contentType,
                                                          String staffId,
                                                          String problemId)
            throws ProblemNotFoundException, StaffNotFoundException {
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.INDICATION_NOT_CREATED);
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem concernedProblem = srProblem1.getAssociatedObject();

        ServerResponse<Indication> srIndicationAdded = indicationService.saveIndication(staffId,
                value, contentType);
        if(srIndicationAdded.getResponseCode() == ResponseCode.INDICATION_CREATED){
            Indication indicationToAddInQuestion = srIndicationAdded.getAssociatedObject();
            concernedProblem.getListofIndication().add(indicationToAddInQuestion);

            Problem problemSaved = this.saveProblem(concernedProblem);
            srProblem.setResponseCode(ResponseCode.INDICATION_ADDED_TO_QUESTION);
            srProblem.setErrorMessage("The indication has been successfully added to the problem");
            srProblem.setAssociatedObject(problemSaved);
        }

        return srProblem;
    }

    public Optional<Indication> findIndicationInIndicationProblemList(String indicationId,
                                                                       Problem problem){
        Indication indicationSearch = null;
        for(Indication indication : problem.getListofIndication()){
            if(indication.getId().equalsIgnoreCase(indicationId)){
                indicationSearch = indication;
                break;
            }
        }
        return Optional.ofNullable(indicationSearch);
    }

    @Override
    public ServerResponse<Problem> updateIndicationToProblem(String indicationId,
                                                             String contentId,
                                                             String value,
                                                             String problemId,
                                                             String staffId)
            throws IndicationNotFoundException, ContentNotFoundException,
            ProblemNotFoundException, IndicationNotBelongingToStaffException {
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.CONTENT_OF_INDICATION_NOT_UPDATED);
        indicationId = indicationId.trim();
        contentId = contentId.trim();
        value = value.trim();
        problemId = problemId.trim();
        staffId = staffId.trim();

        /****
         * We must find problem in the system
         */
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToUpdateIndication = srProblem1.getAssociatedObject();

        /****
         * We must check if the indication belonging to staff
         */
        Optional<Indication> optionalIndication =
                this.findIndicationInIndicationProblemList(indicationId, problemToUpdateIndication);
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
                ServerResponse<Problem> srProblemIndicationUpdated =
                        this.findProblemById(problemId);
                if(srProblemIndicationUpdated.getResponseCode() != ResponseCode.PROBLEM_FOUND){
                    throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                            "system");
                }
                Problem problemUpdateIndication = srProblemIndicationUpdated.getAssociatedObject();

                srProblem.setResponseCode(ResponseCode.CONTENT_OF_INDICATION_UPDATED);
                srProblem.setErrorMessage("The indication (his content) has been updated successfully.");
                srProblem.setAssociatedObject(problemUpdateIndication);
            }
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srProblem.setErrorMessage("The content associated to the indication does not belonging " +
                    "to that indication precised");
            srProblem.setResponseCode(ResponseCode.CONTENT_OF_INDICATION_EXCEPTION);
            srProblem.setMoreDetails(e.getMessage());
        }

        return srProblem;
    }

    @Override
    public ServerResponse<Problem> removeIndicationToProblem(String indicationId,
                                                             String problemId,
                                                             String staffId)
            throws IndicationNotFoundException, ProblemNotFoundException,
            IndicationNotBelongingToStaffException, StaffNotFoundException {
        indicationId = indicationId.trim();
        problemId = problemId.trim();
        staffId = staffId.trim();
        ServerResponse<Problem> srProblem = new ServerResponse<>(
                ResponseCode.INDICATION_OF_PROBLEM_NOT_DELETED);

        /****
         * We must find problem in the system
         */
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToRemoveIndication = srProblem1.getAssociatedObject();

        /****
         * We must check if the indication belonging to staff
         */
        Optional<Indication> optionalIndication =
                this.findIndicationInIndicationProblemList(indicationId, problemToRemoveIndication);
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
            srProblem.setResponseCode(ResponseCode.INDICATION_OF_PROBLEM_DELETED);
            srProblem.setErrorMessage("The indication has been remove to the problem successfully");
        }
        else{
            srProblem.setResponseCode(ResponseCode.INDICATION_OF_PROBLEM_NOT_DELETED);
            srProblem.setErrorMessage("The indication has not been remove to the problem");
        }

        /****
         * We must find problem again in the system to actualize
         */
        srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToRemoveIndicationActualize = srProblem1.getAssociatedObject();

        srProblem.setAssociatedObject(problemToRemoveIndicationActualize);

        return srProblem;
    }

    @Override
    public ServerResponse<Problem> addQuestionToProblem(String questionId, String problemId,
                                                        String staffId)
            throws QuestionNotFoundException, ProblemNotFoundException, StaffNotFoundException,
            QuestionAlreadyBelongingToProblemException, ProblemNotBelongingToStaffException {
        questionId = questionId.trim();
        problemId = problemId.trim();
        staffId = staffId.trim();
        ServerResponse<Problem> srProblem  = new ServerResponse<>(
                ResponseCode.QUESTION_NOT_ADDED_TO_PROBLEM);
        /**************************
         * We must first of all find the problem in witch we are going to add the question
         */
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToAddQuestion = srProblem1.getAssociatedObject();

        /*****************
         * We must check if the precised staff is found in the system
         */
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The precised staffId does not match any staff in the " +
                    "system");
        }

        /*****************
         * We are sure that the staffId identify some staff in the system
         * But we must check now if that staff is the owner of the problem
         * If not he cannot add a question to the problem but he can duplicate
         * it before to have a copy in his name
         */
        if(!problemToAddQuestion.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new ProblemNotBelongingToStaffException("The precised staff cannot add " +
                    "a question to the problem because he is not the owner. He must duplicate it " +
                    "before");
        }

        /****************************
         * And then we can find the question which will be added to the problem found above
         */
        ServerResponse<Question> srQuestion = questionService.findQuestionById(questionId);
        if(srQuestion.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToBeAddedInTheProblem = srQuestion.getAssociatedObject();

        /***********
         * We must verified now if the question found does not already belonging to
         * the problem
         */
        for(Question question : problemToAddQuestion.getListofQuestion()){
            if(question.getId().equalsIgnoreCase(questionToBeAddedInTheProblem.getId())){
                throw new QuestionAlreadyBelongingToProblemException("The question already belonging " +
                        "to the precised problem");
            }
        }

        problemToAddQuestion.getListofQuestion().add(questionToBeAddedInTheProblem);

        Problem problemUpdated = this.saveProblem(problemToAddQuestion);

        srProblem.setResponseCode(ResponseCode.QUESTION_ADDED_TO_PROBLEM);
        srProblem.setErrorMessage("The precise question has been successfully added to the problem");
        srProblem.setAssociatedObject(problemUpdated);

        return srProblem;
    }

    @Override
    public ServerResponse<Problem> removeQuestionToProblem(String questionId, String problemId,
                                                           String staffId)
            throws QuestionNotFoundException, ProblemNotFoundException, StaffNotFoundException,
            QuestionNotBelongingToProblemException, ProblemNotBelongingToStaffException {
        questionId = questionId.trim();
        problemId = problemId.trim();
        staffId = staffId.trim();
        ServerResponse<Problem> srProblem  = new ServerResponse<>(
                ResponseCode.QUESTION_NOT_REMOVED_TO_PROBLEM);
        /**************************
         * We must first of all find the problem in witch we are going to add the question
         */
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToRemoveQuestion = srProblem1.getAssociatedObject();

        /*****************
         * We must check if the precised staff is found in the system
         */
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The precised staffId does not match any staff in the " +
                    "system");
        }

        /*****************
         * We are sure that the staffId identify some staff in the system
         * But we must check now if that staff is the owner of the problem
         * If not he cannot add a question to the problem but he can duplicate
         * it before to have a copy in his name
         */
        if(!problemToRemoveQuestion.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new ProblemNotBelongingToStaffException("The precised staff cannot add " +
                    "a question to the problem because he is not the owner. He must duplicate it " +
                    "before");
        }

        /****************************
         * And then we can find the question which will be added to the problem found above
         */
        ServerResponse<Question> srQuestion = questionService.findQuestionById(questionId);
        if(srQuestion.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToBeRemovedInTheProblem = srQuestion.getAssociatedObject();

        /***********
         * We must verified now if the question found belonging to the problem
         */
        int questionBelongingToProblem = 0;
        for(Question question : problemToRemoveQuestion.getListofQuestion()){
            if(question.getId().equalsIgnoreCase(questionToBeRemovedInTheProblem.getId())){
                questionBelongingToProblem = 1;
            }
        }

        if (questionBelongingToProblem == 1){
            problemToRemoveQuestion.getListofQuestion().remove(questionToBeRemovedInTheProblem);
            Problem problemUpdated = this.saveProblem(problemToRemoveQuestion);
            srProblem.setResponseCode(ResponseCode.QUESTION_REMOVED_TO_PROBLEM);
            srProblem.setErrorMessage("The precise question has been successfully removed to the problem");
            srProblem.setAssociatedObject(problemUpdated);
        }
        else{
            throw new QuestionNotBelongingToProblemException("The precised question does not be a part " +
                    " the precised problem");
        }

        return srProblem;
    }


    @Override
    public ServerResponse<Problem> updateProblem(String problemId,
                                                 String newTitle,
                                                 String problemType,
                                                 String levelofDifficulty,
                                                 String problemScope,
                                                 String courseId,
                                                 String concernedPartId,
                                                 String staffId)
            throws ConcernedPartNotBelongingToCourseException, ProblemNotBelongingToStaffException,
            CourseNotFoundException, StaffNotFoundException, ProblemNotFoundException {

        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.PROBLEM_NOT_UPDATED);
        problemId = problemId.trim();
        problemType = problemType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        staffId = staffId.trim();
        courseId = courseId.trim();
        concernedPartId = concernedPartId.trim();
        problemScope = problemScope.trim();

        Staff ownerStaff = null;
        Course concernedCourse = null;

        Module concernedModule = null;
        Chapter concernedChapter = null;
        Section concernedSection = null;
        SubSection concernedSubSection = null;
        Paragraph concernedParagraph = null;

        /****
         * We must find problem in the system
         */
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToUpdate = srProblem1.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        ownerStaff = srStaff.getAssociatedObject();

        if(problemToUpdate.getOwnerStaff().getId() != ownerStaff.getId()){
            throw new ProblemNotBelongingToStaffException("The precised staff can not updated the " +
                    "problem because he does not have permission. he can duplicated it first ");
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

            EnumProblemType enumProblemType = EnumProblemType.valueOf(
                    problemType.toUpperCase());
            EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                    levelofDifficulty.toUpperCase());
            EnumScope enumScope = EnumScope.valueOf(problemScope.toUpperCase());


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

            problemToUpdate.setTitle(newTitle);
            problemToUpdate.setConcernedChapter(concernedChapter);
            problemToUpdate.setConcernedCourse(concernedCourse);
            problemToUpdate.setConcernedModule(concernedModule);
            problemToUpdate.setConcernedParagraph(concernedParagraph);
            problemToUpdate.setConcernedSection(concernedSection);
            problemToUpdate.setConcernedSubSection(concernedSubSection);
            problemToUpdate.setLevelofDifficulty(enumLevelofDifficulty);
            problemToUpdate.setProblemScope(enumScope);
            problemToUpdate.setProblemType(enumProblemType);

            Problem problemUpdated = problemRepository.save(problemToUpdate);

            srProblem.setErrorMessage("The problem has been updated successfully");
            srProblem.setResponseCode(ResponseCode.PROBLEM_UPDATED);
            srProblem.setAssociatedObject(problemUpdated);

        } catch (IllegalArgumentException e) {
            srProblem.setResponseCode(ResponseCode.EXCEPTION_ENUM_PROBLEM_OR_LEVELOFDIFFICULTY_TYPE);
            srProblem.setErrorMessage("IllegalArgumentException");
            srProblem.setMoreDetails(e.getMessage());
        }
        ////////

        return srProblem;
    }

    @Override
    public ServerResponse<Problem> deleteProblem(String problemId, String staffId)
            throws ProblemNotFoundException, ProblemNotBelongingToStaffException,
            IndicationNotFoundException {

        problemId = problemId.trim();
        staffId = staffId.trim();
        /****
         * We must find problem in the system
         */
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToDelete = srProblem1.getAssociatedObject();

        if(problemToDelete.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new ProblemNotBelongingToStaffException("The precised problem does not " +
                    "belonging to the precised staff and then the cancelation can not been done " +
                    "by the precised staff");
        }
        /***
         * if we are here then all the conditions are filled to delete the problem
         */
        return this.deleteProblem(problemId);
    }

    @Override
    public ServerResponse<Problem> deleteProblem(String problemId)
            throws ProblemNotFoundException, IndicationNotFoundException {
        ServerResponse<Problem> srProblem = new ServerResponse<>(ResponseCode.PROBLEM_NOT_DELETED);
        problemId = problemId.trim();
        /****
         * We must find question in the system
         */
        ServerResponse<Problem> srProblem1 = this.findProblemById(problemId);
        if(srProblem1.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToDelete = srProblem1.getAssociatedObject();
        /****
         * To delete a question all the associated object must be deleted means
         * the indication list, the proposition list and the content list
         */
        for(Content content : problemToDelete.getListofContent()){
            contentRepository.delete(content);
        }
        for (Indication indication: problemToDelete.getListofIndication()){
            indicationService.removeIndication(indication.getId());
        }


        problemRepository.delete(problemToDelete);

        srProblem.setResponseCode(ResponseCode.PROBLEM_DELETED);
        srProblem.setErrorMessage("The problem has been deleted successfully");
        srProblem.setAssociatedObject(problemToDelete);
        return srProblem;
    }


}
