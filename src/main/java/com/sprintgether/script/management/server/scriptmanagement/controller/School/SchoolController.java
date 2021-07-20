package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.form.School.InstitutionForm;
import com.sprintgether.script.management.server.scriptmanagement.form.School.InstitutionSchoolForm;
import com.sprintgether.script.management.server.scriptmanagement.form.School.SchoolForm;
import com.sprintgether.script.management.server.scriptmanagement.form.School.SchoolFormList;
import com.sprintgether.script.management.server.scriptmanagement.form.user.StaffRoleForm;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.school.SchoolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/school")
public class SchoolController {
    SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    public Pageable getSchoolPageable(SchoolFormList schoolFormList){

        Sort.Order order1 = new Sort.Order(schoolFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, schoolFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(schoolFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, schoolFormList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(schoolFormList.getPageNumber(), schoolFormList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/schoolPage")
    public ServerResponse<Page<School>> getSchoolPage(@Valid @RequestBody SchoolFormList schoolFormList,
                                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<School>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        //System.out.println("staffFormList "+staffFormList.toString());
        Pageable sort = this.getSchoolPageable(schoolFormList);

        if(!schoolFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return schoolService.findAllSchool(schoolFormList.getKeyword(), sort);
        }

        return schoolService.findAllSchool(sort);
    }

    @GetMapping(path = "/school")
    public ServerResponse<School> getSchool(@Valid @RequestBody SchoolFormList schoolFormList){
        return schoolService.findSchoolByName(schoolFormList.getName());
    }

    @GetMapping(path = "/schoolList")
    public ServerResponse<List<School>> getSchoolList(){

        return schoolService.findAllSchool();
    }

    @PostMapping(path = "/schoolSaved")
    public ServerResponse<School> postSchoolSaved(@Valid @RequestBody SchoolForm schoolForm,
                                                            BindingResult bindingResult) {
        ServerResponse<School> srSchool = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<School>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srSchool = schoolService.saveSchool(schoolForm.getName(), schoolForm.getAcronym(),
                    schoolForm.getDescription(), schoolForm.getLogoSchool(),
                    schoolForm.getOwnerInstitution(), schoolForm.getParentInstitution());
            srSchool.setErrorMessage("The school has been successfully created");

        } catch (DuplicateSchoolException e) {
            //e.printStackTrace();
            srSchool.setErrorMessage("The name of school specified is used by another school in the system");
            srSchool.setResponseCode(ResponseCode.EXCEPTION_SAVED_SCHOOL);
            srSchool.setMoreDetails(e.getMessage());
        }

        return srSchool;
    }

    @PutMapping(path = "/schoolUpdated")
    public ServerResponse<School> putSchoolUpdated(@Valid @RequestBody SchoolForm schoolForm,
                                                             BindingResult bindingResult) {
        ServerResponse<School> srSchool = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<School>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for udpate",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srSchool = schoolService.updateSchool(schoolForm.getName(), schoolForm.getAcronym(),
                    schoolForm.getDescription(), schoolForm.getLogoSchool(),
                    schoolForm.getOwnerInstitution(), schoolForm.getParentInstitution());
            srSchool.setErrorMessage("The school has been successfully updated");
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSchool.setErrorMessage("The school name does not match any school");
            srSchool.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
            srSchool.setMoreDetails(e.getMessage());
        }

        return srSchool;
    }

    @PostMapping(path = "/schoolAddedToInstitution")
    public ServerResponse<Institution> postSchoolAddedToInstitution(@Valid @RequestBody InstitutionSchoolForm institutionSchoolForm,
                                                                    BindingResult bindingResult) {
        ServerResponse<Institution> srInst = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Institution>(error.getDefaultMessage(),
                        "Some form entry are not well filled for Password update",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srInst = schoolService.addSchoolToInstitution(institutionSchoolForm.getInstitutionName(), institutionSchoolForm.getSchoolName());
            srInst.setErrorMessage("The school specified by "+institutionSchoolForm.getSchoolName()+
                    " has been successfully added in the institution specified by "+institutionSchoolForm.getInstitutionName());
        } catch (InstitutionNotFoundException e) {
            //e.printStackTrace();
            srInst.setErrorMessage("The institution name specified does not match any institution in the system");
            srInst.setMoreDetails(e.getMessage());
            srInst.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srInst.setErrorMessage("The school name specified does not match any school in the system");
            srInst.setMoreDetails(e.getMessage());
            srInst.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
        }


        return srInst;
    }

    @PostMapping(path = "/schoolRemovedToInstitution")
    public ServerResponse<Institution> postSchoolRemovedToInstitution(@Valid @RequestBody InstitutionSchoolForm institutionSchoolForm,
                                                                    BindingResult bindingResult) {
        ServerResponse<Institution> srInst = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Institution>(error.getDefaultMessage(),
                        "Some form entry are not well filled for Password update",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srInst = schoolService.removeSchoolToInstitution(institutionSchoolForm.getInstitutionName(), institutionSchoolForm.getSchoolName());
            srInst.setErrorMessage("The school specified by "+institutionSchoolForm.getSchoolName()+
                    " has been successfully removed in the institution specified by "+institutionSchoolForm.getInstitutionName());
        } catch (InstitutionNotFoundException e) {
            //e.printStackTrace();
            srInst.setErrorMessage("The institution name specified does not match any institution in the system");
            srInst.setMoreDetails(e.getMessage());
            srInst.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
        } catch (SchoolNotExistInInstitutionException e) {
            //e.printStackTrace();
            srInst.setErrorMessage("The school name specified does not match any school in the system");
            srInst.setMoreDetails(e.getMessage());
            srInst.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
        }

        return srInst;
    }



}
