package com.example.CityCompass.services;



import com.example.CityCompass.FindJobsDTOs.CompanyRegisterRequest;
import com.example.CityCompass.RequestDtos.*;
import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.models.UserType;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.UserRepository;
import com.example.CityCompass.services.BookServices.ServiceProvidedService;

import com.example.CityCompass.services.FindJobs.CompanyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    CompanyService companyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceProvidedService serviceProvidedService;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    public String createUser(UserCreateRequest userCreateRequest) {
        Users users = userCreateRequest.toUser(userCreateRequest);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setUserType(UserType.USER);
        this.userRepository.save(users);
        return "SUCCESSFUL";
    }

    public String verify(UserSignInRequest userSignInRequest, HttpServletResponse httpServletResponse) {
        Users users = userSignInRequest.to(userSignInRequest);
        Authentication authentication = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        if(authentication.isAuthenticated()){

            users = this.userRepository.findByUsername(users.getUsername());
            String token = this.jwtService.generateToken(users.getUsername(), users.getUserType());
            httpServletResponse.setHeader("Authorized", "Bearer "+ token);
            return token;
        }
        else return "Unauthenticated";
    }

    public Users getDetails(String username) {
        return userRepository.findByUsername(username);
    }

    public String createSpUser(SpCreateRequest spCreateRequest) {
        Users users = spCreateRequest.toUser(spCreateRequest);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setUserType(UserType.SERVICE_PROVIDER);
        users = this.userRepository.save(users);
        serviceProvidedService.createSp(users,spCreateRequest);
        return "SUCCESSFUL";
    }

    public String createAdmin(UserCreateRequest userCreateRequest) {
        Users users = userCreateRequest.toUser(userCreateRequest);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setUserType(UserType.ADMIN);
        this.userRepository.save(users);
        return "SUCCESSFUL";
    }


    public Users getUser(String username) {
        return this.userRepository.findByUsername(username);
    }


    public String createSlot(Integer serviceId, List<SlotDto> slotDtoList, String username) {
        Users users = getUser(username);
        return this.serviceProvidedService.createSlot(serviceId,slotDtoList,users);
    }




    public List<Users> getAll() {
     return userRepository.findAll();
    }

    public String  updateServiceProvidedDetails(ServiceEditDto serviceEditDto, String username)  {
        Users users = this.userRepository.findByUsername(username);
        return this.serviceProvidedService.updateDetails(serviceEditDto,users);

    }

    public List<ServicesProvided> getAllProviderServices(String username) {
        Users users = this.userRepository.findByUsername(username);
        return this.serviceProvidedService.getAllProviderServices(users);
    }

    public Users getUserId(int userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    public Users getUserType(UserType userType) {
        return this.userRepository.findByUserType(userType);
    }

    public String createJPUser(CompanyRegisterRequest companyRegisterRequest) {
        Users users = companyRegisterRequest.companyAsUser(companyRegisterRequest);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setUserType(UserType.JOB_PROVIDER);
        users = this.userRepository.save(users);
        companyService.createCompany(users,companyRegisterRequest);
        return "SUCCESSFUL";
    }


}
