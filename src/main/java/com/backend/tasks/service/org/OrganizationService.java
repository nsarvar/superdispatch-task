package com.backend.tasks.service.org;

import com.backend.tasks.entity.Organization;
import javassist.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {

    Organization getOrganizationById(Long id) throws NotFoundException;

    List<Organization> getAllOrganizations();

    void save(Organization organization);

    Organization update(Long id, Organization organization);

    void delete(Long id);
}
