package com.sprintgether.script.management.server.scriptmanagement.form.program.course;

import lombok.Data;

@Data
public class CourseList {
    int pageNumber;
    int PageSize;
    String sortBy1;//title
    String direction1;
    String sortBy2;//courseCode
    String direction2;
    String sortBy3;//nbreCredit
    String direction3;

    String courseId;
    String courseTitle;
    String courseType;
    String levelName;
    String optionName;
    String departmentName;
    String schoolName;
    String keyword;

    public CourseList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "title";
        this.direction2 = "ASC";
        this.sortBy2 = "courseCode";
        this.direction3 = "ASC";
        this.sortBy3 = "nbreCredit";
        this.courseTitle = "";
        this.courseType = "ALL";
        this.levelName = "";
        this.schoolName = "";
        this.departmentName = "";
        this.optionName = "";
        this.keyword = "";
    }
}
