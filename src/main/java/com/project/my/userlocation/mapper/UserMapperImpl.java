package com.project.my.userlocation.mapper;

import com.project.my.userlocation.dto.UserInDto;
import com.project.my.userlocation.dto.UserOutDto;
import com.project.my.userlocation.entity.User;
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
