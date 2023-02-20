package com.project.my.userlocation.repository;

import com.project.my.userlocation.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

    Page<Location> findAllById_User_Id(String userId, PageRequest pageRequest);

}
