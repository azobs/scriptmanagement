package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchoolService {
    ServerResponse<School> findSchoolByName(String name);
    ServerResponse<List<School>> findAllSchool();
    ServerResponse<Page<School>> findAllSchool(String keyword, Pageable pageable);
    ServerResponse<Page<School>> findAllSchool(Pageable pageable);
    ServerResponse<Page<School>> findSchoolOfInstitution(String instName, Pageable pageable) throws InstitutionNotFoundException;
    ServerResponse<List<School>> findSchoolOfInstitution(String instName) throws InstitutionNotFoundException;
    ServerResponse<School> saveSchool(String name, String acronym, String description,
                                      String logoSchool, String ownerInstitutionName,
                                      String parentInstitutionName) throws DuplicateSchoolException;
    School saveSchool(School school);
    ServerResponse<School> updateSchool(String name, String acronym, String description,
                                        String logoSchool, String ownerInstitutionName,
                                        String parentInstitutionName) throws SchoolNotFoundException;
    ServerResponse<School> updateSchoolName(String schoolId, String schoolName)
            throws SchoolNotFoundException, DuplicateSchoolException;
    ServerResponse<School> deleteSchool(String name) throws SchoolNotFoundException;
}
