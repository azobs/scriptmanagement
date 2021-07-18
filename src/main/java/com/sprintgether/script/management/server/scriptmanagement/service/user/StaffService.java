package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;


public interface StaffService {

    public ServerResponse<Staff> findStaffByEmail(String email);
    public ServerResponse<Staff> saveStaff(String firstName, String lastName, String staffType, String email,
                                           String phoneNumber, String address, String description, String username,
                                           String password) throws DuplicateStaffException;
    public ServerResponse<Staff> updateStaff(String email, String newFirstName, String newLastName, String newAddress,
                                             String newDescription) throws StaffNotFoundException;
    public ServerResponse<Staff> updateStaffType(String email, String newStaffType) throws StaffNotFoundException;
    public ServerResponse<Staff> updatePasswordStaff(String email, String newPassword) throws StaffNotFoundException;
    public ServerResponse<Staff> deleteStaff(String email) throws StaffNotFoundException;

}
