package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.user.UserRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateUserException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.UserNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            //Ceci signifie qu'il y deja dans la BD un user avec le meme username
            throw new DuplicateUserException("The username specified is already taken. Please change it");
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
        serverResponse.setAssociatedObject(userSaved);
        return serverResponse;
    }

    @Override
    public ServerResponse<User> updatePassword(String username, String newPassword) throws UserNotFoundException, DuplicateUserException {
        ServerResponse<User> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.USER_NOT_UPDATED);
        serverResponse.setAssociatedObject(null);

        ServerResponse<User> srUserExist = this.findUserByUsername(username);
        if(srUserExist.getResponseCode()==ResponseCode.USER_NOT_FOUND){
            //Ceci signifie qu'il y deja dans la BD un user avec le meme username
            throw new UserNotFoundException("The username specified is not found. Please check it");
        }
        if(srUserExist.getResponseCode()==ResponseCode.USER_FOUND){
            User userFound = srUserExist.getAssociatedObject();
            Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();
            p.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
            userFound.setPassword(p.encode(newPassword));
            userRepository.save(userFound);
            serverResponse.setResponseCode(ResponseCode.USER_UPDATED);
            serverResponse.setAssociatedObject(userFound);
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
