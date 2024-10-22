package com.example.CityCompass.repositories;

import com.example.CityCompass.models.ServicesRequested;
import com.example.CityCompass.models.UserRequestStatus;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestedRepository extends JpaRepository<ServicesRequested,Integer> {


    List<ServicesRequested> findByRequestedUser(Users users);

    List<ServicesRequested> findByProvidedUserAndUserRequestStatus(Users users, UserRequestStatus userRequestStatus);
}
