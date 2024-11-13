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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    S3Service s3Service;

    @Autowired
    EmailService emailService;

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


    public String createProfilePicture(MultipartFile file, String username) {
        Users users = userRepository.findByUsername(username);
        String filePath = s3Service.createFile(file);
        users.setProfilePicture(filePath);
        return s3Service.generatePresignedUrl(filePath,30);
    }

    public String updateProfile(String email, String number, String name, String username) {
        Users users = userRepository.findByUsername(username);
        if(userRepository.existsByEmail(email) ) return "Email already Exists";
        if( userRepository.existsByNumber(number)) return "Number Already Exists";
        if( name != null && !name.isEmpty() ) users.setName(name);
        if(email != null && !email.isEmpty()) users.setEmail(email);
        if(number != null && !number.isEmpty()) users.setNumber(number);
        userRepository.save(users);
        return "Updated Successfully";
    }

    public String updatePassword(String oldPassword, String newPassword, String username) {
        Users users = userRepository.findByUsername(username);
        String oldOne = users.getPassword();
        if(passwordEncoder.matches(oldPassword,oldOne)){
            String newOne = passwordEncoder.encode(newPassword);
            users.setPassword(newOne);
            userRepository.save(users);
            return "Password Changed Successfully";
        }
        return "Old Password is Wrong";

    }


    public String forgotPassword(String email) {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return"User not found.";
        }
        // Generate reset token
        String resetToken = jwtService.generateResetToken();
        Users user = userOptional.get();
        user.setResetToken(resetToken);
        user.setTokenExpirationDate(new Date(System.currentTimeMillis() + 3 * 60 * 1000)); // 1-hour expiry
        userRepository.save(user);

        // Send email (implement the sendEmail function)
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        emailService.sendForgotPasswordResponse(user.getEmail(), resetLink);
        return resetToken;
    }

    public boolean validateTokenAndResetPassword(String token, String newPassword) {
        Optional<Users> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            // Check if the token is expired
            if (user.getTokenExpirationDate().after(new Date())) {
                // Reset password
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetToken(null); // Clear reset token
                user.setTokenExpirationDate(null); // Clear expiry date
                userRepository.save(user);
                return true;
            } else {
                throw new IllegalArgumentException("Token expired");
            }
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

}
