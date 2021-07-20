package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.user.UserRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateUserException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.RoleNotExistForUserException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.RoleNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.UserNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Role;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {

        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public ServerResponse<User> findUserByUsername(String username) {
        ServerResponse<User> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.USER_NOT_FOUND);
        serverResponse.setAssociatedObject(null);

        serverResponse.setAssociatedObject(null);
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if(optionalUser.isPresent()){
            serverResponse.setResponseCode(ResponseCode.USER_FOUND);
            serverResponse.setAssociatedObject(optionalUser.get());
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<User> saveUser(String username, String password) throws DuplicateUserException {
        ServerResponse<User> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.USER_NOT_CREATED);
        serverResponse.setAssociatedObject(null);

        ServerResponse<User> srUserExist = this.findUserByUsername(username);
        if(srUserExist.getResponseCode()==ResponseCode.USER_FOUND){
            throw new DuplicateUserException("The specified username is already taken. Please change it");
        }
        Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();
        p.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
        User userASaved =  new User();
        userASaved.setUsername(username);
        userASaved.setPassword(p.encode(password));
        userASaved.setActive(false);
        /*
        On enregistre le User dans la BD
         */
        User userSaved = userRepository.save(userASaved);
        serverResponse.setResponseCode(ResponseCode.USER_CREATED);
        serverResponse.setErrorMessage("The user has been successfully created");
        serverResponse.setAssociatedObject(userSaved);
        return serverResponse;
    }

    @Override
    public ServerResponse<User> updatePassword(String username,
                                               String newPassword) throws UserNotFoundException {
        ServerResponse<User> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.USER_NOT_UPDATED);
        serverResponse.setAssociatedObject(null);

        ServerResponse<User> srUserExist = this.findUserByUsername(username);
        if(srUserExist.getResponseCode()==ResponseCode.USER_NOT_FOUND){
            throw new UserNotFoundException("The username specified is not found. Please check it");
        }
        if(srUserExist.getResponseCode()==ResponseCode.USER_FOUND){
            User userFound = srUserExist.getAssociatedObject();
            Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();
            p.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
            userFound.setPassword(p.encode(newPassword));
            User userUpdated = userRepository.save(userFound);

            serverResponse.setErrorMessage("Password Updated successfully");
            serverResponse.setResponseCode(ResponseCode.USER_UPDATED);
            serverResponse.setAssociatedObject(userUpdated);
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<User> addRoleToUser(String username, String roleName) throws RoleNotFoundException, UserNotFoundException {
        ServerResponse<User> srUser = new ServerResponse<>();
        srUser.setResponseCode(ResponseCode.USER_NOT_UPDATED);
        /****
         * Searching for the user object
         */
        ServerResponse<User> srUser1 = this.findUserByUsername(username);
        ServerResponse<Role> srRole = roleService.findByRoleName(roleName);
        if(srUser1.getResponseCode() == ResponseCode.USER_FOUND
                && srRole.getResponseCode() == ResponseCode.ROLE_FOUND){
            User user = srUser1.getAssociatedObject();
            Role role = srRole.getAssociatedObject();
            user.getListofRole().add(role);
            User userUpdated = userRepository.save(user);
            srUser.setErrorMessage("Role specified by the "+roleName+" has been successfully added to "+username);
            srUser.setResponseCode(ResponseCode.USER_UPDATED);
            srUser.setAssociatedObject(userUpdated);
        }
        else{
            if(srUser1.getResponseCode() == ResponseCode.USER_NOT_FOUND){
                throw new UserNotFoundException("The username specified does not match any user");
            }
            if(srRole.getResponseCode() == ResponseCode.ROLE_NOT_FOUND){
                throw new UserNotFoundException("The roleName specified does not match any Role");
            }
        }
        return srUser;
    }

    @Override
    public ServerResponse<User> removeRoleToUser(String username, String roleName)
            throws RoleNotExistForUserException, UserNotFoundException {
        ServerResponse<User> srUser = new ServerResponse<>();
        ServerResponse<User> srUser1 = this.findUserByUsername(username);
        ServerResponse<Role> srRole = roleService.findByRoleName(roleName);
        if(srUser1.getResponseCode() == ResponseCode.USER_NOT_FOUND){
            throw new UserNotFoundException("The username specified does not match any user");
        }

        Role roleToRemove = null;
        if(srUser1.getResponseCode() == ResponseCode.USER_FOUND){
            User userConcerned = srUser1.getAssociatedObject();
            for (Role role : userConcerned.getListofRole()){
                if(role.getRoleName().equalsIgnoreCase(roleName)){
                    roleToRemove = role;
                }
            }
            if(roleToRemove == null){
                throw new RoleNotExistForUserException("The role specified don't belonging to the user");
            }
            userConcerned.getListofRole().remove(roleToRemove);
            User userUpdated = userRepository.save(userConcerned);
            srUser.setErrorMessage("Role specified by the "+roleName+" has been successfully removed to "+username);
            srUser.setResponseCode(ResponseCode.USER_UPDATED);
            srUser.setAssociatedObject(userUpdated);
        }


        return srUser;
    }

    @Override
    public ServerResponse<User> activateUser(String username, boolean active) throws UserNotFoundException {
        ServerResponse<User> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.USER_NOT_UPDATED);
        serverResponse.setAssociatedObject(null);
        ServerResponse<User> srUserExist = this.findUserByUsername(username);
        if(srUserExist.getResponseCode()==ResponseCode.USER_NOT_FOUND){
            throw new UserNotFoundException("The username specified is not found. Please check it");
        }
        if(srUserExist.getResponseCode()==ResponseCode.USER_FOUND){
            User userFound = srUserExist.getAssociatedObject();
            userFound.setActive(active);
            User userUpdated = userRepository.save(userFound);

            serverResponse.setErrorMessage("Account activation set successfully to "+active);
            serverResponse.setResponseCode(ResponseCode.USER_UPDATED);
            serverResponse.setAssociatedObject(userUpdated);
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<User> deleteUser(String username) throws UserNotFoundException {
        ServerResponse<User> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.USER_NOT_DELETED);
        serverResponse.setAssociatedObject(null);

        ServerResponse<User> srUserExist = this.findUserByUsername(username);
        if(srUserExist.getResponseCode()==ResponseCode.USER_NOT_FOUND){
            //Ceci signifie qu'il y deja dans la BD un user avec le meme username
            throw new UserNotFoundException("The username specified is not found. Please check it");
        }
        if(srUserExist.getResponseCode()==ResponseCode.USER_FOUND){
            User userFound = srUserExist.getAssociatedObject();
            userRepository.delete(userFound);
            serverResponse.setResponseCode(ResponseCode.USER_DELETED);
            serverResponse.setAssociatedObject(userFound);
        }
        return serverResponse;
    }
}
