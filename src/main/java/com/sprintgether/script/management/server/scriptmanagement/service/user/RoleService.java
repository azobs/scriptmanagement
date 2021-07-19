package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.*;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Role;

import java.util.List;

public interface RoleService {
    ServerResponse<Role> findByRoleName(String roleName);
    ServerResponse<Role> saveRole(String roleName, String roleAlias, String roleDescription)
            throws DuplicateRoleException;
    ServerResponse<Role> updateRole(String roleName, String roleAlias, String roleDescription)
            throws RoleNotFoundException;
    ServerResponse<List<Role>> findAllRole();
    ServerResponse<Role> deleteRole(String roleName) throws RoleNotFoundException;
}
