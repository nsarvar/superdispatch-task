package com.backend.tasks.controller;

import com.backend.tasks.Application;
import com.backend.tasks.entity.Organization;
import com.backend.tasks.repository.OrganizationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Implement tests for OrganizationController
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class OrganizationControllerTest {

    private static final String API_URL = "/orgs/";

    private Organization orgGoogle;
    private Organization orgApple;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    // used for converting objects to json and vice versa
    private ObjectMapper mapper = new ObjectMapper();

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

    /**
     * Should get Organization by orgId
     *
     * @throws Exception
     */
    @Test
    public void shouldGetOrganization() throws Exception {
        Organization[] orgs = jsonToObject(getAllOrganizations().andReturn(), Organization[].class);

        assertThat(orgs.length).isEqualTo(2);

        getOrganization(orgs[0].getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orgs[0].getId().intValue())))
                .andExpect(jsonPath("$.name", is("Google")));
    }

    /**
     * Should get 404 status when Org is not found
     *
     * @throws Exception
     */
    @Test
    public void shouldGetUnknownOrganizationWithStatusNotFound() throws Exception {
        getOrganization(100L)
                .andExpect(status().isNotFound());
    }

    /**
     * Should create organization and return created status (201)
     *
     * @throws Exception
     */
    @Test
    public void shouldCreateOrganizationAndReturnCreatedStatus() throws Exception {
        Organization organization = new Organization("Microsoft");

        createOrganization(organization)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(organization.getName())));
    }

    /**
     * Should update organization
     *
     * @throws Exception
     */
    @Test
    public void shouldUpdateOrganization() throws Exception {
        Organization organization = new Organization("Apple Inc.");

        updateOrganization(orgApple.getId(), organization)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(organization.getName())));
    }

    @Test
    public void shouldNotUpdateUnkownOrganization() throws Exception {
        updateOrganization(100L, new Organization("Apple"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteOrganization() throws Exception {
        deleteOrganization(orgGoogle.getId())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldNotDeleteUnkownOrganizationReturnNotFoundStatus() throws Exception {
        deleteOrganization(100L)
                .andExpect(status().isNotFound());
    }

    private ResultActions createOrganization(Organization organization) throws Exception {
        return mockMvc.perform(post(API_URL).content(toJson(organization)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions updateOrganization(Long id, Organization organization) throws Exception {
        return mockMvc.perform(put(API_URL + id).content(toJson(organization)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions getOrganization(Long id) throws Exception {
        return mockMvc.perform(get(API_URL + id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions getAllOrganizations() throws Exception {
        return mockMvc.perform(get(API_URL).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions deleteOrganization(Long id) throws Exception {
        return mockMvc.perform(delete(API_URL + id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
    }


    <T> T jsonToObject(MvcResult result, Class<T> tClass) throws Exception {
        return this.mapper.readValue(result.getResponse().getContentAsString(), tClass);
    }

    private byte[] toJson(Object object) throws Exception {
        return this.mapper
                .writeValueAsString(object).getBytes();
    }
}