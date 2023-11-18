package com.order_lunch.model;

import java.time.LocalTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TimePeriod {

    // @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @NonNull
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    @Min(0)
    @Max(23)
    LocalTime startTime;

    // @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @NonNull
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    @Min(0)
    @Max(23)
    LocalTime endTime;

    public TimePeriod(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }



}
