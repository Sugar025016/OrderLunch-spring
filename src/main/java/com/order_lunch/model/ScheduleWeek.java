package com.order_lunch.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ScheduleWeek {

    @NotNull
    @Min(0)
    @Max(6)
    private Integer week;

    @NotNull
    @Valid
    @JsonProperty("timePeriods")
    private List<TimePeriod> timePeriods;

    /**
     * @param schedules
     */
    // public  ScheduleWeek(Schedule schedule) {

    //     this.week = schedule.getWeek(); 
    //      this.timePeriods = schedule.stream()
    //      .map(v -> new TimePeriod(v.getStartTime(), v.getEndTime()))
    //      .collect(Collectors.toList());

    // }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setTimePeriods(List<TimePeriod> scheduleTimes) {
        this.timePeriods = scheduleTimes;
    }

    public ScheduleWeek(int week, List<TimePeriod> scheduleTimes) {
        this.week = week;
        this.timePeriods = scheduleTimes;
    }

    public ScheduleWeek(int week) {
        this.week = week;
    }
}