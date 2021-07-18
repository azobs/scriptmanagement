package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.user.StaffRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateUserException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.UserNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.user.EnumStaffType;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StaffServiceImpl implements StaffService{

    StaffRepository staffRepository;
    UserService userService;

    public StaffServiceImpl(StaffRepository staffRepository, UserService userService) {

        this.staffRepository = staffRepository;
        this.userService = userService;
    }

    @Override
    public ServerResponse<Staff> findStaffByEmail(String email) {
        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        Optional<Staff> optionalStaff = staffRepository.findStaffByEmail(email);
        if(optionalStaff.isPresent()){
            serverResponse.setErrorMessage("");
            serverResponse.setMoreDetails("");
            serverResponse.setResponseCode(ResponseCode.STAFF_FOUND);
            serverResponse.setAssociatedObject(optionalStaff.get());
        }
        else{
            //Ceci signifie qu'on a pas trouver de user avec le username passe en parametre
            serverResponse.setErrorMessage("");
            serverResponse.setMoreDetails("");
            serverResponse.setResponseCode(ResponseCode.STAFF_NOT_FOUND);
            serverResponse.setAssociatedObject(null);
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<Staff> saveStaff(String firstName, String lastName, String staffType,
                                           String email, String phoneNumber, String address,
                                           String description, String username, String password)
            throws DuplicateStaffException {
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
                    EnumStaffType enumStaffType = EnumStaffType.valueOf(staffType);
                    staffASaved.setStaffType(enumStaffType);
                    staffASaved.setUserAssociated(srUser.getAssociatedObject());

                    Staff staffSaved = staffRepository.save(staffASaved);
                    serverResponse.setResponseCode(ResponseCode.STAFF_CREATED);
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
    public ServerResponse<Staff> updateStaff(String email, String newFirstName, String newLastName,
                                             String newAddress, String newDescription)
            throws StaffNotFoundException {
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
            staffToUpdate.setDescription(newDescription);
            Staff staffUpdated = staffRepository.save(staffToUpdate);

            serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
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
                EnumStaffType enumStaffType = EnumStaffType.valueOf(newStaffType);
                staffToUpdate.setStaffType(enumStaffType);
                Staff staffUpdated = staffRepository.save(staffToUpdate);

                serverResponse.setResponseCode(ResponseCode.STAFF_UPDATE);
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
    public ServerResponse<Staff> updatePasswordStaff(String email,
                                                     String newPassword) throws StaffNotFoundException {
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
                    serverResponse.setAssociatedObject(staffToUpdate);
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
    public ServerResponse<Staff> deleteStaff(String email) throws StaffNotFoundException {
        return null;
    }


}
