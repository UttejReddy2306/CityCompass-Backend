package com.example.CityCompass.services.BookServices;

import com.example.CityCompass.RequestDtos.DeleteTimeSlotDto;
import com.example.CityCompass.RequestDtos.ServiceRequestDto;
import com.example.CityCompass.models.*;
import com.example.CityCompass.repositories.ServiceRequestedRepository;
import com.example.CityCompass.services.EmailService;
import com.example.CityCompass.services.S3Service;
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

    @Autowired
    DateSlotService dateSlotService;

    @Autowired
    S3Service s3Service;

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
                 .address(serviceRequestDto.getAddress())
                 .userRequestStatus(UserRequestStatus.REQUESTED)
                 .permission(Permission.Pending)
                 .name(serviceRequestDto.getName())
                 .email(serviceRequestDto.getEmail())
                 .number(serviceRequestDto.getNumber())
                 .imageList(serviceRequestDto.getMultipartFileList() != null ? serviceRequestDto.getMultipartFileList()
                         .stream().map(x -> s3Service.createFile(x) ).toList():null)
                 .build();
         serviceRequestedRepository.save(serviceRequested);
         timeSlotService.timeSlotRepository.save(timeSlot);
         this.emailService.sendBookingConfirmation(providerUser.getEmail(), requestedUser.getName(), servicesProvided.getService().name());
         return "Successfull";
    }

    public List<ServicesRequested> getAllProviderRequests(String username) {
        Users users = this.userService.getUser(username);

        return serviceRequestedRepository.findByProvidedUserAndUserRequestStatusAndPermission(users,UserRequestStatus.REQUESTED,Permission.Pending);
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
            if(!servicesRequested.getPermission().equals(Permission.Completed) &&
                    (!servicesRequested.getPermission().equals(Permission.Accepted)) &&
                    decision.equals("declined")){
                servicesRequested.setPermission(Permission.Rejected);
                TimeSlot timeSlot = servicesRequested.getTimeSlot();
                this.timeSlotService.makingItAvailable(timeSlot);
            }
            else if(!servicesRequested.getPermission().equals(Permission.Completed) && decision.equals("accepted")){
                servicesRequested.setPermission(Permission.Accepted);
            }
            else if(!servicesRequested.getPermission().equals(Permission.Completed) && decision.equals("completed"))servicesRequested.setPermission(Permission.Completed);
            else servicesRequested.setPermission(Permission.Rejected);
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


    public String deleteSlot(DeleteTimeSlotDto deleteTimeSlotDto, String username) {
        Users users = this.userService.getUser(username);
        ServicesProvided servicesProvided = this.serviceProvidedService.findById(deleteTimeSlotDto.getServiceId());
        if(servicesProvided != null){
            if(servicesProvided.getUser() != users) return "UnAuthorized";
            for(Integer timeSlotId : deleteTimeSlotDto.getLocalTimeSlotIdList()){
                TimeSlot timeSlot = this.timeSlotService.findByTimeSlotId(timeSlotId);
                if(timeSlot == null) return "InValid TimeSlot ";
                this.serviceRequestedRepository.clearAllServiceRequestedByTimeSlotId(timeSlotId,Permission.Rejected);
                this.timeSlotService.deleteByTimeSlotId(timeSlot);
            }
            return "Succesfully";
        }
        return "Invalid Service ";
    }

    public List<ServicesRequested> getAllAcceptedServiceRequests(String username) {
        Users users = userService.getUser(username);
        return serviceRequestedRepository.findByPermissionOrderByUpdatedOnDesc(Permission.Accepted);
    }

    public List<ServicesRequested> getAllProviderRequestsByStatus(String username, Permission status) {
        Users users =userService.getUser(username);
        return serviceRequestedRepository.findByProvidedUserAndPermission(users,status);
    }
}