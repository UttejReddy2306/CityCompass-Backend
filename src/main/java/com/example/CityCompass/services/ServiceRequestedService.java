package com.example.CityCompass.services;

import com.example.CityCompass.models.*;
import com.example.CityCompass.repositories.ServiceRequestedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestedService {

    @Autowired
    ServiceRequestedRepository serviceRequestedRepository;

    @Autowired
    UserService userService;

    @Autowired
    ServiceProvidedService serviceProvidedService;

    public String createRequest(Integer serviceId, String username) {
        Users requestedUser = userService.getUser(username);
        ServicesProvided servicesProvided = serviceProvidedService.getProvider(serviceId);
         ServicesRequested serviceRequested = ServicesRequested.builder()
                 .servicesProvided(servicesProvided)
                 .requestedUser(requestedUser)
                 .providedUser(servicesProvided.getUser())
                 .userRequestStatus(UserRequestStatus.REQUESTED)
                 .permission(Permission.Pending)
                 .build();
         serviceRequestedRepository.save(serviceRequested);
         return "Successfull";
    }

    public List<ServicesRequested> getAllProviderRequests(String username) {
        return serviceRequestedRepository.findByProvidedUserAndUserRequestStatus(username,UserRequestStatus.REQUESTED);
    }

    public List<ServicesRequested> getAllServicesRequestedByUser(String username) {
        return serviceRequestedRepository.findByRequestedUser(username);
    }

    public String AcceptResponse(Integer srId) {
        ServicesRequested servicesRequested = serviceRequestedRepository.findById(srId).orElse(null);
        if(servicesRequested != null) {
            servicesRequested.setPermission(Permission.Accepted);
            serviceRequestedRepository.save(servicesRequested);
            return "Successfull";
        }
        return "Unsuccessfull";

    }

    public String RejectResponse(Integer srId) {
        ServicesRequested servicesRequested = serviceRequestedRepository.findById(srId).orElse(null);
        if(servicesRequested != null) {
            servicesRequested.setPermission(Permission.Rejected);
            serviceRequestedRepository.save(servicesRequested);
            return "Successfull";
        }
        return "Unsuccessfull";
    }
}
