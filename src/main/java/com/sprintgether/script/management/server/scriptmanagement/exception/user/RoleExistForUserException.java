package com.sprintgether.script.management.server.scriptmanagement.exception.user;

public class RoleExistForUserException extends Exception {
    public RoleExistForUserException() {
    }

    public RoleExistForUserException(String message) {
        super(message);
    }

    public RoleExistForUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleExistForUserException(Throwable cause) {
        super(cause);
    }

    public RoleExistForUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
