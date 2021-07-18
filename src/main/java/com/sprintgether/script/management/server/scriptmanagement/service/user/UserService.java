package com.sprintgether.script.management.server.scriptmanagement.service.user;

import com.sprintgether.script.management.server.scriptmanagement.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface UserService {
    public ServerResponse<User> findUserByUsername(String username);
}
