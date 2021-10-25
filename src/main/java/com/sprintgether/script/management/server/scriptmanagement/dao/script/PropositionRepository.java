package com.sprintgether.script.management.server.scriptmanagement.dao.script;

import com.sprintgether.script.management.server.scriptmanagement.model.script.Proposition;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PropositionRepository extends MongoRepository<Proposition, String> {
    List<Proposition> findAllByOwnerStaff(Staff ownerStaff);
}
