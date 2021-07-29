package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DepartmentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateDepartmentInSchoolException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.SchoolNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.form.School.DepartmentForm;
import com.sprintgether.script.management.server.scriptmanagement.form.School.DepartmentFormList;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Department;
import com.sprintgether.script.management.server.scriptmanagement.service.school.DepartmentService;
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
public class DepartmentController {
    DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public Pageable getDepartmentPageable(DepartmentFormList departmentFormList){

        Sort.Order order1 = new Sort.Order(departmentFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, departmentFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(departmentFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, departmentFormList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(departmentFormList.getPageNumber(), departmentFormList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/departmentPage")
    public ServerResponse<Page<Department>> getDepartmentPage(@Valid @RequestBody DepartmentFormList departmentFormList,
                                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Department>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the departmentForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getDepartmentPageable(departmentFormList);

        if(!departmentFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return departmentService.findAllDepartment(departmentFormList.getKeyword(), sort);
        }

        return departmentService.findAllDepartment(sort);
    }

    @GetMapping(path = "/departmentPageOfSchool")
    public ServerResponse<Page<Department>> getDepartmentPageOfSchool(@Valid @RequestBody DepartmentFormList departmentFormList,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Department>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        Pageable sort = this.getDepartmentPageable(departmentFormList);

        String schoolName = departmentFormList.getSchoolName();
        ServerResponse<Page<Department>> srDepartmentPage = new ServerResponse<Page<Department>>();
        try {
            if(!departmentFormList.getKeyword().equalsIgnoreCase("")){
                srDepartmentPage = departmentService.findAllDepartmentOfSchool(schoolName,
                        departmentFormList.getKeyword(), sort);
            }
            else{
                srDepartmentPage = departmentService.findAllDepartmentOfSchool(schoolName, sort);
            }
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srDepartmentPage.setErrorMessage("The specified school name does not match any school");
            srDepartmentPage.setMoreDetails(e.getMessage());
            srDepartmentPage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_NAME);
        }
        return srDepartmentPage;
    }

    @GetMapping(path = "/departmentListOfSchool")
    public ServerResponse<List<Department>> getDepartmentListOfSchool(@Valid @RequestBody DepartmentFormList departmentFormList,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Department>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String schoolName = departmentFormList.getSchoolName();
        ServerResponse<List<Department>> srDepartmentList = new ServerResponse<List<Department>>();
        try {
            srDepartmentList = departmentService.findAllDepartmentOfSchool(schoolName);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srDepartmentList.setErrorMessage("The specified School name does not match any school");
            srDepartmentList.setMoreDetails(e.getMessage());
            srDepartmentList.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_NAME);
        }
        return srDepartmentList;

    }

    @GetMapping(path = "/department")
    public ServerResponse<Department> getDepartment(@Valid @RequestBody DepartmentFormList departmentFormList){
        ServerResponse<Department> srDepartment = new ServerResponse<>();
        try {
            srDepartment = departmentService.findDepartmentOfSchoolByName(
                    departmentFormList.getSchoolName(),departmentFormList.getDepartmentName());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srDepartment.setErrorMessage("The specified School name does not match any school");
            srDepartment.setMoreDetails(e.getMessage());
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        }
        return srDepartment;
    }

    @GetMapping(path = "/departmentList")
    public ServerResponse<List<Department>> getDepartmentList(){
        ServerResponse<List<Department>> srListDepartment = new ServerResponse<>();
        srListDepartment = departmentService.findAllDepartment();
        List<Department> listofDepartment = srListDepartment.getAssociatedObject();
        Collections.sort(listofDepartment, new Comparator<Department>() {
            @Override
            public int compare(Department o1, Department o2) {
                if(o1.getName().compareToIgnoreCase(o2.getName())<0) return -1;
                if(o1.getName().compareToIgnoreCase(o2.getName())>0) return 1;
                return 0;
            }
        });
        srListDepartment.setAssociatedObject(listofDepartment);
        return srListDepartment;
    }

    @PostMapping(path = "/departmentSaved")
    public ServerResponse<Department> postDepartmentSaved(@Valid @RequestBody DepartmentForm departmentForm,
                                                  BindingResult bindingResult) {
        ServerResponse<Department> srDepartment = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Department>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srDepartment = departmentService.saveDepartment(departmentForm.getName(),
                    departmentForm.getAcronym(), departmentForm.getDescription(),
                    departmentForm.getOwnerSchoolName());
            srDepartment.setErrorMessage("The department has been successfully created");
        } catch (DuplicateDepartmentInSchoolException e) {
            srDepartment.setErrorMessage("The specified Department name identify another department " +
                    "in the school specified");
            srDepartment.setMoreDetails(e.getMessage());
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_SAVED_DEPARTMENT);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srDepartment.setErrorMessage("The specified school name does not identify any school in the system");
            srDepartment.setMoreDetails(e.getMessage());
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        }

        return srDepartment;
    }

    @PutMapping(path = "/departmentUpdated")
    public ServerResponse<Department> putDepartmentUpdated(@Valid @RequestBody DepartmentForm departmentForm,
                                                   BindingResult bindingResult) {
        ServerResponse<Department> srDepartment = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Department>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for udpate",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        try {
            srDepartment = departmentService.updateDepartment(departmentForm.getDepartmentId(),
                    departmentForm.getName(),
                    departmentForm.getAcronym(), departmentForm.getDescription(),
                    departmentForm.getOwnerSchoolName());
            srDepartment.setErrorMessage("The department has been successfully updated");
        } catch (DepartmentNotFoundException e) {
            srDepartment.setErrorMessage("The specified department name does not identify any department in the system");
            srDepartment.setMoreDetails(e.getMessage());
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (DuplicateDepartmentInSchoolException e) {
            srDepartment.setErrorMessage("The specified department already exist in the " +
                    "school specified");
            srDepartment.setMoreDetails(e.getMessage());
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_DUPLICATED);
        }
        return srDepartment;
    }

    @PutMapping(path = "/departmentNameUpdated")
    public ServerResponse<Department> putDepartmentNameUpdated(@Valid @RequestBody DepartmentForm departmentForm,
                                                           BindingResult bindingResult) {
        ServerResponse<Department> srDepartment = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Department>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the staffForm for udpate",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String departmentId = departmentForm.getDepartmentId();
        String newDepartmentName = departmentForm.getName();

        try {
            srDepartment = departmentService.updateDepartmentName(departmentId, newDepartmentName);
            srDepartment.setErrorMessage("The department name has been successfully updated");
        } catch (DepartmentNotFoundException e) {
            srDepartment.setErrorMessage("The specified department name does not identify any department in the system");
            srDepartment.setMoreDetails(e.getMessage());
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (DuplicateDepartmentInSchoolException e) {
            srDepartment.setErrorMessage("The specified department already exist in the " +
                    "school specified");
            srDepartment.setMoreDetails(e.getMessage());
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_DUPLICATED);
        }
        return srDepartment;
    }

}
