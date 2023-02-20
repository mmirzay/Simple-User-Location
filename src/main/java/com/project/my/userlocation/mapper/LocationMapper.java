package com.project.my.userlocation.mapper;

import com.project.my.userlocation.dto.in.UserLocationInDto;
import com.project.my.userlocation.dto.out.UserLocationOutDto;
import com.project.my.userlocation.entity.Location;

import java.util.List;

/**
 * It is a mapper class to map entity and DTOs of {@link Location}/.
 * <br/>
 * Note that it will be refactored using {@code MapStruct}.
 */
public interface LocationMapper {
    Location toEntity(UserLocationInDto dto);

    UserLocationOutDto toCreatedLocationOutDto(Location result);

    UserLocationOutDto toLastLocationOutDto(Location result);

    UserLocationOutDto toLocationsListOutDto(String userId, List<Location> locations);
}