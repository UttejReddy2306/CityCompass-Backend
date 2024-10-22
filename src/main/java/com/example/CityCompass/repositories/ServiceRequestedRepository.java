package com.example.CityCompass.repositories;

import com.example.CityCompass.models.ServicesRequested;
import com.example.CityCompass.models.UserRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestedRepository extends JpaRepository<ServicesRequested,Integer> {
    List<ServicesRequested> findByProvidedUserAndPermission(String username);

    List<ServicesRequested> findByRequestedUser(String username);

    List<ServicesRequested> findByProvidedUserAndUserRequestStatus(String username, UserRequestStatus userRequestStatus);
}
