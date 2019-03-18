package com.backend.tasks.service.org.impl;

import com.backend.tasks.entity.Organization;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.exception.ResourceNotFoundException;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.service.org.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public Organization getOrganizationById(Long id) throws ResourceNotFoundException {
        Optional<Organization> organization = organizationRepository.findById(id);

        if (!organization.isPresent())
            throw new ResourceNotFoundException("Organization with ID=" + id + " not found");

        return organization.get();
    }

    @Override
    public List<Organization> getAllOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        organizationRepository.findAll().forEach(organization -> organizations.add(organization));
        return organizations;
    }

    @Override
    public void save(Organization organization) throws RecordExistsException {
        if (organizationRepository.findByName(organization.getName()) != null)
            throw new RecordExistsException("Organization ("+organization.getName()+") exists!");

        organizationRepository.save(organization);
    }

    @Override
    public Organization update(Long id, Organization organization) {

        Optional<Organization> currentOrg = organizationRepository.findById(id);

        if (!currentOrg.isPresent())
            throw new ResourceNotFoundException("Organization with ID=" + id + " not found");

        return currentOrg.map(org -> {
            org.setName(organization.getName());
            return organizationRepository.save(org);
        }).get();
    }

    @Override
    public void delete(Long id) {
        Optional<Organization> org = organizationRepository.findById(id);

        if (!org.isPresent())
            throw new ResourceNotFoundException("Organization with ID=" + id + " not found");

        organizationRepository.deleteById(id);
    }
}
