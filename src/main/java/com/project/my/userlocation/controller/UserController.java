package com.project.my.userlocation.controller;

import com.project.my.userlocation.dto.UserInDto;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PutMapping
    public ResponseEntity<User> addOrUpdate(@RequestBody @Valid UserInDto userInDto) {
        User result = service.addOrUpdate(userInDto.toUser());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}