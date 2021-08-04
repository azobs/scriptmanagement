package com.sprintgether.script.management.server.scriptmanagement.form.program.module;

import lombok.Data;

@Data
public class ModuleList {
    int pageNumber;
    int PageSize;
    String sortBy1;//title
    String direction1;
    String sortBy2;//moduleOrder
    String direction2;

    String courseId;
    String moduleType;
    String moduleTitle;
    String courseTitle;
    String levelName;
    String optionName;
    String departmentName;
    String schoolName;
    String keyword;

    public ModuleList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "moduleOrder";
        this.direction2 = "ASC";
        this.sortBy2 = "title";
        this.moduleTitle = "";
        this.moduleType = "ALL";
        this.courseTitle = "";
        this.levelName = "";
        this.schoolName = "";
        this.departmentName = "";
        this.optionName = "";
        this.keyword = "";
    }
}
