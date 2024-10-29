package com.example.CityCompass.repositories;

import com.example.CityCompass.models.DateSlot;
import com.example.CityCompass.models.ServicesProvided;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DateSlotRepository extends JpaRepository<DateSlot,Integer> {


    DateSlot findByServicesProvidedAndLocalDate(ServicesProvided servicesProvided, LocalDate localDate);

    @Modifying
    @Transactional
    @Query("SELECT d FROM DateSlot d " +
            "JOIN d.timeSlotList t " +
            "WHERE d.servicesProvided.id = :servicesProvidedId " +
            "AND (d.localDate > :currentDate " +
            "OR (d.localDate = :currentDate AND t.startTime >= :currentTime))")
    List<DateSlot> findDateSlotsWithAvailableTimes(
            @Param("servicesProvidedId") Integer servicesProvidedId,
            @Param("currentDate") LocalDate currentDate,
            @Param("currentTime") LocalTime currentTime);

    List<DateSlot> findByServicesProvidedId(Integer id);
}
