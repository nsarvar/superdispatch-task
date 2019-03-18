package com.backend.tasks.service.user;

import com.backend.tasks.entity.User;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.exception.ResourceNotFoundException;

import java.util.List;

public interface UserService {

    void save(Long orgId, User user) throws RecordExistsException;

    User getUserById(Long orgId, Long userId) throws ResourceNotFoundException;

    List<User> getAllUsersByOrgId(Long orgId);

    User update(Long userId, Long orgId, User user);

    void delete(Long userId, Long orgId);
}
