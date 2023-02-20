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
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @Operation(summary = "Endpoint for get last Location info of user if exist.")
    @GetMapping("/{id}/locations-last")
    public ResponseEntity<UserLocationOutDto> getLastLocation(@PathVariable String id) {
        Location result = service.getLastLocation(id);
        UserLocationOutDto lastLocationOutDto = locationMapper.toLastLocationOutDto(result);
        return new ResponseEntity<>(lastLocationOutDto, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint for get Locations info of user if exist by range. Date format must be: yyyy-MM-dd'T'HH:mm:ss.SSS",
            parameters = {
                    @Parameter(name = "from", required = true, example = "2022-02-08T13:44:00.0"),
                    @Parameter(name = "to", required = true, example = "2022-02-08T13:44:00.0")
            })
    @GetMapping("/{id}/locations")
    public ResponseEntity<UserLocationOutDto> getLocations(@PathVariable String id, @RequestParam String from, @RequestParam String to) {
        List<Location> result = service.getLocationsByRange(id, from, to);
        UserLocationOutDto locationsListOutDto = locationMapper.toLocationsListOutDto(id, result);
        HttpStatus status = locationsListOutDto.getLocations().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(locationsListOutDto, status);
    }
}