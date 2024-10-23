package com.example.CityCompass.services.BookServiceControllers;

import com.example.CityCompass.models.*;
import com.example.CityCompass.repositories.ServiceRequestedRepository;
import com.example.CityCompass.services.BookServiceControllers.ServiceProvidedService;
import com.example.CityCompass.services.EmailService;
import com.example.CityCompass.services.UserService;
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
    EmailService emailService;

    @Autowired
    ServiceProvidedService serviceProvidedService;

    public String createRequest(Integer serviceId, String username) {
        Users requestedUser = userService.getUser(username);
        ServicesProvided servicesProvided = serviceProvidedService.getProvider(serviceId);
        Users providerUser = servicesProvided.getUser();
         ServicesRequested serviceRequested = ServicesRequested.builder()
                 .servicesProvided(servicesProvided)
                 .requestedUser(requestedUser)
                 .providedUser(providerUser)
                 .userRequestStatus(UserRequestStatus.REQUESTED)
                 .permission(Permission.Pending)
                 .build();
         serviceRequestedRepository.save(serviceRequested);
         this.emailService.sendBookingConfirmation(providerUser.getEmail(), requestedUser.getName(), servicesProvided.getService().name());
         return "Successfull";
    }

    public List<ServicesRequested> getAllProviderRequests(String username) {
        Users users = this.userService.getUser(username);

        return serviceRequestedRepository.findByProvidedUserAndUserRequestStatus(users,UserRequestStatus.REQUESTED);
    }

    public List<ServicesRequested> getAllServicesRequestedByUser(String username) {
        Users users = this.userService.getUser(username);
        return serviceRequestedRepository.findByRequestedUser(users);
    }

    public String updateResponse(Integer srId,String decision) {
        ServicesRequested servicesRequested = serviceRequestedRepository.findById(srId).orElse(null);
        if(servicesRequested != null) {
            servicesRequested.setPermission(decision.equals("accepted")?Permission.Accepted:Permission.Rejected);
            serviceRequestedRepository.save(servicesRequested);
            this.emailService.sendProviderResponse(servicesRequested.getRequestedUser().getEmail(), servicesRequested.getProvidedUser().getName()
                    , servicesRequested.getServicesProvided().getService().name(), decision.equals("accepted"));
            return "Successfull";
        }
        return "Unsuccessfull";

    }


    public String cancelRequest(Integer serviceId, String username) {
        ServicesRequested servicesRequested = this.serviceRequestedRepository.findById(serviceId).orElse(null);
        if(servicesRequested != null) {
            servicesRequested.setUserRequestStatus(UserRequestStatus.CANCELLED);
            return "Cancelled Successfully";
        }
        return "Bad Request";
    }

}
