package com.sprintgether.script.management.server.scriptmanagement.dao.script;

import com.sprintgether.script.management.server.scriptmanagement.model.program.Course;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Answer;
import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumScope;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends MongoRepository<Answer, String> {
    Optional<Answer> findById(String answerId);

    //////////////////////// concerning Question //////////////////////////
    List<Answer> findAllByConcernedQuestionOrderByTitleAsc(Course concernedCourse);
    List<Answer> findAllByConcernedQuestionOrderByTitleDesc(Course concernedCourse);
    List<Answer> findAllByConcernedQuestionAndAnswerScopeOrderByTitleAsc(Course concernedCourse,
                                                                         EnumScope enumScope);
    List<Answer> findAllByConcernedQuestionAndAnswerScopeOrderByTitleDesc(Course concernedCourse,
                                                                         EnumScope enumScope);

    //////////////////////// concerning question and staff /////////////////////////////
    List<Answer> findAllByOwnerStaffAndConcernedQuestionOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse);
    List<Answer> findAllByOwnerStaffAndConcernedQuestionOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse);
    List<Answer> findAllByOwnerStaffAndConcernedQuestionAndAnswerScopeOrderByTitleAsc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope);
    List<Answer> findAllByOwnerStaffAndConcernedQuestionAndAnswerScopeOrderByTitleDesc(
            Staff ownerStaff, Course concernedCourse, EnumScope enumScope);

    //////////////////////// concerning question and title //////////////////////////////////
    List<Answer> findAllByConcernedQuestionAndTitleOrderByTitleAsc(Course concernedCourse,
                                                                   String title);
    List<Answer> findAllByConcernedQuestionAndTitleOrderByTitleDesc(Course concernedCourse,
                                                                    String title);
    List<Answer> findAllByConcernedQuestionAndTitleAndAnswerScopeOrderByTitleAsc(Course concernedCourse,
                                                                   String title, EnumScope enumScope);
    List<Answer> findAllByConcernedQuestionAndTitleAndAnswerScopeOrderByTitleDesc(Course concernedCourse,
                                                                    String title, EnumScope enumScope);
}
