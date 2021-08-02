package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.DepartmentRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DepartmentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateDepartmentInSchoolException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.SchoolNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Department;
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
public class DepartmentServiceImpl implements DepartmentService{
    DepartmentRepository departmentRepository;
    SchoolService schoolService;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 SchoolService schoolService) {
        this.departmentRepository = departmentRepository;
        this.schoolService = schoolService;
    }

    @Override
    public ServerResponse<Department> findDepartmentOfSchoolById(String departmentId) {
        departmentId = departmentId.toLowerCase().trim();
        ServerResponse<Department> srDepartment = new ServerResponse<>();

        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
        if(optionalDepartment.isPresent()){
            srDepartment.setErrorMessage("The department has been identified and returned");
            srDepartment.setResponseCode(ResponseCode.DEPARTMENT_FOUND);
            srDepartment.setAssociatedObject(optionalDepartment.get());
        }
        else{
            srDepartment.setResponseCode(ResponseCode.DEPARTMENT_NOT_FOUND);
            srDepartment.setErrorMessage("The department has not been found in the system");
        }
        return srDepartment;
    }

    @Override
    public ServerResponse<Department> findDepartmentOfSchoolByName(String schoolName,
                                                                   String departmentName) throws SchoolNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        ServerResponse<Department> srDepartment = new ServerResponse<>();
        ServerResponse<School> srSchool = schoolService.findSchoolByName(schoolName);
        if(srSchool.getResponseCode() != ResponseCode.SCHOOL_FOUND){
            throw new SchoolNotFoundException("The specified school name does not match any school in the system");
        }
        School school = srSchool.getAssociatedObject();
        Optional<Department> optionalDepartment = departmentRepository.findByOwnerSchoolAndName(
                school, departmentName);
        if(optionalDepartment.isPresent()){
            srDepartment.setErrorMessage("The department has been identified and returned");
            srDepartment.setResponseCode(ResponseCode.DEPARTMENT_FOUND);
            srDepartment.setAssociatedObject(optionalDepartment.get());
        }
        else{
            srDepartment.setResponseCode(ResponseCode.DEPARTMENT_NOT_FOUND);
            srDepartment.setErrorMessage("The department has not been found in the system");
        }
        return srDepartment;
    }

    @Override
    public ServerResponse<List<Department>> findAllDepartment() {
        ServerResponse<List<Department>> srDepartmentList = new ServerResponse<>();
        List<Department> listofDepartment = departmentRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "name")));
        srDepartmentList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srDepartmentList.setErrorMessage("The list of department has been made successfully");
        srDepartmentList.setAssociatedObject(listofDepartment);
        return srDepartmentList;
    }

    @Override
    public ServerResponse<Page<Department>> findAllDepartment(Pageable pageable) {
        ServerResponse<Page<Department>> srDepartmentPage = new ServerResponse<>();
        Page<Department> pageofDepartment = departmentRepository.findAll(pageable);
        srDepartmentPage.setErrorMessage("The department page has been made successfully");
        srDepartmentPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srDepartmentPage.setAssociatedObject(pageofDepartment);
        return srDepartmentPage;
    }

    @Override
    public ServerResponse<Page<Department>> findAllDepartment(String keyword, Pageable pageable) {
        keyword  = keyword.toLowerCase().trim();
        ServerResponse<Page<Department>> srPageDepartment = new ServerResponse<Page<Department>>();
        Page<Department> pageofDepartment = departmentRepository.findAllByNameContaining(keyword, pageable);
        srPageDepartment.setErrorMessage("The page of department has been made successfully");
        srPageDepartment.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageDepartment.setAssociatedObject(pageofDepartment);
        return srPageDepartment;
    }

    @Override
    public ServerResponse<Page<Department>> findAllDepartmentOfSchool(String schoolName,
                                                                      String keyword,
                                                                      Pageable pageable)
            throws SchoolNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        keyword = keyword.toLowerCase().trim();
        ServerResponse<Page<Department>> srPageDepartment = new ServerResponse<Page<Department>>();
        ServerResponse<School> srSchool = schoolService.findSchoolByName(schoolName);
        if(srSchool.getResponseCode() != ResponseCode.SCHOOL_FOUND){
            throw new SchoolNotFoundException("The specifed school is not found in the system");
        }
        School concernedSchool = srSchool.getAssociatedObject();
        Page<Department> pageofDepartment = departmentRepository.findAllByOwnerSchoolAndNameContaining(concernedSchool, keyword, pageable);
        srPageDepartment.setErrorMessage("The page of department has been made successfully");
        srPageDepartment.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageDepartment.setAssociatedObject(pageofDepartment);
        return srPageDepartment;
    }

    @Override
    public ServerResponse<Page<Department>> findAllDepartmentOfSchool(String schoolName, Pageable pageable)
            throws SchoolNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        ServerResponse<Page<Department>> srPageofDepartment = new ServerResponse<>();
        ServerResponse<School> srSchool = schoolService.findSchoolByName(schoolName);
        if(srSchool.getResponseCode() != ResponseCode.SCHOOL_FOUND){
            throw new SchoolNotFoundException("The school specified is not found in the system");
        }
        School school = srSchool.getAssociatedObject();
        Page<Department> pageOfDepartment = departmentRepository.findAllByOwnerSchool(school, pageable);
        srPageofDepartment.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageofDepartment.setErrorMessage("The school department school page has been made successfully");
        srPageofDepartment.setAssociatedObject(pageOfDepartment);
        return srPageofDepartment;
    }

    @Override
    public ServerResponse<List<Department>> findAllDepartmentOfSchool(String schoolName)
            throws SchoolNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        ServerResponse<List<Department>> srListofDepartment = new ServerResponse<>();
        ServerResponse<School> srSchool = schoolService.findSchoolByName(schoolName);
        if(srSchool.getResponseCode() != ResponseCode.SCHOOL_FOUND){
            throw new SchoolNotFoundException("The school specified is not found in the system");
        }
        School school = srSchool.getAssociatedObject();
        List<Department> listOfDepartment = departmentRepository.findAllByOwnerSchoolOrderByName(school);
        srListofDepartment.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srListofDepartment.setErrorMessage("The school department school list has been made successfully");
        srListofDepartment.setAssociatedObject(listOfDepartment);
        return srListofDepartment;
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public ServerResponse<Department> saveDepartment(String name, String acronym, String description,
                                                     String ownerSchoolName)
            throws DuplicateDepartmentInSchoolException, SchoolNotFoundException {
        name = name.toLowerCase().trim();
        acronym = acronym.trim();
        description = description.trim();
        ownerSchoolName = ownerSchoolName.toLowerCase().trim();
        ServerResponse<Department> srDepartment = new ServerResponse<>();
        /****
         * It is not possible to have a department without school
         */
        ServerResponse<School> srSchool = schoolService.findSchoolByName(ownerSchoolName);
        if(srSchool.getResponseCode() != ResponseCode.SCHOOL_FOUND){
            throw new SchoolNotFoundException("The owner school specified for the department is not found");
        }
        /****
         * It is not possible to have 2 departments with the same name in one school
         */
        ServerResponse<Department> srDepartment1 = new ServerResponse<>();
        School ownerSchool = srSchool.getAssociatedObject();
        srDepartment1 = this.findDepartmentOfSchoolByName(ownerSchool.getName(), name);
        if(srDepartment1.getResponseCode() == ResponseCode.DEPARTMENT_FOUND){
            throw new DuplicateDepartmentInSchoolException("The department name specified is " +
                    "already used by another department in that school");
        }
        /**
         * There, we are sure that the department will be unique in that school
         */
        Department department = new Department();
        department.setAcronym(acronym);
        department.setDescription(description);
        department.setName(name);
        department.setOwnerSchool(ownerSchool);
        Department departmentSaved = departmentRepository.save(department);

        srDepartment.setResponseCode(ResponseCode.DEPARTMENT_CREATED);
        srDepartment.setErrorMessage("The department has been successfully created in the system");
        srDepartment.setAssociatedObject(departmentSaved);

        return srDepartment;
    }

    @Override
    public ServerResponse<Department> updateDepartment(String departmentId, String name, String acronym, String description,
                                                       String ownerSchoolName)
            throws DuplicateDepartmentInSchoolException, DepartmentNotFoundException {
        departmentId = departmentId.trim();
        name  = name.toLowerCase().trim();
        acronym = acronym.trim();
        description = description.trim();
        ownerSchoolName = ownerSchoolName.toLowerCase().trim();
        ServerResponse<Department> srDepartment = new ServerResponse<>();
        srDepartment.setResponseCode(ResponseCode.DEPARTMENT_NOT_UPDATED);

        //////////////////////////////////
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
        if(!optionalDepartment.isPresent()){
            throw new DepartmentNotFoundException("The department id specified does not match " +
                    "any department object in the system");
        }
        else{
            Department departmentToUpdated1 = optionalDepartment.get();

            departmentToUpdated1.setDescription(description);
            departmentToUpdated1.setAcronym(acronym);

            ServerResponse<Department> srDepartment2 = null;
            try {
                srDepartment2 = this.findDepartmentOfSchoolByName(ownerSchoolName, name);

                if(srDepartment2.getResponseCode() == ResponseCode.DEPARTMENT_FOUND){
                    Department departmentToUpdated2 = srDepartment2.getAssociatedObject();
                    if(!departmentToUpdated1.getId().equalsIgnoreCase(departmentToUpdated2.getId())){
                        throw new DuplicateDepartmentInSchoolException("The new department name will " +
                                "cause duplication in the school specified");
                    }
                }
                else{
                    departmentToUpdated1.setName(name);
                }
                Department departmentUpdated = departmentRepository.save(departmentToUpdated1);

                srDepartment.setResponseCode(ResponseCode.DEPARTMENT_UPDATED);
                srDepartment.setErrorMessage("The department specified has been updated successfully");
                srDepartment.setAssociatedObject(departmentUpdated);

            } catch (SchoolNotFoundException e) {
                //e.printStackTrace();
                srDepartment.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
                srDepartment.setErrorMessage("The school name specified does not match any school " +
                        "in the system");
                srDepartment.setMoreDetails(e.getMessage());
            }

        }
        //////////////////////////////////
        return srDepartment;
    }

    @Override
    public ServerResponse<Department> updateDepartmentName(String departmentId,
                                                           String departmentName)
            throws DepartmentNotFoundException, DuplicateDepartmentInSchoolException {
        departmentId = departmentId.trim();
        departmentName = departmentName.toLowerCase().trim();
        ServerResponse<Department> srDepartment = new ServerResponse<>();
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
        if(!optionalDepartment.isPresent()){
            throw new DepartmentNotFoundException("The specified department ID does not match any department in the system");
        }
        Department departmentToUpdate = optionalDepartment.get();
        try {
            ServerResponse<Department> srDepartment1 = this.findDepartmentOfSchoolByName(
                    departmentToUpdate.getOwnerSchool().getName(), departmentName);
            if(srDepartment1.getResponseCode() == ResponseCode.DEPARTMENT_FOUND){
                throw new DuplicateDepartmentInSchoolException("The department name is already used by another department in the school specified");
            }
            departmentToUpdate.setName(departmentName);
            Department departmentUpdate = this.saveDepartment(departmentToUpdate);
            srDepartment.setErrorMessage("The department name has been successfully updated");
            srDepartment.setResponseCode(ResponseCode.DEPARTMENT_UPDATED);
            srDepartment.setAssociatedObject(departmentUpdate);
        } catch (SchoolNotFoundException e) {
            srDepartment.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srDepartment.setErrorMessage("The school name specified does not match any school in the system");
            srDepartment.setMoreDetails(e.getMessage());
        }
        return srDepartment;
    }

    @Override
    public ServerResponse<Department> deleteDepartmentOfSchoolByName(String schoolName,
                                                                     String departmentName) throws SchoolNotFoundException, DepartmentNotFoundException {
        schoolName  = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        return null;
    }

    @Override
    public ServerResponse<Department> deleteDepartmentOfSchool(String departmentID)
            throws DepartmentNotFoundException {
        return null;
    }

}
