package com.project.my.userlocation.controller;

import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PutMapping
    public ResponseEntity<User> addOrUpdate(@RequestBody User user){
        User result = service.addOrUpdate(user);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}