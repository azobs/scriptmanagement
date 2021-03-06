package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.user.StaffRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Institution;
import com.sprintgether.script.management.server.scriptmanagement.model.user.EnumStaffType;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Role;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StaffServiceImpl implements StaffService{

    StaffRepository staffRepository;
    UserService userService;
    RoleService  roleService;

    public StaffServiceImpl(StaffRepository staffRepository, UserService userService, RoleService  roleService) {

        this.staffRepository = staffRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public ServerResponse<Staff> findStaffByEmail(String email) {
        email = email.trim();
        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        Optional<Staff> optionalStaff = staffRepository.findStaffByEmail(email);
        //System.out.println("dans le service email = "+email);
        if(optionalStaff.isPresent()){
            serverResponse.setErrorMessage("The staff research method has been successfull made");
            serverResponse.setResponseCode(ResponseCode.STAFF_FOUND);
            serverResponse.setAssociatedObject(optionalStaff.get());
        }
        else{
            //Ceci signifie qu'on a pas trouver de user avec le username passe en parametre
            serverResponse.setErrorMessage("There is no staff with the email specified");
            serverResponse.setMoreDetails("");
            serverResponse.setResponseCode(ResponseCode.STAFF_NOT_FOUND);
            serverResponse.setAssociatedObject(null);
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> findStaffById(String staffId) {
        staffId = staffId.trim();
        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        Optional<Staff> optionalStaff = staffRepository.findStaffById(staffId);
        //System.out.println("dans le service email = "+email);
        if(optionalStaff.isPresent()){
            serverResponse.setErrorMessage("The staff research method has been successfull made");
            serverResponse.setResponseCode(ResponseCode.STAFF_FOUND);
            serverResponse.setAssociatedObject(optionalStaff.get());
        }
        else{
            //Ceci signifie qu'on a pas trouver de user avec le username passe en parametre
            serverResponse.setErrorMessage("There is no staff with the email specified");
            serverResponse.setMoreDetails("");
            serverResponse.setResponseCode(ResponseCode.STAFF_NOT_FOUND);
            serverResponse.setAssociatedObject(null);
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Page<Staff>> findAllStaff(Pageable pageable) {
        Page<Staff> pageofStaff = staffRepository.findAll(pageable);
        ServerResponse<Page<Staff>> srlistStaff = new ServerResponse<>();
        srlistStaff.setErrorMessage("The staff research has been successfull made");
        srlistStaff.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srlistStaff.setAssociatedObject(pageofStaff);
        return srlistStaff;
    }

    @Override
    public ServerResponse<Page<Staff>> findAllStaff(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase().trim();
        ServerResponse<Page<Staff>> srPageStaff = new ServerResponse<>();
        Page<Staff> pageofStaff = staffRepository.findByFirstNameContaining(keyword, pageable);
        srPageStaff.setErrorMessage("The page of staff has been made");
        srPageStaff.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srPageStaff.setAssociatedObject(pageofStaff);
        return srPageStaff;
    }

    @Override
    public ServerResponse<Page<Staff>> findStaffByStaffType(String staffType, Pageable pageable) {
        ServerResponse<Page<Staff>> srlistStaff = new ServerResponse<>();
        try {
            Page<Staff> pageofStaff = staffRepository.findStaffByStaffType(
                    EnumStaffType.valueOf(staffType.toUpperCase()), pageable);
            srlistStaff.setErrorMessage("The staff research has been successfull made");
            srlistStaff.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srlistStaff.setAssociatedObject(pageofStaff);
        }
        catch (IllegalArgumentException e){
            srlistStaff.setErrorMessage("The staff type specified is not taken into acccount");
            srlistStaff.setMoreDetails(e.getMessage());
            srlistStaff.setResponseCode(ResponseCode.EXCEPTION_ENUM_STAFF_TYPE);
        }
        return srlistStaff;
    }

    @Override
    public ServerResponse<List<Staff>> findAllStaff() {
        List<Staff> listofStaff = staffRepository.findAll();
        ServerResponse<List<Staff>> srlistStaff = new ServerResponse<>();
        srlistStaff.setErrorMessage("The staff research has been successfull made");
        srlistStaff.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srlistStaff.setAssociatedObject(listofStaff);
        return srlistStaff;
    }

    @Override
    public ServerResponse<List<Staff>> findStaffByStaffType(String staffType) {
        ServerResponse<List<Staff>> srlistStaff = new ServerResponse<>();
        try {
            List<Staff> listofStaff = staffRepository.findStaffByStaffType(
                    EnumStaffType.valueOf(staffType.toUpperCase()));
            srlistStaff.setErrorMessage("The staff research has been successfull made");
            srlistStaff.setResponseCode(ResponseCode.NORMAL_RESPONSE);
            srlistStaff.setAssociatedObject(listofStaff);
        }
        catch (IllegalArgumentException e){
            srlistStaff.setErrorMessage("The staff type specified is not taken into account");
            srlistStaff.setMoreDetails(e.getMessage());
            srlistStaff.setResponseCode(ResponseCode.EXCEPTION_ENUM_STAFF_TYPE);
        }
        return srlistStaff;
    }


    @Override
    public ServerResponse<Staff> saveStaff(String firstName, String lastName, String staffType,
                                           String email, String phoneNumber, String address,
                                           String description, String username, String password)
            throws DuplicateStaffException {
        email = email.trim();
        firstName = firstName.toLowerCase().trim();
        lastName = lastName.toLowerCase().trim();
        phoneNumber = phoneNumber.toLowerCase().trim();
        address = address.toLowerCase().trim();
        description = description.toLowerCase().trim();
        username = username.trim();
        password = password.trim();

        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.STAFF_NOT_CREATED);
        serverResponse.setAssociatedObject(null);

        /**
         * We must start by check if the specified username will be unique
         */
        ServerResponse<Staff> srStaff = this.findStaffByEmail(email);
        if(srStaff.getResponseCode() == ResponseCode.STAFF_FOUND){
            throw new DuplicateStaffException("The specified email is already used by anyone else. Please change it");
        }
        else if(srStaff.getResponseCode() == ResponseCode.STAFF_NOT_FOUND){
            /***
             * We must continue by saving the object user that will be associated
             */
            try {
                ServerResponse<User> srUser = userService.saveUser(username, password);
                /***
                 * Here we have the user saved in the database. we can now continue
                 */
                Staff staffASaved = new Staff();
                staffASaved.setAddress(address);
                staffASaved.setDescription(description);
                staffASaved.setEmail(email);
                staffASaved.setLastName(lastName);
                staffASaved.setFirstName(firstName);
                staffASaved.setPhoneNumber(phoneNumber);
                try {
                    EnumStaffType enumStaffType = EnumStaffType.valueOf(staffType.toUpperCase());
                    staffASaved.setStaffType(enumStaffType);
                    staffASaved.setUserAssociated(srUser.getAssociatedObject());

                    Staff staffSaved = staffRepository.save(staffASaved);
                    serverResponse.setResponseCode(ResponseCode.STAFF_CREATED);
                    serverResponse.setErrorMessage("The staff saving method has been successfull executed");
                    serverResponse.setAssociatedObject(staffSaved);

                    /**
                     * After saving staff object we must send an email in the mail box of the staff member
                     * to activate the created account. The content of that mail must be a link that the staff
                     * going to use to activate the account.
                     */
                }
                catch (IllegalArgumentException e){
                    serverResponse.setResponseCode(ResponseCode.EXCEPTION_ENUM_STAFF_TYPE);
                    serverResponse.setErrorMessage("IllegalArgumentException");
                    serverResponse.setMoreDetails(e.getMessage());
                }

            } catch (DuplicateUserException e) {
                serverResponse.setResponseCode(ResponseCode.EXCEPTION_SAVED_USER);
                serverResponse.setErrorMessage("DuplicateUserException");
                serverResponse.setMoreDetails(e.getMessage());
            }
        }
        return serverResponse;
    }

    @Override
    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public ServerResponse<Staff> updateStaff(String email, String newFirstName, String newLastName,
                                             String newAddress, String newPhoneNumber, String newDescription)
            throws StaffNotFoundException {
        email = email.trim();
        newFirstName = newFirstName.toLowerCase().trim();
        newLastName = newLastName.toLowerCase().trim();
        newPhoneNumber = newPhoneNumber.toLowerCase().trim();
        newAddress = newAddress.toLowerCase().trim();
        newDescription = newDescription.toLowerCase().trim();

        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
        serverResponse.setAssociatedObject(null);
        /***
         * Search for staff to update
         */
        ServerResponse<Staff> srStaff = this.findStaffByEmail(email);
        if(srStaff.getResponseCode() == ResponseCode.STAFF_FOUND){
            Staff staffToUpdate = srStaff.getAssociatedObject();
            staffToUpdate.setFirstName(newFirstName);
            staffToUpdate.setLastName(newLastName);
            staffToUpdate.setAddress(newAddress);
            staffToUpdate.setAddress(newPhoneNumber);
            staffToUpdate.setDescription(newDescription);
            Staff staffUpdated = staffRepository.save(staffToUpdate);

            serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
            serverResponse.setErrorMessage("The update staff method has been successfull executed");
            serverResponse.setAssociatedObject(staffUpdated);
        }
        else if(srStaff.getResponseCode() == ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The specified email does not match any staff in DB. Please check it");
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> updateStaffType(String email,
                                                 String newStaffType) throws StaffNotFoundException {
        email = email.trim();

        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
        serverResponse.setAssociatedObject(null);
        /***
         * Search for staff to update
         */
        ServerResponse<Staff> srStaff = this.findStaffByEmail(email);
        if(srStaff.getResponseCode() == ResponseCode.STAFF_FOUND){
            Staff staffToUpdate = srStaff.getAssociatedObject();
            try {
                EnumStaffType enumStaffType = EnumStaffType.valueOf(newStaffType.toUpperCase());
                staffToUpdate.setStaffType(enumStaffType);
                Staff staffUpdated = staffRepository.save(staffToUpdate);

                serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
                serverResponse.setErrorMessage("The update staff type method has been successfull executed");
                serverResponse.setAssociatedObject(staffUpdated);
            }
            catch (IllegalArgumentException e){
                serverResponse.setResponseCode(ResponseCode.EXCEPTION_ENUM_STAFF_TYPE);
                serverResponse.setErrorMessage("IllegalArgumentException");
                serverResponse.setMoreDetails(e.getMessage());
            }
        }
        else if(srStaff.getResponseCode() == ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The specified email does not match any staff in DB. Please check it");
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> updateStaffPassword(String email,
                                                     String newPassword) throws StaffNotFoundException {
        email = email.trim();
        newPassword = newPassword.trim();
        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
        serverResponse.setAssociatedObject(null);
        /***
         * Search for staff to update
         */
        ServerResponse<Staff> srStaff = this.findStaffByEmail(email);
        if(srStaff.getResponseCode() == ResponseCode.STAFF_FOUND){
            Staff staffToUpdate = srStaff.getAssociatedObject();
            User userAssociated = staffToUpdate.getUserAssociated();
            try {
                ServerResponse<User> srUser = userService.updatePassword(userAssociated.getUsername(),
                        newPassword);
                if(srUser.getResponseCode()==ResponseCode.USER_UPDATED){
                    serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
                    serverResponse.setErrorMessage("The update password method has been successfull executed");
                    serverResponse.setAssociatedObject(this.findStaffByEmail(staffToUpdate.getEmail()).getAssociatedObject());
                }
            } catch (UserNotFoundException e) {
                //e.printStackTrace();
                serverResponse.setResponseCode(ResponseCode.EXCEPTION_UPDATED_STAFF);
                serverResponse.setErrorMessage("UserNotFoundException");
                serverResponse.setMoreDetails(e.getMessage());
            }
        }
        else if(srStaff.getResponseCode() == ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The specified email does not match any staff in DB. Please check it");
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> updateStaffUsername(String staffId, String newUsername)
            throws StaffNotFoundException, DuplicateUserException {
        staffId = staffId.trim();
        newUsername = newUsername.toLowerCase().trim();
        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
        serverResponse.setAssociatedObject(null);
        /***
         * Search for staff to update
         */
        ServerResponse<Staff> srStaff = this.findStaffById(staffId);
        if(srStaff.getResponseCode() == ResponseCode.STAFF_FOUND){
            Staff staffToUpdate = srStaff.getAssociatedObject();
            User userAssociated = staffToUpdate.getUserAssociated();
            try {
                ServerResponse<User> srUser = userService.updateUsername(userAssociated.getId(),
                        newUsername);
                if(srUser.getResponseCode()==ResponseCode.USER_UPDATED){
                    serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
                    serverResponse.setErrorMessage("The update password method has been successfull executed");
                    serverResponse.setAssociatedObject(this.findStaffByEmail(staffToUpdate.getEmail()).getAssociatedObject());
                }
            } catch (UserNotFoundException e) {
                //e.printStackTrace();
                serverResponse.setResponseCode(ResponseCode.EXCEPTION_UPDATED_STAFF);
                serverResponse.setErrorMessage("UserNotFoundException");
                serverResponse.setMoreDetails(e.getMessage());
            }
        }
        else if(srStaff.getResponseCode() == ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The specified email does not match any staff in DB. Please check it");
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> updateStaffEmail(String staffId, String newEmail)
            throws StaffNotFoundException, DuplicateStaffException {
        staffId = staffId.trim();
        newEmail = newEmail.trim();
        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
        serverResponse.setAssociatedObject(null);
        /***
         * Search for staff to update
         */
        ServerResponse<Staff> srStaff = this.findStaffById(staffId);
        ServerResponse<Staff> srStaffDuplicated = this.findStaffByEmail(newEmail);
        if(srStaff.getResponseCode() == ResponseCode.STAFF_FOUND){
            Staff staffToUpdated = srStaff.getAssociatedObject();
            if(srStaffDuplicated.getResponseCode() == ResponseCode.STAFF_FOUND){
                Staff staffDuplicate = srStaffDuplicated.getAssociatedObject();
                if(!staffToUpdated.getId().equalsIgnoreCase(staffDuplicate.getId())) {
                    throw new DuplicateStaffException("The new email is already used by another " +
                            "staff in the system");
                }
            }

            staffToUpdated.setEmail(newEmail);
            Staff staffUpdated = this.saveStaff(staffToUpdated);

            serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
            serverResponse.setErrorMessage("The update password method has been successfull " +
                    "executed");
            serverResponse.setAssociatedObject(staffUpdated);
        }
        else if(srStaff.getResponseCode() == ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The specified Id does not match any staff in DB. " +
                    "Please check it");
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> activateStaff(String email, boolean active) throws StaffNotFoundException {
        email = email.trim();

        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        serverResponse.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
        serverResponse.setAssociatedObject(null);
        /***
         * Search for staff to update
         */
        ServerResponse<Staff> srStaff = this.findStaffByEmail(email);
        if(srStaff.getResponseCode() == ResponseCode.STAFF_FOUND){
            Staff staffToUpdate = srStaff.getAssociatedObject();
            User userAssociated = staffToUpdate.getUserAssociated();
            try {
                ServerResponse<User> srUser = userService.activateUser(userAssociated.getUsername(),
                        active);
                if(srUser.getResponseCode()==ResponseCode.USER_UPDATED){
                    serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
                    serverResponse.setErrorMessage("The activated method has been successfull executed");
                    serverResponse.setAssociatedObject(this.findStaffByEmail(staffToUpdate.getEmail()).getAssociatedObject());
                }
            } catch (UserNotFoundException e) {
                //e.printStackTrace();
                serverResponse.setResponseCode(ResponseCode.EXCEPTION_UPDATED_STAFF);
                serverResponse.setErrorMessage("UserNotFoundException");
                serverResponse.setMoreDetails(e.getMessage());
            }
        }
        else if(srStaff.getResponseCode() == ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The specified email does not match any staff in DB. Please check it");
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> addRoleToStaff(String email, String roleName)
            throws RoleNotFoundException, StaffNotFoundException {
        email = email.trim();
        roleName =  roleName.toLowerCase().trim();

        ServerResponse<Staff> srStaff = new ServerResponse<>();
        srStaff.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);

        ServerResponse<Role> srRole = roleService.findByRoleName(roleName);
        if(srRole.getResponseCode()==ResponseCode.ROLE_NOT_FOUND){
            throw new RoleNotFoundException("The roleName specified does not macth any Role object");
        }
        ServerResponse<Staff> srStaff1 = this.findStaffByEmail(email);
        if(srStaff1.getResponseCode()==ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The email specified does not match any Staff object");
        }

        if(srRole.getResponseCode() == ResponseCode.ROLE_FOUND &&
            srStaff1.getResponseCode() ==  ResponseCode.STAFF_FOUND){
            Role role = srRole.getAssociatedObject();
            Staff staff = srStaff1.getAssociatedObject();
            try {
                ServerResponse<User> srUser = this.userService.addRoleToUser(
                        staff.getUserAssociated().getUsername(), role.getRoleName());
                srStaff = this.findStaffByEmail(email);
            } catch (UserNotFoundException e) {
                //e.printStackTrace();
                srStaff.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
                srStaff.setErrorMessage("There is no USER object associated with the Staff");
                srStaff.setMoreDetails(e.getMessage());
            } catch (RoleExistForUserException e) {
                //e.printStackTrace();
                srStaff.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
                srStaff.setErrorMessage("The role specified already belonging to the Staff");
                srStaff.setMoreDetails(e.getMessage());
            }
        }

        return srStaff;
    }

    @Override
    public ServerResponse<Staff> removeRoleToStaff(String email, String roleName)
            throws RoleNotExistForUserException, StaffNotFoundException {
        email = email.trim();
        email = roleName.toLowerCase().trim();

        ServerResponse<Staff> srStaff = new ServerResponse<>();

        ServerResponse<Staff> srStaff1 = this.findStaffByEmail(email);
        if(srStaff1.getResponseCode()==ResponseCode.STAFF_NOT_FOUND){
            throw new StaffNotFoundException("The email specified does not match any Staff object");
        }

        if(srStaff1.getResponseCode() ==  ResponseCode.STAFF_FOUND){
            Staff staff = srStaff1.getAssociatedObject();
            try {
                ServerResponse<User> srUser = this.userService.removeRoleToUser(
                        staff.getUserAssociated().getUsername(), roleName);
                srStaff = this.findStaffByEmail(email);
            } catch (UserNotFoundException e) {
                //e.printStackTrace();
                srStaff.setResponseCode(ResponseCode.STAFF_NOT_UPDATE);
                srStaff.setErrorMessage("There is no USER object associated with the Staff");
                srStaff.setMoreDetails(e.getMessage());
            }
        }

        return srStaff;
    }

    @Override
    public ServerResponse<Staff> deleteStaff(String email) throws StaffNotFoundException {
        email = email.trim();
        return null;
    }


}
