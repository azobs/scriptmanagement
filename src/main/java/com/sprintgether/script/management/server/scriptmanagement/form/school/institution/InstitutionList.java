package com.sprintgether.script.management.server.scriptmanagement.form.school.institution;

import lombok.Data;

@Data
public class InstitutionList {
    int pageNumber;
    int PageSize;
    String sortBy1;
    String direction1;
    String sortBy2;
    String direction2;

    String institutionId;
    String institutionName;
    String keyword;

    public InstitutionList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "name";
        this.direction2 = "ASC";
        this.sortBy2 = "acronym";
        this.institutionName = "";
        this.keyword = "";
    }
}
