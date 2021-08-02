package com.sprintgether.script.management.server.scriptmanagement.form.school.school;

import lombok.Data;

@Data
public class SchoolList {
    int pageNumber;
    int PageSize;
    String sortBy1;
    String direction1;
    String sortBy2;
    String direction2;

    String schoolId;
    String schoolName;
    String instName;
    String keyword;
    public SchoolList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "name";
        this.direction2 = "ASC";
        this.sortBy2 = "acronym";
        this.schoolName = "";
        this.instName = "";
        this.keyword = "";
    }
}
