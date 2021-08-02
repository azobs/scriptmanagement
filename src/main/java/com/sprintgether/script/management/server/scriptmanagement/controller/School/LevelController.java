package com.sprintgether.script.management.server.scriptmanagement.controller.School;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.form.school.level.LevelNameUpdated;
import com.sprintgether.script.management.server.scriptmanagement.form.school.level.LevelSaved;
import com.sprintgether.script.management.server.scriptmanagement.form.school.level.LevelList;
import com.sprintgether.script.management.server.scriptmanagement.form.school.level.LevelUpdated;
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

    public Pageable getLevelPageable(LevelList levelList){

        Sort.Order order1 = new Sort.Order(levelList.getDirection1().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, levelList.getSortBy1());

        Sort.Order order2 = new Sort.Order(levelList.getDirection2().equalsIgnoreCase("ASC")
                ?Sort.Direction.ASC:Sort.Direction.DESC, levelList.getSortBy2());

        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        orderList.add(order1);
        orderList.add(order2);
        Pageable sort = PageRequest.of(levelList.getPageNumber(), levelList.getPageSize(), Sort.by(orderList));

        return sort;
    }

    @GetMapping(path = "/levelPage")
    public ServerResponse<Page<Level>> getLevelPage(@Valid @RequestBody LevelList levelList,
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

        Pageable sort = this.getLevelPageable(levelList);

        if(!levelList.getKeyword().equalsIgnoreCase("")){
            /***
             * We must make research by keyword
             */
            return levelService.findAllLevel(levelList.getKeyword(), sort);
        }

        return levelService.findAllLevel(sort);
    }

    @GetMapping(path = "/levelPageOfOption")
    public ServerResponse<Page<Level>> getLevelPageOfOption(@Valid @RequestBody LevelList levelList,
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
        Pageable sort = this.getLevelPageable(levelList);
        String schoolName = levelList.getSchoolName();
        String departmentName = levelList.getDepartmentName();
        String optionName = levelList.getOptionName();
        String levelId = levelList.getLevelId();

        ServerResponse<Page<Level>> srLevelPage = new ServerResponse<>();
        try {
            if(!levelList.getKeyword().equalsIgnoreCase("")){
                srLevelPage = levelService.findAllLevelOfOption(levelId, schoolName, departmentName,
                        optionName, levelList.getKeyword(), sort);
            }
            else{
                srLevelPage = levelService.findAllLevelOfOption(levelId, schoolName, departmentName,
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
    public ServerResponse<List<Level>> getLevelListOfOption(@Valid @RequestBody LevelList levelList,
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
        String schoolName = levelList.getSchoolName();
        String departmentName = levelList.getDepartmentName();
        String optionName = levelList.getOptionName();
        String levelId = levelList.getLevelId();

        ServerResponse<List<Level>> srLevelList = new ServerResponse<>();
        try {
            srLevelList = levelService.findAllLevelOfOption(levelId, schoolName, departmentName,
                    optionName);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srLevelList.setErrorMessage("The associated option has not found");
            srLevelList.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
            srLevelList.setMoreDetails(e.getMessage());
        }
        return srLevelList;
    }

    @GetMapping(path = "/level")
    public ServerResponse<Level> getLevel(@Valid @RequestBody LevelList levelList){
        ServerResponse<Level> srLevel = new ServerResponse<>();
        try {
            srLevel = levelService.findLevelOfOptionByName(levelList.getSchoolName(), levelList.getDepartmentName(), levelList.getOptionName(), levelList.getLevelName());
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
    public ServerResponse<Level> postLevelSaved(@Valid @RequestBody LevelSaved levelSaved,
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
            srLevel = levelService.saveLevel(levelSaved.getName(), levelSaved.getAcronym(),
                    levelSaved.getOwnerOption(), levelSaved.getEmailClassPerfect(),
                    levelSaved.getOptionId(), levelSaved.getOwnerDepartment(),
                    levelSaved.getOwnerSchool());
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
    public ServerResponse<Level> postLevelUpdated(@Valid @RequestBody LevelUpdated levelUpdated,
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
            srLevel = levelService.updateLevel(levelUpdated.getLevelId(), levelUpdated.getName(),
                    levelUpdated.getAcronym(), levelUpdated.getOwnerOption(),
                    levelUpdated.getEmailClassPerfect(), levelUpdated.getOwnerDepartment(),
                    levelUpdated.getOwnerSchool());
            srLevel.setErrorMessage("The Level has been successfully updated");
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The level name does not match any level in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        } catch (DuplicateLevelInOptionException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The level name already exist in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_LEVEL_DUPLICATED);
            srLevel.setMoreDetails(e.getMessage());
        }

        return srLevel;
    }

    @PutMapping(path = "/levelNameUpdated")
    public ServerResponse<Level> postLevelNameUpdated(@Valid @RequestBody LevelNameUpdated levelNameUpdated,
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
        String levelId = levelNameUpdated.getLevelId();
        String newLevelName = levelNameUpdated.getNewLevelName();

        try {
            srLevel = levelService.updateLevelName(levelId, newLevelName);
            srLevel.setErrorMessage("The Level name has been successfully updated");
        } catch (LevelNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The level name does not match any level in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_LEVEL_FOUND);
            srLevel.setMoreDetails(e.getMessage());
        } catch (DuplicateLevelInOptionException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("The level name already exist in the system");
            srLevel.setResponseCode(ResponseCode.EXCEPTION_LEVEL_DUPLICATED);
            srLevel.setMoreDetails(e.getMessage());
        }

        return srLevel;
    }



}
