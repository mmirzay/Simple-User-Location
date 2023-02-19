package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.exception.NotFoundException;
import com.project.my.userlocation.repository.UserRepository;
import com.project.my.userlocation.utility.MessageTranslatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing {@link User}s.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User addOrUpdate(User user) {
        user = updateUserIfPossible(user);
        return userRepository.save(user);
    }

    private User updateUserIfPossible(User user) {
        if (user.getId() == null) {
            log.info("adding new user with email: {}", user.getEmail());
            return user;
        }

        User userToUpdate = getUserById(user.getId());
        log.info("updating user with id: {}", user.getId());
        return userToUpdate.update(user);
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageTranslatorUtil.getText("service.user.get.notFound")));
    }
}