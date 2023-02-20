package com.project.my.userlocation.mapper;

import com.project.my.userlocation.dto.UserLocationInDto;
import com.project.my.userlocation.dto.UserLocationOutDto;
import com.project.my.userlocation.entity.Location;

/**
 * It is a mapper class to map entity and DTOs of {@link Location}/.
 * <br/>
 * Note that it will be refactored using {@code MapStruct}.
 */
public interface LocationMapper {
    Location toEntity(UserLocationInDto dto);

    UserLocationOutDto toCreatedLocationOutDto(Location result);

}