package com.sprintgether.script.management.server.scriptmanagement.model.program;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Level;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    String id;
    String title;
    String courseCode;
    @DBRef
    Level ownerLevel;
    int nbreCredit;
    @DBRef
    List<Staff> listofLecturer = new ArrayList<Staff>();
    /******
     * We can add a course summary during the creation or modification of a course
     */
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    EnumCoursePartType courseType;
    @DBRef
    CourseOutline courseOutline;

    public Course(String id, String title, String courseCode, Level ownerLevel, int nbreCredit,
                  List<Staff> listofLecturer, CourseOutline courseOutline) {
        this.id = id;
        this.title = title;
        this.courseCode = courseCode;
        this.ownerLevel = ownerLevel;
        this.nbreCredit = nbreCredit;
        this.listofLecturer = listofLecturer;
        this.courseOutline = courseOutline;
    }

    public Course(String title, String courseCode, Level ownerLevel, List<Staff> listofLecturer) {
        this.title = title;
        this.courseCode = courseCode;
        this.ownerLevel = ownerLevel;
        this.listofLecturer = listofLecturer;
    }

    public Course(String title, String courseCode, Level ownerLevel, int nbreCredit,
                  List<Staff> listofLecturer) {
        this.title = title;
        this.courseCode = courseCode;
        this.ownerLevel = ownerLevel;
        this.nbreCredit = nbreCredit;
        this.listofLecturer = listofLecturer;
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
