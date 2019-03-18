package com.backend.tasks.controller;

import com.backend.tasks.entity.User;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Implement create, read, update, delete  rest controller endpoints for user.
 * Map endpoints to /orgs/{orgId}/users path
 * 1. Post to /orgs/{orgId}/users endpoint should create and return user for organization with id=orgId. Response status should be 201.
 * 2. Put to /orgs/{orgId}/users/{userId} endpoint should update, save and return user with id=userId for organization with id=orgId.
 * 3. Get to /orgs/{orgId}/users/{userId} endpoint should fetch and return user with id=userId for organization with id=orgId.
 * 4. Delete to /orgs/{orgId}/users/{userId} endpoint should delete user with id=userId for organization with id=orgId. Response status should be 204.
 * 5. Get to /orgs/{orgId}/users endpoint should return list of all users for organization with id=orgId
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Create new user
     *
     * @param orgId Organization ID
     * @param user  User object
     * @return User
     */
    @PostMapping("/orgs/{orgId}/users")
    private ResponseEntity<?> create(@PathVariable Long orgId, @Valid @RequestBody User user) throws RecordExistsException {
        userService.save(orgId, user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/orgs/{orgId}/users/{userId}")
    private User update(@PathVariable Long orgId, @PathVariable Long userId, @Valid @RequestBody User user) {
        return userService.update(userId, orgId, user);
    }

    @GetMapping("/orgs/{orgId}/users/{userId}")
    private User get(@PathVariable Long orgId, @PathVariable Long userId) {
        return userService.getUserById(orgId, userId);
    }

    @DeleteMapping("/orgs/{orgId}/users/{userId}")
    private ResponseEntity<?> delete(@PathVariable Long orgId, @PathVariable Long userId) {
        userService.delete(userId, orgId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/orgs/{orgId}/users")
    private List<User> getAll(@PathVariable Long orgId) {
        return userService.getAllUsersByOrgId(orgId);
    }
}
