package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;

@Data
public class ModuleFormList {
    int pageNumber;
    int PageSize;
    String sortBy1;//title
    String direction1;
    String sortBy2;//moduleOrder
    String direction2;

    String moduleTitle;
    String moduleType;
    String courseTitle;
    String levelName;
    String optionName;
    String departmentName;
    String schoolName;
    String keyword;

    public ModuleFormList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "title";
        this.direction2 = "ASC";
        this.sortBy2 = "courseTitle";
        this.courseTitle = "";
        this.courseTitle = "ALL";
        this.levelName = "";
        this.schoolName = "";
        this.departmentName = "";
        this.optionName = "";
        this.keyword = "";
    }
}
