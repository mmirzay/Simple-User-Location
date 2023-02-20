package com.project.my.userlocation.controller.v2;

import com.project.my.userlocation.dto.UserInDto;
import com.project.my.userlocation.dto.UserOutDto;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.mapper.UserMapper;
import com.project.my.userlocation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v2/users")
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @Operation(summary = "Endpoint for add or update user info.")
    @PutMapping
    public ResponseEntity<UserOutDto> addOrUpdate(@RequestBody @Valid UserInDto userInDto) {
        User user = mapper.toEntity(userInDto);
        User result = service.addOrUpdate(user);
        UserOutDto userOutDto = mapper.toDto(result);
        HttpStatus httpStatus = userInDto.getUserId() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return new ResponseEntity<>(userOutDto, httpStatus);
    }

}