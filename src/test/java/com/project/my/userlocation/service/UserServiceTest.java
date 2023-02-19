package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("A new user data is created. Then User is added. Finally new user values are asserted.")
    void givenANewUser_whenAddOrUpdateIt_thenItMustBeAdded() {
        User newUser = getNewUser();

        User addedUser = service.addOrUpdate(newUser);

        assertUserEquality(addedUser, newUser);
    }

    private void assertUserEquality(User expected, User actual) {
        assertNotNull(expected.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getSecondName(), actual.getSecondName());
    }

    private User getNewUser() {
        return User.builder()
                .email("mirzay.mohsen@gmail.com")
                .firstName("mohsen")
                .secondName("mirzaei").
                build();
    }
}