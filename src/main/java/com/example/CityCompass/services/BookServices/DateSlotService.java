package com.example.CityCompass.services.BookServices;

import com.example.CityCompass.RequestDtos.SlotDto;
import com.example.CityCompass.models.DateSlot;
import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.repositories.DateSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DateSlotService {


    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    DateSlotRepository dateSlotRepository;
    public String createSlot(ServicesProvided servicesProvided, List<SlotDto> slotDtoList) {
        if(!isValidate(slotDtoList)) return "BAD INPUT";
        for(SlotDto slotDto : slotDtoList) {
            DateSlot dateSlot = findByDateAndService(servicesProvided,slotDto.getLocalDate());
            if(dateSlot == null){
                dateSlot = DateSlot.builder()
                        .servicesProvided(servicesProvided)
                        .localDate(slotDto.getLocalDate())
                        .build();
                this.dateSlotRepository.save(dateSlot);
            }
            timeSlotService.createSlot(dateSlot,slotDto.getLocalTimeList());
        }
        return "Successful";
    }

    private boolean isValidate(List<SlotDto> slotDtoList) {
        for(SlotDto slotDto : slotDtoList){
            if(slotDto.getLocalDate().isBefore(LocalDate.now())) return false;
        }
        return true;
    }

    public DateSlot findByDateAndService(ServicesProvided servicesProvided,LocalDate localDate){
        return this.dateSlotRepository.findByServicesProvidedAndLocalDate(servicesProvided,localDate);
    }

    public List<DateSlot> findDateSlotsWithAvailableTimes(Integer serviceId, LocalDate localDate, LocalTime localTime) {
        return this.dateSlotRepository.findDateSlotsWithAvailableTimes(serviceId, localDate, localTime);
    }

    public List<DateSlot> findByServicesProvidedId(Integer serviceId) {
        return this.dateSlotRepository.findByServicesProvidedId(serviceId);
    }
}
