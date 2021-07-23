package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.form.School.LevelForm;
import com.sprintgether.script.management.server.scriptmanagement.form.School.LevelFormList;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Level;
import com.sprintgether.script.management.server.scriptmanagement.service.school.LevelService;
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
public class LevelController {
    LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    public Pageable getLevelPageable(LevelFormList levelFormList){

        Sort.Order order1 = new Sort.Order(levelFormList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, levelFormList.getSortBy1());

        Sort.Order order2 = new Sort.Order(levelFormList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, levelFormList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(levelFormList.getPageNumber(), levelFormList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/levelPage")
    public ServerResponse<Page<Level>> getLevelPage(@Valid @RequestBody LevelFormList levelFormList,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Level>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the departmentForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        Pageable sort = this.getLevelPageable(levelFormList);

        if(!levelFormList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return levelService.findAllLevel(levelFormList.getKeyword(), sort);
        }

        return levelService.findAllLevel(sort);
    }

    @GetMapping(path = "/levelPageOfOption")
    public ServerResponse<Page<Level>> getLevelPageOfOption(@Valid @RequestBody LevelFormList levelFormList,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Page<Level>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        Pageable sort = this.getLevelPageable(levelFormList);
        String schoolName = levelFormList.getSchoolName();
        String departmentName = levelFormList.getDepartmentName();
        String optionName = levelFormList.getOptionName();

        ServerResponse<Page<Level>> srLevelPage = new ServerResponse<>();
        try {
            if(!levelFormList.getKeyword().equalsIgnoreCase("")){
                srLevelPage = levelService.findAllLevelOfOption(schoolName, departmentName,
                        optionName, levelFormList.getKeyword(), sort);
            }
            else{
                srLevelPage = levelService.findAllLevelOfOption(schoolName, departmentName,
                        optionName, sort);
            }
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srLevelPage.setErrorMessage("The associated option has not found");
            srLevelPage.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srLevelPage.setMoreDetails(e.getMessage());
        }

        return srLevelPage;
    }

    @GetMapping(path = "/levelListOfOption")
    public ServerResponse<List<Level>> getLevelListOfOption(@Valid @RequestBody LevelFormList levelFormList,
                                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<List<Level>>(error.getDefaultMessage(),
                        "Some form entry are not well filled in the institutionForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }
        String schoolName = levelFormList.getSchoolName();
        String departmentName = levelFormList.getDepartmentName();
        String optionName = levelFormList.getOptionName();

        ServerResponse<List<Level>> srLevelList = new ServerResponse<>();
        try {
            srLevelList = levelService.findAllLevelOfOption(schoolName, departmentName, optionName);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srLevelList.setErrorMessage("The associated option has not found");
            srLevelList.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srLevelList.setMoreDetails(e.getMessage());
        }
        return srLevelList;
    }

    @GetMapping(path = "/level")
    public ServerResponse<Level> getLevel(@Valid @RequestBody LevelFormList levelFormList){
        ServerResponse<Level> srLevel = new ServerResponse<>();
        try {
            srLevel = levelService.findLevelOfOptionByName(levelFormList.getSchoolName(), levelFormList.getDepartmentName(), levelFormList.getOptionName(), levelFormList.getLevelName());
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The associated school has not found");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The associated department has not found");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The associated option has not found");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        }
        return srLevel;
    }

    @GetMapping(path = "/levelList")
    public ServerResponse<List<Level>> getLevelList(){
        ServerResponse<List<Level>> srListLevel = new ServerResponse<>();
        srListLevel = levelService.findAllLevel();
        List<Level> listofLevel = srListLevel.getAssociatedObject();
        Collections.sort(listofLevel, new Comparator<Level>() {
            @Override
            public int compare(Level o1, Level o2) {
                if(o1.getName().compareToIgnoreCase(o2.getName())<0) return -1;
                if(o1.getName().compareToIgnoreCase(o2.getName())>0) return 1;
                return 0;
            }
        });
        srListLevel.setAssociatedObject(listofLevel);
        return srListLevel;
    }


    @PostMapping(path = "/levelSaved")
    public ServerResponse<Level> postLevelSaved(@Valid @RequestBody LevelForm levelForm,
                                                  BindingResult bindingResult) {
        ServerResponse<Level> srLevel = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Level>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srLevel = levelService.saveLevel(levelForm.getName(), levelForm.getAcronym(),
                    levelForm.getOwnerOption(), levelForm.getEmailClassPerfect(),
                    levelForm.getOwnerDepartment(), levelForm.getOwnerSchool());
            srLevel.setErrorMessage("The Level has been successfully created");
        } catch (DuplicateLevelInOptionException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The level name does not match any level in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_SAVED_LEVEL);
            srLevel.setMoreDetails(e.getMessage());
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The option name does not match any option in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        }

        return srLevel;
    }

    @PutMapping(path = "/levelUpdated")
    public ServerResponse<Level> postLevelUpdated(@Valid @RequestBody LevelForm levelForm,
                                                    BindingResult bindingResult) {
        ServerResponse<Level> srLevel = new ServerResponse("", "", ResponseCode.BAD_REQUEST, null);

        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.toString());
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                return new ServerResponse<Level>(error.getDefaultMessage(),
                        "Some entry are not well filled in the schoolForm for save",
                        ResponseCode.ERROR_IN_FORM_FILLED,
                        null);
            }
        }

        try {
            srLevel = levelService.updateLevel(levelForm.getName(), levelForm.getAcronym(), levelForm.getOwnerOption(), levelForm.getEmailClassPerfect(), levelForm.getOwnerDepartment(), levelForm.getOwnerSchool());
            srLevel.setErrorMessage("The Level has been successfully updated");
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The option name does not match any option in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The level name does not match any level in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        }

        return srLevel;
    }


}
