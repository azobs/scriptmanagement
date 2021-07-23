package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DepartmentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateOptionInDepartmentException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.OptionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.SchoolNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.School.OptionForm;
import com.sprintgether.script.management.server.scriptmanagement.form.School.OptionFormList;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Option;
import com.sprintgether.script.management.server.scriptmanagement.service.school.OptionService;
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
public class OptionController {
    OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    public Pageable getOptionPageable(OptionFormList optionFormList){

        Sort.Order order1 = new Sort.Order(optionFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, optionFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(optionFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, optionFormList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(optionFormList.getPageNumber(), optionFormList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/optionPage")
    public ServerResponse<Page<Option>> getOptionPage(@Valid @RequestBody OptionFormList optionFormList,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Option>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the departmentForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getOptionPageable(optionFormList);

        if(!optionFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return optionService.findAllOption(optionFormList.getKeyword(), sort);
        }

        return optionService.findAllOption(sort);
    }

    @GetMapping(path = "/optionPageOfDepartment")
    public ServerResponse<Page<Option>> getOptionPageOfDepartment(@Valid @RequestBody OptionFormList optionFormList,
                                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Option>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        Pageable sort = this.getOptionPageable(optionFormList);
        String schoolName = optionFormList.getSchoolName();
        String departmentName = optionFormList.getDepartmentName();

        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();
        try {
            if(!optionFormList.getKeyword().equalsIgnoreCase("")){
                srOptionPage = optionService.findAllOptionOfDepartment(schoolName, departmentName,
                        optionFormList.getKeyword(), sort);
            }
            else{
                srOptionPage = optionService.findAllOptionOfDepartment(schoolName,
                        departmentName, sort);
            }
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srOptionPage.setErrorMessage("The associated department has not found");
            srOptionPage.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srOptionPage.setMoreDetails(e.getMessage());
        }

        return srOptionPage;
    }


    @GetMapping(path = "/optionListOfDepartment")
    public ServerResponse<List<Option>> getOptionListOfDepartment(@Valid @RequestBody OptionFormList optionFormList,
                                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Option>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        String schoolName = optionFormList.getSchoolName();
        String departmentName = optionFormList.getDepartmentName();

        ServerResponse<List<Option>> srOptionList = new ServerResponse<>();
        try {
            srOptionList = optionService.findAllOptionOfDepartment(schoolName, departmentName);
        } catch (DepartmentNotFoundException e) {
            srOptionList.setErrorMessage("The associated department has not found");
            srOptionList.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srOptionList.setMoreDetails(e.getMessage());
        }
        return srOptionList;
    }

    @GetMapping(path = "/option")
    public ServerResponse<Option> getOption(@Valid @RequestBody OptionFormList optionFormList){
        ServerResponse<Option> srOption = new ServerResponse<>();
        try {
            srOption = optionService.findOptionOfDepartmentByName(optionFormList.getSchoolName(), optionFormList.getDepartmentName(), optionFormList.getOptionName());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The associated school has not found");
            srOption.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srOption.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The associated department has not found");
            srOption.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srOption.setMoreDetails(e.getMessage());
        }
        return srOption;
    }

    @GetMapping(path = "/optionList")
    public ServerResponse<List<Option>> getOptionList(){
        ServerResponse<List<Option>> srListOption = new ServerResponse<>();
        srListOption = optionService.findAllOption();
        List<Option> listofOption = srListOption.getAssociatedObject();
        Collections.sort(listofOption, new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                if(o1.getName().compareToIgnoreCase(o2.getName())<0) return -1;
                if(o1.getName().compareToIgnoreCase(o2.getName())>0) return 1;
                return 0;
            }
        });
        srListOption.setAssociatedObject(listofOption);
        return srListOption;
    }

    @PostMapping(path = "/optionSaved")
    public ServerResponse<Option> postOptionSaved(@Valid @RequestBody OptionForm optionForm,
                                                          BindingResult bindingResult) {
        ServerResponse<Option> srOption = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Option>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srOption = optionService.saveOption(optionForm.getName(), optionForm.getAcronym(),
                    optionForm.getDescription(), optionForm.getOwnerDepartment(),
                    optionForm.getOwnerSchool());
            srOption.setErrorMessage("The Option has been successfully created");
        } catch (DuplicateOptionInDepartmentException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The option name will not be unique in the department of " +
                    "school specified");
            srOption.setResponseCode(ResponseCode.EXCEPTION_SAVED_OPTION);
            srOption.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The department name does not match any department in the system");
            srOption.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srOption.setMoreDetails(e.getMessage());
        }

        return srOption;
    }

    @PutMapping(path = "/optionUpdated")
    public ServerResponse<Option> postOptionUpdated(@Valid @RequestBody OptionForm optionForm,
                                                  BindingResult bindingResult) {
        ServerResponse<Option> srOption = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Option>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srOption = optionService.updateOption(optionForm.getName(), optionForm.getAcronym(),
                    optionForm.getDescription(), optionForm.getOwnerDepartment(),
                    optionForm.getOwnerSchool());
            srOption.setErrorMessage("The Option has been successfully updated");
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The department name does not match any department in the system");
            srOption.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srOption.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The option name does not match any option in the system");
            srOption.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srOption.setMoreDetails(e.getMessage());
        }

        return srOption;
    }


}
