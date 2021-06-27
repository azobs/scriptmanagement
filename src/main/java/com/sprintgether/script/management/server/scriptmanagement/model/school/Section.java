package com.sprintgether.script.management.server.scriptmanagement.model.school;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@ToString(exclude = "listofSubsection")
@NoArgsConstructor
public class Section {
    @Id
    String id;
    String title;
    @DBRef
    Chapter ownerChapter;
    @DBRef
    List<SubSection> listofSubsection = new ArrayList<SubSection>();

    public Section(String title, Chapter ownerChapter) {
        this.title = title;
        this.ownerChapter = ownerChapter;
    }

    public String getTitle() {
        try{
            return this.title;
        }
        catch(NullPointerException e){
            return "";
        }
    }

    @Override
    public String toString() {
        return "Section{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", ownerChapter=" + ownerChapter +
                ", listofSubsection=" + listofSubsection +
                '}';
    }
}
