package com.example.CityCompass.services;

import com.example.CityCompass.dtos.SpCreateRequest;
import com.example.CityCompass.models.*;
import com.example.CityCompass.repositories.ServiceProvidedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProvidedService {

    @Autowired
    ServiceProvidedRepository serviceProvidedRepository;


    public void createSp(Users users, SpCreateRequest spCreateRequest) {
        ServicesProvided servicesProvided = spCreateRequest.toServiceProvided(spCreateRequest);
        servicesProvided.setUser(users);
        this.serviceProvidedRepository.save(servicesProvided);
    }

    public List<ServicesProvided> getAllPendingSp() {
        return this.serviceProvidedRepository.findByPermission(Permission.Pending);
    }

    public String allowPermission(Integer spId) {

        ServicesProvided servicesProvided = serviceProvidedRepository.findById(spId).orElse(null);
        if(servicesProvided != null) {
            servicesProvided.setPermission(Permission.Accepted);
            serviceProvidedRepository.save(servicesProvided);
            return "AcceptedSuccessful";
        }
        return "Invalid Id";



    }

    public String declinePermission(Integer spId) {
        ServicesProvided servicesProvided = serviceProvidedRepository.findById(spId).orElse(null);
        if(servicesProvided != null) {
            servicesProvided.setPermission(Permission.Rejected);
            serviceProvidedRepository.save(servicesProvided);
            return "RejectedSuccessful";
        }
        return "Invalid Id";
    }

    public List<ServicesProvided> getService(Services services) {
        return serviceProvidedRepository.findByServiceAndStatusAndPermission(services, Status.ACTIVE ,Permission.Accepted);
    }

    public List<ServicesProvided> getAllServices() {
        return serviceProvidedRepository.findByStatusAndPermission(Status.ACTIVE,Permission.Accepted);
    }

    public ServicesProvided getProvider(Integer serviceId) {
        return serviceProvidedRepository.findById(serviceId).orElse(null);
    }
}
