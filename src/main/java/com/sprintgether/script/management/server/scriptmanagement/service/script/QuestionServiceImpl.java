package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.QuestionRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.*;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.script.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.commonused.CommonService;
import com.sprintgether.script.management.server.scriptmanagement.service.program.CourseService;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
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
    StaffService staffService;
    IndicationService indicationService;
    PropositionService propositionService;
    CommonService commonService;
    CourseService courseService;
    ProblemService problemService;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               ContentRepository contentRepository,
                               StaffService staffService,
                               IndicationService indicationService,
                               PropositionService propositionService,
                               CommonService commonService,
                               CourseService courseService,
                               ProblemService problemService) {
        this.questionRepository = questionRepository;
        this.contentRepository = contentRepository;
        this.staffService = staffService;
        this.indicationService = indicationService;
        this.propositionService = propositionService;
        this.commonService = commonService;
        this.courseService = courseService;
        this.problemService = problemService;
    }

    @Override
    public ServerResponse<Question> findQuestionById(String questionId) {
        ServerResponse<Question> srQuestion = new ServerResponse<>();
        questionId = questionId.trim();
        srQuestion.setResponseCode(ResponseCode.QUESTION_NOT_FOUND);
        srQuestion.setErrorMessage("The question does not found in the system");
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if(optionalQuestion.isPresent()){
            srQuestion.setResponseCode(ResponseCode.QUESTION_FOUND);
            srQuestion.setErrorMessage("The question is found in the system");
            srQuestion.setAssociatedObject(optionalQuestion.get());
        }

        return srQuestion;
    }

    @Override
    public ServerResponse<List<Proposition>> findAllPropositionofQuestion(String questionId,
                                                                          String sortBy,
                                                                          String direction)
            throws QuestionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Indication>> findAllIndicationofQuestion(String questionId, String sortBy, String direction) throws QuestionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Content>> findAllContentofQuestion(String questionId, String sortBy, String direction) throws QuestionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofStaff(String staffId, String sortBy, String direction) throws StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofStaffByType(String staffId, String questionType, String sortBy, String direction) throws StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofStaffByTypeAndLevelofDifficulty(String staffId, String questionType, String levelofDifficulty, String sortBy, String direction) throws StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofCourseAndStaffByType(String staffId, String courseId, String questionType, String sortBy, String direction) throws StaffNotFoundException, CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofCourseAndStaffByTypeAndLevelofDifficulty(String staffId, String courseId, String questionType, String levelofDifficulty, String sortBy, String direction) throws StaffNotFoundException, CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionByType(String questionType, String sortBy, String direction) {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionByLevelofDifficulty(String levelofDifficulty, String sortBy, String direction) {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofCourse(String courseId, String sortBy, String direction) throws CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofModule(String moduleId, String sortBy, String direction) throws ModuleNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofChapter(String chapterId, String sortBy, String direction) throws ChapterNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSection(String sectionId, String sortBy, String direction) throws SectionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSubSection(String subSectionId, String sortBy, String direction) throws SubSectionNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofParagraph(String paragraphId, String sortBy, String direction) throws ParagraphNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofCourseAndStaff(String courseId, String staffId, String sortBy, String direction) throws CourseNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofModuleAndStaff(String moduleId, String staffId, String sortBy, String direction) throws ModuleNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofChapterAndStaff(String chapterId, String staffId, String sortBy, String direction) throws ChapterNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSectionAndStaff(String sectionId, String staffId, String sortBy, String direction) throws SectionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofSubSectionAndStaff(String subSectionId, String staffId, String sortBy, String direction) throws SubSectionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<List<Question>> findAllQuestionofParagraphAndStaff(String paragraphId, String staffId, String sortBy, String direction) throws ParagraphNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public Question saveQuestion(Question question){
        return questionRepository.save(question);
    }

    @Override
    public ServerResponse<Question> saveQuestion(String questionType,
                                                 String levelofDifficulty,
                                                 String ownerStaffId, String courseId,
                                                 String concernedPartId, String problemId)
            throws ConcernedPartNotBelongingToCourseException,
            StaffNotFoundException, CourseNotFoundException {
        ServerResponse<Question> srQuestion = new ServerResponse<>();
        questionType = questionType.trim();
        levelofDifficulty = levelofDifficulty.trim();
        ownerStaffId = ownerStaffId.trim();
        courseId = courseId.trim();
        concernedPartId = concernedPartId.trim();
        problemId = problemId.trim();

        Staff ownerStaff = null;
        Course concernedCourse = null;
        Problem concernedProblem = null;

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

        ServerResponse<Problem> srProblem = problemService.findProblemById(problemId);
        if(srProblem.getResponseCode() == ResponseCode.PROBLEM_FOUND){
            concernedProblem = srProblem.getAssociatedObject();
        }

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
            questionToSaved.setConcernedChapter(concernedChapter);
            questionToSaved.setConcernedCourse(concernedCourse);
            questionToSaved.setConcernedModule(concernedModule);
            questionToSaved.setConcernedParagraph(concernedParagraph);
            questionToSaved.setConcernedProblem(concernedProblem);
            questionToSaved.setConcernedSection(concernedSection);
            questionToSaved.setConcernedSubSection(concernedSubSection);
            questionToSaved.setLevelofDifficulty(enumLevelofDifficulty);
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
        ServerResponse<Question> srQuestion = new ServerResponse<>();
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
        questionCopy.setConcernedChapter(questionToDuplicate.getConcernedChapter());
        questionCopy.setConcernedCourse(questionToDuplicate.getConcernedCourse());
        questionCopy.setConcernedModule(questionToDuplicate.getConcernedModule());
        questionCopy.setConcernedParagraph(questionToDuplicate.getConcernedParagraph());
        questionCopy.setConcernedProblem(questionToDuplicate.getConcernedProblem());
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
        ServerResponse<Question> srQuestion = new ServerResponse<>();
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
        ServerResponse<Question> srQuestion = new ServerResponse<>();
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
            srQuestion.setResponseCode(ResponseCode.INDICATIION_ADDED_TO_QUESTION);
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
        ServerResponse<Question> srQuestion = new ServerResponse<>();
        indicationId = indicationId.trim();
        contentId = contentId.trim();
        value = value.trim();
        questionId = questionId.trim();
        staffId = staffId.trim();

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
    public ServerResponse<Question> removeIndicationToQuestion(String indicationId, String questionId, String staffId) throws IndicationNotFoundException, QuestionNotFoundException, IndicationNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Question> addPropositionToQuestion(boolean valid, String value, String contentType, String staffId, String questionId) throws QuestionNotFoundException, StaffNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Question> updatePropositionToQuestion(String propositionId, boolean valid, String contentId, String value, String questionId, String staffId) throws PropositionNotFoundException, ContentNotFoundException, QuestionNotFoundException, PropositionNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Question> removePropositionToQuestion(String propositionId, String questionId, String staffId) throws PropositionNotFoundException, QuestionNotFoundException, PropositionNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Question> updateQuestion(EnumQuestionType questionType, EnumLevelofDifficulty levelofDifficulty, String courseId, String concernedPartId, String staffId) throws ConcernedPartNotBelongingToCourseException, QuestionNotBelongingToStaffException, StaffNotFoundException, CourseNotFoundException {
        return null;
    }

    @Override
    public ServerResponse<Question> deleteQuestion(String questionId, String staffId) throws QuestionNotFoundException, QuestionNotBelongingToStaffException {
        return null;
    }

    @Override
    public ServerResponse<Question> deleteQuestion(String questionId) throws QuestionNotFoundException {
        return null;
    }
}
