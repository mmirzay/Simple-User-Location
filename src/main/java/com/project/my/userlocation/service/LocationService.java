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
}
