package com.example.CityCompass.repositories;

import com.example.CityCompass.models.*;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface ServiceProvidedRepository extends JpaRepository<ServicesProvided,Integer> {

    ServicesProvided findByUserAndStatusAndPermission(Users user, Status status, Permission permission);
    List<ServicesProvided> findByServiceAndStatusAndPermission(Services service, Status status, Permission permission);

    List<ServicesProvided> findByPermission(Permission permission);


    List<ServicesProvided> findByStatusAndPermission(Status status, Permission permission);


    @Modifying
    @Transactional
    @Query("update ServicesProvided s set s.permission = ?1 where s.id = ?2")
    void updatePermission(Permission permission, Integer id);

    List<ServicesProvided> findByUserId(Integer id);
}
