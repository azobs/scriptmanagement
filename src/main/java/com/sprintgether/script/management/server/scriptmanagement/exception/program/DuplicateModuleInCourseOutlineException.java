package com.sprintgether.script.management.server.scriptmanagement.exception.program;

public class DuplicateModuleInCourseOutlineException extends Exception{
    public DuplicateModuleInCourseOutlineException() {
    }

    public DuplicateModuleInCourseOutlineException(String message) {
        super(message);
    }

    public DuplicateModuleInCourseOutlineException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateModuleInCourseOutlineException(Throwable cause) {
        super(cause);
    }

    public DuplicateModuleInCourseOutlineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
