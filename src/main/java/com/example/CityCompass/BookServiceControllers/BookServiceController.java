package com.example.CityCompass.BookServiceControllers;

import com.example.CityCompass.RequestDtos.DeleteTimeSlotDto;
import com.example.CityCompass.RequestDtos.ServiceEditDto;
import com.example.CityCompass.RequestDtos.ServiceRequestDto;
import com.example.CityCompass.RequestDtos.SlotDto;
import com.example.CityCompass.ResponseDtos.DateSlotDto;
import com.example.CityCompass.ResponseDtos.ServicesProvidedDto;
import com.example.CityCompass.ResponseDtos.ServicesRequestedDto;
import com.example.CityCompass.ResponseDtos.TimeSlotsDto;
import com.example.CityCompass.models.*;
import com.example.CityCompass.services.BookServices.ServiceProvidedService;
import com.example.CityCompass.services.BookServices.ServiceRequestedService;
import com.example.CityCompass.services.S3Service;
import com.example.CityCompass.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.example.CityCompass.models.Services;
import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.models.ServicesRequested;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")


@RestController
@RequestMapping("/bookServices")
public class BookServiceController {
    @Autowired
    ServiceProvidedService serviceProvidedService;

    @Autowired
    ServiceRequestedService serviceRequestedService;

    @Autowired
    UserService userService;

    @Autowired
    S3Service s3Service;

    @GetMapping("/public/getService/{service}")
    public List<ServicesProvidedDto> getService(@PathVariable("service") String service){
        return serviceProvidedService.getService(Services.valueOf(service)).stream().map(x -> ServicesProvidedDto.builder()
                .serviceId(x.getId())
                .name(x.getUser().getName())
                .serviceName(x.getService().name())
                .experience(x.getExperience())
                .charge(x.getCharge())
                .dateSlotList(x.getDateSlotList().stream().map(y -> DateSlotDto.builder()
                        .dateSlotId(y.getId())
                        .localDate(y.getLocalDate())
                        .timeSlotsDtoList(y.getTimeSlotList().stream().map(z -> TimeSlotsDto.builder()
                                .localTime(z.getStartTime())
                                .timeSlotId(z.getId())
                                .isAvailable(z.getIsAvailable())
                                .build())
                                .collect(Collectors.toList()))
                        .build())
                        .filter(z -> z.getLocalDate().isAfter(LocalDate.now()))
                        .filter(z -> !z.getTimeSlotsDtoList().isEmpty())
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    @GetMapping("/public/getAllService")
    public List<ServicesProvidedDto> getAllService(){
        return  serviceProvidedService.getAllServices().stream().map(x -> ServicesProvidedDto.builder()
                        .profilePicture(s3Service.
                                generatePresignedUrl(x.getUser().getProfilePicture(),30))
                .serviceId(x.getId())
                .name(x.getUser().getName())
                .serviceName(x.getService().name())
                .experience(x.getExperience())
                .charge(x.getCharge())
                .dateSlotList(x.getDateSlotList().stream().map(y -> DateSlotDto.builder()
                        .dateSlotId(y.getId())
                        .localDate(y.getLocalDate())
                        .timeSlotsDtoList(y.getTimeSlotList().stream().map(z -> TimeSlotsDto.builder()
                                .localTime(z.getStartTime())
                                .timeSlotId(z.getId())
                                .isAvailable(z.getIsAvailable())
                                .build())
                                .collect(Collectors.toList()))
                        .build())
                        .filter(z -> z.getLocalDate().isAfter(LocalDate.now()))
                        .filter(z -> !z.getTimeSlotsDtoList().isEmpty())
                        .collect(Collectors.toList()))
                .build())
                .filter(x -> !x.getDateSlotList().isEmpty())
                .collect(Collectors.toList());

    }

    @PostMapping(value = "/all/requestService",  consumes = "multipart/form-data")
    public String requestService( @ModelAttribute ServiceRequestDto serviceRequestDto, HttpServletRequest request)
    {
        return serviceRequestedService.createRequest(serviceRequestDto,request.getAttribute("username").toString());


    }

    @PatchMapping("/all/cancelService/{serviceId}")
    public String cancelService(@PathVariable("serviceId") Integer serviceId, HttpServletRequest request)
    {
        return serviceRequestedService.cancelRequest(serviceId,request.getAttribute("username").toString());
    }

    @GetMapping("/provider/getAllProviderRequests")
    public List<ServicesRequestedDto> getAllProviderRequests(HttpServletRequest request){
        return this.serviceRequestedService.getAllProviderRequests(request.getAttribute("username").toString())
                .stream().map(x -> ServicesRequestedDto.builder()
                        .serviceRequestedId(x.getId())
                        .profilePicture(s3Service.generatePresignedUrl(x.getRequestedUser()
                                .getProfilePicture(),30))
                        .requestedUserProblem(x.getRequestedUserProblem())
                        .charge(x.getServicesProvided().getCharge())
                        .service(x.getServicesProvided().getService())
                        .permission(x.getPermission())
                        .providerUserName(x.getName())
                        .userRequestStatus(x.getUserRequestStatus())
                        .address(x.getAddress())
                        .email(x.getEmail())
                        .number(x.getNumber())
                        .imageUrlList(x.getImageList() != null ? x.getImageList().stream()
                                .map(y -> s3Service.generatePresignedUrl(y,30)).toList() : null)
                        .requestedUserName(x.getRequestedUser().getName())
                        .localDate(x.getTimeSlot()!= null ? x.getTimeSlot().getDateSlot().getLocalDate(): null)
                        .localTime(x.getTimeSlot() != null ? x.getTimeSlot().getStartTime() : null)
                        .build())
                .filter(x -> x.getLocalTime() != null)
                .filter(x -> (x.getLocalDate().isAfter(LocalDate.now()))
                        || (x.getLocalDate().isEqual(LocalDate.now())
                        && x.getLocalTime().isAfter(LocalTime.now())))
                .collect(Collectors.toList());
    }

    @GetMapping("/all/getAllServiceRequestsByUser")
    public List<ServicesRequestedDto> getAllServicesRequestedByUser(HttpServletRequest request){
        List<ServicesRequested> servicesRequestedList = this.serviceRequestedService.getAllServicesRequestedByUser(request.getAttribute("username").toString());
        return servicesRequestedList
                .stream().map(x -> ServicesRequestedDto.builder()
                        .serviceRequestedId(x.getId())
                        .profilePicture(s3Service.generatePresignedUrl(x.getProvidedUser()
                                .getProfilePicture(),30))
                        .requestedUserProblem(x.getRequestedUserProblem())
                        .charge(x.getServicesProvided().getCharge())
                        .service(x.getServicesProvided().getService())
                        .permission(x.getPermission())
                        .providerUserName(x.getProvidedUser().getName())
                        .userRequestStatus(x.getUserRequestStatus())
                        .address(x.getAddress())
                        .email(x.getEmail())
                        .number(x.getNumber())
                        .imageUrlList(x.getImageList() != null ? x.getImageList().stream()
                                .map(y -> s3Service.generatePresignedUrl(y,30)).toList() : null)
                        .requestedUserName(x.getRequestedUser().getName())
                        .localDate(x.getTimeSlot()!= null ? x.getTimeSlot().getDateSlot().getLocalDate(): null)
                        .localTime(x.getTimeSlot() != null ? x.getTimeSlot().getStartTime() : null)
                        .build())
                .filter(x -> x.getLocalTime() != null)
                .filter(x -> !x.getUserRequestStatus().equals(UserRequestStatus.CANCELLED))
                .toList();

    }

    @PatchMapping("/provider/updateResponse/{srId}/{decision}")

    public String updateResponse(@PathVariable("srId") Integer srId, @PathVariable("decision") String decision , HttpServletRequest request){
        return this.serviceRequestedService.updateResponse(srId,decision,request.getAttribute("username").toString());
    }

    @PostMapping("/provider/createSlot/{serviceId}")
    public String createSlot(@PathVariable("serviceId") Integer serviceId, @RequestBody List<SlotDto> SlotDtoList,HttpServletRequest request){
        return this.userService.createSlot(serviceId,SlotDtoList,request.getAttribute("username").toString());
    }

    @DeleteMapping("/provider/deleteSlot")
    public String deleteSlot(@RequestBody DeleteTimeSlotDto deleteTimeSlotDto, HttpServletRequest request){
        return this.serviceRequestedService.deleteSlot(deleteTimeSlotDto, request.getAttribute("username").toString());
    }

    @GetMapping("/public/seeAvailability/{serviceId}")
    public List<DateSlot> allTimeSlots(@PathVariable("serviceId") Integer serviceId){
        return this.serviceProvidedService.getAllTimeSlots(serviceId);
    }

    @GetMapping("public/dateSlots/{serviceId}")
    public List<DateSlot> allDateTimeSlots(@PathVariable("serviceId") Integer serviceId){
        return this.serviceProvidedService.allDateTimeSlots(serviceId);
    }

    @PatchMapping("/provider/updateServiceProvidedDetails")
    public String updateServiceProvidedDetails(@RequestBody ServiceEditDto serviceEditDto, HttpServletRequest request) {
        return this.userService.updateServiceProvidedDetails(serviceEditDto,request.getAttribute("username").toString());
    }

    @GetMapping("/provider/getAllProviderServices")
    public List<ServicesProvidedDto> getAllProviderServices(HttpServletRequest request){
        return this.userService.getAllProviderServices(request.getAttribute("username").toString()).stream().map(x -> ServicesProvidedDto.builder()
                .serviceId(x.getId())
                .name(x.getUser().getName())
                .serviceName(x.getService().name())
                .experience(x.getExperience())
                .charge(x.getCharge())
                .status(x.getStatus())
                .dateSlotList(x.getDateSlotList().stream().map(y -> DateSlotDto.builder()
                        .dateSlotId(y.getId())
                        .localDate(y.getLocalDate())
                        .timeSlotsDtoList(y.getTimeSlotList().stream().map(z -> TimeSlotsDto.builder()
                                .localTime(z.getStartTime())
                                .timeSlotId(z.getId())
                                .isAvailable(z.getIsAvailable())
                                .build())
                                .collect(Collectors.toList()))
                        .build())
                        .filter( y -> !y.getTimeSlotsDtoList().isEmpty())
                        .collect(Collectors.toList()))
                .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/provider/getAllAcceptedServiceRequests")
    public List<ServicesRequestedDto> getAllAcceptedServiceRequests(HttpServletRequest request){
        return this.serviceRequestedService.getAllAcceptedServiceRequests(request.getAttribute("username").toString())
                .stream().map(x -> ServicesRequestedDto.builder()
                        .serviceRequestedId(x.getId())
                        .requestedUserProblem(x.getRequestedUserProblem())
                        .profilePicture(s3Service.generatePresignedUrl(x.getRequestedUser()
                                .getProfilePicture(),30))
                        .charge(x.getServicesProvided().getCharge())
                        .service(x.getServicesProvided().getService())
                        .permission(x.getPermission())
                        .providerUserName(x.getProvidedUser().getName())
                        .userRequestStatus(x.getUserRequestStatus())
                        .address(x.getAddress())
                        .email(x.getEmail())
                        .number(x.getNumber())
                        .imageUrlList(x.getImageList() != null ? x.getImageList().stream()
                                .map(y -> s3Service.generatePresignedUrl(y,30)).toList() : null)
                        .requestedUserName(x.getRequestedUser().getName())
                        .localDate(x.getTimeSlot()!= null ? x.getTimeSlot().getDateSlot().getLocalDate(): null)
                        .localTime(x.getTimeSlot() != null ? x.getTimeSlot().getStartTime() : null)
                        .build())
                .filter(x -> x.getLocalTime() != null)
                .collect(Collectors.toList());
    }


    @GetMapping("/provider/getRequestsCountByStatus")
    public Integer getRequestsCountByStatus(
            @RequestParam("status") Permission status,
            HttpServletRequest request) {
        return this.serviceRequestedService
                .getAllProviderRequestsByStatus(request.getAttribute("username").toString(),status).size();




    }


}
