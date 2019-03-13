package com.backend.tasks.controller;

import com.backend.tasks.entity.Organization;
import com.backend.tasks.service.org.OrganizationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Implement create, read, update, delete  rest controller endpoints for organization.
 * Map endpoints to /orgs path.
 * 1. Post to /orgs endpoint should create and return organization. Response status should be 201.
 * 2. Put to /orgs/{orgId} endpoint should update, save and return organization with id=orgId.
 * 3. Get to /orgs/{orgId} endpoint should fetch and return organization with id=orgId.
 * 4. Delete to /orgs/{orgId} endpoint should delete organization with id=orgId. Response status should be 204.
 * 5. Get to /orgs endpoint should return list of all organizations
 */
@RestController
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    /**
     * @param organization
     * @return
     */
    @PostMapping("/orgs")
    private ResponseEntity<?> create(@RequestBody Organization organization) {
        organizationService.save(organization);
        return new ResponseEntity<>(organization, HttpStatus.CREATED);
    }

    /**
     * @param orgId
     * @param organization
     * @return
     */
    @PutMapping("/orgs/{orgId}")
    private Organization update(@PathVariable Long orgId, @RequestBody Organization organization) {
        return organizationService.update(orgId, organization);
    }

    /**
     * @param orgId
     * @return
     */
    @GetMapping("/orgs/{orgId}")
    private Organization get(@PathVariable Long orgId) throws NotFoundException {
        return organizationService.getOrganizationById(orgId);
    }

    /**
     * @param orgId
     */
    @DeleteMapping("/orgs/{orgId}")
    private ResponseEntity<?> delete(@PathVariable Long orgId) {
        organizationService.delete(orgId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * @return
     */
    @GetMapping("/orgs")
    private List<Organization> getAll() {
        return organizationService.getAllOrganizations();
    }
}
