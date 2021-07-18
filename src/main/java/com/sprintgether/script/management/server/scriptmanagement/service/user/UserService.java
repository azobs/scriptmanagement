package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateUserException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.UserNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;


public interface UserService {
    public ServerResponse<User> findUserByUsername(String username);
    public ServerResponse<User> saveUser(String username, String password) throws DuplicateUserException;
    public ServerResponse<User> updatePassword(String username, String newPassword) throws UserNotFoundException;
    public ServerResponse<User> deleteUser(String username) throws UserNotFoundException;
}
