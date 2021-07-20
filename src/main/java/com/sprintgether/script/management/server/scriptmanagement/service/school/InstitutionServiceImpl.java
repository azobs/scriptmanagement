package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.InstitutionRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateInstitutionException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.InstitutionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InstitutionServiceImpl implements InstitutionService{

    InstitutionRepository institutionRepository;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    @Override
    public ServerResponse<Institution> findInstitutionByName(String name) {
        ServerResponse<Institution> srInstitution = new ServerResponse<>();
        Optional<Institution> optionalInstitution = institutionRepository.findInstitutionByName(name);
        if(optionalInstitution.isPresent()){
            srInstitution.setErrorMessage("The institution has been found successfully");
            srInstitution.setResponseCode(ResponseCode.INSTITUTION_FOUND);
            srInstitution.setAssociatedObject(optionalInstitution.get());
        }
        else{
            srInstitution.setErrorMessage("There is no institution with the name specified");
            srInstitution.setResponseCode(ResponseCode.INSTITUTION_NOT_FOUND);
        }
        return srInstitution;
    }

    @Override
    public ServerResponse<List<Institution>> findAllInstitution() {
        ServerResponse<List<Institution>> srListInstitution = new ServerResponse<>();
        List<Institution> listofInstitution = institutionRepository.findAll();
        srListInstitution.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srListInstitution.setErrorMessage("The list of institution has been made");
        srListInstitution.setAssociatedObject(listofInstitution);
        return srListInstitution;
    }

    @Override
    public ServerResponse<Page<Institution>> findAllInstitution(String keyword, Pageable pageable) {
        ServerResponse<Page<Institution>> srPageInstitution = new ServerResponse<>();
        Page<Institution> pageofInstitution = institutionRepository.findByNameContaining(keyword, pageable);
        srPageInstitution.setErrorMessage("The page of institution has been made");
        srPageInstitution.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageInstitution.setAssociatedObject(pageofInstitution);
        return srPageInstitution;
    }

    @Override
    public ServerResponse<Page<Institution>> findAllInstitution(Pageable pageable) {
        ServerResponse<Page<Institution>> srPageInstitution = new ServerResponse<>();
        Page<Institution> pageofInstitution = institutionRepository.findAll(pageable);
        srPageInstitution.setErrorMessage("The page of institution has been made");
        srPageInstitution.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageInstitution.setAssociatedObject(pageofInstitution);
        return srPageInstitution;
    }

    @Override
    public ServerResponse<Institution> savedInstitution(String name, String acronym, String description,
                                                        String location, String address,
                                                        String logoInstitution) throws DuplicateInstitutionException {
        ServerResponse<Institution> srInst = new ServerResponse<>();
        srInst.setResponseCode(ResponseCode.INSTITUTION_NOT_CREATED);

        /***
         * We must check if the institution will be unique
         */
        ServerResponse<Institution> srInst1 = this.findInstitutionByName(name);
        if(srInst1.getResponseCode() == ResponseCode.INSTITUTION_FOUND){
            throw new DuplicateInstitutionException("The specified name is already used by anyone else.");
        }
        else if(srInst1.getResponseCode() == ResponseCode.INSTITUTION_NOT_FOUND){
            Institution inst = new Institution();
            inst.setAcronym(acronym);
            inst.setAddress(address);
            inst.setDescription(description);
            inst.setLocation(location);
            inst.setLogoInstitution(logoInstitution);
            inst.setName(name);

            Institution instSaved = institutionRepository.save(inst);
            srInst.setResponseCode(ResponseCode.INSTITUTION_CREATED);
            srInst.setErrorMessage("The institution has been created successfully");
            srInst.setAssociatedObject(instSaved);
        }

        return srInst;
    }

    @Override
    public ServerResponse<Institution> updatedInstitution(String name, String acronym, String description,
                                                          String location, String address,
                                                          String logoInstitution) throws InstitutionNotFoundException {
        ServerResponse<Institution> srInst = new ServerResponse<>();
        srInst.setResponseCode(ResponseCode.INSTITUTION_NOT_UPDATED);

        ServerResponse<Institution> srInst1 = this.findInstitutionByName(name);
        if(srInst1.getResponseCode() == ResponseCode.INSTITUTION_FOUND){
            Institution instAUpadte = srInst1.getAssociatedObject();
            instAUpadte.setAcronym(acronym);
            instAUpadte.setLogoInstitution(logoInstitution);
            instAUpadte.setLocation(location);
            instAUpadte.setDescription(description);
            instAUpadte.setAddress(address);

            Institution instUpdate = institutionRepository.save(instAUpadte);
            srInst.setResponseCode(ResponseCode.INSTITUTION_UPDATED);
            srInst.setErrorMessage("The institution has been successfully updated");
            srInst.setAssociatedObject(instUpdate);
        }
        else {
            throw new InstitutionNotFoundException("The name of institution specified does not match any institution object");
        }

        return srInst;
    }

    @Override
    public ServerResponse<Institution> addSchoolToInstitution(String institutionName, String schoolName) {
        return null;
    }

    @Override
    public ServerResponse<Institution> deleteInstitution(String name) {
        return null;
    }
}