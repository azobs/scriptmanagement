package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.user.StaffRepository;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class StaffServiceImpl implements StaffService{

    StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public ServerResponse<Staff> findStaffByEmail(String email) {
        ServerResponse<Staff> serverResponse = new ServerResponse<>();
        Optional<Staff> optionalStaff = staffRepository.findStaffByEmail(email);
        if(optionalStaff.isPresent()){
            serverResponse.setErrorMessage("");
            serverResponse.setMoreDetails("");
            serverResponse.setResponseCode(ResponseCode.OBJECT_FOUND);
            serverResponse.setAssociatedObject(optionalStaff.get());
        }
        else{
            //Ceci signifie qu'on a pas trouver de user avec le username passe en parametre
            serverResponse.setErrorMessage("");
            serverResponse.setMoreDetails("");
            serverResponse.setResponseCode(ResponseCode.OBJECT_NOT_FOUND);
            serverResponse.setAssociatedObject(null);
        }
        return serverResponse;
    }
}
