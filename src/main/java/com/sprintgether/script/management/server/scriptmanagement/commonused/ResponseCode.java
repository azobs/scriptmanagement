package com.sprintgether.script.management.server.scriptmanagement.commonused;

public  class ResponseCode {
    public static final int INITIALISATION = 0;
    public static final int BAD_REQUEST = 1;
    public static final int NORMAL_RESPONSE = 100;

    //Concerning User objet
    public static final int USER_NOT_FOUND = 101;
    public static final int USER_FOUND = 102;
    public static final int USER_NOT_CREATED = 103;
    public static final int USER_CREATED = 104;
    public static final int USER_NOT_UPDATED = 105;
    public static final int USER_UPDATED = 106;
    public static final int USER_NOT_DELETED = 107;
    public static final int USER_DELETED = 108;
    //Concerning Staff object
    public static final int STAFF_NOT_FOUND = 109;
    public static final int STAFF_FOUND = 110;
    public static final int STAFF_NOT_CREATED = 111;
    public static final int STAFF_CREATED = 112;
    public static final int STAFF_NOT_UPDATE = 113;
    public static final int STAFF_UPDATE = 114;
    public static final int STAFF_NOT_DELETE = 115;
    public static final int STAFF_DELETE = 116;
    //Concerning Role objet
    public static final int ROLE_NOT_FOUND = 117;
    public static final int ROLE_FOUND = 118;
    public static final int ROLE_NOT_CREATED = 119;
    public static final int ROLE_CREATED = 120;
    public static final int ROLE_NOT_UPDATED = 121;
    public static final int ROLE_UPDATED = 122;
    public static final int ROLE_NOT_DELETED = 123;
    public static final int ROLE_DELETED = 124;
    //Concerning Institution objet
    public static final int INSTITUTION_NOT_FOUND = 125;
    public static final int INSTITUTION_FOUND = 126;
    public static final int INSTITUTION_NOT_CREATED = 127;
    public static final int INSTITUTION_CREATED = 128;
    public static final int INSTITUTION_NOT_UPDATED = 129;
    public static final int INSTITUTION_UPDATED = 130;
    public static final int INSTITUTION_NOT_DELETED = 131;
    public static final int INSTITUTION_DELETED = 132;
    //Concerning School objet
    public static final int SCHOOL_NOT_FOUND = 133;
    public static final int SCHOOL_FOUND = 134;
    public static final int SCHOOL_INSTITUTION_NOT_FOUND = 135;
    public static final int SCHOOL_INSTITUTION_FOUND = 136;
    public static final int SCHOOL_NOT_CREATED = 137;
    public static final int SCHOOL_CREATED = 138;
    public static final int SCHOOL_NOT_UPDATED = 139;
    public static final int SCHOOL_UPDATED = 140;
    public static final int SCHOOL_NOT_DELETED = 141;
    public static final int SCHOOL_DELETED = 142;
    //Common used value
    public static final int EXCEPTION_SAVED_USER = 600;
    public static final int EXCEPTION_UPDATED_USER = 601;
    public static final int EXCEPTION_SAVED_STAFF = 602;
    public static final int EXCEPTION_ENUM_STAFF_TYPE = 603;
    public static final int EXCEPTION_UPDATED_STAFF = 604;
    public static final int EXCEPTION_SAVED_ROLE = 605;
    public static final int EXCEPTION_UPDATED_ROLE = 606;
    public static final int EXCEPTION_SAVED_INSTITUTION = 607;
    public static final int EXCEPTION_UPDATED_INSTITUTION = 608;
    public static final int EXCEPTION_SAVED_SCHOOL = 609;
    public static final int EXCEPTION_UPDATED_SCHOOL = 610;

    public static final int ERROR_IN_FORM_FILLED = 800;
}
