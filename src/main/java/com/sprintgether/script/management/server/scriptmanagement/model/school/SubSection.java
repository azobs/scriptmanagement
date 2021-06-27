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
@ToString(exclude = "listofParagraph")
@NoArgsConstructor
public class SubSection {
    @Id
    String id;
    String title;
    @DBRef
    Section ownerSection;
    @DBRef
    List<Paragraph> listofParagraph =  new ArrayList<Paragraph>();

    public SubSection(String title, Section ownerSection) {
        this.title = title;
        this.ownerSection = ownerSection;
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
        return "SubSection{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", ownerSection=" + ownerSection +
                ", listofParagraph=" + listofParagraph +
                '}';
    }
}
