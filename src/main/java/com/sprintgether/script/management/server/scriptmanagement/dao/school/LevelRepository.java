package com.sprintgether.script.management.server.scriptmanagement.dao.school;

import com.sprintgether.script.management.server.scriptmanagement.model.school.Level;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LevelRepository extends MongoRepository<Level, String> {
    Optional<Level> findByOwnerOptionAndName(Option ownerOption, String levelName);
    Page<Level> findAllByNameContaining(String keyword, Pageable pageable);
    Page<Level> findAllByOwnerOptionAndNameContaining(Option option, String keyword, Pageable pageable);
    Page<Level> findAllByOwnerOption(Option option, Pageable pageable);
    List<Level> findAllByOwnerOptionOrderByName(Option option);
}
