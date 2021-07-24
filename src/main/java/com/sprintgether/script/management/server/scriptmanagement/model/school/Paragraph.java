package com.sprintgether.script.management.server.scriptmanagement.model.school;

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
public class Paragraph {
    @Id
    String id;
    String title;
    int paragraphOrder;
    /******
     * We can add a paragraph summary during the creation or modification of a paragraph
     */
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    EnumCoursePartType paragraphType;
    @DBRef
    SubSection ownerSubSection;

    public Paragraph(String title, int paragraphOrder, SubSection ownerSubSection) {
        this.title = title;
        this.paragraphOrder = paragraphOrder;
        this.ownerSubSection = ownerSubSection;
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
        return "Paragraph{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", ownerSubSection=" + ownerSubSection +
                '}';
    }
}
