package com.example.CityCompass.services.BookServices;

import com.example.CityCompass.RequestDtos.ServiceEditDto;
import com.example.CityCompass.RequestDtos.SpCreateRequest;
import com.example.CityCompass.RequestDtos.SlotDto;
import com.example.CityCompass.models.*;
import com.example.CityCompass.repositories.ServiceProvidedRepository;
import com.example.CityCompass.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//
//    public GetStudentDetailsResponse update(UpdateStudentRequest updateStudentRequest,int studentId) {
//        Student student = updateStudentRequest.to();
//        GetStudentDetailsResponse studentDetailsResponse = this.getStudentDetails(studentId);
//        Student savedStudent = studentDetailsResponse.getStudent();
//        Student target = this.merge(student, savedStudent);
//        this.studentRepository.save(target);
//        studentDetailsResponse.setStudent(target);
//        return studentDetailsResponse;
//    }
//    public Student merge(Student student , Student savedStudent){
//        JSONObject current = mapper.convertValue(student,JSONObject.class);
//        JSONObject saved = mapper.convertValue(savedStudent,JSONObject.class);
//        Iterator itr = saved.keySet().iterator();
//        while(itr.hasNext()){
//            String key = (String)itr.next();
//            if(current.get(key) != null){
//                saved.put(key,current.get(key));
//            }
//        }
//        return mapper.convertValue(saved,Student.class);
//    }

//    public String updateServiceDetails(ServiceEditDto serviceEditDto, Users users) {
//        ServicesProvided servicesProvided = serviceEditDto.to();
//        ServicesProvided savedServicesProvided = this.serviceProvidedRepository.findById(servicesProvided.getId()).orElse(null);
//        if(users != savedServicesProvided.getUser()) return "UNAuthorized";
//        ServicesProvided target = this.merge(servicesProvided,savedServicesProvided);
//
//    }
//    public ServicesProvided merge(ServicesProvided newService,  ServicesProvided savedService ){
//        JSONObject
//    }
}
