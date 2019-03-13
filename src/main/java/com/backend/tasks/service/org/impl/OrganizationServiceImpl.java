package com.backend.tasks.service.org.impl;

import com.backend.tasks.entity.Organization;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.service.org.OrganizationService;
import javassist.NotFoundException;
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
    public Organization getOrganizationById(Long id) throws NotFoundException {
        Optional<Organization> organization = organizationRepository.findById(id);

        if(!organization.isPresent())
            throw new NotFoundException("Not found");

        return organization.get();
    }

    @Override
    public List<Organization> getAllOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        organizationRepository.findAll().forEach(organization -> organizations.add(organization));
        return organizations;
    }

    @Override
    public void save(Organization organization) {
        organizationRepository.save(organization);
    }

    @Override
    public Organization update(Long id, Organization organization) {
        return organizationRepository.findById(id).map(org -> {
            org.setName(organization.getName());
            return organizationRepository.save(org);
        }).get();
    }

    @Override
    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }
}
