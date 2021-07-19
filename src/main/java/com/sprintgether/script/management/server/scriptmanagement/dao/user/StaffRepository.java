package com.sprintgether.script.management.server.scriptmanagement.dao.user;

import com.sprintgether.script.management.server.scriptmanagement.model.user.EnumStaffType;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends MongoRepository<Staff, String> {
    Optional<Staff> findStaffByEmail(String email);
    List<Staff> findStaffByStaffType(EnumStaffType staffType);
    Page<Staff> findStaffByStaffType(EnumStaffType staffType, Pageable pageable);
}
