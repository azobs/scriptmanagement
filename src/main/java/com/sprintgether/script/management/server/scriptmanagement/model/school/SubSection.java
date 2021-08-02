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
public class SubSection {
    @Id
    String id;
    String title;
    int subSectionOrder;
    /******
     * We can add a subsection summary during the creation or modification of a subsection
     */
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    EnumCoursePartType subSectionType;
    @DBRef
    Section ownerSection;

    public SubSection(String title, int subSectionOrder, Section ownerSection) {
        this.title = title;
        this.subSectionOrder = subSectionOrder;
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
                '}';
    }
}
