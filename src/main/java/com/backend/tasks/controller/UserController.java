package com.backend.tasks.controller;

import com.backend.tasks.entity.User;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/orgs/{orgId}/users")
    private User create(@RequestBody User user) {
        return user;
    }

    @PutMapping("/orgs/{orgId}/users/{userId}")
    private User update(@RequestBody Long userId) {
        return null;
    }

    @GetMapping("/orgs/{orgId}/users/{userId}")
    private User get(@RequestBody Long orgId) {
        return null;
    }

    @DeleteMapping("/orgs/{orgId}/users/{userId}")
    private void delete(@RequestBody Long orgId) {

    }

    @GetMapping("/orgs/{orgId}/users")
    private List<User> getAll() {
        return null;
    }
}
