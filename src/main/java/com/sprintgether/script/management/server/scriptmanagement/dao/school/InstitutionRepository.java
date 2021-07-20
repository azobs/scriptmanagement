package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InstitutionRepository extends MongoRepository<Institution, String> {
    Optional<Institution> findInstitutionByName(String name);
    Optional<Institution> findInstitutionByAcronym(String acronym);
    Page<Institution> findByNameContaining(String keyword, Pageable pageable);
}
