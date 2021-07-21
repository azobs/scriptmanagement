package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.SchoolRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService{

    SchoolRepository schoolRepository;
    InstitutionService institutionService;


    public SchoolServiceImpl(SchoolRepository schoolRepository, InstitutionService institutionService) {
        this.schoolRepository = schoolRepository;
        this.institutionService = institutionService;
    }

    @Override
    public ServerResponse<School> findSchoolByName(String name) {
        ServerResponse<School> srSchool = new ServerResponse<>();
        Optional<School> optionalSchool = schoolRepository.findSchoolByName(name);
        if(optionalSchool.isPresent()){
            srSchool.setErrorMessage("The school has been found successfully");
            srSchool.setResponseCode(ResponseCode.SCHOOL_FOUND);
            srSchool.setAssociatedObject(optionalSchool.get());
        }
        else{
            srSchool.setResponseCode(ResponseCode.SCHOOL_NOT_FOUND);
            srSchool.setErrorMessage("The school has not been found in the system");
        }
        return srSchool;
    }

    @Override
    public ServerResponse<List<School>> findAllSchool() {
        ServerResponse<List<School>> srSchoolList = new ServerResponse<>();
        List<School> listofSchool = schoolRepository.findAll();
        srSchoolList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srSchoolList.setErrorMessage("The list of school has been made successfully");
        srSchoolList.setAssociatedObject(listofSchool);
        return srSchoolList;
    }

    @Override
    public ServerResponse<Page<School>> findAllSchool(String keyword, Pageable pageable) {
        ServerResponse<Page<School>> srPageSchool = new ServerResponse<Page<School>>();
        Page<School> pageofSchool = schoolRepository.findByNameContaining(keyword, pageable);
        srPageSchool.setErrorMessage("The page of school has been made successfully");
        srPageSchool.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageSchool.setAssociatedObject(pageofSchool);
        return srPageSchool;
    }

    @Override
    public ServerResponse<Page<School>> findAllSchool(Pageable pageable) {
        ServerResponse<Page<School>> srPageSchool = new ServerResponse<Page<School>>();
        Page<School> pageofSchool = schoolRepository.findAll(pageable);
        srPageSchool.setErrorMessage("The page of school has been made successfully");
        srPageSchool.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageSchool.setAssociatedObject(pageofSchool);
        return srPageSchool;
    }

    @Override
    public ServerResponse<Page<School>> findSchoolOfInstitution(String instName, Pageable pageable)
            throws InstitutionNotFoundException {
        ServerResponse<Page<School>> srPageofSchool = new ServerResponse<>();
        ServerResponse<Institution> srInst = institutionService.findInstitutionByName(instName);
        if(srInst.getResponseCode() == ResponseCode.INSTITUTION_FOUND){
            Institution ownerInst = srInst.getAssociatedObject();
            Page<School> pageofSchool = schoolRepository.findAllByOwnerInstitution(ownerInst, pageable);
            srPageofSchool.setResponseCode(ResponseCode.SCHOOL_INSTITUTION_FOUND);
            srPageofSchool.setAssociatedObject(pageofSchool);
            srPageofSchool.setErrorMessage("The school page of the institution specified has been made");
        }
        else if(srInst.getResponseCode() == ResponseCode.INSTITUTION_NOT_FOUND){
            throw new InstitutionNotFoundException("The institution name specified does not match any institution in the system");
        }
        return srPageofSchool;
    }

    @Override
    public ServerResponse<List<School>> findSchoolOfInstitution(String instName) throws InstitutionNotFoundException {
        ServerResponse<List<School>> srListofSchool = new ServerResponse<>();
        ServerResponse<Institution> srInst = institutionService.findInstitutionByName(instName);
        if(srInst.getResponseCode() == ResponseCode.INSTITUTION_FOUND){
            Institution ownerInst = srInst.getAssociatedObject();
            List<School> listofSchool = schoolRepository.findAllByOwnerInstitution(ownerInst);
            Collections.sort(listofSchool, new Comparator<School>() {
                @Override
                public int compare(School o1, School o2) {
                    if(o1.getName().compareToIgnoreCase(o2.getName())<0) return -1;
                    if(o1.getName().compareToIgnoreCase(o2.getName())>0) return 1;
                    return 0;
                }
            });
            srListofSchool.setErrorMessage("The school list of the institution specified has been made");
            srListofSchool.setResponseCode(ResponseCode.SCHOOL_INSTITUTION_FOUND);
            srListofSchool.setAssociatedObject(listofSchool);
        }
        else if(srInst.getResponseCode() == ResponseCode.INSTITUTION_NOT_FOUND){
            throw new InstitutionNotFoundException("The institution name specified does not match any institution in the system");
        }
        return srListofSchool;
    }

    @Override
    public ServerResponse<School> saveSchool(String name, String acronym, String description,
                                             String logoSchool, String ownerInstitutionName,
                                             String parentInstitutionName)
            throws DuplicateSchoolException {
        ServerResponse<School> srSchool = new ServerResponse<>();
        /*****
         * We must retrieve the ownerInstitution (It can be null) and the parentInstitution (can be null)
         */
        ServerResponse<Institution> srInst = institutionService.findInstitutionByName(ownerInstitutionName);
        ServerResponse<Institution> srInst1 = institutionService.findInstitutionByName(parentInstitutionName);
        /****************
         * We must check if there is no other school with the same name
         */
        ServerResponse<School>  srSchool1 = this.findSchoolByName(name);
        if(srSchool1.getResponseCode() == ResponseCode.SCHOOL_FOUND){
            throw new DuplicateSchoolException("The school name specified is already used by other school");
        }
        School school = new School();
        school.setName(name);
        school.setAcronym(acronym);
        school.setDescription(description);
        school.setLogoSchool(logoSchool);
        school.setOwnerInstitution(srInst.getAssociatedObject());
        school.setParentInstitution(srInst1.getAssociatedObject());

        School schoolSaved =  schoolRepository.save(school);
        srSchool.setErrorMessage("The school specified has been successfully saved in the system");
        srSchool.setResponseCode(ResponseCode.SCHOOL_CREATED);
        srSchool.setAssociatedObject(schoolSaved);

        return srSchool;
    }

    @Override
    public School saveSchool(School school) {
        return this.schoolRepository.save(school);
    }

    @Override
    public ServerResponse<School> updateSchool(String name, String acronym, String description,
                                               String logoSchool, String ownerInstitutionName,
                                               String parentInstitutionName) throws SchoolNotFoundException {
        ServerResponse<School> srSchool = new ServerResponse<>();
        /*****
         * We must retrieve the ownerInstitution (It can be null) and the parentInstitution (can be null)
         */
        ServerResponse<Institution> srInst = institutionService.findInstitutionByName(ownerInstitutionName);
        ServerResponse<Institution> srInst1 = institutionService.findInstitutionByName(parentInstitutionName);
        /****************
         * We must check if there is no other school with the same name
         */
        ServerResponse<School>  srSchool1 = this.findSchoolByName(name);
        if(srSchool1.getResponseCode() == ResponseCode.SCHOOL_NOT_FOUND){
            throw new SchoolNotFoundException("The school name specified does not match any school in the system");
        }
        School school = srSchool1.getAssociatedObject();
        school.setAcronym(acronym);
        school.setDescription(description);
        school.setLogoSchool(logoSchool);
        school.setOwnerInstitution(srInst.getAssociatedObject());
        school.setParentInstitution(srInst1.getAssociatedObject());

        School schoolUpdated =  schoolRepository.save(school);
        srSchool.setErrorMessage("The school specified has been successfully updated in the system");
        srSchool.setResponseCode(ResponseCode.SCHOOL_UPDATED);
        srSchool.setAssociatedObject(schoolUpdated);

        return srSchool;
    }


    @Override
    public ServerResponse<School> deleteSchool(String name) throws SchoolNotFoundException {
        return null;
    }
}
