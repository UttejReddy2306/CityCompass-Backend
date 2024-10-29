package com.example.CityCompass.repositories;

import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.models.DateSlot;
import com.example.CityCompass.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot,Integer> {


    boolean existsByDateSlotAndStartTime(DateSlot dateSlot, LocalTime localTime);
}
