package com.example.CityCompass.controllers;

import com.example.CityCompass.models.Services;
import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.models.ServicesRequested;
import com.example.CityCompass.services.ServiceProvidedService;
import com.example.CityCompass.services.ServiceRequestedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookServices")
public class BookServiceController {
    @Autowired
    ServiceProvidedService serviceProvidedService;

    @Autowired
    ServiceRequestedService serviceRequestedService;

    @GetMapping("/public/getService/{service}")
    public List<ServicesProvided> getService(@PathVariable("service") String service){
        return serviceProvidedService.getService(Services.valueOf(service));
    }

    @GetMapping("/public/getAllService")
    public List<ServicesProvided> getAllService(){
        return  serviceProvidedService.getAllServices();
    }

    @PostMapping("/all/requestService/{serviceId}")
    public String requestService(@PathVariable("serviceId") Integer serviceId, HttpServletRequest request)
    {
        return serviceRequestedService.createRequest(serviceId,request.getAttribute("username").toString());
    }

    @GetMapping("/provider/getAllProviderRequests")
    public List<ServicesRequested> getAllProviderRequests(HttpServletRequest request){
        return this.serviceRequestedService.getAllProviderRequests(request.getAttribute("username").toString());
    }

    @GetMapping("/all/getAllRequests")
    public List<ServicesRequested> getAllServicesRequestedByUser(HttpServletRequest request){
        return this.serviceRequestedService.getAllServicesRequestedByUser(request.getAttribute("username").toString());
    }

    @PatchMapping("/provider/AcceptResponse/{srId}")
    public String AcceptResponse(@PathVariable("srId") Integer srId){
        return this.serviceRequestedService.AcceptResponse(srId);
    }

    @PatchMapping("/provider/RejectResponse/{srId}")
    public String RejectResponse(@PathVariable("srId") Integer srId){
        return this.serviceRequestedService.RejectResponse(srId);
    }
}
