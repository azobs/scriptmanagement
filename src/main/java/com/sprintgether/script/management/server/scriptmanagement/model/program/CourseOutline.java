package com.sprintgether.script.management.server.scriptmanagement.model.program;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CourseOutline {
    @Id
    String id;
    String title;

    public CourseOutline(String title) {
        this.title = title;
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
