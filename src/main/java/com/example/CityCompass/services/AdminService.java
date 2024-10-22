package com.example.CityCompass.services;

import com.example.CityCompass.models.ServicesProvided;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {


    @Autowired
    ServiceProvidedService serviceProvidedService;
    public List<ServicesProvided> getAllPendingSp() {
        return serviceProvidedService.getAllPendingSp();
    }

    public String allowPermission(Integer spId) {
        return serviceProvidedService.allowPermission(spId);
    }

    public String declinePermission(Integer spId) {
        return serviceProvidedService.declinePermission(spId);
    }
}
