package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface StaffService {

    ServerResponse<Staff> findStaffByEmail(String email);
    ServerResponse<Staff> findStaffById(String staffId);
    ServerResponse<Page<Staff>> findAllStaff(Pageable pageable);
    ServerResponse<Page<Staff>> findAllStaff(String keyword, Pageable pageable);
    ServerResponse<Page<Staff>> findStaffByStaffType(String staffType, Pageable pageable);
    ServerResponse<List<Staff>> findAllStaff();
    ServerResponse<List<Staff>> findStaffByStaffType(String staffType);
    ServerResponse<Staff> saveStaff(String firstName, String lastName, String staffType, String email,
                                    String phoneNumber, String address, String description, String username,
                                    String password) throws DuplicateStaffException;
    Staff saveStaff(Staff staff);
    ServerResponse<Staff> updateStaff(String email, String newFirstName, String newLastName, String newAddress,
                                      String newPhoneNumber, String newDescription) throws StaffNotFoundException;
    ServerResponse<Staff> updateStaffType(String email, String newStaffType) throws StaffNotFoundException;
    ServerResponse<Staff> updateStaffPassword(String email, String newPassword)
            throws StaffNotFoundException;
    ServerResponse<Staff> updateStaffUsername(String staffId, String newUsername)
            throws StaffNotFoundException, DuplicateUserException;
    ServerResponse<Staff> updateStaffEmail(String staffId, String newEmail)
            throws StaffNotFoundException, DuplicateStaffException;

    ServerResponse<Staff> activateStaff(String email, boolean active) throws StaffNotFoundException;
    ServerResponse<Staff> addRoleToStaff(String email, String roleName) throws RoleNotFoundException, StaffNotFoundException;
    ServerResponse<Staff> removeRoleToStaff(String email, String roleName) throws RoleNotExistForUserException
            , StaffNotFoundException;
    ServerResponse<Staff> deleteStaff(String email) throws StaffNotFoundException;

}
