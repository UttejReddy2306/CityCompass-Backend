package com.example.CityCompass.services.BookServices;

import com.example.CityCompass.RequestDtos.ServiceRequestDto;
import com.example.CityCompass.models.*;
import com.example.CityCompass.repositories.ServiceRequestedRepository;
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

    @Autowired
    TimeSlotService timeSlotService;

    public String createRequest(ServiceRequestDto serviceRequestDto, String username) {
        Users requestedUser = userService.getUser(username);
        ServicesProvided servicesProvided = serviceProvidedService.getProvider(serviceRequestDto.getServiceId());
        if(servicesProvided == null) return "Invalid Service Id";
        TimeSlot timeSlot = timeSlotService.getTimeSlotById(serviceRequestDto.getLocalTimeId());
        if(timeSlot == null) return "Invalid Time Slot";
        if(!timeSlot.getIsAvailable()) return "TimeSlot is not available";
        timeSlot.setIsAvailable(false);
        Users providerUser = servicesProvided.getUser();
         ServicesRequested serviceRequested = ServicesRequested.builder()
                 .servicesProvided(servicesProvided)
                 .requestedUser(requestedUser)
                 .providedUser(providerUser)
                 .timeSlot(timeSlot)
                 .requestedUserProblem(serviceRequestDto.getUserReason())
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

    public String updateResponse(Integer srId, String decision, String username) {
        Users users = this.userService.getUser(username);
        ServicesRequested servicesRequested = serviceRequestedRepository.findById(srId).orElse(null);
        if(servicesRequested != null) {
            if(servicesRequested.getProvidedUser() != users ) return "UnAuthorized";
            if(decision.equals("declined")){
                servicesRequested.setPermission(Permission.Rejected);
                TimeSlot timeSlot = servicesRequested.getTimeSlot();
                this.timeSlotService.makingItAvailable(timeSlot);
            }
            else servicesRequested.setPermission(Permission.Accepted);
            serviceRequestedRepository.save(servicesRequested);
            this.emailService.sendProviderResponse(servicesRequested.getRequestedUser().getEmail(), servicesRequested.getProvidedUser().getName()
                    , servicesRequested.getServicesProvided().getService().name(), decision.equals("accepted"));
            return "Successfull";
        }
        return "UnSuccessfull";

    }


    public String cancelRequest(Integer serviceId, String username) {
        Users users = this.userService.getUser(username);
        ServicesRequested servicesRequested = this.serviceRequestedRepository.findById(serviceId).orElse(null);
        if(servicesRequested != null) {
            if(servicesRequested.getRequestedUser() != users) return "UnAuthorized";
            servicesRequested.setUserRequestStatus(UserRequestStatus.CANCELLED);
            TimeSlot timeSlot = servicesRequested.getTimeSlot();
            this.timeSlotService.makingItAvailable(timeSlot);
            this.serviceRequestedRepository.save(servicesRequested);
            return "Cancelled Successfully";
        }
        return "Bad Request";
    }


    public String deleteSlot(Integer serviceId, Integer timeSlotId, String username) {
        Users users = this.userService.getUser(username);
        ServicesProvided servicesProvided = this.serviceProvidedService.findById(serviceId);
        if(servicesProvided != null){
            if(servicesProvided.getUser() != users) return "UnAuthorized";
            TimeSlot timeSlot = this.timeSlotService.findByTimeSlotId(timeSlotId);
            if(timeSlot == null) return "InValid TimeSlot ";

            this.serviceRequestedRepository.clearAllServiceRequestedByTimeSlotId(timeSlotId,Permission.Rejected);
            this.timeSlotService.deleteByTimeSlotId(timeSlot);
            return "Succesfull";
        }
        return "Invalid Service ";
    }
}
