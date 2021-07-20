package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateInstitutionException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.InstitutionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.School.InstitutionForm;
import com.sprintgether.script.management.server.scriptmanagement.form.School.InstitutionFormList;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import com.sprintgether.script.management.server.scriptmanagement.service.school.InstitutionService;
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
@RequestMapping(path = "/sm/school/institution")
public class InstitutionController {
    InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    public Pageable getInstitutionPageable(InstitutionFormList institutionFormList){

        Sort.Order order1 = new Sort.Order(institutionFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, institutionFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(institutionFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, institutionFormList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(institutionFormList.getPageNumber(), institutionFormList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/institutionPage")
    public ServerResponse<Page<Institution>> getInstitutionPage(@Valid @RequestBody InstitutionFormList institutionFormList,
                                                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Institution>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_RESPONSE,
                        null);
            }
        }
        //System.out.println("staffFormList "+staffFormList.toString());
        Pageable sort = this.getInstitutionPageable(institutionFormList);

        if(!institutionFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return institutionService.findAllInstitution(institutionFormList.getKeyword(), sort);
        }

        return institutionService.findAllInstitution(sort);
    }

    @GetMapping(path = "/institution")
    public ServerResponse<Institution> getInstitution(@Valid @RequestBody String name){
        return institutionService.findInstitutionByName(name);
    }

    @GetMapping(path = "/institutionList")
    public ServerResponse<List<Institution>> getInstitutionList(){

        return institutionService.findAllInstitution();
    }

    @PostMapping(path = "/savedInstitution")
    public ServerResponse<Institution> postSavedInstitution(@Valid @RequestBody InstitutionForm institutionForm,
                                                BindingResult bindingResult) {
        ServerResponse<Institution> srInstitution = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Institution>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for save",
                        ResponseCode.ERROR_RESPONSE,
                        null);
            }
        }

        try {
            srInstitution = institutionService.savedInstitution(institutionForm.getName(),
                    institutionForm.getAcronym(), institutionForm.getDescription(),
                    institutionForm.getLocation(), institutionForm.getAddress(),
                    institutionForm.getLogoInstitution());
            srInstitution.setErrorMessage("The institution has been successfully created");

        } catch (DuplicateInstitutionException e) {
            //e.printStackTrace();
            srInstitution.setErrorMessage("The name of institution specified is used by another institution in the system");
            srInstitution.setResponseCode(ResponseCode.EXCEPTION_SAVED_INSTITUTION);
            srInstitution.setMoreDetails(e.getMessage());
        }

        return srInstitution;
    }

    @PutMapping(path = "/updatedInstitution")
    public ServerResponse<Institution> putUpdateInstitution(@Valid @RequestBody InstitutionForm institutionForm,
                                                BindingResult bindingResult) {
        ServerResponse<Institution> srInstitution = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Institution>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for udpate",
                        ResponseCode.ERROR_RESPONSE,
                        null);
            }
        }

        try {
            srInstitution = institutionService.updatedInstitution(institutionForm.getName(), institutionForm.getAcronym(), institutionForm.getDescription(), institutionForm.getLocation(), institutionForm.getAddress(), institutionForm.getLogoInstitution());
            srInstitution.setErrorMessage("The institution has been successfully updated");
        } catch (InstitutionNotFoundException e) {
            //e.printStackTrace();
            srInstitution.setErrorMessage("The institution name does not match any institution");
            srInstitution.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
            srInstitution.setMoreDetails(e.getMessage());
        }

        return srInstitution;
    }


}
