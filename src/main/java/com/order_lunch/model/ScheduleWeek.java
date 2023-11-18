package com.order_lunch.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
// @Setter
public class ScheduleWeek {

    private int week;

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