package com.example.CityCompass.FindJobsController;

import com.amazonaws.Response;
import com.example.CityCompass.FindJobsDTOs.CompanyRegisterRequest;
import com.example.CityCompass.FindJobsDTOs.CompanyUpdateRequestDto;
import com.example.CityCompass.FindJobsDTOs.JobPostRequest;

import com.example.CityCompass.FindJobsDTOs.JobPostingResponseDto;
import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.UserType;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.services.FindJobs.CompanyService;
import com.example.CityCompass.services.FindJobs.JobPostService;
import com.example.CityCompass.services.S3Service;
import com.example.CityCompass.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/jobPostings")
public class JobPostController {

    @Autowired
    JobPostService jobPostService;

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;


    @Autowired
    S3Service s3Service;
    @PostMapping("/company/createJobPost")
    public ResponseEntity<String> createJobPosting(@RequestBody JobPostRequest jobPostRequest, HttpServletRequest request) {
        Users user = userService.getUser(request.getAttribute("username").toString());
        // Ensure that only Job_Providers can create job postings
        if (user.getUserType() != UserType.JOB_PROVIDER) {
            return ResponseEntity.badRequest().body("Only Job Providers can create job postings.");
        }

        Company company = companyService.getCompanyByUser(user);
        if (company == null) {
            return ResponseEntity.badRequest().body("User does not belong to any company.");
        }

        jobPostService.createJobPost(jobPostRequest, user, company);
        return ResponseEntity.ok("Job posting created successfully.");
    }

    @GetMapping("/public/company/{companyName}")
    public ResponseEntity<List<JobPosting>> getAllJobPostingsByCompanyName(@PathVariable("companyName") String companyName) {
        Company company = companyService.getCompanyByName(companyName);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        List<JobPosting> jobPostings = jobPostService.getJobPostingsByCompany(company);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/public/employmentType/{type}")
    public ResponseEntity<List<JobPosting>> getJobPostingsByEmploymentType(@PathVariable("type") String type) {
        try {
            List<JobPosting> jobPostings = jobPostService.getJobPostingsByEmploymentType(type);
            return ResponseEntity.ok(jobPostings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/company/update/{jobId}")
    public ResponseEntity<String> updateJobPosting(@PathVariable("jobId") Integer jobId,
                                                   @RequestBody JobPostRequest jobPostRequest,HttpServletRequest request) {
        Users user = userService.getUser(request.getAttribute("username").toString());

        if (user.getUserType() != UserType.JOB_PROVIDER) {
            return ResponseEntity.badRequest().body("Only Job Providers can update job postings.");
        }

        String result = jobPostService.updateJobPost(jobId, jobPostRequest, user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("public/company/{companyName}/title/{jobTitle}")
    public ResponseEntity<List<JobPosting>> getJobPostingsByCompanyNameAndTitle(@PathVariable("companyName") String companyName,
                                                                                @PathVariable("jobTitle") String jobTitle) {
        Company company = companyService.getCompanyByName(companyName);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        List<JobPosting> jobPostings = jobPostService.getJobPostingsByCompanyAndJobTitle(company, jobTitle);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/public/{jobId}")
    public ResponseEntity<JobPosting> getJobPostingById(@PathVariable("jobId") Integer jobId) {
        JobPosting jobPosting = jobPostService.getJobPostingById(jobId);
        if (jobPosting == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobPosting);
    }

    @GetMapping("/public/company/jobTitle/{jobTitle}")
    public ResponseEntity<List<Company>> getCompaniesByJobTitle(@PathVariable String jobTitle) {
        List<Company> companies = jobPostService.getCompaniesByJobTitle(jobTitle);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/public/getAllJobs")
    public ResponseEntity<List<JobPostingResponseDto>> getAllJobs(HttpServletRequest request){
        List<JobPosting> jobPostingList = jobPostService.getAllJobs();
        List<JobPostingResponseDto> jobPostingResponseDtoList  = jobPostingList.stream()
                .map(x -> JobPostingResponseDto.builder()
                        .jobId(x.getJobId())
                        .jobTitle(x.getJobTitle())
                        .jobApplicationList(x.getJobApplicationList())
                        .jobDescription(x.getJobDescription())
                        .postedOn(x.getPostedOn())
                        .profilePicture(s3Service.generatePresignedUrl(x.getCompany()
                                .getUser().getProfilePicture(),30))
                        .baseSalary(x.getBaseSalary())
                        .experience(x.getExperience())
                        .employmentType(x.getEmploymentType())
                        .location(x.getLocation())
                        .status(x.getStatus())
                        .isApplied(jobPostService.findIfApplied(request,x.getJobId()))
                        .isSaved(jobPostService.findIfSaved(request,x.getJobId()))
                        .build()).toList();
        return ResponseEntity.ok(jobPostingResponseDtoList);
    }

    @GetMapping("/company/getAllJobsByCompany")
    public ResponseEntity<List<JobPosting>> getAllJobsByCompany(HttpServletRequest request){
        return ResponseEntity.ok(jobPostService.getAllJobsByCompany(request.getAttribute("username").toString()));
    }

    @PatchMapping("/company/updateCompanyDetails")
    public ResponseEntity<String> updateCompanyDetails(@RequestBody CompanyUpdateRequestDto companyUpdateRequestDto, HttpServletRequest request){
        Users users = userService.getUser(request.getAttribute("username").toString());
        return ResponseEntity.ok(companyService.UpdateCompanyDetails(companyUpdateRequestDto,users));
    }


    @GetMapping("/company/allDetails")
    public ResponseEntity<Company> getCompanyDetails(HttpServletRequest request) {
        Users users = userService.getUser(request.getAttribute("username").toString());
        return ResponseEntity.ok(companyService.getCompanyDetails(users));
    }

    @GetMapping("/company/activeJobPostings")
    public ResponseEntity<Integer> getAllActiveJobPostings(HttpServletRequest request){
        Users users = userService.getUser(request.getAttribute("username").toString());
        return ResponseEntity.ok(companyService.getAllActiveJobPostingCount(users));
    }



}