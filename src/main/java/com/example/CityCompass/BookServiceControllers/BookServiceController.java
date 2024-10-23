package com.example.CityCompass.BookServiceControllers;

import com.example.CityCompass.models.Services;
import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.models.ServicesRequested;
import com.example.CityCompass.services.BookServiceControllers.ServiceProvidedService;
import com.example.CityCompass.services.BookServiceControllers.ServiceRequestedService;
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

    @PatchMapping("/all/cancelService/{serviceId}")
    public String cancelService(@PathVariable("serviceId") Integer serviceId, HttpServletRequest request)
    {
        return serviceRequestedService.cancelRequest(serviceId,request.getAttribute("username").toString());
    }

    @GetMapping("/provider/getAllProviderRequests")
    public List<ServicesRequested> getAllProviderRequests(HttpServletRequest request){
        return this.serviceRequestedService.getAllProviderRequests(request.getAttribute("username").toString());
    }

    @GetMapping("/all/getAllRequests")
    public List<ServicesRequested> getAllServicesRequestedByUser(HttpServletRequest request){
        return this.serviceRequestedService.getAllServicesRequestedByUser(request.getAttribute("username").toString());
    }

    @PatchMapping("/provider/updateResponse/{srId}/{decision}")
    public String updateResponse(@PathVariable("srId") Integer srId, @PathVariable("decision") String decision){
        return this.serviceRequestedService.updateResponse(srId,decision);
    }

}
