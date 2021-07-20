package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SchoolRepository extends MongoRepository<School, String> {
    Optional<School> findSchoolByName(String name);
    Page<School> findByNameContaining(String keyword, Pageable pageable);
    Page<School> findAllByOwnerInstitution(Institution institution, Pageable pageable);

}
