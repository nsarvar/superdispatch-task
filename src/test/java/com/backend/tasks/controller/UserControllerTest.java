package com.backend.tasks.controller;

import com.backend.tasks.Application;
import com.backend.tasks.entity.Organization;
import com.backend.tasks.entity.User;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.repository.UserRepository;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Implement tests for UserController
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MockMvc mockMvc;

    private Organization orgGoogle;
    private Organization orgApple;
    private User userAlice;
    private User userBob;


    // used for converting objects to json and vice versa
    private ObjectMapper mapper = new ObjectMapper();

    private static String getUrl(Long orgId) {
        return "/orgs/" + orgId + "/users/";
    }

    private static String getUrl(Long orgId, Long userId) {
        return "/orgs/" + orgId + "/users/" + userId;
    }

    /**
     * Loading initial data
     */
    @Before
    public void setup() {
        orgGoogle = new Organization("Google");
        orgApple = new Organization("Apple");
        userAlice = new User("alice", "123");
        userBob = new User("bob", "123");

        userAlice.setOrg(orgGoogle);
        userBob.setOrg(orgApple);

        organizationRepository.deleteAll();
        organizationRepository.saveAll(Arrays.asList(orgGoogle, orgApple));
        userRepository.saveAll(Arrays.asList(userAlice, userBob));
    }

    @Test
    public void shouldCreateUserAndReturnStatus() throws Exception {
        User user = new User("john", "1234");
        user.setOrg(orgApple);

        mockMvc.perform(post(getUrl(orgApple.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(user))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        User user = new User("bob", "12345");

        mockMvc.perform(put(getUrl(orgApple.getId(), userBob.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));
    }

    @Test
    public void shouldGetUserByOrgId() throws Exception {
        mockMvc.perform(get(getUrl(orgGoogle.getId(), userAlice.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userAlice.getId().intValue())))
                .andExpect(jsonPath("$.username", is(userAlice.getUsername())));
    }

    @Test
    public void shouldGetAllUsersByOrgId() throws Exception {
        mockMvc.perform(get(getUrl(orgGoogle.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userAlice.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is(userAlice.getUsername())));
    }

    @Test
    public void shouldDeleteUserAndReturns204() throws Exception {
        mockMvc.perform(delete(getUrl(orgGoogle.getId(), userAlice.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    <T> T jsonToObject(MvcResult result, Class<T> tClass) throws Exception {
        return this.mapper.readValue(result.getResponse().getContentAsString(), tClass);
    }

    private byte[] toJson(Object object) throws Exception {
        return this.mapper
                .writeValueAsString(object).getBytes();
    }
}