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
    SchoolService schoolService;

    public OptionServiceImpl(OptionRepository optionRepository,
                             DepartmentService departmentService,
                             SchoolService schoolService) {
        this.optionRepository = optionRepository;
        this.departmentService = departmentService;
        this.schoolService = schoolService;
    }

    @Override
    public ServerResponse<Option> findOptionOfDepartmentByName(String schoolName, String departmentName,
                                                               String optionName)
            throws SchoolNotFoundException, DepartmentNotFoundException {
        schoolName = schoolName.toLowerCase();
        departmentName = departmentName.toLowerCase();
        optionName = optionName.toLowerCase();
        ServerResponse<Option> srOption = new ServerResponse<>();
        ServerResponse<School> srSchool = schoolService.findSchoolByName(schoolName);
        if(srSchool.getResponseCode() != ResponseCode.SCHOOL_FOUND){
            throw new SchoolNotFoundException("The school name specified does not match any school in the system");
        }
        School concernedSchool = srSchool.getAssociatedObject();

        ServerResponse<Department> srDepartment = departmentService.findDepartmentOfSchoolByName(
                concernedSchool.getName(), departmentName);
        if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
            throw new DepartmentNotFoundException("The department name specified does not match any department in the school specified");
        }
        Department concernedDepartment = srDepartment.getAssociatedObject();

        Optional<Option> optionalOption = optionRepository.findByOwnerDepartmentAndName(concernedDepartment, optionName);
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
        keyword = keyword.toLowerCase();
        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();
        Page<Option> pageOfOption = optionRepository.findAllByNameContaining(keyword, pageable);
        srOptionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srOptionPage.setErrorMessage("The option page has been successfully made");
        srOptionPage.setAssociatedObject(pageOfOption);
        return srOptionPage;
    }

    @Override
    public ServerResponse<Page<Option>> findAllOptionOfDepartment(String schoolName,
                                                                  String departmentName, String keyword,
                                                                  Pageable pageable) throws DepartmentNotFoundException {
        schoolName = schoolName.toLowerCase();
        departmentName = departmentName.toLowerCase();
        keyword = keyword.toLowerCase();
        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();
        try {
            ServerResponse<Department> srDepartment = departmentService.findDepartmentOfSchoolByName(schoolName, departmentName);
            if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
                throw new DepartmentNotFoundException("The department name does not match any department in the system");
            }
            Department concernedDepartment = srDepartment.getAssociatedObject();
            Page<Option> pageofOption = optionRepository.findAllByOwnerDepartmentAndNameContaining(
                    concernedDepartment, keyword, pageable);
            srOptionPage.setErrorMessage("The option page of the department specified has been made");
            srOptionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srOptionPage.setAssociatedObject(pageofOption);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srOptionPage.setErrorMessage("The school and department name specified does not match any department in the system");
            srOptionPage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srOptionPage.setMoreDetails(e.getMessage());
        }
        return srOptionPage;
    }

    @Override
    public ServerResponse<Page<Option>> findAllOptionOfDepartment(String schoolName,
                                                                  String departmentName,
                                                                  Pageable pageable)
            throws DepartmentNotFoundException {
        schoolName = schoolName.toLowerCase();
        departmentName = departmentName.toLowerCase();
        ServerResponse<Page<Option>> srOptionPage = new ServerResponse<>();
        try {
            ServerResponse<Department> srDepartment = departmentService.findDepartmentOfSchoolByName(schoolName, departmentName);
            if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
                throw new DepartmentNotFoundException("The department name does not match any department in the system");
            }
            Department concernedDepartment = srDepartment.getAssociatedObject();
            Page<Option> pageofOption = optionRepository.findAllByOwnerDepartment(concernedDepartment, pageable);
            srOptionPage.setErrorMessage("The option page of the department specified has been made");
            srOptionPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srOptionPage.setAssociatedObject(pageofOption);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srOptionPage.setErrorMessage("The school and department name specified does not match any department in the system");
            srOptionPage.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srOptionPage.setMoreDetails(e.getMessage());
        }
        return srOptionPage;
    }

    @Override
    public ServerResponse<List<Option>> findAllOptionOfDepartment(String schoolName,
                                                                  String departmentName) throws DepartmentNotFoundException {
        schoolName = schoolName.toLowerCase();
        departmentName = departmentName.toLowerCase();
        ServerResponse<List<Option>> srOptionList = new ServerResponse<>();
        try {
            ServerResponse<Department> srDepartment = departmentService.findDepartmentOfSchoolByName(schoolName, departmentName);
            if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
                throw new DepartmentNotFoundException("The department name does not match any department in the system");
            }
            Department concernedDepartment = srDepartment.getAssociatedObject();
            List<Option> pageofOption = optionRepository.findAllByOwnerDepartmentOrderByName(concernedDepartment);
            srOptionList.setErrorMessage("The option list of the department specified has been made");
            srOptionList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srOptionList.setAssociatedObject(pageofOption);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srOptionList.setErrorMessage("The school and department name specified does not match any department in the system");
            srOptionList.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srOptionList.setMoreDetails(e.getMessage());
        }
        return srOptionList;
    }

    @Override
    public Option saveOption(Option option) {
        return optionRepository.save(option);
    }

    @Override
    public ServerResponse<Option> saveOption(String name, String acronym, String description,
                                             String departmentName, String schoolName)
            throws DuplicateOptionInDepartmentException, DepartmentNotFoundException {
        name = name.toLowerCase();
        departmentName = departmentName.toLowerCase();
        schoolName = schoolName.toLowerCase();
        ServerResponse<Option> srOption = new ServerResponse<>();
        try {
            ServerResponse<Department> srDepartment = departmentService.findDepartmentOfSchoolByName(schoolName, departmentName);
            if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
                throw new DepartmentNotFoundException("The department name specified does not match any department in the precised school");
            }
            Department concernedDepartment = srDepartment.getAssociatedObject();
            ServerResponse<Option> srOption1 = this.findOptionOfDepartmentByName(schoolName,
                    departmentName, name);
            if(srOption1.getResponseCode() == ResponseCode.OPTION_FOUND){
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
    public ServerResponse<Option> updateOption(String name, String acronym, String description,
                                               String departmentName, String schoolName)
            throws OptionNotFoundException, DepartmentNotFoundException {
        name  = name.toLowerCase();
        departmentName = departmentName.toLowerCase();
        schoolName =  schoolName.toLowerCase();
        ServerResponse<Option> srOption = new ServerResponse<>();
        ServerResponse<Department> srDepartment = null;
        try {
            srDepartment = departmentService.findDepartmentOfSchoolByName(schoolName, departmentName);
            if(srDepartment.getResponseCode() != ResponseCode.DEPARTMENT_FOUND){
                throw new DepartmentNotFoundException("The department name specified does not match any department in the precised school");
            }
            Department concernedDepartment = srDepartment.getAssociatedObject();
            ServerResponse<Option> srOption1 = this.findOptionOfDepartmentByName(schoolName,
                    departmentName, name);
            if(srOption1.getResponseCode() != ResponseCode.OPTION_FOUND){
                throw new OptionNotFoundException("The option name does not match any option in the department specified");
            }
            Option option = srOption1.getAssociatedObject();
            option.setAcronym(acronym);
            option.setDescription(description);
            Option optionUpdated = optionRepository.save(option);

            srOption.setResponseCode(ResponseCode.OPTION_UPDATED);
            srOption.setErrorMessage("The option has been successfully updated");
            srOption.setAssociatedObject(optionUpdated);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srOption.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srOption.setErrorMessage("The school name specified does not match any school in the system");
            srOption.setMoreDetails(e.getMessage());
        }

        return srOption;
    }

    @Override
    public ServerResponse<Option> deleteOption(String schoolName, String departmentName,
                                               String optionName)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException {
        schoolName = schoolName.toLowerCase();
        departmentName = departmentName.toLowerCase();
        optionName = optionName.toLowerCase();
        return null;
    }
}
