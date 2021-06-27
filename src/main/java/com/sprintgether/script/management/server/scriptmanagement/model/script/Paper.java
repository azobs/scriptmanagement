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
public class Paper {
    @Id
    String id;
    String title;
    EnumPaperType paperType;
    @DBRef
    List<Question> listofQuestion = new ArrayList<Question>();
    @DBRef
    List<Problem> listofProblem = new ArrayList<Problem>();
    @DBRef
    List<Indication> listofIndication = new ArrayList<Indication>();

    public Paper(String title, EnumPaperType paperType) {
        this.title = title;
        this.paperType = paperType;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", paperType=" + paperType +
                ", listofQuestion=" + listofQuestion +
                ", listofProblem=" + listofProblem +
                ", listofIndication=" + listofIndication +
                '}';
    }
}
