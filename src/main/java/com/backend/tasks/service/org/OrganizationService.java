package com.backend.tasks.service.org;

import com.backend.tasks.entity.Organization;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.exception.ResourceNotFoundException;

import java.util.List;

public interface OrganizationService {

    Organization getOrganizationById(Long id) throws ResourceNotFoundException;

    List<Organization> getAllOrganizations();

    void save(Organization organization) throws RecordExistsException;

    Organization update(Long id, Organization organization);

    void delete(Long id);
}
