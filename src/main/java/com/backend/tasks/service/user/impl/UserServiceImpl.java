package com.backend.tasks.service.user.impl;

import com.backend.tasks.entity.Organization;
import com.backend.tasks.entity.User;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.exception.ResourceNotFoundException;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.repository.UserRepository;
import com.backend.tasks.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void save(Long orgId, User user) throws RecordExistsException {
        Optional<Organization> organization = organizationRepository.findById(orgId);

        if (!organization.isPresent())
            throw new ResourceNotFoundException("Organization with ID=" + orgId + " not found");

        if (userRepository.findByUsername(user.getUsername()) != null)
            throw new RecordExistsException("Username (" + user.getUsername() + ") already exists!");

        user.setOrg(organization.get());
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long orgId, Long userId) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findByIdAndOrgId(userId, orgId);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User with ID=" + userId + " not found");

        return user.get();
    }

    @Override
    public User update(Long userId, Long orgId, User userToUpdate) {
        Optional<User> currentUser = userRepository.findByIdAndOrgId(userId, orgId);

        if (!currentUser.isPresent())
            throw new ResourceNotFoundException("User with ID=" + userId + " not found");

        return currentUser.map(user -> {
            user.setUsername(userToUpdate.getUsername());
            user.setPassword(userToUpdate.getPassword());
            return userRepository.save(user);
        }).get();
    }

    @Override
    public void delete(Long userId, Long orgId) {
        Optional<User> user = userRepository.findByIdAndOrgId(userId, orgId);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User with ID=" + userId + " not found");

        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUsersByOrgId(Long orgId) {
        return userRepository.findAllByOrgId(orgId);
    }
}
