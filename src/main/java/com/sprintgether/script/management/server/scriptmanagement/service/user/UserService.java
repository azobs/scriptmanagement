package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;


public interface UserService {
     ServerResponse<User> findUserByUsername(String username);
     ServerResponse<User> findUserById(String userId);
     ServerResponse<User> saveUser(String username, String password) throws DuplicateUserException;
     ServerResponse<User> updatePassword(String username, String newPassword) throws UserNotFoundException;
     ServerResponse<User> updateUsername(String userId, String newUsername)
             throws UserNotFoundException, DuplicateUserException;
     ServerResponse<User> addRoleToUser(String username, String roleName) throws RoleNotFoundException, RoleExistForUserException, UserNotFoundException;
     ServerResponse<User> removeRoleToUser(String username, String roleName) throws RoleNotExistForUserException, UserNotFoundException;
     ServerResponse<User> activateUser(String username, boolean active) throws UserNotFoundException;
     ServerResponse<User> deleteUser(String username) throws UserNotFoundException;


}
