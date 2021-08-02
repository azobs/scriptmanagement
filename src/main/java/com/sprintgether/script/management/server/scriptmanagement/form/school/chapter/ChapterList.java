package com.sprintgether.script.management.server.scriptmanagement.form.school.chapter;

import lombok.Data;

@Data
public class ChapterList {
    int pageNumber;
    int PageSize;
    String sortBy1;//title
    String direction1;
    String sortBy2;//chapterOrder
    String direction2;

    String chapterId;
    String chapterTitle;
    String chapterType;
    String moduleTitle;
    String courseTitle;
    String levelName;
    String optionName;
    String departmentName;
    String schoolName;
    String keyword;

    public ChapterList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "moduleOrder";
        this.direction2 = "ASC";
        this.sortBy2 = "title";
        this.courseTitle = "";
        this.chapterType = "ALL";
        this.levelName = "";
        this.schoolName = "";
        this.departmentName = "";
        this.optionName = "";
        this.keyword = "";
    }
}
