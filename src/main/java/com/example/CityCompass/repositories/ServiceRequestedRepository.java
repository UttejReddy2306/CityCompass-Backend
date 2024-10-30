package com.example.CityCompass.repositories;

import com.example.CityCompass.models.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.CityCompass.models.ServicesRequested;
import com.example.CityCompass.models.UserRequestStatus;
import com.example.CityCompass.models.Users;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestedRepository extends JpaRepository<ServicesRequested,Integer> {


    List<ServicesRequested> findByRequestedUser(Users users);

    List<ServicesRequested> findByProvidedUserAndUserRequestStatus(Users users, UserRequestStatus userRequestStatus);


    ServicesRequested findByTimeSlotAndUserRequestStatusAndPermission(TimeSlot timeSlot, UserRequestStatus userRequestStatus, Permission permission);

    @Modifying
    @Transactional
    @Query("update ServicesRequested s set s.timeSlot = null , s.permission = ?2 where s.timeSlot.id = ?1")
    void clearAllServiceRequestedByTimeSlotId(Integer timeSlotId, Permission permission);

}
