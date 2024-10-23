package com.example.CityCompass.AdminController;

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

    @PatchMapping("/updatePermission/{spId}/{decision}")
    public String updatePermission(@PathVariable("spId") Integer spId, @PathVariable("decision") String decision){
        return this.adminService.updatePermission(spId ,decision);
    }


}
