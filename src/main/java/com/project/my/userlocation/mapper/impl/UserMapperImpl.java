package com.project.my.userlocation.mapper.impl;

import com.project.my.userlocation.dto.in.UserInDto;
import com.project.my.userlocation.dto.out.UserOutDto;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserInDto dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .secondName(dto.getSecondName())
                .build();
        user.setId(dto.getUserId());
        return user;
    }

    @Override
    public UserOutDto toDto(User entity) {
        return UserOutDto.builder()
                .userId(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .build();
    }
}
