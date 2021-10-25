package com.sprintgether.script.management.server.scriptmanagement.dao.script;

import com.sprintgether.script.management.server.scriptmanagement.model.program.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PaperRepository extends MongoRepository<Paper, String> {
    Optional<Paper> findById(String paperId);

    ////////////// ALL CONCERNING STAFF /////////////////////////////////
    List<Paper> findAllByOwnerStaffOrderByTitleAsc(Staff ownerStaff);

    List<Paper> findAllByOwnerStaffOrderByTitleDesc(Staff ownerStaff);

    List<Paper> findAllByOwnerStaffAndPaperTypeOrderByTitleAsc(Staff ownerStaff,
                                                               EnumPaperType enumPaperType);

    List<Paper> findAllByOwnerStaffAndPaperTypeOrderByTitleDesc(Staff ownerStaff,
                                                                EnumPaperType enumPaperType);

    List<Paper> findAllByOwnerStaffAndPaperScopeOrderByTitleAsc(Staff ownerStaff,
                                                                EnumScope enumScope);

    List<Paper> findAllByOwnerStaffAndPaperScopeOrderByTitleDesc(Staff ownerStaff,
                                                                 EnumScope enumScope);

    List<Paper> findAllByOwnerStaffAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                       EnumLevelofDifficulty enumLevelofDifficulty);

    List<Paper> findAllByOwnerStaffAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                        EnumLevelofDifficulty enumLevelofDifficulty);

    List<Paper> findAllByOwnerStaffAndPaperTypeAndPaperScopeOrderByTitleAsc(Staff ownerStaff,
                                                                            EnumPaperType enumPaperType,
                                                                            EnumScope enumScope);

    List<Paper> findAllByOwnerStaffAndPaperTypeAndPaperScopeOrderByTitleDesc(Staff ownerStaff,
                                                                             EnumPaperType enumPaperType,
                                                                             EnumScope enumScope);

    List<Paper> findAllByOwnerStaffAndPaperTypeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                   EnumPaperType enumPaperType,
                                                                                   EnumLevelofDifficulty enumLevelofDifficulty);

    List<Paper> findAllByOwnerStaffAndPaperTypeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                    EnumPaperType enumPaperType,
                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);

    List<Paper> findAllByOwnerStaffAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(Staff ownerStaff,
                                                                                    EnumScope enumScope,
                                                                                    EnumLevelofDifficulty enumLevelofDifficulty);

    List<Paper> findAllByOwnerStaffAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(Staff ownerStaff,
                                                                                     EnumScope enumScope,
                                                                                     EnumLevelofDifficulty enumLevelofDifficulty);

    List<Paper> findAllByOwnerStaffAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, EnumPaperType enumPaperType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);

    List<Paper> findAllByOwnerStaffAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, EnumPaperType enumPaperType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);

    /////////////// ALL CONCERNING COURSE////////////////////////////////////////////////
    List<Paper>  findAllByConcernedCourseOrderByTitleAsc(Course concernedCourse);
    List<Paper>  findAllByConcernedCourseOrderByTitleDesc(Course concernedCourse);
    ////////////// PaperType ///////////////////////////////
    List<Paper>  findAllByConcernedCourseAndPaperTypeOrderByTitleAsc(Course concernedCourse,
                                                                     EnumPaperType enumPaperType);
    List<Paper>  findAllByConcernedCourseAndPaperTypeOrderByTitleDesc(Course concernedCourse,
                                                                      EnumPaperType enumPaperType);
    ////////////// PaperScope  ///////////////////////////////
    List<Paper>  findAllByConcernedCourseAndPaperScopeOrderByTitleAsc(Course concernedCourse,
                                                                      EnumScope enumScope);
    List<Paper>  findAllByConcernedCourseAndPaperScopeOrderByTitleDesc(Course concernedCourse,
                                                                       EnumScope enumScope);
    ////////////// PaperLevelofDifficulty ///////////////////////////////
    List<Paper>  findAllByConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// PaperType and PaperScope ///////////////////////////////
    List<Paper>  findAllByConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleAsc(
            Course concernedCourse, EnumPaperType enumPaperType, EnumScope enumScope);
    List<Paper>  findAllByConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleDesc(
            Course concernedCourse, EnumPaperType enumPaperType, EnumScope enumScope);
    ////////////// PaperType and paperLevelofDifficulty ///////////////////////////////
    List<Paper>  findAllByConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumPaperType enumPaperType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumPaperType enumPaperType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// PaperScope and PaperLevelofDifficulty ///////////////////////////////
    List<Paper>  findAllByConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// PaperType, PaperScope and PaperLevelofDifficulty //////////////////
    List<Paper>  findAllByConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
            Course concernedCourse, EnumPaperType enumPaperType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
            Course concernedCourse, EnumPaperType enumPaperType, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);

    /////////////// ALL CONCERNING STAFF AND COURSE ////////////////////////////////////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseOrderByTitleAsc(Staff ownerStaff,
                                                                         Course concernedCourse);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseOrderByTitleDesc(Staff ownerStaff,
                                                                          Course concernedCourse);
    ////////////// PaperType  ///////////////////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType);
    ////////////// PaperScope  ///////////////////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperScopeOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperScopeOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope);
    ////////////// PaperLevelofDifficulty ///////////////////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// PaperType and PaperScope ///////////////////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType,
            EnumScope enumScope);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType,
            EnumScope enumScope);
    ////////////// PaperType and PaperLevelofDifficulty ///////////////////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType,
            EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// PaperScope and PaperLevelofDifficulty ///////////////////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope,
            EnumLevelofDifficulty enumLevelofDifficulty);
    ////////////// PaperType, PaperScope and PaperLevelofDifficulty //////////////////
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);
    List<Paper>  findAllByOwnerStaffAndConcernedCourseAndPaperTypeAndPaperScopeAndLevelofDifficultyOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumPaperType enumPaperType,
            EnumScope enumScope, EnumLevelofDifficulty enumLevelofDifficulty);










}
