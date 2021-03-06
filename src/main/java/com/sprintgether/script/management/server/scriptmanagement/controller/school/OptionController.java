package com.sprintgether.script.management.server.scriptmanagement.controller.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DepartmentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateOptionInDepartmentException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.OptionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.SchoolNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.school.option.OptionNameUpdated;
import com.sprintgether.script.management.server.scriptmanagement.form.school.option.OptionSaved;
import com.sprintgether.script.management.server.scriptmanagement.form.school.option.OptionList;
import com.sprintgether.script.management.server.scriptmanagement.form.school.option.OptionUpdated;
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

    public Pageable getOptionPageable(OptionList optionList){

        Sort.Order order1 = new Sort.Order(optionList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, optionList.getSortBy1());

        Sort.Order order2 = new Sort.Order(optionList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, optionList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(optionList.getPageNumber(), optionList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/optionPage")
    public ServerResponse<Page<Option>> getOptionPage(@Valid @RequestBody OptionList optionList,
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

        Pageable sort = this.getOptionPageable(optionList);

        if(!optionList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return optionService.findAllOption(optionList.getKeyword(), sort);
        }

        return optionService.findAllOption(sort);
    }

    @GetMapping(path = "/optionPageOfDepartment")
    public ServerResponse<Page<Option>> getOptionPageOfDepartment(@Valid @RequestBody OptionList optionList,
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
        Pageable sort = this.getOptionPageable(optionList);
        String optionId = optionList.getOptionId();
        String schoolName = optionList.getSchoolName();
        String departmentName = optionList.getDepartmentName();

        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();
        try {
            if(!optionList.getKeyword().equalsIgnoreCase("")){
                srOptionPage = optionService.findAllOptionOfDepartment(optionId, schoolName,
                        departmentName, optionList.getKeyword(), sort);
            }
            else{
                srOptionPage = optionService.findAllOptionOfDepartment(optionId, schoolName,
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
    public ServerResponse<List<Option>> getOptionListOfDepartment(@Valid @RequestBody OptionList optionList,
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
        String optionId = optionList.getOptionId();
        String schoolName = optionList.getSchoolName();
        String departmentName = optionList.getDepartmentName();

        ServerResponse<List<Option>> srOptionList = new ServerResponse<>();
        try {
            srOptionList = optionService.findAllOptionOfDepartment(optionId, schoolName,
                    departmentName);
        } catch (DepartmentNotFoundException e) {
            srOptionList.setErrorMessage("The associated department has not found");
            srOptionList.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srOptionList.setMoreDetails(e.getMessage());
        }
        return srOptionList;
    }

    @GetMapping(path = "/option")
    public ServerResponse<Option> getOption(@Valid @RequestBody OptionList optionList){
        ServerResponse<Option> srOption = new ServerResponse<>();
        try {
            srOption = optionService.findOptionOfDepartmentByName(optionList.getSchoolName(), optionList.getDepartmentName(), optionList.getOptionName());
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
    public ServerResponse<Option> postOptionSaved(@Valid @RequestBody OptionSaved optionSaved,
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
            srOption = optionService.saveOption(optionSaved.getName(), optionSaved.getAcronym(),
                    optionSaved.getDescription(), optionSaved.getOwnerDepartment(),
                    optionSaved.getDepartmentId(), optionSaved.getOwnerSchool());
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
    public ServerResponse<Option> postOptionUpdated(@Valid @RequestBody OptionUpdated optionUpdated,
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
            srOption = optionService.updateOption(optionUpdated.getOptionId(), optionUpdated.getName(),
                    optionUpdated.getAcronym(),
                    optionUpdated.getDescription(), optionUpdated.getOwnerDepartment(),
                    optionUpdated.getOwnerSchool());
            srOption.setErrorMessage("The Option has been successfully updated");
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The option name does not match any option in the system");
            srOption.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srOption.setMoreDetails(e.getMessage());
        } catch (DuplicateOptionInDepartmentException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("There is other option with the name specified in the system");
            srOption.setResponseCode(ResponseCode.EXCEPTION_OPTION_DUPLICATED);
            srOption.setMoreDetails(e.getMessage());
        }

        return srOption;
    }

    @PutMapping(path = "/optionNameUpdated")
    public ServerResponse<Option> postOptionNameUpdated(@Valid @RequestBody OptionNameUpdated optionNameUpdated,
                                                    BindingResult bindingResult) {
        ServerResponse<Option> srOption = new ServerResponse("", "",
                ResponseCode.BAD_REQUEST, null);

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

        String optionId = optionNameUpdated.getOptionId();
        String newOptionName = optionNameUpdated.getNewOptionName();

        try {
            srOption = optionService.updateOptionName(optionId, newOptionName);
            srOption.setErrorMessage("The Option name has been successfully updated");
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("The option name does not match any option in the system");
            srOption.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srOption.setMoreDetails(e.getMessage());
        } catch (DuplicateOptionInDepartmentException e) {
            //e.printStackTrace();
            srOption.setErrorMessage("There is other option with the name specified in the system");
            srOption.setResponseCode(ResponseCode.EXCEPTION_OPTION_DUPLICATED);
            srOption.setMoreDetails(e.getMessage());
        }

        return srOption;
    }


}
