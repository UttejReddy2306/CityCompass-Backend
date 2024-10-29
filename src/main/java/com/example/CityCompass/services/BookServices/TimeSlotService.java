package com.example.CityCompass.services.BookServices;

import com.example.CityCompass.models.DateSlot;
import com.example.CityCompass.models.TimeSlot;
import com.example.CityCompass.repositories.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    TimeSlotRepository timeSlotRepository;


    public void createSlot(DateSlot dateSlot, List<LocalTime> localTimeList) {
        for(LocalTime localTime : localTimeList){
            if(isExist(localTime,dateSlot)) continue;
            TimeSlot timeSlot = TimeSlot.builder()
                    .isAvailable(true)
                    .dateSlot(dateSlot)
                    .startTime(localTime)
                    .build();
            timeSlotRepository.save(timeSlot);
        }

    }

    public Boolean isExist(LocalTime localTime, DateSlot dateSlot){
        return this.timeSlotRepository.existsByDateSlotAndStartTime(dateSlot, localTime);
    }


    public TimeSlot getTimeSlotById(Integer localTimeId) {
        return timeSlotRepository.findById(localTimeId).orElse(null);
    }

    public void makingItAvailable(TimeSlot timeSlot) {
        timeSlot.setIsAvailable(true);
        this.timeSlotRepository.save(timeSlot);
    }

    public TimeSlot findByTimeSlotId(Integer timeSlotId) {
        return this.timeSlotRepository.findById(timeSlotId).orElse(null);
    }

    public void deleteByTimeSlotId(TimeSlot timeSlot) {
        this.timeSlotRepository.deleteById(timeSlot.getId());
    }
}
