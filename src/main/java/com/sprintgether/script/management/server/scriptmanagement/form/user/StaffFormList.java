package com.sprintgether.script.management.server.scriptmanagement.form.user;

import lombok.Data;

@Data
public class StaffFormList {
    int pageNumber;
    int PageSize;
    String sortBy1;
    String direction1;
    String sortBy2;
    String direction2;
    String sortBy3;
    String direction3;
    String sortBy4;
    String direction4;

    /**
     * type staff to be selected
     */
    String staffType;

    String email;
    String keyword;

    public StaffFormList() {
        this.PageSize = 10;
        this.pageNumber = 0;
        this.sortBy1 = "EMAIL";
        this.direction1 = "ASC";
        this.sortBy2  = "FIRSTNAME";
        this.direction2 = "ASC";
        this.sortBy3 = "LASTNAME";
        this.direction3 = "ASC";
        this.sortBy4 = "USERNAME";
        this.direction4 = "ASC";
        this.staffType = "ALL";
        this.email = "";
        this.keyword = "";
    }
}
