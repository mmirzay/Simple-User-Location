package com.project.my.userlocation.mapper;

import com.project.my.userlocation.dto.in.UserInDto;
import com.project.my.userlocation.dto.out.UserOutDto;
import com.project.my.userlocation.entity.User;

/**
 * It is a mapper class to map entity and DTOs of {@link User}/.
 * <br/>
 * Note that it will be refactored using {@code MapStruct}.
 */
public interface UserMapper {
    User toEntity(UserInDto dto);
    UserOutDto toDto(User entity);
}