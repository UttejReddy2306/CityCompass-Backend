package com.example.CityCompass.controllers;

import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/getAllPendingServiceProvided")
    public List<ServicesProvided> getAllSp(){
        return this.adminService.getAllPendingSp();
    }

    @PatchMapping("/allowPermission/{spId}")
    public String allowPermission(@PathVariable("spId") Integer spId){
        return this.adminService.allowPermission(spId);
    }

    @PatchMapping("/declinePermission/{spId}")
    public String declinePermission(@PathVariable("spId") Integer spId){
        return this.adminService.declinePermission(spId);
    }
}
