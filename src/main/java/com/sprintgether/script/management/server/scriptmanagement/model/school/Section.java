package com.sprintgether.script.management.server.scriptmanagement.model.school;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Section {
    @Id
    String id;
    String title;
    int sectionOrder;
    /******
     * We can add a section summary during the creation or modification of a section
     */
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    EnumCoursePartType sectionType;
    @DBRef
    Chapter ownerChapter;

    public Section(String title, int sectionOrder, Chapter ownerChapter) {
        this.title = title;
        this.sectionOrder = sectionOrder;
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
                '}';
    }
}
