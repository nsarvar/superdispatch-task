package com.backend.tasks.service.user.impl;

import com.backend.tasks.Application;
import com.backend.tasks.entity.User;
import com.backend.tasks.exception.ResourceNotFoundException;
import com.backend.tasks.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    private Long orgId = 1L;
    private Long userId = 1L;

    @Test
    public void user_service_should_be_autowired() {
        assertNotNull(userService);
    }

    @Test
    public void should_get_user_by_id() {
        User user = userService.getUserById(orgId, userId);

        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getUsername()).isEqualTo("test_user");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_throw_recordNotFoundException_when_user_not_exists() {
        userService.getUserById(100L, 100L);
    }

    @Test
    public void should_create_user() {
        User user = new User("create_test", "12345");
        userService.save(orgId, user);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void should_update_user() {
        User user = new User("update_test", "12345");
        userService.save(orgId, user);

        user.setUsername("test_user_for_update");

        User userUpdated = userService.update(user.getId(), user.getOrg().getId(), user);

        assertThat(userUpdated.getUsername()).isEqualTo(user.getUsername());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_delete_user() {
        User user = new User("test_for_delete", "12345");
        userService.save(orgId, user);

        Long newUserId = user.getId();

        assertThat(newUserId).isNotNull();

        userService.delete(newUserId, orgId);

        userService.getUserById(orgId, newUserId);
    }
}