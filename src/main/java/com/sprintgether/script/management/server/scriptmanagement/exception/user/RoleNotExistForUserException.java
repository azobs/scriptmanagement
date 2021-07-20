package com.sprintgether.script.management.server.scriptmanagement.exception.user;

public class RoleNotExistForUserException extends Exception {
    public RoleNotExistForUserException() {
    }

    public RoleNotExistForUserException(String message) {
        super(message);
    }

    public RoleNotExistForUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNotExistForUserException(Throwable cause) {
        super(cause);
    }

    public RoleNotExistForUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
