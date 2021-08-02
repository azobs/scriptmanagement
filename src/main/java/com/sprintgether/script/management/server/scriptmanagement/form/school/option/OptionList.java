package com.sprintgether.script.management.server.scriptmanagement.form.school.option;

import lombok.Data;

@Data
public class OptionList {
    int pageNumber;
    int PageSize;
    String sortBy1;
    String direction1;
    String sortBy2;
    String direction2;

    String optionId;
    String optionName;
    String departmentName;
    String schoolName;
    String keyword;

    public OptionList() {
        this.pageNumber = 0;
        this.PageSize = 10;
        this.direction1  = "ASC";
        this.sortBy1 = "name";
        this.direction2 = "ASC";
        this.sortBy2 = "acronym";
        this.schoolName = "";
        this.departmentName = "";
        this.optionName = "";
        this.keyword = "";
    }
}
