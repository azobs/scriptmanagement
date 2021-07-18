package com.sprintgether.script.management.server.scriptmanagement.controller.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateUserException;
import com.sprintgether.script.management.server.scriptmanagement.form.user.StaffForm;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;
import com.sprintgether.script.management.server.scriptmanagement.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/user/staff")
public class StaffController {

    UserService userService;

    public StaffController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(path = "/testAPI")
    public String getTestApi(){
        return "2 get";
    }
    @PostMapping(path = "/testAPI")
    public String postTestApi(){
       return "2 post";
    }

    @PostMapping(path = "/savedStaff")
    public ServerResponse postSavedStaff(@Valid @RequestBody StaffForm staffForm,
                                         BindingResult bindingResult){
        ServerResponse<Staff> srStaff =  new ServerResponse("","",ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled",
                        ResponseCode.ERROR_RESPONSE,
                        null);
            }
        }

        /****
         * To save staff we must first of all save a corresponding User.
         * Since we perform  a saving we must ensure that there is no other user
         * in BD with the same username and email because those information must be unique.
         */
        try {
            ServerResponse<User> srUser = userService.saveUser(staffForm.getUsername(), staffForm.getPassword());
            if(srUser.getResponseCode() == ResponseCode.USER_CREATED){
                //The user is created means we can continue with the creation of Staff object
            }
            else if(srUser.getResponseCode() == ResponseCode.USER_NOT_CREATED){
                srStaff.setResponseCode(ResponseCode.STAFF_NOT_CREATED);
                srStaff.setErrorMessage("Username duplicated");
                srStaff.setMoreDetails("An error found during saving user. The precise username is already taken");
            }
        } catch (DuplicateUserException e) {
            //e.printStackTrace();
            srStaff.setErrorMessage(e.getMessage());
            srStaff.setMoreDetails("Exception possibly due to the duplication of the username");
            srStaff.setResponseCode(ResponseCode.EXCEPTION_SAVED_USER);
        }

        return srStaff;
    }

}
