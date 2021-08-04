package com.sprintgether.script.management.server.scriptmanagement.form.program.subsection;

import lombok.Data;

@Data
public class SubSectionList {
    int pageNumber;
    int PageSize;
    String sortBy1;//title
    String direction1;
    String sortBy2;//chapterOrder
    String direction2;

    String sectionId;
    String subSectionTitle;
    String sectionTitle;
    String chapterTitle;
    String subSectionType;
    String moduleTitle;
    String courseTitle;
    String levelName;
    String optionName;
    String departmentName;
    String schoolName;
    String keyword;

    public SubSectionList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "chapterOrder";
        this.direction2 = "ASC";
        this.sortBy2 = "title";
        this.subSectionType = "ALL";
    }
}
