package com.sprintgether.script.management.server.scriptmanagement.model.script;

import com.sprintgether.script.management.server.scriptmanagement.model.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
public class Question {
    @Id
    String id;
    String title;
    EnumQuestionType questionType;
    EnumLevelofDifficulty levelofDifficulty;
    EnumScope questionScope;
    /**********************
     * A question must be proposed by a staff. Any staff except class perfect can propose a question
     * for any course.
     */
    @DBRef
    Staff ownerStaff;
    /******
     * A question can concerned a paragraph, a subsection, a section, a module, a chapter or
     * a whole course.
     * If it concerns a whole course then concernedModule, concernedChapter, concernedSection, concernedSubSection,
     * and concernedParagraph must be null. Only concernedCourse must have a value.
     * BUT
     * If it concerns a Module then concernedChapter, concernedSection, concernedSubSection,
     * and concernedParagraph must be null. Only concernedCourse and concernedModule must have a value.
     * If it concerns a chapter then concernedSection, concernedSubSection,
     * and concernedParagraph must be null. Only concernedCourse, concernedModule and concernedChapter
     * must have a value.
     * If it concerns a Section then concernedSection, concernedSubSection,
     * and concernedParagraph must be null. Only concernedCourse, concernedModule, concernedChapter,
     *  and concernedSection must have a value.
     * If it concerns a SubSection then concernedSubSection,
     * and concernedParagraph must be null. Only concernedCourse, concernedModule, concernedChapter,
     * concernedSection, concernedSubSection must have a value.
     * If it concerns a Paragraph then
     * concernedCourse, concernedModule, concernedChapter,
     * concernedSection, concernedSubSection and concernedParagraph must have a value.
     */
    @DBRef
    Course concernedCourse;
    @DBRef
    Module concernedModule;
    @DBRef
    Chapter concernedChapter;
    @DBRef
    Section concernedSection;
    @DBRef
    SubSection concernedSubSection;
    @DBRef
    Paragraph concernedParagraph;
    /*****
     * A question can have a list of proposition if it's a MCQ otherwise the field
     * must be null
     */
    @DBRef
    List<Proposition> listofProposition = new ArrayList<Proposition>();
    /****
     * A question can have many indications than allows to get the answer
     */
    @DBRef
    List<Indication> listofIndication = new ArrayList<Indication>();
    /*****
     * A question must have his proper content. And that content can be a list
     * of several content ordering in one maner. Text, image, text, table and so on.
     */
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    /****************************************************************
     * A question must have a multiple answer (a list of answer)
     */
    @DBRef
    List<Answer> listofAnswer = new ArrayList<>();

    /****************************************************
     * A question can be related to a problem. In that fact
     * the question can be display if and only if the problem will be
     * This field can be null because all the question ne 
     */
    /*@DBRef
    Problem concernedProblem;*/




    public Question(EnumQuestionType questionType, EnumLevelofDifficulty levelofDifficulty,
                    Staff ownerStaff, Course concernedCourse, Module concernedModule,
                    Chapter concernedChapter, Section concernedSection, SubSection concernedSubSection,
                    Paragraph concernedParagraph, List<Proposition> listofProposition,
                    List<Indication> listofIndication, List<Content> listofContent) {
        this.questionType = questionType;
        this.levelofDifficulty = levelofDifficulty;
        this.ownerStaff = ownerStaff;
        this.concernedCourse = concernedCourse;
        this.concernedModule = concernedModule;
        this.concernedChapter = concernedChapter;
        this.concernedSection = concernedSection;
        this.concernedSubSection = concernedSubSection;
        this.concernedParagraph = concernedParagraph;
        this.listofProposition = listofProposition;
        this.listofIndication = listofIndication;
        this.listofContent = listofContent;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", questionType=" + questionType +
                ", levelofDifficulty=" + levelofDifficulty +
                ", concernedCourse=" + concernedCourse.getTitle() +
                ", concernedChapter=" + concernedChapter.getTitle() +
                ", concernedSection=" + concernedSection.getTitle() +
                ", concernedSubSection=" + concernedSubSection.getTitle() +
                ", concernedParagraph=" + concernedParagraph.getTitle() +
                ", listofProposition=" + listofProposition +
                ", listofIndication=" + listofIndication +
                ", listofContent=" + listofContent+
                '}';
    }
}
