package com.sprintgether.script.management.server.scriptmanagement.model.script;

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
    @DBRef
    Question concernedQuestion;
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
