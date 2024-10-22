package com.example.CityCompass.repositories;

import com.example.CityCompass.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProvidedRepository extends JpaRepository<ServicesProvided,Integer> {

    ServicesProvided findByUserAndStatusAndPermission(Users user, Status status, Permission permission);
    List<ServicesProvided> findByServiceAndStatusAndPermission(Services service, Status status, Permission permission);

    List<ServicesProvided> findByPermission(Permission permission);


    List<ServicesProvided> findByStatusAndPermission(Status status, Permission permission);
}
