package com.example.CityCompass.FindJobsController;


import com.example.CityCompass.FindJobsDTOs.SaveJobRequest;
import com.example.CityCompass.FindJobsDTOs.SaveJobResponse;
import com.example.CityCompass.services.FindJobs.SaveJobsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saveJobs")
public class SaveJobsController {

    @Autowired
    SaveJobsService saveJobsService;

    @PostMapping("/users/save")
    public ResponseEntity<String> saveJob(@RequestBody SaveJobRequest saveJobRequest, HttpServletRequest request) {
        String result = saveJobsService.saveJob(saveJobRequest,request.getAttribute("username").toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    public ResponseEntity<List<SaveJobResponse>> getSavedJobsByUser(HttpServletRequest request) {
        List<SaveJobResponse> savedJobs = saveJobsService
                .getSavedJobsByUser(request.getAttribute("username").toString());
        if (savedJobs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(savedJobs);
    }
}