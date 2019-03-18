package com.backend.tasks.service.user.impl;

import com.backend.tasks.Application;
import com.backend.tasks.entity.Organization;
import com.backend.tasks.entity.User;
import com.backend.tasks.exception.RecordExistsException;
import com.backend.tasks.exception.ResourceNotFoundException;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.repository.UserRepository;
import com.backend.tasks.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Implement tests for UserServiceImpl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization orgYahoo;
    private Organization orgFacebook;
    private User userAlice;
    private User userBob;

    /**
     * Loading initial data
     */
    @Before
    public void setup() {
        orgYahoo = new Organization("Yahoo!");
        orgFacebook = new Organization("Facebook");
        userAlice = new User("alice", "123");
        userBob = new User("bob", "123");

        userAlice.setOrg(orgYahoo);
        userBob.setOrg(orgFacebook);

        organizationRepository.deleteAll();
        organizationRepository.saveAll(Arrays.asList(orgYahoo, orgFacebook));
        userRepository.saveAll(Arrays.asList(userAlice, userBob));
    }

    @Test
    public void user_service_should_be_autowired() {
        assertNotNull(userService);
    }

    @Test
    public void user_and_org_repozitories_should_be_autowired() {
        assertNotNull(userRepository);
        assertNotNull(organizationRepository);
    }

    @Test
    public void should_get_user_by_id() {
        User user = userService.getUserById(orgYahoo.getId(), userAlice.getId());

        assertThat(user.getId()).isEqualTo(userAlice.getId());
        assertThat(user.getUsername()).isEqualTo(userAlice.getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_throw_recordNotFoundException_when_user_not_exists() {
        userService.getUserById(100L, 100L);
    }

    @Test
    public void should_create_user() throws RecordExistsException {
        User userJohn = new User("john", "12345");
        userService.save(orgFacebook.getId(), userJohn);

        assertThat(userJohn.getId()).isNotNull();
    }

    @Test(expected = RecordExistsException.class)
    public void should_return_RecordExistsException_when_user_already_exists() throws RecordExistsException {
        User user = new User("alice", "123");
        userService.save(orgYahoo.getId(), user);
    }

    @Test
    public void should_update_user() throws RecordExistsException {
        User user = new User("bob", "12345");

        User userUpdated = userService.update(userBob.getId(), userBob.getOrg().getId(), user);

        assertThat(user.getUsername()).isEqualTo(userUpdated.getUsername());
        assertThat(user.getPassword()).isEqualTo(userUpdated.getPassword());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_delete_user() throws RecordExistsException {
        userService.delete(userBob.getId(), orgFacebook.getId());
        userService.getUserById(orgFacebook.getId(), userBob.getId());
        userService.getUserById(orgFacebook.getId(), userBob.getId());
    }
}