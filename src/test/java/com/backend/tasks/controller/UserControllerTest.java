package com.backend.tasks.controller;

import com.backend.tasks.Application;
import com.backend.tasks.entity.Organization;
import com.backend.tasks.entity.User;
import com.backend.tasks.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
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
    private MockMvc mockMvc;

    // converting objects to json and vice versa
    private ObjectMapper mapper = new ObjectMapper();

    private static String getUrl(Long orgId) {
        return "/orgs/" + orgId + "/users/";
    }

    private static String getUrl(Long orgId, Long userId) {
        return "/orgs/" + orgId + "/users/" + userId;
    }

    @Test
    public void shouldCreateUserAndReturnStatus() throws Exception {
        Organization[] orgs = jsonToObject(getAllOrganizations().andReturn(), Organization[].class);
        assertThat(orgs.length).isEqualTo(2);

        User user = new User("user1", "123");
        user.setOrg(orgs[0]);

        createUser(orgs[0].getId(), user).andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        Organization[] orgs = jsonToObject(getAllOrganizations().andReturn(), Organization[].class);
        assertThat(orgs.length).isEqualTo(2);

        User user = new User("test_user", "12345");

        mockMvc.perform(put(getUrl(orgs[0].getId(), 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password", is(user.getPassword())));
    }

    @Test
    public void shouldGetUserByOrgId() throws Exception {
        Long userId = Long.valueOf(1);
        Long orgId = Long.valueOf(1);

        mockMvc.perform(get(getUrl(orgId, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("test_user")));
    }

    @Test
    public void shouldGetAllUsersByOrgId() throws Exception {
        Long orgId = Long.valueOf(1);
        mockMvc.perform(get(getUrl(orgId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("test_user")));
    }

    @Test
    public void shouldDeleteUserAndReturns204() throws Exception {
        Long userId = Long.valueOf(1);
        Long orgId = Long.valueOf(1);

        mockMvc.perform(delete(getUrl(orgId, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    private ResultActions createUser(Long orgId, User user) throws Exception {
        return mockMvc.perform(post(getUrl(orgId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(user)));
    }

    public ResultActions getAllOrganizations() throws Exception {
        return mockMvc.perform(get("/orgs/").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
    }

    <T> T jsonToObject(MvcResult result, Class<T> tClass) throws Exception {
        return this.mapper.readValue(result.getResponse().getContentAsString(), tClass);
    }

    private byte[] toJson(Object object) throws Exception {
        return this.mapper
                .writeValueAsString(object).getBytes();
    }
}