package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.OptionRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DepartmentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateOptionInDepartmentException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.OptionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.SchoolNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Department;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Option;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OptionServiceImpl implements OptionService {

    OptionRepository optionRepository;
    DepartmentService departmentService;

    public OptionServiceImpl(OptionRepository optionRepository,
                             DepartmentService departmentService) {
        this.optionRepository = optionRepository;
        this.departmentService = departmentService;
    }

    @Override
    public ServerResponse<Option> findOptionOfDepartmentByName(String schoolName, String departmentName,
                                                               String optionName)
            throws DepartmentNotFoundException, SchoolNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        ServerResponse<Option> srOption = new ServerResponse<>();
        ServerResponse<Department> srDepartment = departmentService.findDepartmentOfSchoolByName(
                schoolName, departmentName);
        if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
            throw new DepartmentNotFoundException("The department name specified does not match any department in the school specified");
        }
        Department concernedDepartment = srDepartment.getAssociatedObject();

        Optional<Option> optionalOption = optionRepository.findByOwnerDepartmentAndName(
                concernedDepartment, optionName);
        if(!optionalOption.isPresent()){
            srOption.setResponseCode(ResponseCode.OPTION_NOT_FOUND);
            srOption.setErrorMessage("The option name specified does not match any option in the department of the school precised");
        }
        else{
            srOption.setResponseCode(ResponseCode.OPTION_FOUND);
            srOption.setErrorMessage("The option has been found successfully");
            srOption.setAssociatedObject(optionalOption.get());
        }

        return srOption;
    }

    @Override
    public ServerResponse<Option> findOptionOfDepartmentById(String optionId) {
        optionId = optionId.trim();
        ServerResponse<Option> srOption = new ServerResponse<>();
        Optional<Option> optionalOption = optionRepository.findById(optionId);
        if(!optionalOption.isPresent()){
            srOption.setResponseCode(ResponseCode.OPTION_NOT_FOUND);
            srOption.setErrorMessage("The option name specified does not match any option " +
                    "in the department of the school precised");
        }
        else{
            srOption.setResponseCode(ResponseCode.OPTION_FOUND);
            srOption.setErrorMessage("The option has been found successfully");
            srOption.setAssociatedObject(optionalOption.get());
        }
        return srOption;
    }

    @Override
    public ServerResponse<List<Option>> findAllOption() {
        ServerResponse<List<Option>> srOptionList = new ServerResponse<>();
        List<Option> listOfOption = optionRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "name")));
        srOptionList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srOptionList.setErrorMessage("The option list has been successfully made");
        srOptionList.setAssociatedObject(listOfOption);
        return srOptionList;
    }

    @Override
    public ServerResponse<Page<Option>> findAllOption(Pageable pageable) {
        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();
        Page<Option> pageOfOption = optionRepository.findAll(pageable);
        srOptionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srOptionPage.setErrorMessage("The option page has been successfully made");
        srOptionPage.setAssociatedObject(pageOfOption);
        return srOptionPage;
    }

    @Override
    public ServerResponse<Page<Option>> findAllOption(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase().trim();
        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();
        Page<Option> pageOfOption = optionRepository.findAllByNameContaining(keyword, pageable);
        srOptionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srOptionPage.setErrorMessage("The option page has been successfully made");
        srOptionPage.setAssociatedObject(pageOfOption);
        return srOptionPage;
    }

    public Optional<Department> findConcernedDepartment(String departmentId,
                                                        String schoolName,
                                                        String departmentName){
        Department concernedDepartment = null;
        ServerResponse<Department> srDepartmentFoundById = departmentService.
                findDepartmentOfSchoolById(departmentId);
        if(srDepartmentFoundById.getResponseCode() != ResponseCode.DEPARTMENT_FOUND) {
            ServerResponse<Department> srDepartment = null;
            try {
                srDepartment = departmentService.findDepartmentOfSchoolByName(
                        schoolName, departmentName);
                concernedDepartment = srDepartment.getAssociatedObject();

            } catch (SchoolNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            concernedDepartment = srDepartmentFoundById.getAssociatedObject();
        }
        return Optional.ofNullable(concernedDepartment);
    }

    @Override
    public ServerResponse<Page<Option>> findAllOptionOfDepartment(String departmentId,
                                                                  String schoolName,
                                                                  String departmentName,
                                                                  String keyword,
                                                                  Pageable pageable) throws DepartmentNotFoundException {
        departmentId = departmentId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        keyword = keyword.toLowerCase().trim();
        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();

        Department concernedDepartment = null;
        Optional<Department> optionalDepartment = this.findConcernedDepartment(departmentId, schoolName, departmentName);
        if(!optionalDepartment.isPresent()){
            throw new DepartmentNotFoundException("The department specified does not found in the system");
        }
        concernedDepartment = optionalDepartment.get();

        Page<Option> pageofOption = optionRepository.findAllByOwnerDepartmentAndNameContaining(
                concernedDepartment, keyword, pageable);
        srOptionPage.setErrorMessage("The option page of the department specified has been made");
        srOptionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srOptionPage.setAssociatedObject(pageofOption);

        return srOptionPage;
    }


    @Override
    public ServerResponse<Page<Option>> findAllOptionOfDepartment(String departmentId,
                                                                  String schoolName,
                                                                  String departmentName,
                                                                  Pageable pageable)
            throws DepartmentNotFoundException {
        departmentId = departmentId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();

        Department concernedDepartment = null;
        Optional<Department> optionalDepartment = this.findConcernedDepartment(departmentId, schoolName, departmentName);
        if(!optionalDepartment.isPresent()){
            throw new DepartmentNotFoundException("The department specified does not found in the system");
        }
        concernedDepartment = optionalDepartment.get();

        Page<Option> pageofOption = optionRepository.findAllByOwnerDepartment(
                concernedDepartment, pageable);
        srOptionPage.setErrorMessage("The option page of the department specified has been made");
        srOptionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srOptionPage.setAssociatedObject(pageofOption);

        return srOptionPage;
    }


    @Override
    public ServerResponse<List<Option>> findAllOptionOfDepartment(String departmentId,
                                                                  String schoolName,
                                                                  String departmentName)
            throws DepartmentNotFoundException {
        departmentId = departmentId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        ServerResponse<List<Option>> srOptionList = new ServerResponse<>();

        Department concernedDepartment = null;
        Optional<Department> optionalDepartment = this.findConcernedDepartment(departmentId, schoolName, departmentName);
        if(!optionalDepartment.isPresent()){
            throw new DepartmentNotFoundException("The department specified does not found in the system");
        }
        concernedDepartment = optionalDepartment.get();

        List<Option> pageofOption = optionRepository.
                findAllByOwnerDepartmentOrderByName(concernedDepartment);
        srOptionList.setErrorMessage("The option list of the department specified " +
                "has been made");
        srOptionList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srOptionList.setAssociatedObject(pageofOption);

        return srOptionList;
    }

    @Override
    public Option saveOption(Option option) {
        return optionRepository.save(option);
    }

    @Override
    public ServerResponse<Option> saveOption(String name,
                                             String acronym,
                                             String description,
                                             String departmentId,
                                             String departmentName,
                                             String schoolName)
            throws DuplicateOptionInDepartmentException, DepartmentNotFoundException {

        name = name.toLowerCase().trim();
        acronym = acronym.trim();
        description = description.trim();
        departmentId = departmentId.trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();
        ServerResponse<Option> srOption = new ServerResponse<>(ResponseCode.OPTION_NOT_CREATED);

        try{
            Department concernedDepartment = null;

            Optional<Department> optionalDepartment = this.findConcernedDepartment(departmentId,
                    schoolName, departmentName);
            if(!optionalDepartment.isPresent()){
                throw new DepartmentNotFoundException("The specified department does not found in " +
                        "the system");
            }
            concernedDepartment = optionalDepartment.get();

            ServerResponse<Option> srOption1 = null;

            srOption1 = this.findOptionOfDepartmentByName(schoolName,
                    departmentName, name);
            if (srOption1.getResponseCode() == ResponseCode.OPTION_FOUND) {
                throw new DuplicateOptionInDepartmentException("The option name already identify other option in the department specified");
            }
            Option option = new Option();
            option.setAcronym(acronym);
            option.setDescription(description);
            option.setName(name);
            option.setOwnerDepartment(concernedDepartment);
            Option optionSaved = optionRepository.save(option);

            srOption.setResponseCode(ResponseCode.OPTION_CREATED);
            srOption.setErrorMessage("The option has been successfully created");
            srOption.setAssociatedObject(optionSaved);


        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srOption.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srOption.setErrorMessage("The school name specified does not match any school in the system");
            srOption.setMoreDetails(e.getMessage());
        }

        return srOption;
    }



    @Override
    public ServerResponse<Option> updateOption(String optionId, String name, String acronym,
                                               String description,
                                               String departmentName, String schoolName)
            throws OptionNotFoundException, DuplicateOptionInDepartmentException {
        optionId = optionId.trim();
        name  = name.toLowerCase().trim();
        acronym = acronym.trim();
        description = description.trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName =  schoolName.toLowerCase().trim();

        ServerResponse<Option> srOption = new ServerResponse<>(ResponseCode.OPTION_NOT_UPDATED);
        srOption.setResponseCode(ResponseCode.OPTION_NOT_UPDATED);


        //////////////////////
        Optional<Option> optionalOption = optionRepository.findById(optionId);
        if(!optionalOption.isPresent()){
            throw new OptionNotFoundException("The option id does not match any option in the system");
        }
        else{
            Option optionToUpdated1 = optionalOption.get();

            optionToUpdated1.setAcronym(acronym);
            optionToUpdated1.setDescription(description);

            try {
                ServerResponse<Option> srOption2 = this.findOptionOfDepartmentByName(schoolName,
                        departmentName, name);
                if(srOption2.getResponseCode() == ResponseCode.OPTION_FOUND){
                    Option optionToUpdated2 = srOption2.getAssociatedObject();
                    if(!optionToUpdated1.getId().equalsIgnoreCase(optionToUpdated2.getId())){
                        throw new DuplicateOptionInDepartmentException("The option name specified " +
                                " already exist un the indicated department");
                    }
                }
                else{
                    optionToUpdated1.setName(name);
                }

                Option optionUpdated = optionRepository.save(optionToUpdated1);

                srOption.setResponseCode(ResponseCode.OPTION_UPDATED);
                srOption.setErrorMessage("The option has been successfully updated");
                srOption.setAssociatedObject(optionUpdated);
            } catch (DepartmentNotFoundException e) {
                //e.printStackTrace();
                srOption.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
                srOption.setErrorMessage("The department name specified does not match any " +
                        "department in the system");
                srOption.setMoreDetails(e.getMessage());
            } catch (SchoolNotFoundException e) {
                //e.printStackTrace();
                srOption.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
                srOption.setErrorMessage("The school name specified does not match any " +
                        "school in the system");
                srOption.setMoreDetails(e.getMessage());
            }
        }
        //////////////////////

        return srOption;
    }

    @Override
    public ServerResponse<Option> updateOptionName(String optionId, String optionName)
            throws OptionNotFoundException, DuplicateOptionInDepartmentException {
        optionId = optionId.trim();
        optionName = optionName.toLowerCase().trim();
        ServerResponse<Option> srOption = new ServerResponse<>(ResponseCode.OPTION_NOT_UPDATED);
        Optional<Option> optionalOption = optionRepository.findById(optionId);
        if(!optionalOption.isPresent()){
            throw new OptionNotFoundException("The option Id does not match any option in the system");
        }
        Option optionToUpdated = optionalOption.get();
        String schoolName = optionToUpdated.getOwnerDepartment().getOwnerSchool().getName();
        String departmentName = optionToUpdated.getOwnerDepartment().getName();
        try {
            ServerResponse<Option> srOption1 = this.findOptionOfDepartmentByName(schoolName,
                    departmentName, optionName);
            if(srOption1.getResponseCode() == ResponseCode.OPTION_FOUND){
                throw new DuplicateOptionInDepartmentException("The new option name match with " +
                        "another option in the same department");
            }
            optionToUpdated.setName(optionName);
            Option optionUpdate = this.saveOption(optionToUpdated);
            srOption.setErrorMessage("The option name has been successfully updated");
            srOption.setResponseCode(ResponseCode.OPTION_UPDATED);
            srOption.setAssociatedObject(optionUpdate);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srOption.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srOption.setErrorMessage("The school name associated with the optionId does not match " +
                    "any school in the system");
            srOption.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srOption.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srOption.setErrorMessage("The department name associated with the optionId does not " +
                    "match any department in the system");
            srOption.setMoreDetails(e.getMessage());
        }
        return srOption;
    }

    @Override
    public ServerResponse<Option> deleteOption(String optionId, String schoolName, String departmentName,
                                               String optionName)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException {
        optionId = optionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        return null;
    }

}
