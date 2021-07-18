package com.sprintgether.script.management.server.scriptmanagement.controller.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateStaffException;
import com.sprintgether.script.management.server.scriptmanagement.form.user.StaffForm;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import com.sprintgether.script.management.server.scriptmanagement.service.user.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/user/staff")
public class StaffController {

    UserService userService;
    StaffService staffService;

    public StaffController(UserService userService, StaffService staffService) {

        this.userService = userService;
        this.staffService = staffService;
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

        System.out.println("Just before the try code");
        try {
            System.out.println("We starting the try code");
            srStaff = staffService.saveStaff(staffForm.getFirstName(), staffForm.getLastName(),
                    staffForm.getStaffType(), staffForm.getEmail(), staffForm.getPhoneNumber(),
                    staffForm.getAddress(), staffForm.getDescription(), staffForm.getUsername(),
                    staffForm.getPassword());
            System.out.println("The call to server has been made");
        } catch (DuplicateStaffException e) {
            srStaff.setResponseCode(ResponseCode.EXCEPTION_SAVED_STAFF);
            srStaff.setErrorMessage("DuplicateStaffException");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }

}
