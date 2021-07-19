package com.sprintgether.script.management.server.scriptmanagement.exception.user;

public class DuplicateRoleException extends Exception {
    public DuplicateRoleException() {
    }

    public DuplicateRoleException(String message) {
        super(message);
    }

    public DuplicateRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRoleException(Throwable cause) {
        super(cause);
    }

    public DuplicateRoleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
