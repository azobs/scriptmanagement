package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.user.RoleRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.DuplicateRoleException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.RoleNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
    RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ServerResponse<Role> findByRoleName(String roleName) {
        roleName = roleName.toLowerCase().trim();
        ServerResponse<Role> srRole = new ServerResponse<>();
        Optional<Role> optionalRole = roleRepository.findRoleByRoleName(roleName);
        if(optionalRole.isPresent()){
            srRole.setErrorMessage("The staff research method has been successfull made");
            srRole.setResponseCode(ResponseCode.ROLE_FOUND);
            srRole.setAssociatedObject(optionalRole.get());
        }
        else{
            //Ceci signifie qu'on a pas trouver de role avec le roleName passe en parametre
            srRole.setErrorMessage("There is no role with the roleName specified");
            srRole.setMoreDetails("");
            srRole.setResponseCode(ResponseCode.ROLE_NOT_FOUND);
            srRole.setAssociatedObject(null);
        }
        return srRole;
    }

    @Override
    public ServerResponse<Role> saveRole(String roleName, String roleAlias, String roleDescription)
            throws DuplicateRoleException {
        roleName = roleName.toLowerCase().trim();
        roleAlias = roleAlias.trim();
        roleDescription = roleDescription.trim();

        ServerResponse<Role> srRole = new ServerResponse<>();
        srRole.setResponseCode(ResponseCode.ROLE_NOT_CREATED);
        srRole.setAssociatedObject(null);

        /**
         * We must start by check if the specified roleName will be unique
         */
        ServerResponse<Role> srR = this.findByRoleName(roleName);
        if(srR.getResponseCode() == ResponseCode.ROLE_FOUND){
            throw new DuplicateRoleException("The specified roleName is already used by anyone else. Please change it");
        }
        else if(srR.getResponseCode() == ResponseCode.ROLE_NOT_FOUND){
            Role roleASaved = new Role();
            roleASaved.setRoleDescription(roleDescription);
            roleASaved.setRoleName(roleName);
            roleASaved.setRoleAlias(roleAlias);
            Role roleSaved = roleRepository.save(roleASaved);
            srRole.setResponseCode(ResponseCode.ROLE_CREATED);
            srRole.setErrorMessage("The role has been successfully created");
            srRole.setAssociatedObject(roleSaved);
        }

        return srRole;
    }

    @Override
    public ServerResponse<Role> updateRole(String roleName, String roleAlias, String roleDescription)
            throws RoleNotFoundException {
        roleName = roleName.toLowerCase().trim();
        roleAlias = roleAlias.trim();
        roleDescription = roleDescription.trim();

        ServerResponse<Role> srRole = new ServerResponse<>();
        srRole.setResponseCode(ResponseCode.ROLE_NOT_UPDATED);
        srRole.setAssociatedObject(null);

        ServerResponse<Role> srRoleExist = this.findByRoleName(roleName);
        if(srRoleExist.getResponseCode()==ResponseCode.ROLE_NOT_FOUND){
            throw new RoleNotFoundException("The roleName specified is not found. Please check it");
        }
        if(srRoleExist.getResponseCode()==ResponseCode.ROLE_FOUND){
            Role roleFound = srRoleExist.getAssociatedObject();
            roleFound.setRoleAlias(roleAlias);
            roleFound.setRoleDescription(roleDescription);
            Role roleUpdated = roleRepository.save(roleFound);

            srRole.setErrorMessage("Role Updated successfully");
            srRole.setResponseCode(ResponseCode.ROLE_UPDATED);
            srRole.setAssociatedObject(roleUpdated);
        }
        return srRole;
    }



    @Override
    public ServerResponse<List<Role>> findAllRole() {
        List<Role> listofRole = roleRepository.findAll();
        ServerResponse<List<Role>> srlistRole = new ServerResponse<>();
        srlistRole.setErrorMessage("The role research has been successfull made");
        srlistRole.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srlistRole.setAssociatedObject(listofRole);
        return srlistRole;
    }

    @Override
    public ServerResponse<Role> deleteRole(String roleName) {
        roleName = roleName.toLowerCase().trim();
        return null;
    }
}
