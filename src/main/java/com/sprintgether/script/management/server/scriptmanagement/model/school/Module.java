package com.sprintgether.script.management.server.scriptmanagement.model.school;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@ToString
public class Module {
    @Id
    String id;
    int moduleOrder;
    String title;
    /******
     * We can add a module summary during the creation or modification of a module
     */
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    EnumCoursePartType moduleType;
    @DBRef
    CourseOutline ownerCourseOutline;

    public Module(String title, int moduleOrder, CourseOutline ownerCourseOutline) {
        this.title = title;
        this.moduleOrder = moduleOrder;
        this.ownerCourseOutline = ownerCourseOutline;
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
