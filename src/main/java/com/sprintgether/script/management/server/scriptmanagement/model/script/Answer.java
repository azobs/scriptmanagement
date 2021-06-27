package com.sprintgether.script.management.server.scriptmanagement.model.script;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Answer {
    @Id
    String id;
    Float mark;
    @DBRef
    Question concernedQuestion;
    @DBRef
    Content content;

    public Answer(Float mark, Question concernedQuestion, Content content) {
        this.mark = mark;
        this.concernedQuestion = concernedQuestion;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id='" + id + '\'' +
                ", mark=" + mark +
                ", concernedQuestion=" + concernedQuestion.getId() +
                ", content=" + content.getValue() +
                '}';
    }
}
