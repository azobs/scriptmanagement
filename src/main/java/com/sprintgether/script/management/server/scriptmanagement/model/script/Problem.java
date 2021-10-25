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
public class Problem {
    @Id
    String id;
    String title;
    EnumProblemType problemType;
    EnumLevelofDifficulty levelofDifficulty;
    EnumScope problemScope;
    /**********************
     * A question must be proposed by a staff. Any staff except class perfect can propose a question
     * for any course.
     */
    @DBRef
    Staff ownerStaff;
    /******
     * A question can concerned a paragraph, a subsection, a section, a chapter or
     * a whole course. Refer to question class to complete explanation about those field.
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
    /******************
     * A problem must have a list of content before the list of question
     * concerning this content. The content must be a list beacause we can have a
     * text, then a table, then an image and at the end a text. All the content
     * in that list must be display in the order that it was inserted in the system
     */
    List<Content> listofContent = new ArrayList<>();
    /*****
     * A problem must have a list of question. But since id a question is for a problem, that question
     * must keep a trace of the ownerproblem. means if we want the list of the question of a problem
     * we must search in the question table all the question with the problem trace.
     */
    @DBRef
    List<Question> listofQuestion = new ArrayList<Question>();
    /****
     * A question can have many indications than allows to get the answer
     */
    @DBRef
    List<Indication> listofIndication = new ArrayList<Indication>();

    public Problem(EnumProblemType problemType, EnumLevelofDifficulty levelofDifficulty,
                   Staff ownerStaff, Course concernedCourse, Module concernedModule,
                   Chapter concernedChapter, Section concernedSection, SubSection concernedSubSection,
                   Paragraph concernedParagraph, List<Content> listofContent,
                   List<Indication> listofIndication) {
        this.problemType = problemType;
        this.levelofDifficulty = levelofDifficulty;
        this.ownerStaff = ownerStaff;
        this.concernedCourse = concernedCourse;
        this.concernedModule = concernedModule;
        this.concernedChapter = concernedChapter;
        this.concernedSection = concernedSection;
        this.concernedSubSection = concernedSubSection;
        this.concernedParagraph = concernedParagraph;
        this.listofContent = listofContent;
        this.listofIndication = listofIndication;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id='" + id + '\'' +
                ", problemType=" + problemType +
                ", concernedCourse=" +concernedCourse.getTitle() +
                ", concernedChapter=" + concernedChapter.getTitle() +
                ", concernedSection=" + concernedSection.getTitle() +
                ", concernedSubSection=" + concernedSubSection.getTitle() +
                ", concernedParagraph=" + concernedParagraph.getTitle() +
                ", listofContent=" + listofContent +
                ", listofIndication=" + listofIndication +
                '}';
    }
}
