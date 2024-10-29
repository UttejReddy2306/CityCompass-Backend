package com.example.CityCompass.BookServiceControllers;
import com.example.CityCompass.RequestDtos.ServiceRequestDto;
import com.example.CityCompass.RequestDtos.SlotDto;
import com.example.CityCompass.ResponseDtos.DateSlotDto;
import com.example.CityCompass.ResponseDtos.ServicesProvidedDto;
import com.example.CityCompass.ResponseDtos.TimeSlotsDto;
import com.example.CityCompass.models.*;
import com.example.CityCompass.services.BookServices.ServiceProvidedService;
import com.example.CityCompass.services.BookServices.ServiceRequestedService;
import com.example.CityCompass.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                                .build()).collect(Collectors.toList()))
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    @GetMapping("/public/getAllService")
    public List<ServicesProvidedDto> getAllService(){
        return  serviceProvidedService.getAllServices().stream().map(x -> ServicesProvidedDto.builder()
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
                                .build()).collect(Collectors.toList()))
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

    }

    @PostMapping("/all/requestService")
    public String requestService(@Valid @RequestBody ServiceRequestDto serviceRequestDto, HttpServletRequest request)
    {
        return serviceRequestedService.createRequest(serviceRequestDto,request.getAttribute("username").toString());
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
    public String updateResponse(@PathVariable("srId") Integer srId, @PathVariable("decision") String decision , HttpServletRequest request){
        return this.serviceRequestedService.updateResponse(srId,decision,request.getAttribute("username").toString());
    }

    @PostMapping("/provider/createSlot/{serviceId}")
    public String createSlot(@PathVariable("serviceId") Integer serviceId, @RequestBody List<SlotDto> SlotDtoList,HttpServletRequest request){
        return this.userService.createSlot(serviceId,SlotDtoList,request.getAttribute("username").toString());
    }

    @DeleteMapping("/provider/deleteSlot/{serviceId}/{timeSlotId}")
    public String deleteSlot(@PathVariable("serviceId") Integer serviceId,@PathVariable("timeSlotId") Integer timeSlotId ,HttpServletRequest request){
        return this.serviceRequestedService.deleteSlot(serviceId,timeSlotId, request.getAttribute("username").toString());
    }

    @GetMapping("/public/seeAvailability/{serviceId}")
    public List<DateSlot> allTimeSlots(@PathVariable("serviceId") Integer serviceId){
        return this.serviceProvidedService.getAllTimeSlots(serviceId);
    }

    @GetMapping("public/dateSlots/{serviceId}")
    public List<DateSlot> allDateTimeSlots(@PathVariable("serviceId") Integer serviceId){
        return this.serviceProvidedService.allDateTimeSlots(serviceId);
    }



}
