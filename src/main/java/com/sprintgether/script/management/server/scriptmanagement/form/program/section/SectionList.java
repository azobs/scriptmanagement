package com.sprintgether.script.management.server.scriptmanagement.form.program.section;

import lombok.Data;

@Data
public class SectionList {
    int pageNumber;
    int PageSize;
    String sortBy1;//title
    String direction1;
    String sortBy2;//chapterOrder
    String direction2;

    String chapterId;
    String sectionTitle;
    String chapterTitle;
    String sectionType;
    String moduleTitle;
    String courseTitle;
    String levelName;
    String optionName;
    String departmentName;
    String schoolName;
    String keyword;

    public SectionList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "chapterOrder";
        this.direction2 = "ASC";
        this.sortBy2 = "title";
        this.sectionType = "ALL";
    }
}
