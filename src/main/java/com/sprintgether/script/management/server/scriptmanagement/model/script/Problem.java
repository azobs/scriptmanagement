package com.sprintgether.script.management.server.scriptmanagement.model.script;

import com.sprintgether.script.management.server.scriptmanagement.model.school.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.el.stream.Optional;
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
    EnumProblemType problemType;
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
     * A problem must have a list of question.
     */
    @DBRef
    List<Question> listofQuestion = new ArrayList<Question>();
    /****
     * A question can have many indications than allows to get the answer
     */
    @DBRef
    List<Indication> listofIndication = new ArrayList<Indication>();

    public Problem(EnumProblemType problemType, Course concernedCourse,
                   Chapter concernedChapter, Section concernedSection,
                   SubSection concernedSubSection, Paragraph concernedParagraph,
                   List<Question> listofQuestion,
                   List<Indication> listofIndication) {
        this.problemType = problemType;
        this.concernedCourse = concernedCourse;
        this.concernedChapter = concernedChapter;
        this.concernedSection = concernedSection;
        this.concernedSubSection = concernedSubSection;
        this.concernedParagraph = concernedParagraph;
        this.listofQuestion = listofQuestion;
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
                ", listofQuestion=" + listofQuestion +
                ", listofIndication=" + listofIndication +
                '}';
    }
}
