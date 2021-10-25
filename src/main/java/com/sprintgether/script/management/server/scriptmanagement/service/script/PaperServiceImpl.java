package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.PaperRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.CourseNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.program.CourseService;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaperServiceImpl implements  PaperService{
    PaperRepository paperRepository;
    StaffService staffService;
    CourseService courseService;
    QuestionService questionService;
    ProblemService problemService;
    IndicationService indicationService;

    public PaperServiceImpl(PaperRepository paperRepository,
                            StaffService staffService,
                            CourseService courseService,
                            QuestionService questionService,
                            ProblemService problemService,
                            IndicationService indicationService) {
        this.paperRepository = paperRepository;
        this.staffService = staffService;
        this.courseService = courseService;
        this.questionService = questionService;
        this.problemService = problemService;
        this.indicationService = indicationService;
    }

    @Override
    public ServerResponse<Paper> findPaperById(String paperId) {
        ServerResponse<Paper> srPaper = new ServerResponse<>();
        paperId = paperId.trim();
        srPaper.setResponseCode(ResponseCode.PAPER_NOT_FOUND);
        srPaper.setErrorMessage("The paper does not found in the system with that paperID");
        Optional<Paper> optionalPaper = paperRepository.findById(paperId);
        if(optionalPaper.isPresent()){
            srPaper.setResponseCode(ResponseCode.PAPER_FOUND);
            srPaper.setErrorMessage("The question is found in the system");
            srPaper.setAssociatedObject(optionalPaper.get());
        }

        return srPaper;
    }

    @Override
    public ServerResponse<List<Paper>> findAllPaperofStaffBy(String staffId,
                                                             String paperType,
                                                             String paperScope,
                                                             String levelofDifficulty,
                                                             String sortBy,
                                                             String direction)
            throws StaffNotFoundException{
        staffId = staffId.trim();
        paperScope = paperScope.trim();
        paperType = paperType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Paper>> srListofPaper = new ServerResponse<>();
        srListofPaper.setErrorMessage("The paper list of the precised staff is " +
                "successfully found");
        srListofPaper.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        Staff concernedStaff = srStaff.getAssociatedObject();
        try{

            List<Paper> listofPaper = new ArrayList<>();

            if(paperType == null && paperScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.findAllByOwnerStaffOrderByTitleAsc(
                                concernedStaff);
                    } else {
                        listofPaper = paperRepository.findAllByOwnerStaffOrderByTitleDesc(
                                concernedStaff);
                    }
                }
            }
            else if(paperType == null && paperScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType == null && paperScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperScopeOrderByTitleAsc(concernedStaff,
                                        enumScope);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperScopeOrderByTitleDesc(concernedStaff,
                                        enumScope);
                    }
                }
            }
            else if(paperType == null && paperScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumScope, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType != null && paperScope == null && levelofDifficulty == null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeOrderByTitleAsc(concernedStaff,
                                        enumPaperType);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeOrderByTitleDesc(concernedStaff,
                                        enumPaperType);
                    }
                }
            }
            else if(paperType != null && paperScope != null && levelofDifficulty == null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeAndPaperScopeOrderByTitleAsc(
                                        concernedStaff, enumPaperType, enumScope);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeAndPaperScopeOrderByTitleDesc(
                                        concernedStaff, enumPaperType, enumScope);
                    }
                }
            }
            else if(paperType != null && paperScope == null && levelofDifficulty != null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumPaperType, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumPaperType, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType != null && paperScope != null && levelofDifficulty != null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, enumPaperType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, enumPaperType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofPaper.setAssociatedObject(listofPaper);
        }
        catch (IllegalArgumentException e) {
            srListofPaper.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofPaper.setErrorMessage("IllegalArgumentException");
            srListofPaper.setMoreDetails(e.getMessage());
        }



        return srListofPaper;
    }

    @Override
    public ServerResponse<List<Paper>> findAllPaperofCourseBy(String courseId,
                                                              String paperType,
                                                              String paperScope,
                                                              String levelofDifficulty,
                                                              String sortBy,
                                                              String direction)
            throws CourseNotFoundException {
        courseId = courseId.trim();
        paperScope = paperScope.trim();
        paperType = paperType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Paper>> srListofPaper = new ServerResponse<>();
        srListofPaper.setErrorMessage("The paper list of the precised staff is " +
                "successfully found");
        srListofPaper.setResponseCode(ResponseCode.NORMAL_RESPONSE);

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id precised does not match any course " +
                    "in the system");
        }
        Course concernedCourse = srCourse.getAssociatedObject();


        try{

            List<Paper> listofPaper = new ArrayList<>();

            if(paperType == null && paperScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseOrderByTitleAsc(
                                        concernedCourse);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseOrderByTitleDesc(
                                        concernedCourse);
                    }
                }
            }
            else if(paperType == null && paperScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType == null && paperScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperScopeOrderByTitleAsc(
                                        concernedCourse, enumScope);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperScopeOrderByTitleDesc(
                                        concernedCourse,
                                        enumScope);
                    }
                }
            }
            else if(paperType == null && paperScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumScope, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType != null && paperScope == null && levelofDifficulty == null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeOrderByTitleAsc(
                                        concernedCourse, enumPaperType);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeOrderByTitleDesc(
                                        concernedCourse, enumPaperType);
                    }
                }
            }
            else if(paperType != null && paperScope != null && levelofDifficulty == null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleAsc(
                                        concernedCourse, enumPaperType, enumScope);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleDesc(
                                        concernedCourse, enumPaperType, enumScope);
                    }
                }
            }
            else if(paperType != null && paperScope == null && levelofDifficulty != null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumPaperType, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumPaperType, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType != null && paperScope != null && levelofDifficulty != null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedCourse, enumPaperType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedCourse, enumPaperType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofPaper.setAssociatedObject(listofPaper);
        }
        catch (IllegalArgumentException e) {
            srListofPaper.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofPaper.setErrorMessage("IllegalArgumentException");
            srListofPaper.setMoreDetails(e.getMessage());
        }




        return srListofPaper;
    }

    @Override
    public ServerResponse<List<Paper>> findAllPaperofCourseAndStaffBy(String courseId,
                                                                      String staffId,
                                                                      String paperType,
                                                                      String paperScope,
                                                                      String levelofDifficulty,
                                                                      String sortBy,
                                                                      String direction)
            throws CourseNotFoundException, StaffNotFoundException {
        staffId = staffId.trim();
        courseId = courseId.trim();
        paperScope = paperScope.trim();
        paperType = paperType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        sortBy = sortBy.trim();
        direction = direction.trim();

        ServerResponse<List<Paper>> srListofPaper = new ServerResponse<>();
        srListofPaper.setErrorMessage("The question list of the precised staff is " +
                "successfully found");
        srListofPaper.setResponseCode(ResponseCode.NORMAL_RESPONSE);

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

            List<Paper> listofPaper = new ArrayList<>();

            if(paperType == null && paperScope == null && levelofDifficulty == null){
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseOrderByTitleAsc(
                                        concernedStaff, concernedCourse);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseOrderByTitleDesc(
                                        concernedStaff, concernedCourse);
                    }
                }
            }
            else if(paperType == null && paperScope == null && levelofDifficulty != null){
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType == null && paperScope != null && levelofDifficulty == null){
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperScopeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumScope);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperScopeOrderByTitleDesc(
                                        concernedStaff, concernedCourse,
                                        enumScope);
                    }
                }
            }
            else if(paperType == null && paperScope != null && levelofDifficulty != null){
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumScope, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumScope, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType != null && paperScope == null && levelofDifficulty == null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumPaperType);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumPaperType);
                    }
                }
            }
            else if(paperType != null && paperScope != null && levelofDifficulty == null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumPaperType, enumScope);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumPaperType, enumScope);
                    }
                }
            }
            else if(paperType != null && paperScope == null && levelofDifficulty != null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumPaperType, enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumPaperType, enumLevelofDifficulty);
                    }
                }
            }
            else if(paperType != null && paperScope != null && levelofDifficulty != null){
                EnumPaperType enumPaperType = EnumPaperType.valueOf(
                        paperType.toUpperCase());
                EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());
                EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                        levelofDifficulty.toUpperCase());
                if(sortBy.equalsIgnoreCase("title")) {
                    if (direction.equalsIgnoreCase("ASC")) {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
                                        concernedStaff, concernedCourse, enumPaperType, enumScope,
                                        enumLevelofDifficulty);
                    } else {
                        listofPaper = paperRepository.
                                findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
                                        concernedStaff, concernedCourse, enumPaperType, enumScope,
                                        enumLevelofDifficulty);
                    }
                }
            }
            srListofPaper.setAssociatedObject(listofPaper);
        }
        catch (IllegalArgumentException e) {
            srListofPaper.setResponseCode(ResponseCode.EXCEPTION_ENUM_CRITERIA);
            srListofPaper.setErrorMessage("IllegalArgumentException");
            srListofPaper.setMoreDetails(e.getMessage());
        }



        return srListofPaper;
    }

    @Override
    public Paper savePaper(Paper paper) {
        return paperRepository.save(paper);
    }


    @Override
    public ServerResponse<Paper> savePaper(String title,
                                           String paperType,
                                           String levelofDifficulty,
                                           String paperScope,
                                           String courseId,
                                           String ownerStaffId)
            throws CourseNotFoundException, StaffNotFoundException {
        ServerResponse<Paper> srPaper = new ServerResponse<>(ResponseCode.PAPER_NOT_CREATED);
        title = title.trim();
        paperType = paperType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        ownerStaffId = ownerStaffId.trim();
        courseId = courseId.trim();
        paperScope = paperScope.trim();

        Staff ownerStaff = null;
        Course concernedCourse = null;

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

        try {

            EnumPaperType enumPaperType = EnumPaperType.valueOf(
                    paperType.toUpperCase());
            EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                    levelofDifficulty.toUpperCase());
            EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());


            List<Question> listofQuestion = new ArrayList<>();
            List<Indication> listofIndication = new ArrayList<Indication>();
            List<Problem> listofProblem = new ArrayList<>();

            Paper paperToSaved = new Paper();
            paperToSaved.setTitle(title);
            paperToSaved.setConcernedCourse(concernedCourse);
            paperToSaved.setLevelofDifficulty(enumLevelofDifficulty);
            paperToSaved.setPaperScope(enumScope);
            paperToSaved.setListofQuestion(listofQuestion);
            paperToSaved.setListofProblem(listofProblem);
            paperToSaved.setListofIndication(listofIndication);
            paperToSaved.setOwnerStaff(ownerStaff);
            paperToSaved.setPaperType(enumPaperType);

            Paper paperSaved = paperRepository.save(paperToSaved);

            srPaper.setErrorMessage("The question has been saved successfully");
            srPaper.setResponseCode(ResponseCode.PAPER_CREATED);
            srPaper.setAssociatedObject(paperSaved);

        } catch (IllegalArgumentException e) {
            srPaper.setResponseCode(ResponseCode.EXCEPTION_ENUM_QUESTION_OR_LEVELOFDIFFICULTY_TYPE);
            srPaper.setErrorMessage("IllegalArgumentException");
            srPaper.setMoreDetails(e.getMessage());
        }

        return srPaper;
    }

    @Override
    public ServerResponse<Paper> duplicatePaperForStaff(String paperId,
                                                        String staffId)
            throws PaperNotFoundException, StaffNotFoundException {
        ServerResponse<Paper> srPaper = new ServerResponse<>(ResponseCode.PAPER_NOT_CREATED);
        paperId = paperId.trim();
        staffId = staffId.trim();

        ServerResponse<Paper> srPaperToDuplicate = this.findPaperById(paperId);
        if(srPaperToDuplicate.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id does not match any staff in the " +
                    "system");
        }
        Staff newOwnerStaff = srStaff.getAssociatedObject();
        Paper paperToDuplicate = srPaperToDuplicate.getAssociatedObject();

        Paper paperCopy = new Paper();
        paperCopy.setTitle(paperToDuplicate.getTitle());
        paperCopy.setConcernedCourse(paperToDuplicate.getConcernedCourse());
        paperCopy.setLevelofDifficulty(paperToDuplicate.getLevelofDifficulty());
        paperCopy.setListofIndication(paperToDuplicate.getListofIndication());
        paperCopy.setListofQuestion(paperToDuplicate.getListofQuestion());
        paperCopy.setListofProblem(paperToDuplicate.getListofProblem());
        paperCopy.setOwnerStaff(newOwnerStaff);
        paperCopy.setPaperType(paperToDuplicate.getPaperType());
        paperCopy.setPaperScope(paperToDuplicate.getPaperScope());

        Paper paperDuplicated = paperRepository.save(paperCopy);

        srPaper.setErrorMessage("The paper has been duplicated successfully");
        srPaper.setResponseCode(ResponseCode.PAPER_CREATED);
        srPaper.setAssociatedObject(paperDuplicated);


        return srPaper;
    }

    @Override
    public ServerResponse<Paper> updatePaper(String paperId,
                                             String newTitle,
                                             String paperType,
                                             String levelofDifficulty,
                                             String paperScope,
                                             String courseId,
                                             String staffId)
            throws PaperNotFoundException, CourseNotFoundException, StaffNotFoundException,
            PaperNotBelongingToStaffException{
        ServerResponse<Paper> srPaper = new ServerResponse<>(ResponseCode.PAPER_NOT_UPDATED);
        newTitle = newTitle.trim();
        paperType = paperType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        staffId = staffId.trim();
        courseId = courseId.trim();
        paperScope = paperScope.trim();

        Staff ownerStaff = null;
        Course concernedCourse = null;

        /****
         * We must find paper in the system
         */
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToUpdate = srPaper1.getAssociatedObject();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        ownerStaff = srStaff.getAssociatedObject();

        if(paperToUpdate.getOwnerStaff().getId() != ownerStaff.getId()){
            throw new PaperNotBelongingToStaffException("The precised staff can not updated the " +
                    "paper because he does not have permission. he can duplicated it first ");
        }

        ServerResponse<Course> srCourse = courseService.findCourseOfLevelById(courseId);
        if(srCourse.getResponseCode() != ResponseCode.COURSE_FOUND){
            throw new CourseNotFoundException("The course id specified does not match any course " +
                    "in the system");
        }
        concernedCourse = srCourse.getAssociatedObject();

        ////////
        try {

            EnumPaperType enumPaperType = EnumPaperType.valueOf(
                    paperType.toUpperCase());
            EnumLevelofDifficulty enumLevelofDifficulty = EnumLevelofDifficulty.valueOf(
                    levelofDifficulty.toUpperCase());
            EnumScope enumScope = EnumScope.valueOf(paperScope.toUpperCase());


            paperToUpdate.setTitle(newTitle);
            paperToUpdate.setConcernedCourse(concernedCourse);
            paperToUpdate.setLevelofDifficulty(enumLevelofDifficulty);
            paperToUpdate.setPaperScope(enumScope);
            paperToUpdate.setPaperType(enumPaperType);

            Paper paperUpdated = paperRepository.save(paperToUpdate);

            srPaper.setErrorMessage("The paper has been updated successfully");
            srPaper.setResponseCode(ResponseCode.PAPER_UPDATED);
            srPaper.setAssociatedObject(paperUpdated);

        } catch (IllegalArgumentException e) {
            srPaper.setResponseCode(ResponseCode.EXCEPTION_ENUM_QUESTION_OR_LEVELOFDIFFICULTY_TYPE);
            srPaper.setErrorMessage("IllegalArgumentException");
            srPaper.setMoreDetails(e.getMessage());
        }
        ////////

        return srPaper;
    }

    @Override
    public ServerResponse<Paper> addQuestionToPaper(String questionId,
                                                    String paperId,
                                                    String staffId)
            throws QuestionNotFoundException, PaperNotFoundException, StaffNotFoundException,
            QuestionAlreadyBelongingToPaperException, PaperNotBelongingToStaffException {
        questionId = questionId.trim();
        paperId = paperId.trim();
        staffId = staffId.trim();
        ServerResponse<Paper> srPaper  = new ServerResponse<>(
                ResponseCode.QUESTION_NOT_ADDED_TO_PAPER);
        /**************************
         * We must first of all find the paper in witch we are going to add the question
         */
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToAddQuestion = srPaper1.getAssociatedObject();

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
         * But we must check now if that staff is the owner of the paper
         * If not he cannot add a question to the paper but he can duplicate
         * it before to have a copy in his name
         */
        if(!paperToAddQuestion.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new PaperNotBelongingToStaffException("The precised staff cannot add " +
                    "a question to the paper because he is not the owner. He must duplicate it " +
                    "before");
        }

        /****************************
         * And then we can find the question which will be added to the paper found above
         */
        ServerResponse<Question> srQuestion = questionService.findQuestionById(questionId);
        if(srQuestion.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToBeAddedInThePaper = srQuestion.getAssociatedObject();

        /***********
         * We must verified now if the question found does not already belonging to
         * the paper
         */
        for(Question question : paperToAddQuestion.getListofQuestion()){
            if(question.getId().equalsIgnoreCase(questionToBeAddedInThePaper.getId())){
                throw new QuestionAlreadyBelongingToPaperException("The question already belonging " +
                        "to the precised paper");
            }
        }

        paperToAddQuestion.getListofQuestion().add(questionToBeAddedInThePaper);

        Paper paperUpdated = this.savePaper(paperToAddQuestion);

        srPaper.setResponseCode(ResponseCode.QUESTION_ADDED_TO_PAPER);
        srPaper.setErrorMessage("The precise question has been successfully added to the paper");
        srPaper.setAssociatedObject(paperUpdated);

        return srPaper;
    }

    @Override
    public ServerResponse<Paper> removeQuestionToPaper(String questionId,
                                                       String paperId,
                                                       String staffId)
            throws QuestionNotFoundException, PaperNotFoundException, StaffNotFoundException,
            QuestionNotBelongingToPaperException, PaperNotBelongingToStaffException {
        questionId = questionId.trim();
        paperId = paperId.trim();
        staffId = staffId.trim();
        ServerResponse<Paper> srPaper  = new ServerResponse<>(
                ResponseCode.QUESTION_NOT_REMOVED_TO_PAPER);
        /**************************
         * We must first of all find the paper in witch we are going to remove the question
         */
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToRemoveQuestion = srPaper1.getAssociatedObject();

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
         * But we must check now if that staff is the owner of the paper
         * If not he cannot remove a question to the paper but he can duplicate
         * it before to have a copy in his name
         */
        if(!paperToRemoveQuestion.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new PaperNotBelongingToStaffException("The precised staff cannot add " +
                    "a question to the paper because he is not the owner. He must duplicate it " +
                    "before");
        }

        /****************************
         * And then we can find the question which will be removed to the problem found above
         */
        ServerResponse<Question> srQuestion = questionService.findQuestionById(questionId);
        if(srQuestion.getResponseCode() != ResponseCode.QUESTION_FOUND){
            throw new QuestionNotFoundException("The question id does not match any question in the " +
                    "system");
        }
        Question questionToBeRemovedInThePaper = srQuestion.getAssociatedObject();

        /***********
         * We must verified now if the question found belonging to the paper
         */
        int questionBelongingToPaper = 0;
        for(Question question : paperToRemoveQuestion.getListofQuestion()){
            if(question.getId().equalsIgnoreCase(questionToBeRemovedInThePaper.getId())){
                questionBelongingToPaper = 1;
            }
        }

        if (questionBelongingToPaper == 1){
            paperToRemoveQuestion.getListofQuestion().remove(questionToBeRemovedInThePaper);
            Paper paperUpdated = this.savePaper(paperToRemoveQuestion);
            srPaper.setResponseCode(ResponseCode.QUESTION_REMOVED_TO_PAPER);
            srPaper.setErrorMessage("The precise question has been successfully removed to the paper");
            srPaper.setAssociatedObject(paperUpdated);
        }
        else{
            throw new QuestionNotBelongingToPaperException("The precised question does not be a part " +
                    " the precised paper");
        }

        return srPaper;
    }


    @Override
    public ServerResponse<Paper> addIndicationToPaper(String value,
                                                      String contentType,
                                                      String paperId,
                                                      String staffId)
            throws PaperNotFoundException, StaffNotFoundException {
        value = value.trim();
        contentType = contentType.trim();
        paperId = paperId.trim();
        staffId = staffId.trim();
        ServerResponse<Paper> srPaper = new ServerResponse<>();
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper concernedPaper = srPaper1.getAssociatedObject();

        ServerResponse<Indication> srIndicationAdded = indicationService.saveIndication(staffId,
                value, contentType);
        if(srIndicationAdded.getResponseCode() == ResponseCode.INDICATION_CREATED){
            Indication indicationToAddInQuestion = srIndicationAdded.getAssociatedObject();
            concernedPaper.getListofIndication().add(indicationToAddInQuestion);

            Paper paperSaved = this.savePaper(concernedPaper);
            srPaper.setResponseCode(ResponseCode.INDICATION_ADDED_TO_QUESTION);
            srPaper.setErrorMessage("The indication has been successfully added to the question");
            srPaper.setAssociatedObject(paperSaved);
        }

        return srPaper;
    }

    public Optional<Indication> findIndicationInIndicationPaperList(String indicationId,
                                                                       Paper paper){
        Indication indicationSearch = null;
        for(Indication indication : paper.getListofIndication()){
            if(indication.getId().equalsIgnoreCase(indicationId)){
                indicationSearch = indication;
                break;
            }
        }
        return Optional.ofNullable(indicationSearch);
    }


    @Override
    public ServerResponse<Paper> updateIndicationToPaper(String indicationId,
                                                         String contentId,
                                                         String value,
                                                         String paperId,
                                                         String staffId)
            throws IndicationNotFoundException,
            PaperNotFoundException, IndicationNotBelongingToStaffException {
        ServerResponse<Paper> srPaper = new ServerResponse<>(
                ResponseCode.CONTENT_OF_INDICATION_NOT_UPDATED);
        indicationId = indicationId.trim();
        contentId = contentId.trim();
        value = value.trim();
        paperId = paperId.trim();
        staffId = staffId.trim();

        /****
         * We must find paper in the system
         */
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToUpdateIndication = srPaper1.getAssociatedObject();

        /****
         * We must check if the indication belonging to staff
         */
        Optional<Indication> optionalIndication =
                this.findIndicationInIndicationPaperList(indicationId, paperToUpdateIndication);
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
                ServerResponse<Paper> srPaperIndicationUpdated =
                        this.findPaperById(paperId);
                if(srPaperIndicationUpdated.getResponseCode() != ResponseCode.PAPER_FOUND){
                    throw new PaperNotFoundException("The paper id does not match any paper in the " +
                            "system");
                }
                Paper paperUpdateIndication = srPaperIndicationUpdated.getAssociatedObject();

                srPaper.setResponseCode(ResponseCode.CONTENT_OF_INDICATION_UPDATED);
                srPaper.setErrorMessage("The indication (his content) has been updated successfully.");
                srPaper.setAssociatedObject(paperUpdateIndication);
            }
        } catch (ContentNotBelongingToException e) {
            //e.printStackTrace();
            srPaper.setErrorMessage("The content associated to the indication does not belonging " +
                    "to that indication precised");
            srPaper.setResponseCode(ResponseCode.CONTENT_OF_INDICATION_EXCEPTION);
            srPaper.setMoreDetails(e.getMessage());
        }

        return srPaper;
    }

    @Override
    public ServerResponse<Paper> removeIndicationToPaper(String indicationId,
                                                         String paperId,
                                                         String staffId)
            throws IndicationNotFoundException, PaperNotFoundException,
            IndicationNotBelongingToStaffException,
            StaffNotFoundException {
        indicationId = indicationId.trim();
        paperId = paperId.trim();
        staffId = staffId.trim();
        ServerResponse<Paper> srPaper = new ServerResponse<>(
                ResponseCode.INDICATION_OF_PAPER_NOT_DELETED);

        /****
         * We must find paper in the system
         */
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToRemoveIndication = srPaper1.getAssociatedObject();

        /****
         * We must check if the indication belonging to staff
         */
        Optional<Indication> optionalIndication =
                this.findIndicationInIndicationPaperList(indicationId, paperToRemoveIndication);
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
            srPaper.setResponseCode(ResponseCode.INDICATION_OF_QUESTION_DELETED);
            srPaper.setErrorMessage("The indication has been remove to the question successfully");
        }
        else{
            srPaper.setResponseCode(ResponseCode.INDICATION_OF_QUESTION_NOT_DELETED);
            srPaper.setErrorMessage("The indication has not been remove to the question");
        }

        /****
         * We must find paper again in the system to actualize
         */
        srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToRemoveIndicationActualize = srPaper1.getAssociatedObject();

        srPaper.setAssociatedObject(paperToRemoveIndicationActualize);

        return srPaper;
    }

    @Override
    public ServerResponse<Paper> addProblemToPaper(String problemId,
                                                   String paperId,
                                                   String staffId)
            throws ProblemNotFoundException, PaperNotFoundException, StaffNotFoundException,
            ProblemAlreadyBelongingToPaperException, PaperNotBelongingToStaffException {
        problemId = problemId.trim();
        paperId = paperId.trim();
        staffId = staffId.trim();
        ServerResponse<Paper> srPaper  = new ServerResponse<>(
                ResponseCode.PROBLEM_NOT_ADDED_TO_PAPER);
        /**************************
         * We must first of all find the paper in witch we are going to add the problem
         */
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToAddProblem = srPaper1.getAssociatedObject();

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
         * But we must check now if that staff is the owner of the paper
         * If not he cannot add a problem to the paper but he can duplicate
         * it before to have a copy in his name
         */
        if(!paperToAddProblem.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new PaperNotBelongingToStaffException("The precised staff cannot add " +
                    "a problem to the paper because he is not the owner. He must duplicate it " +
                    "before");
        }

        /****************************
         * And then we can find the problem which will be added to the paper found above
         */
        ServerResponse<Problem> srProblem = problemService.findProblemById(problemId);
        if(srProblem.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToBeAddedInThePaper = srProblem.getAssociatedObject();

        /***********
         * We must verified now if the problem found does not already belonging to
         * the paper
         */
        for(Problem problem : paperToAddProblem.getListofProblem()){
            if(problem.getId().equalsIgnoreCase(problemToBeAddedInThePaper.getId())){
                throw new ProblemAlreadyBelongingToPaperException("The problem already belonging " +
                        "to the precised paper");
            }
        }

        paperToAddProblem.getListofProblem().add(problemToBeAddedInThePaper);

        Paper paperUpdated = this.savePaper(paperToAddProblem);

        srPaper.setResponseCode(ResponseCode.PROBLEM_ADDED_TO_PAPER);
        srPaper.setErrorMessage("The precise problem has been successfully added to the paper");
        srPaper.setAssociatedObject(paperUpdated);

        return srPaper;
    }

    @Override
    public ServerResponse<Paper> removeProblemToPaper(String problemId,
                                                      String paperId,
                                                      String staffId)
            throws ProblemNotFoundException, PaperNotFoundException, StaffNotFoundException,
            ProblemNotBelongingToPaperException, PaperNotBelongingToStaffException {
        paperId = paperId.trim();
        paperId = paperId.trim();
        staffId = staffId.trim();
        ServerResponse<Paper> srPaper  = new ServerResponse<>(
                ResponseCode.PROBLEM_NOT_REMOVED_TO_PAPER);
        /**************************
         * We must first of all find the paper in witch we are going to remove the problem
         */
        ServerResponse<Paper> srPaper1 = this.findPaperById(paperId);
        if(srPaper1.getResponseCode() != ResponseCode.PAPER_FOUND){
            throw new PaperNotFoundException("The paper id does not match any paper in the " +
                    "system");
        }
        Paper paperToRemoveProblem = srPaper1.getAssociatedObject();

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
         * But we must check now if that staff is the owner of the paper
         * If not he cannot remove a problem to the paper but he can duplicate
         * it before to have a copy in his name
         */
        if(!paperToRemoveProblem.getOwnerStaff().getId().equalsIgnoreCase(staffId)){
            throw new PaperNotBelongingToStaffException("The precised staff cannot add " +
                    "a question to the paper because he is not the owner. He must duplicate it " +
                    "before");
        }

        /****************************
         * And then we can find the problem which will be removed to the paper found above
         */
        ServerResponse<Problem> srProblem = problemService.findProblemById(problemId);
        if(srProblem.getResponseCode() != ResponseCode.PROBLEM_FOUND){
            throw new ProblemNotFoundException("The problem id does not match any problem in the " +
                    "system");
        }
        Problem problemToBeRemovedInThePaper = srProblem.getAssociatedObject();

        /***********
         * We must verified now if the problem found belonging to the paper
         */
        int problemBelongingToPaper = 0;
        for(Problem problem : paperToRemoveProblem.getListofProblem()){
            if(problem.getId().equalsIgnoreCase(problemToBeRemovedInThePaper.getId())){
                problemBelongingToPaper = 1;
            }
        }

        if (problemBelongingToPaper == 1){
            paperToRemoveProblem.getListofQuestion().remove(problemToBeRemovedInThePaper);
            Paper paperUpdated = this.savePaper(paperToRemoveProblem);
            srPaper.setResponseCode(ResponseCode.PROBLEM_REMOVED_TO_PAPER);
            srPaper.setErrorMessage("The precise problem has been successfully removed to the paper");
            srPaper.setAssociatedObject(paperUpdated);
        }
        else{
            throw new ProblemNotBelongingToPaperException("The precised problem does not be a part " +
                    " the precised paper");
        }

        return srPaper;
    }


    @Override
    public ServerResponse<Paper> deletePaper(String paperId, String staffId)
            throws PaperNotFoundException, StaffNotFoundException, PaperNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Paper> deletePaper(String paperId) throws PaperNotFoundException{
        return null;
    }
}
