package com.example.CityCompass.services.BookServices;

import com.example.CityCompass.RequestDtos.ServiceEditDto;
import com.example.CityCompass.RequestDtos.SpCreateRequest;
import com.example.CityCompass.RequestDtos.SlotDto;
import com.example.CityCompass.models.*;
import com.example.CityCompass.repositories.ServiceProvidedRepository;
import com.example.CityCompass.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.DataInput;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
@Service
public class ServiceProvidedService {

    @Autowired
    ServiceProvidedRepository serviceProvidedRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    DateSlotService dateSlotService;

    @Autowired
    ObjectMapper mapper;

    public void createSp(Users users, SpCreateRequest spCreateRequest) {
        ServicesProvided servicesProvided = spCreateRequest.toServiceProvided(spCreateRequest);
        servicesProvided.setUser(users);
        this.serviceProvidedRepository.save(servicesProvided);


    }

    public List<ServicesProvided> getAllPendingSp() {
        return this.serviceProvidedRepository.findByPermission(Permission.Pending);
    }

    public String updatePermission(Integer spId,String decision) {

        ServicesProvided servicesProvided = serviceProvidedRepository.findById(spId).orElse(null);
        if (servicesProvided != null) {
            if ("accepted".equalsIgnoreCase(decision)) {
                this.serviceProvidedRepository.updatePermission(Permission.Accepted,servicesProvided.getId());
            } else if ("rejected".equalsIgnoreCase(decision)) {
                this.serviceProvidedRepository.updatePermission(Permission.Rejected,servicesProvided.getId());
            } else {
                return "Invalid decision value provided.";
            }

            // Save the updated entity



            return "Permission updated successfully.";
        } else {
            return "Service provided entry not found.";
        }


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

    public String createSlot(Integer serviceId, List<SlotDto> slotDtoList, Users users) {
        ServicesProvided servicesProvided = this.serviceProvidedRepository.findById(serviceId).orElseThrow(() ->
                new IllegalArgumentException("Invalid Service"));

        if(users != servicesProvided.getUser()) return "UnAuthorized";
        return dateSlotService.createSlot(servicesProvided,slotDtoList);

    }


    public ServicesProvided findById(Integer serviceId) {
        return this.serviceProvidedRepository.findById(serviceId).orElse(null);
    }

    public List<DateSlot> getAllTimeSlots(Integer serviceId) {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        return this.dateSlotService.findDateSlotsWithAvailableTimes(serviceId,localDate,localTime);

    }

    public List<DateSlot> allDateTimeSlots(Integer serviceId) {
        return this.dateSlotService.findByServicesProvidedId(serviceId);
    }

    public String updateDetails(ServiceEditDto serviceEditDto, Users users)  {

        ServicesProvided savedServicesProvided = this.serviceProvidedRepository.findById(serviceEditDto.getServiceId()).orElse(null);
        if(users != savedServicesProvided.getUser()) return "UnAuthorized";
        if(!serviceEditDto.getExperience().isEmpty()) savedServicesProvided.setExperience(serviceEditDto.getExperience());
        if(serviceEditDto.getStatus() != null) savedServicesProvided.setStatus(serviceEditDto.getStatus());
        if(!serviceEditDto.getCharge().isEmpty()) savedServicesProvided.setCharge(serviceEditDto.getCharge());
        this.serviceProvidedRepository.save(savedServicesProvided);
        return "Successfully Updated";

    }


    public List<ServicesProvided> getAllProviderServices(Users users) {
        return this.serviceProvidedRepository.findByUserId(users.getId());
    }
}
