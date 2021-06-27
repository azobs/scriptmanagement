package com.sprintgether.script.management.server.scriptmanagement.model.script;

import com.sprintgether.script.management.server.scriptmanagement.model.school.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    EnumQuestionType questionType;
    EnumLevelofDifficulty levelofDifficulty;
    /******
     * A question can concerned a paragraph, a subsection, a section, a chapter or
     * a whole course. Then for the above field, only one must have a value other
     * than null.
     */
    @DBRef
    Course concernedCourse;
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
     * A question must have his proper content
     */
    @DBRef
    Content content;

    public Question(EnumQuestionType questionType,
                    EnumLevelofDifficulty levelofDifficulty,
                    Course concernedCourse, Chapter concernedChapter,
                    Section concernedSection, SubSection concernedSubSection,
                    Paragraph concernedParagraph,
                    List<Proposition> listofProposition,
                    List<Indication> listofIndication, Content content) {
        this.questionType = questionType;
        this.levelofDifficulty = levelofDifficulty;
        this.concernedCourse = concernedCourse;
        this.concernedChapter = concernedChapter;
        this.concernedSection = concernedSection;
        this.concernedSubSection = concernedSubSection;
        this.concernedParagraph = concernedParagraph;
        this.listofProposition = listofProposition;
        this.listofIndication = listofIndication;
        this.content = content;
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
                ", content=" + content.getValue() +
                '}';
    }
}
