package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.exception.NotFoundException;
import com.project.my.userlocation.repository.UserRepository;
import com.project.my.userlocation.utility.DateUtil;
import com.project.my.userlocation.utility.MessageTranslatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link User}s.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final LocationService locationService;

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

    @Transactional
    public Location addLocationForUser(String userId, Location location) {
        log.info("adding new location for user with id: [{}]", userId);
        User user = getUserById(userId);
        location.setId(Location.LocationId.builder()
                .user(user)
                .createdOn(location.getCreatedOn())
                .build());
        return locationService.add(location);
    }

    @Transactional
    public Location getLastLocation(String userId) {
        log.info("getting last location of user with id: [{}]", userId);
        getUserById(userId);
        Optional<Location> location = locationService.getLast(userId).stream().findFirst();
        return location.orElseThrow(() -> new NotFoundException(MessageTranslatorUtil.getText("service.user.lastLocation.get.notFound")));
    }

    @Transactional
    public List<Location> getLocationsByRange(String userId, String from, String to) {
        log.info("getting locations of user with id: [{}] by range from: [{}] to: [{}]", userId, from, to);
        Date fromDate = DateUtil.parse(from);
        Date toDate = DateUtil.parse(to);
        return locationService.getLocationByRange(userId, fromDate, toDate);
    }
}