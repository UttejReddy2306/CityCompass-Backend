package com.example.CityCompass.repositories;

import com.example.CityCompass.models.UserType;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<Users,Integer> {

    Users findByUsername(String username);

    Users findByUserType(UserType userType);

    boolean existsByEmail(String email);

    boolean existsByNumber(String number);


    Optional<Users> findByEmail(String email);

    Optional<Users> findByResetToken(String token);
}
