package com.sprintgether.script.management.server.scriptmanagement.controller.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.form.school.school.SchoolNameUpdated;
import com.sprintgether.script.management.server.scriptmanagement.form.school.school.SchoolSaved;
import com.sprintgether.script.management.server.scriptmanagement.form.school.school.SchoolList;
import com.sprintgether.script.management.server.scriptmanagement.form.school.school.SchoolUpdated;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(path = "/sm/school")
public class SchoolController {
    SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    public Pageable getSchoolPageable(SchoolList schoolList){

        Sort.Order order1 = new Sort.Order(schoolList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, schoolList.getSortBy1());

        Sort.Order order2 = new Sort.Order(schoolList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, schoolList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(schoolList.getPageNumber(), schoolList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/schoolPage")
    public ServerResponse<Page<School>> getSchoolPage(@Valid @RequestBody SchoolList schoolList,
                                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<School>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        //System.out.println("staffFormList "+staffFormList.toString());
        Pageable sort = this.getSchoolPageable(schoolList);

        if(!schoolList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return schoolService.findAllSchool(schoolList.getKeyword(), sort);
        }

        return schoolService.findAllSchool(sort);
    }

    @GetMapping(path = "/schoolPageOfInstitution")
    public ServerResponse<Page<School>> getSchoolPageOfInstitution(@Valid @RequestBody SchoolList schoolList,
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
        Pageable sort = this.getSchoolPageable(schoolList);

        String instName = schoolList.getInstName();
        ServerResponse<Page<School>> srSchoolPage = new ServerResponse<Page<School>>();
        try {
            srSchoolPage = schoolService.findSchoolOfInstitution(instName, sort);
        } catch (InstitutionNotFoundException e) {
            //e.printStackTrace();
            srSchoolPage.setErrorMessage("The specified institution name does not match any institution");
            srSchoolPage.setMoreDetails(e.getMessage());
            srSchoolPage.setResponseCode(ResponseCode.EXCEPTION_INSTITUTION_NAME);
        }
        return srSchoolPage;
    }

    @GetMapping(path = "/schoolListOfInstitution")
    public ServerResponse<List<School>> getSchoolListOfInstitution(@Valid @RequestBody SchoolList schoolList,
                                                                   BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<School>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        String instName = schoolList.getInstName();
        ServerResponse<List<School>> srSchoolList = new ServerResponse<List<School>>();
        try {
            srSchoolList = schoolService.findSchoolOfInstitution(instName);
        } catch (InstitutionNotFoundException e) {
            //e.printStackTrace();
            srSchoolList.setErrorMessage("The specified institution name does not match any institution");
            srSchoolList.setMoreDetails(e.getMessage());
            srSchoolList.setResponseCode(ResponseCode.EXCEPTION_INSTITUTION_NAME);
        }
        return srSchoolList;
    }

    @GetMapping(path = "/school")
    public ServerResponse<School> getSchool(@Valid @RequestBody SchoolList schoolList){
        return schoolService.findSchoolByName(schoolList.getSchoolName());
    }

    @GetMapping(path = "/schoolList")
    public ServerResponse<List<School>> getSchoolList(){
        ServerResponse<List<School>> srListSchool = new ServerResponse<>();
        srListSchool = schoolService.findAllSchool();
        List<School> listofSchool = srListSchool.getAssociatedObject();
         Collections.sort(listofSchool, new Comparator<School>() {
            @Override
            public int compare(School o1, School o2) {
                if(o1.getName().compareToIgnoreCase(o2.getName())<0) return -1;
                if(o1.getName().compareToIgnoreCase(o2.getName())>0) return 1;
                return 0;
            }
        });
         srListSchool.setAssociatedObject(listofSchool);
        return srListSchool;
    }

    @PostMapping(path = "/schoolSaved")
    public ServerResponse<School> postSchoolSaved(@Valid @RequestBody SchoolSaved schoolSaved,
                                                            BindingResult bindingResult) {
        ServerResponse<School> srSchool = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<School>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolSaved for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srSchool = schoolService.saveSchool(schoolSaved.getName(), schoolSaved.getAcronym(),
                    schoolSaved.getDescription(), schoolSaved.getLogoSchool(),
                    schoolSaved.getOwnerInstitution(), schoolSaved.getParentInstitution());
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
    public ServerResponse<School> putSchoolUpdated(@Valid @RequestBody SchoolUpdated schoolUpdated,
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
            srSchool = schoolService.updateSchool(schoolUpdated.getSchoolId(),
                    schoolUpdated.getName(), schoolUpdated.getAcronym(),
                    schoolUpdated.getDescription(), schoolUpdated.getLogoSchool(),
                    schoolUpdated.getOwnerInstitution(), schoolUpdated.getParentInstitution());
            srSchool.setErrorMessage("The school has been successfully updated");
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSchool.setErrorMessage("The school name does not match any school");
            srSchool.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSchool.setMoreDetails(e.getMessage());
        } catch (DuplicateSchoolException e) {
            //e.printStackTrace();
            srSchool.setErrorMessage("The school name already exist in the system");
            srSchool.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_DUPLICATED);
            srSchool.setMoreDetails(e.getMessage());
        }

        return srSchool;
    }


    @PutMapping(path = "/schoolNameUpdated")
    public ServerResponse<School> putSchoolNameUpdated(@Valid @RequestBody SchoolNameUpdated schoolNameUpdated,
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

        String schoolId = schoolNameUpdated.getSchoolId();
        String newSchoolName = schoolNameUpdated.getNewSchoolName();

        try {
            srSchool = schoolService.updateSchoolName(schoolId, newSchoolName);
            srSchool.setErrorMessage("The school name has been successfully updated");
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srSchool.setErrorMessage("The school name does not match any school");
            srSchool.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srSchool.setMoreDetails(e.getMessage());
        } catch (DuplicateSchoolException e) {
            //e.printStackTrace();
            srSchool.setErrorMessage("The school name already exist in the system");
            srSchool.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_DUPLICATED);
            srSchool.setMoreDetails(e.getMessage());
        }

        return srSchool;
    }


}
