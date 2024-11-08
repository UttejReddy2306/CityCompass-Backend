package com.example.CityCompass.AdminController;


import com.example.CityCompass.ResponseDtos.DateSlotDto;
import com.example.CityCompass.ResponseDtos.ServicesProvidedDto;
import com.example.CityCompass.ResponseDtos.TimeSlotsDto;

import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/getAllPendingServiceProvided")

    public List<ServicesProvidedDto> getAllSp() {
        return this.adminService.getAllPendingSp().stream().map(x -> ServicesProvidedDto.builder()
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
    @PatchMapping("/updatePermission/{spId}/{decision}")
    public String updatePermission(@PathVariable("spId") Integer spId, @PathVariable("decision") String decision){
        return this.adminService.updatePermission(spId ,decision);
    }

    @GetMapping("/getAllPendingCompanyPermission")
    public List<Company> getAllCompanies(){ return this.adminService.getAllPendingPermission();
    }

    @PatchMapping("/updateCompanyPermission/{regId}/{decision}")
    public String updateCompanyPermission(@PathVariable("regId") Integer regId, @PathVariable("decision") String decision){
        return this.adminService.updateCompanyPermission(regId ,decision);
    }


}
