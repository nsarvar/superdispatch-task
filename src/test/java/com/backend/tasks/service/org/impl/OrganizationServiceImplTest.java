package com.backend.tasks.service.org.impl;

import com.backend.tasks.Application;
import com.backend.tasks.entity.Organization;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.exception.ResourceNotFoundException;
import com.backend.tasks.service.org.OrganizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void organization_service_should_be_autowired() {
        assertNotNull(organizationService);
    }

    @Test
    public void should_get_organization_by_id() {
        String orgName = "Google";
        Organization organization = organizationService.getOrganizationById(1L);
        assertThat(organization.getName()).isEqualTo(orgName);
        assertThat(organization.getId()).isEqualTo(1);
    }


    @Test
    public void should_create_and_check_id_is_created() throws RecordExistsException {
        Organization organization = new Organization("Yahoo!");

        organizationService.save(organization);
        assertThat(organization.getId()).isEqualTo(3);
    }

    @Test
    public void should_get_all_organization() {
        List<Organization> orgList = organizationService.getAllOrganizations();
        assertThat(orgList.size()).isGreaterThanOrEqualTo(2);
        assertThat(orgList.get(0).getName()).isEqualTo("Google");
        assertThat(orgList.get(0).getId()).isEqualTo(1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_delete_by_id_and_should_not_be_found() throws RecordExistsException {
        Organization organization = new Organization("Yandex");
        organizationService.save(organization);
        Long orgId = organization.getId();

        assertThat(orgId).isNotNull();

        organizationService.delete(orgId);
        organizationService.getOrganizationById(orgId);
    }
}