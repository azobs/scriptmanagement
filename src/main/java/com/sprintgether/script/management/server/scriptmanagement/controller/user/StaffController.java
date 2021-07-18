package com.sprintgether.script.management.server.scriptmanagement.controller.user;

import com.sprintgether.script.management.server.scriptmanagement.EnumResponseType;
import com.sprintgether.script.management.server.scriptmanagement.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.form.user.StaffForm;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/user/staff")
public class StaffController {
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
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled",
                        EnumResponseType.ERREUR,
                        null);
            }
        }

        /****
         * To save staff we must first of all save a corresponding User.
         * Since we perform  a saving we must ensure that there is no other user
         * in BD with the same username and email because those information must be unique.
         */


        return new ServerResponse("","",EnumResponseType.NOTHING, null);
    }

}
