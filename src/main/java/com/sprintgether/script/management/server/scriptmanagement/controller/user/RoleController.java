package com.sprintgether.script.management.server.scriptmanagement.controller.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateRoleException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.RoleNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.user.RoleForm;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Role;
import com.sprintgether.script.management.server.scriptmanagement.service.user.RoleService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/user/role")
public class RoleController {
    RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(path = "/roleList")
    public ServerResponse<List<Role>> getRoleList(){

        return roleService.findAllRole();
    }

    @PostMapping(path = "/savedRole")
    public ServerResponse<Role> postSavedRole(@Valid @RequestBody RoleForm roleForm,
                                                BindingResult bindingResult){
        ServerResponse<Role> srRole =  new ServerResponse("","",ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Role>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for save",
                        ResponseCode.ERROR_RESPONSE,
                        null);
            }
        }

        //System.out.println("Just before the try code");
        try {
            //System.out.println("We starting the try code");
            srRole = roleService.saveRole(roleForm.getRoleName(), roleForm.getRoleAlias(), roleForm.getRoleDescription());
            srRole.setErrorMessage("The role has been successfully added");
            //System.out.println("The call to server has been made");
        }
        catch (DuplicateRoleException e) {
            //e.printStackTrace();
            srRole.setErrorMessage("This role is already exists in the system");
            srRole.setMoreDetails(e.getMessage());
        }

        return srRole;
    }

    @PutMapping(path = "/updatedRole")
    public ServerResponse<Role> putUpdateRole(@Valid @RequestBody RoleForm roleForm,
                                                    BindingResult bindingResult) {
        ServerResponse<Role> srRole = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Role>(error.getDefaultMessage(),
                        "Some form entry are not well filled for staffType update",
                        ResponseCode.ERROR_RESPONSE,
                        null);
            }
        }

        //System.out.println("Just before enter in the try block code");
        try{
            //System.out.println("Tne execution of the code in the try block just started like this");
            srRole = roleService.updateRole(roleForm.getRoleName(), roleForm.getRoleAlias(), roleForm.getRoleDescription());
            srRole.setErrorMessage("The designated role has been successfully updated");
        }
        catch (RoleNotFoundException e) {
            //e.printStackTrace();
            srRole.setErrorMessage("The roleName sent does not match any Role in the system");
            srRole.setMoreDetails(e.getMessage());
        }

        return srRole;
    }

}
