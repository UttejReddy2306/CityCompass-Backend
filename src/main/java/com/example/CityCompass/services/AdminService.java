package com.example.CityCompass.services;

import com.example.CityCompass.models.ServicesProvided;

import com.example.CityCompass.services.BookServices.ServiceProvidedService;



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

    public String updatePermission(Integer spId, String decision) {
        return serviceProvidedService.updatePermission(spId,decision);
    }


}
