package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.DuplicateInstitutionException;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.InstitutionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstitutionService {

    ServerResponse<Institution> findInstitutionByName(String name);
    ServerResponse<List<Institution>> findAllInstitution();
    ServerResponse<Page<Institution>> findAllInstitution(String keyword, Pageable pageable);
    ServerResponse<Page<Institution>> findAllInstitution(Pageable pageable);
    ServerResponse<Institution> saveInstitution(String name, String acronym, String description,
                                                 String location, String address, String logoInstitution)
            throws DuplicateInstitutionException;
    Institution saveInstitution(Institution institution);
    ServerResponse<Institution> updateInstitution(String name, String acronym, String description,
                                                 String location, String address, String logoInstitution)
            throws InstitutionNotFoundException;
    ServerResponse<Institution> updateInstitutionName(String institutionId, String institutionName)
            throws InstitutionNotFoundException, DuplicateInstitutionException;
    ServerResponse<Institution> deleteInstitution(String name) throws InstitutionNotFoundException;
}
