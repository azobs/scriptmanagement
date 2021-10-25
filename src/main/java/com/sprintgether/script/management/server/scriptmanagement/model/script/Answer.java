package com.sprintgether.script.management.server.scriptmanagement.model.script;

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
public class Answer {
    @Id
    String id;
    String title;
    EnumScope answerScope;
    @DBRef
    Question concernedQuestion;
    @DBRef
    Staff ownerStaff;
    @DBRef
    List<Content> listofContent = new ArrayList<>();


    public Answer(Question concernedQuestion, List<Content> listofContent) {
        this.concernedQuestion = concernedQuestion;
        this.listofContent = listofContent;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id='" + id + '\'' +
                ", concernedQuestion=" + concernedQuestion.getId() +
                ", content=" + listofContent +
                '}';
    }
}
