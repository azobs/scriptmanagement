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
public class Chapter {
    @Id
    String id;
    String title;
    int chapterOrder;
    /******
     * We can add a chapter summary during the creation or modification of a chapter
     */
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    EnumCoursePartType chapterType;
    @DBRef
    Module ownerModule;

    public Chapter(String title, int chapterOrder, Module ownerModule) {
        this.title = title;
        this.chapterOrder = chapterOrder;
        this.ownerModule = ownerModule;
    }
    public String getTitle() {
        try{
            return this.title;
        }
        catch(NullPointerException e){
            return "";
        }
    }

}
