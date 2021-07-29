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
    public static final int SCHOOL_NOT_CREATED = 135;
    public static final int SCHOOL_CREATED = 136;
    public static final int SCHOOL_NOT_UPDATED = 137;
    public static final int SCHOOL_UPDATED = 138;
    public static final int SCHOOL_NOT_DELETED = 139;
    public static final int SCHOOL_DELETED = 140;
    //Concerning Department objet
    public static final int DEPARTMENT_NOT_FOUND = 141;
    public static final int DEPARTMENT_FOUND = 142;
    public static final int DEPARTMENT_NOT_CREATED = 143;
    public static final int DEPARTMENT_CREATED = 144;
    public static final int DEPARTMENT_NOT_UPDATED = 145;
    public static final int DEPARTMENT_UPDATED = 146;
    public static final int DEPARTMENT_NOT_DELETED = 147;
    public static final int DEPARTMENT_DELETED = 148;
    //Concerning Option objet
    public static final int OPTION_NOT_FOUND = 149;
    public static final int OPTION_FOUND = 150;
    public static final int OPTION_NOT_CREATED = 151;
    public static final int OPTION_CREATED = 152;
    public static final int OPTION_NOT_UPDATED = 153;
    public static final int OPTION_UPDATED = 154;
    public static final int OPTION_NOT_DELETED = 155;
    public static final int OPTION_DELETED = 156;
    //Concerning Level objet
    public static final int LEVEL_NOT_FOUND = 157;
    public static final int LEVEL_FOUND = 158;
    public static final int LEVEL_NOT_CREATED = 159;
    public static final int LEVEL_CREATED = 160;
    public static final int LEVEL_NOT_UPDATED = 161;
    public static final int LEVEL_UPDATED = 162;
    public static final int LEVEL_NOT_DELETED = 163;
    public static final int LEVEL_DELETED = 164;
    //Concerning Course objet
    public static final int COURSE_NOT_FOUND = 165;
    public static final int COURSE_FOUND = 166;
    public static final int COURSE_NOT_CREATED = 167;
    public static final int COURSE_CREATED = 168;
    public static final int COURSE_NOT_UPDATED = 169;
    public static final int COURSE_UPDATED = 170;
    public static final int COURSE_NOT_DELETED = 171;
    public static final int COURSE_DELETED = 172;
    //Concerning CourseOutline objet
    public static final int COURSEOUTLINE_NOT_UPDATED = 173;
    public static final int COURSEOUTLINE_UPDATED = 174;
    //Concerning Content objet
    public static final int CONTENT_NOT_ADDED = 175;
    public static final int CONTENT_ADDED = 176;
    public static final int CONTENT_NOT_UPDATED = 177;
    public static final int CONTENT_UPDATED = 178;
    public static final int CONTENT_NOT_DELETED = 179;
    public static final int CONTENT_DELETED = 180;
    //Concerning Module objet
    public static final int MODULE_NOT_FOUND = 181;
    public static final int MODULE_FOUND = 182;
    public static final int MODULE_NOT_CREATED = 183;
    public static final int MODULE_CREATED = 184;
    public static final int MODULE_NOT_UPDATED = 185;
    public static final int MODULE_UPDATED = 186;
    public static final int MODULE_NOT_DELETED = 187;
    public static final int MODULE_DELETED = 188;






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
    public static final int EXCEPTION_INSTITUTION_NAME = 611;
    public static final int EXCEPTION_SCHOOL_FOUND = 612;
    public static final int EXCEPTION_SCHOOL_NAME = 613;
    public static final int EXCEPTION_DEPARTMENT_FOUND = 614;
    public static final int EXCEPTION_SAVED_DEPARTMENT = 615;
    public static final int EXCEPTION_SAVED_OPTION = 616;
    public static final int EXCEPTION_OPTION_FOUND = 617;
    public static final int EXCEPTION_SAVED_LEVEL = 618;
    public static final int EXCEPTION_LEVEL_FOUND = 619;
    public static final int EXCEPTION_COURSE_SAVED = 620;
    public static final int EXCEPTION_COURSE_FOUND = 621;
    public static final int EXCEPTION_STAFF_FOUND = 622;
    public static final int EXCEPTION_STAFF_COURSE_EXIST = 623;
    public static final int EXCEPTION_ENUM_COURSE_PART_TYPE = 624;
    public static final int EXCEPTION_CONTENT_ADDED = 625;
    public static final int EXCEPTION_CONTENT_FOUND = 626;
    public static final int EXCEPTION_COURSEOUTLINE_FOUND = 627;
    public static final int EXCEPTION_SCHOOL_DUPLICATED = 628;
    public static final int EXCEPTION_DEPARTMENT_DUPLICATED = 629;
    public static final int EXCEPTION_OPTION_DUPLICATED = 630;
    public static final int EXCEPTION_LEVEL_DUPLICATED = 631;
    public static final int EXCEPTION_COURSE_DUPLICATED = 632;
    public static final int EXCEPTION_MOUDULE_DUPLICATED = 633;
    public static final int EXCEPTION_MODULE_FOUND = 634;

    public static final int ERROR_IN_FORM_FILLED = 800;



}
