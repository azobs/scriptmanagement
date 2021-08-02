package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateInstitutionException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.InstitutionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.school.institution.InstitutionNameUpdated;
import com.sprintgether.script.management.server.scriptmanagement.form.school.institution.InstitutionSaved;
import com.sprintgether.script.management.server.scriptmanagement.form.school.institution.InstitutionList;
import com.sprintgether.script.management.server.scriptmanagement.form.school.institution.InstitutionUpdated;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/school")
public class InstitutionController {
    InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    public Pageable getInstitutionPageable(InstitutionList institutionList){

        Sort.Order order1 = new Sort.Order(institutionList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, institutionList.getSortBy1());

        Sort.Order order2 = new Sort.Order(institutionList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, institutionList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(institutionList.getPageNumber(), institutionList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/institutionPage")
    public ServerResponse<Page<Institution>> getInstitutionPage(@Valid @RequestBody InstitutionList institutionList,
                                                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Institution>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        //System.out.println("staffFormList "+staffFormList.toString());
        Pageable sort = this.getInstitutionPageable(institutionList);

        if(!institutionList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return institutionService.findAllInstitution(institutionList.getKeyword(), sort);
        }

        return institutionService.findAllInstitution(sort);
    }

    @GetMapping(path = "/institution")
    public ServerResponse<Institution> getInstitution(@Valid @RequestBody InstitutionList institutionList){
        return institutionService.findInstitutionByName(institutionList.getInstitutionName());
    }

    @GetMapping(path = "/institutionList")
    public ServerResponse<List<Institution>> getInstitutionList(){

        ServerResponse<List<Institution>> srListInst = institutionService.findAllInstitution();
        List<Institution> listofInst = srListInst.getAssociatedObject();
        Collections.sort(listofInst, new Comparator<Institution>() {
            @Override
            public int compare(Institution o1, Institution o2) {
                if(o1.getName().compareToIgnoreCase(o2.getName())<0) return -1;
                if(o1.getName().compareToIgnoreCase(o2.getName())>0) return 1;
                return 0;
            }
        });
        srListInst.setAssociatedObject(listofInst);
        return srListInst;
    }

    @PostMapping(path = "/institutionSaved")
    public ServerResponse<Institution> postInstitutionSaved(@Valid @RequestBody InstitutionSaved institutionSaved,
                                                BindingResult bindingResult) {
        ServerResponse<Institution> srInstitution = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Institution>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srInstitution = institutionService.saveInstitution(institutionSaved.getName(),
                    institutionSaved.getAcronym(), institutionSaved.getDescription(),
                    institutionSaved.getLocation(), institutionSaved.getAddress(),
                    institutionSaved.getLogoInstitution());
            srInstitution.setErrorMessage("The institution has been successfully created");

        } catch (DuplicateInstitutionException e) {
            //e.printStackTrace();
            srInstitution.setErrorMessage("The name of institution specified is used by another institution in the system");
            srInstitution.setResponseCode(ResponseCode.EXCEPTION_SAVED_INSTITUTION);
            srInstitution.setMoreDetails(e.getMessage());
        }

        return srInstitution;
    }

    @PutMapping(path = "/institutionUpdated")
    public ServerResponse<Institution> putInstitutionUpdated(@Valid @RequestBody InstitutionUpdated institutionUpdated,
                                                BindingResult bindingResult) {
        ServerResponse<Institution> srInstitution = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Institution>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for udpate",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srInstitution = institutionService.updateInstitution(institutionUpdated.getInstitutionId(),
                    institutionUpdated.getName(), institutionUpdated.getAcronym(),
                    institutionUpdated.getDescription(), institutionUpdated.getLocation(),
                    institutionUpdated.getAddress(), institutionUpdated.getLogoInstitution());
            srInstitution.setErrorMessage("The institution has been successfully updated");
        } catch (InstitutionNotFoundException e) {
            //e.printStackTrace();
            srInstitution.setErrorMessage("The institution name does not match any institution");
            srInstitution.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
            srInstitution.setMoreDetails(e.getMessage());
        } catch (DuplicateInstitutionException e) {
            //e.printStackTrace();
            srInstitution.setErrorMessage("The institution name match other institution in " +
                    "the system");
            srInstitution.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
            srInstitution.setMoreDetails(e.getMessage());
        }

        return srInstitution;
    }


    @PutMapping(path = "/institutionNameUpdated")
    public ServerResponse<Institution> putInstitutionNameUpdated(@Valid @RequestBody InstitutionNameUpdated institutionNameUpdated,
                                                             BindingResult bindingResult) {
        ServerResponse<Institution> srInstitution = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Institution>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for udpate",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String institutionId = institutionNameUpdated.getInstitutionId();
        String newInstitutionName = institutionNameUpdated.getNewInstitutionName();

        try {
            srInstitution = institutionService.updateInstitutionName(institutionId, newInstitutionName);
            srInstitution.setErrorMessage("The institution name has been successfully updated");
        } catch (InstitutionNotFoundException e) {
            //e.printStackTrace();
            srInstitution.setErrorMessage("The institution name does not match any institution");
            srInstitution.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
            srInstitution.setMoreDetails(e.getMessage());
        } catch (DuplicateInstitutionException e) {
            //e.printStackTrace();
            srInstitution.setErrorMessage("The institution name match other institution in " +
                    "the system");
            srInstitution.setResponseCode(ResponseCode.EXCEPTION_UPDATED_INSTITUTION);
            srInstitution.setMoreDetails(e.getMessage());
        }

        return srInstitution;
    }


}
