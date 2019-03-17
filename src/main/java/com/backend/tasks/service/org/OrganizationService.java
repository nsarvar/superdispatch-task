package com.backend.tasks.service.org;

import com.backend.tasks.entity.Organization;
import com.backend.tasks.exception.ResourceNotFoundException;

import java.util.List;

public interface OrganizationService {

    Organization getOrganizationById(Long id) throws ResourceNotFoundException;

    List<Organization> getAllOrganizations();

    void save(Organization organization);

    Organization update(Long id, Organization organization);

    void delete(Long id);
}
