package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;

@Data
public class SchoolFormList {
    int pageNumber;
    int PageSize;
    String sortBy1;
    String direction1;
    String sortBy2;
    String direction2;

    String name;
    String keyword;
    public SchoolFormList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "NAME";
        this.direction2 = "ASC";
        this.sortBy2 = "ACRONYM";
        this.name = "";
        this.keyword = "";
    }
}
