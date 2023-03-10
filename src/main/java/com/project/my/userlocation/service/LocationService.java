package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service class for managing {@link Location}s of {@link User}s.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    private final LocationRepository repository;

    @Transactional
    public Location add(Location location) {
        log.info("adding new location for user with id: [{}]", location.getUserId());
        return repository.save(location);
    }

    /**
     * Return a page of the last locations of user, given user id.
     * @param userId id of user to get last location
     * @return page of locations order by createdOn date DESC.
     */
    @Transactional
    public Page<Location> getLast(String userId) {
        log.info("getting last location of user with id: [{}]", userId);
        return repository.findAllById_User_Id(userId,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id.createdOn")));
    }

    /**
     * It returns an ordered list of locations based on an inclusive range of createdOn dates for user.
     * If locations are not exists, an empty list is returned.
     * @param userId id of user to get locations
     * @param from beginning of the date range (inclusive)
     * @param to end of the date range (inclusive)
     * @return list of location based on a range of createdOn dates.
     */
    @Transactional
    public List<Location> getLocationByRange(String userId, Date from, Date to) {
        log.info("getting locations of user with id: [{}] by range from: [{}] to: [{}]", userId, from, to);
        return repository.findAllById_User_IdAndAndId_CreatedOnBetweenOrderById_CreatedOn(userId, from, to);
    }
}