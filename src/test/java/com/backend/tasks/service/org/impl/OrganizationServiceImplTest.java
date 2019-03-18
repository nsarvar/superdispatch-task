package com.backend.tasks.service.org.impl;

import com.backend.tasks.Application;
import com.backend.tasks.entity.Organization;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.exception.ResourceNotFoundException;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.service.org.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Implement tests for UserServiceImpl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrganizationServiceImplTest {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    OrganizationRepository organizationRepository;

    private Organization orgGoogle;
    private Organization orgApple;

    /**
     * Loading initial data
     */
    @Before
    public void setup() {
        orgGoogle = new Organization("Google");
        orgApple = new Organization("Apple");

        organizationRepository.deleteAll();
        organizationRepository.saveAll(Arrays.asList(orgGoogle, orgApple));
    }

    @Test
    public void organization_repository_should_be_autowired() {
        assertNotNull(organizationRepository);
    }

    @Test
    public void organization_service_should_be_autowired() {
        assertNotNull(organizationService);
    }

    @Test
    public void should_get_organization_by_id() {
        Organization organization = organizationService.getOrganizationById(orgGoogle.getId());
        assertThat(organization.getName()).isEqualTo(orgGoogle.getName());
        assertThat(organization.getId()).isEqualTo(orgGoogle.getId());
    }

    @Test
    public void should_create_and_check_id_is_created() throws RecordExistsException {
        Organization newOrg = new Organization("Yahoo!");

        organizationService.save(newOrg);
        Organization organization = organizationService.getOrganizationById(newOrg.getId());
        assertThat(organization.getName()).isEqualTo(newOrg.getName());
        assertThat(organization.getId()).isEqualTo(newOrg.getId());
    }

    @Test
    public void should_get_all_organization() {
        List<Organization> orgList = organizationService.getAllOrganizations();
        assertThat(orgList.size()).isGreaterThanOrEqualTo(2);
        assertThat(orgList.get(0).getName()).isEqualTo(orgGoogle.getName());
        assertThat(orgList.get(0).getId()).isEqualTo(orgGoogle.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_delete_by_id_and_get_not_found() throws RecordExistsException {
        organizationService.delete(orgApple.getId());
        organizationService.getOrganizationById(orgApple.getId());
    }
}