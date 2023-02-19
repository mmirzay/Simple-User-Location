package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    void addOrUpdate() {
        User user = User.builder().
                email("email")
                .firstName("mohsen")
                .secondName("mirzaei").
                build();

        service.addOrUpdate(user);
    }
}