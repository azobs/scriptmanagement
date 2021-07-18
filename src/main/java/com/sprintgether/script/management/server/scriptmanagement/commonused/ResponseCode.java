package com.sprintgether.script.management.server.scriptmanagement.commonused;

public  class ResponseCode {
    public static final int BAD_REQUEST = 100;
    public static final int NORMAL_RESPONSE = 200;
    //Concerning User objet
    public static final int USER_NOT_FOUND = 201;
    public static final int USER_FOUND = 202;
    public static final int USER_NOT_CREATED = 203;
    public static final int USER_CREATED = 204;
    public static final int USER_NOT_UPDATED = 205;
    public static final int USER_UPDATED = 206;
    public static final int USER_NOT_DELETED = 205;
    public static final int USER_DELETED = 206;
    //Concerning Staff object
    public static final int STAFF_NOT_FOUND = 207;
    public static final int STAFF_FOUND = 208;
    public static final int STAFF_NOT_CREATED = 209;
    public static final int STAFF_CREATED = 210;
    public static final int STAFF_NOT_UPDATE = 211;
    public static final int STAFF_UPDATE = 212;
    public static final int STAFF_NOT_DELETE = 213;
    public static final int STAFF_DELETE = 214;
    //Common used value
    public static final int ERROR_RESPONSE = 400;
    public static final int EXCEPTION_SAVED_USER = 500;
    public static final int EXCEPTION_SAVED_STAFF = 501;
    public static final int EXCEPTION_ENUM_STAFF_TYPE = 502;
    public static final int EXCEPTION_UPDATED_USER = 500;
    public static final int EXCEPTION_UPDATED_STAFF = 501;
}
