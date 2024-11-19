//
//package com.example.CityCompass.configs;
//
//import com.example.CityCompass.models.UserType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class WebConfig {
//    @Autowired
//    JwtFilter jwtFilter;
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedMethods("*")
//                        .allowedOriginPatterns("*");
//            }
//        };
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(x -> x.disable())
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/user/all/details").hasAnyAuthority(UserType.USER.name(), UserType.SERVICE_PROVIDER.name(), UserType.ADMIN.name())
//                        .requestMatchers("/user/admin/**").hasAuthority(UserType.ADMIN.name())
//                        .requestMatchers("/user/public/**").permitAll())
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/bookServices/all/**").hasAnyAuthority(UserType.USER.name(), UserType.SERVICE_PROVIDER.name(), UserType.ADMIN.name())
//                        .requestMatchers("/bookServices/provider/**").hasAnyAuthority( UserType.SERVICE_PROVIDER.name())
//                        .requestMatchers("/bookServices/public/**").permitAll())
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasAuthority(UserType.ADMIN.name()))
//                .formLogin(withDefaults())
//                .httpBasic(withDefaults())
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//
//        return http.build();
//    }
//
//
//    @Bean
//    PasswordEncoder getPE (){
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }

//    @Bean
//    public ObjectMapper mapper(){
//        return new ObjectMapper();
//    }
//}

package com.example.CityCompass.configs;

import com.example.CityCompass.models.UserType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebConfig {
    @Autowired
    JwtFilter jwtFilter;

        @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOriginPatterns("*");
            }
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/user/all/details").hasAnyAuthority(UserType.USER.name(), UserType.SERVICE_PROVIDER.name(), UserType.ADMIN.name(),UserType.JOB_PROVIDER.name())
                        .requestMatchers("/user/admin/**").hasAuthority(UserType.ADMIN.name())
                        .requestMatchers("/user/public/**").permitAll()
                        .requestMatchers("/user/all/**").hasAnyAuthority(UserType.USER.name(), UserType.SERVICE_PROVIDER.name(), UserType.ADMIN.name(),UserType.JOB_PROVIDER.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/bookServices/all/**").hasAnyAuthority(UserType.USER.name(), UserType.SERVICE_PROVIDER.name(), UserType.ADMIN.name())
                        .requestMatchers("/bookServices/provider/**").hasAnyAuthority( UserType.SERVICE_PROVIDER.name())
                        .requestMatchers("/bookServices/public/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasAuthority(UserType.ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/posts/**").hasAnyAuthority(UserType.USER.name(),UserType.ADMIN.name(),UserType.SERVICE_PROVIDER.name(),UserType.JOB_PROVIDER.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/comments/**").hasAnyAuthority(UserType.USER.name(),UserType.ADMIN.name(),UserType.SERVICE_PROVIDER.name(),UserType.JOB_PROVIDER.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/likes/**").hasAnyAuthority(UserType.USER.name(),UserType.ADMIN.name(),UserType.SERVICE_PROVIDER.name(),UserType.JOB_PROVIDER.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/jobPostings/all/**").hasAnyAuthority(UserType.USER.name(), UserType.JOB_PROVIDER.name(), UserType.ADMIN.name())
                        .requestMatchers("/jobPostings/company/**").hasAuthority( UserType.JOB_PROVIDER.name())
                        .requestMatchers("/jobPostings/public/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/jobApplications/all/**").hasAnyAuthority(UserType.USER.name(), UserType.JOB_PROVIDER.name(), UserType.ADMIN.name())
                        .requestMatchers("/jobApplications/company/**").hasAuthority( UserType.JOB_PROVIDER.name())
                        .requestMatchers("/jobApplications/users/**").hasAuthority(UserType.USER.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/saveJobs/all/**").hasAnyAuthority(UserType.USER.name(), UserType.JOB_PROVIDER.name(), UserType.ADMIN.name())
                        .requestMatchers("/saveJobs/users/**").hasAuthority( UserType.USER.name()))
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


    @Bean
    PasswordEncoder getPE (){
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}



