package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing {@link User}s.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User addOrUpdate(User user){
        return userRepository.save(user);
    }
}