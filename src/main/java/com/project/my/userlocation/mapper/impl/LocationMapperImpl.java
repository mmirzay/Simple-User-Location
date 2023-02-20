package com.project.my.userlocation.mapper.impl;

import com.project.my.userlocation.dto.out.LocationOutDto;
import com.project.my.userlocation.dto.in.UserLocationInDto;
import com.project.my.userlocation.dto.out.UserLocationOutDto;
import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.mapper.LocationMapper;
import org.springframework.stereotype.Service;

@Service
public class LocationMapperImpl implements LocationMapper {

    @Override
    public Location toEntity(UserLocationInDto dto) {
        return Location.builder()
                .id(Location.LocationId.builder()
                        .createdOn(dto.getCreatedOn())
                        .build())
                .latitude(dto.getLocation().getLatitude())
                .longitude(dto.getLocation().getLongitude())
                .build();
    }

    @Override
    public UserLocationOutDto toCreatedLocationOutDto(Location location) {
        String userId = location.getUserId();
        LocationOutDto locationOutDto = LocationOutDto.builder()
                .createdOn(location.getCreatedOn())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        return UserLocationOutDto.builder()
                .userId(userId)
                .location(locationOutDto)
                .build();
    }
}
