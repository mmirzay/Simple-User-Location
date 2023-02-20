package com.project.my.userlocation.controller.v2;

import com.project.my.userlocation.dto.in.UserInDto;
import com.project.my.userlocation.dto.in.UserLocationInDto;
import com.project.my.userlocation.dto.out.UserLocationOutDto;
import com.project.my.userlocation.dto.out.UserOutDto;
import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.mapper.LocationMapper;
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
    private final LocationMapper locationMapper;

    @Operation(summary = "Endpoint for add or update user info.")
    @PutMapping
    public ResponseEntity<UserOutDto> addOrUpdate(@RequestBody @Valid UserInDto userInDto) {
        User user = mapper.toEntity(userInDto);
        User result = service.addOrUpdate(user);
        UserOutDto userOutDto = mapper.toDto(result);
        HttpStatus httpStatus = userInDto.getUserId() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return new ResponseEntity<>(userOutDto, httpStatus);
    }

    @Operation(summary = "Endpoint for add Location info for user.")
    @PostMapping("/locations")
    public ResponseEntity<UserLocationOutDto> addLocation(@RequestBody @Valid UserLocationInDto userLocationInDto) {
        Location location = locationMapper.toEntity(userLocationInDto);
        Location result = service.addLocationForUser(userLocationInDto.getUserId(), location);
        UserLocationOutDto createdLocationOutDto = locationMapper.toCreatedLocationOutDto(result);
        return new ResponseEntity<>(createdLocationOutDto, HttpStatus.CREATED);
    }

}