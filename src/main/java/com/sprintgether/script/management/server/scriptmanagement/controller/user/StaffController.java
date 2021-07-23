package com.sprintgether.script.management.server.scriptmanagement.controller.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.RoleNotExistForUserException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.RoleNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.user.StaffForm;
import com.sprintgether.script.management.server.scriptmanagement.form.user.StaffFormList;
import com.sprintgether.script.management.server.scriptmanagement.form.user.StaffRoleForm;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import com.sprintgether.script.management.server.scriptmanagement.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/user")
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

    public Pageable getStaffPageable(StaffFormList staffFormList){
        Sort.Order order1 = new Sort.Order(staffFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, staffFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(staffFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, staffFormList.getSortBy2());

        Sort.Order order3 = new Sort.Order(staffFormList.getDirection3().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, staffFormList.getSortBy3());

        Sort.Order order4 = new Sort.Order(staffFormList.getDirection4().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, staffFormList.getSortBy4());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);
        Pageable sort = PageRequest.of(staffFormList.getPageNumber(), staffFormList.getPageSize(),
                Sort.by(orderList));
        return sort;
    }

    @GetMapping(path = "/staff")
    public ServerResponse<Staff> getStaff(@Valid @RequestBody StaffFormList staffFormList){
        //System.out.println("email "+staffFormList.getEmail());
        return staffService.findStaffByEmail(staffFormList.getEmail());
    }

    @GetMapping(path = "/staffPage")
    public ServerResponse<Page<Staff>> getStaffPage(@Valid @RequestBody StaffFormList staffFormList,
                                                    BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Staff>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        //System.out.println("staffFormList "+staffFormList.toString());
        Pageable sort = this.getStaffPageable(staffFormList);

        if(!staffFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return staffService.findAllStaff(staffFormList.getKeyword(), sort);
        }

        if(staffFormList.getStaffType().equalsIgnoreCase("ALL")){
            return staffService.findAllStaff(sort);
        }

        return staffService.findStaffByStaffType(staffFormList.getStaffType(), sort);
    }

    public Comparator<Staff> getStaffComparator(StaffFormList staffFormList){
        return new Comparator<Staff>(){

            @Override
            public int compare(Staff o1, Staff o2) {
                return 0;
            }
        };
    }

    @GetMapping(path = "/staffList")
    public ServerResponse<List<Staff>> getStaffList(@Valid @RequestBody StaffFormList staffFormList,
                                                    BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Staff>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        if(staffFormList.getStaffType().equalsIgnoreCase("ALL")){
            return staffService.findAllStaff();
        }

        return staffService.findStaffByStaffType(staffFormList.getStaffType());
    }

    @PostMapping(path = "/staffSaved")
    public ServerResponse<Staff> postStaffSaved(@Valid @RequestBody StaffForm staffForm,
                                         BindingResult bindingResult){
        ServerResponse<Staff> srStaff =  new ServerResponse("","",ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        //System.out.println("Just before the try code");
        try {
            //System.out.println("We starting the try code");
            srStaff = staffService.saveStaff(staffForm.getFirstName(), staffForm.getLastName(),
                    staffForm.getStaffType(), staffForm.getEmail(), staffForm.getPhoneNumber(),
                    staffForm.getAddress(), staffForm.getDescription(), staffForm.getUsername(),
                    staffForm.getPassword());
            srStaff.setErrorMessage("The staff has been succefully created");
            //System.out.println("The call to server has been made");
        } catch (DuplicateStaffException e) {
            srStaff.setResponseCode(ResponseCode.EXCEPTION_SAVED_STAFF);
            srStaff.setErrorMessage("DuplicateStaffException");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }


    @PutMapping(path = "/staffUpdated")
    public ServerResponse<Staff> putStaffUpdated(@Valid @RequestBody StaffForm staffForm,
                                         BindingResult bindingResult) {
        ServerResponse<Staff> srStaff = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for udpate",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        //System.out.println("Just before enter in the try block code");
        try{
            //System.out.println("Tne execution of the code in the try block just started like this");
            srStaff = staffService.updateStaff(staffForm.getEmail(), staffForm.getFirstName(),
                    staffForm.getLastName(), staffForm.getAddress(), staffForm.getPhoneNumber(), staffForm.getDescription());
            srStaff.setErrorMessage("The staff has been succefully updated");
        }
        catch (StaffNotFoundException e){
            srStaff.setResponseCode(ResponseCode.EXCEPTION_UPDATED_STAFF);
            srStaff.setErrorMessage("StaffNotFoundException");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }

    @PutMapping(path = "/staffTypeUpdated")
    public ServerResponse<Staff> putStaffTypeUpdated(@Valid @RequestBody StaffForm staffForm,
                                         BindingResult bindingResult) {
        ServerResponse<Staff> srStaff = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled for staffType update",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        //System.out.println("Just before enter in the try block code");
        try{
            //System.out.println("Tne execution of the code in the try block just started like this");
            srStaff = staffService.updateStaffType(staffForm.getEmail(), staffForm.getStaffType());
            srStaff.setErrorMessage("The staff type has been succefully updated");
        }
        catch (StaffNotFoundException e){
            srStaff.setResponseCode(ResponseCode.EXCEPTION_UPDATED_STAFF);
            srStaff.setErrorMessage("StaffNotFoundException");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }

    @PutMapping(path = "/staffPasswordUpdated")
    public ServerResponse<Staff> putStaffPasswordUpdated(@Valid @RequestBody StaffForm staffForm,
                                         BindingResult bindingResult) {
        ServerResponse<Staff> srStaff = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled for Password update",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        //System.out.println("Just before enter in the try block code");
        try{
            //System.out.println("Tne execution of the code in the try block just started like this");
            srStaff = staffService.updateStaffPassword(staffForm.getEmail(), staffForm.getPassword());
            srStaff.setErrorMessage("The password of the staff has been succefully updated");
        }
        catch (StaffNotFoundException e){
            srStaff.setResponseCode(ResponseCode.EXCEPTION_UPDATED_STAFF);
            srStaff.setErrorMessage("StaffNotFoundException");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }


    @PutMapping(path = "/staffActivated")
    public ServerResponse<Staff> putStaffActivated(@Valid @RequestBody StaffForm staffForm,
                                                        BindingResult bindingResult) {
        ServerResponse<Staff> srStaff = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled for Password update",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        //System.out.println("Just before enter in the try block code");
        try{
            //System.out.println("Tne execution of the code in the try block just started like this");
            srStaff = staffService.activateStaff(staffForm.getEmail(),staffForm.isActive());
            String s = staffForm.isActive()?"activated":"desactivated";
            srStaff.setErrorMessage("The account related to "+staffForm.getEmail()+" has been " + s);
        }
        catch (StaffNotFoundException e){
            srStaff.setResponseCode(ResponseCode.EXCEPTION_UPDATED_STAFF);
            srStaff.setErrorMessage("StaffNotFoundException");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }

    @PostMapping(path = "/roleAddedToStaff")
    public ServerResponse<Staff> postRoleAddedToStaff(@Valid @RequestBody StaffRoleForm staffRoleForm,
                                                  BindingResult bindingResult) {
        ServerResponse<Staff> srStaff = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled for Password update",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srStaff = this.staffService.addRoleToStaff(staffRoleForm.getEmail(), staffRoleForm.getRoleName());
            srStaff.setErrorMessage("The role "+staffRoleForm.getRoleName()+" has been successfully added to "+staffRoleForm.getEmail());
        } catch (RoleNotFoundException e) {
            //e.printStackTrace();
            srStaff.setErrorMessage("The roleName specified does not match any Role");
            srStaff.setMoreDetails(e.getMessage());
        } catch (StaffNotFoundException e) {
            //e.printStackTrace();
            srStaff.setErrorMessage("The email specified does not match any Staff");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }

    @PostMapping(path = "/roleRemovedToStaff")
    public ServerResponse<Staff> postRoleRemovedToStaff(@Valid @RequestBody StaffRoleForm staffRoleForm,
                                                 BindingResult bindingResult) {
        ServerResponse<Staff> srStaff = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Staff>(error.getDefaultMessage(),
                        "Some form entry are not well filled for Password update",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srStaff = this.staffService.removeRoleToStaff(staffRoleForm.getEmail(), staffRoleForm.getRoleName());
            srStaff.setErrorMessage("The role "+staffRoleForm.getRoleName()+" has been successfully removed to "+staffRoleForm.getEmail());
        } catch (StaffNotFoundException e) {
            //e.printStackTrace();
            srStaff.setErrorMessage("The email specified does not match any Staff");
            srStaff.setMoreDetails(e.getMessage());
        } catch (RoleNotExistForUserException e) {
            //e.printStackTrace();
            srStaff.setErrorMessage("The role specified don't belonging to Staff specified");
            srStaff.setMoreDetails(e.getMessage());
        }

        return srStaff;
    }


}//end class brace
