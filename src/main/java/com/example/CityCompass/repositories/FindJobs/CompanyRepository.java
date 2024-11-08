package com.example.CityCompass.repositories.FindJobs;

import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.Permission;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findByRegistrationId(int registrationId);

    Company findByCompanyName(String companyName);

    Company findByUser(Users user);

    List<Company> findByPermission(Permission permission);

    List<Company> findByUserAndPermissionAndStatus(Users user, Permission permission, Status status);

    List<Company> findByStatusAndPermission(Status status, Permission permission);

}