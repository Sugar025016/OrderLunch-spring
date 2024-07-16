package com.order_lunch.model.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.order_lunch.model.ScheduleWeek;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SchedulesRequest {

    @NotNull
    @Min(0)
    @Max(1)
    private Integer type;

    @Valid
    @NotNull
    @JsonProperty("schedules")
    private List<ScheduleWeek> scheduleWeeks;

    public void setType(int type) {
        this.type = type; 
    }

    public void setScheduleWeeks(List<ScheduleWeek> schedules) {
        this.scheduleWeeks = schedules;
    }

    // @NoArgsConstructor
    // @Getter
    // // @Setter
    // public class ScheduleWeek {

    //     private int week;

    //     @JsonProperty("timePeriods")
    //     private List<ScheduleTime> scheduleTimes;

    //     public void setWeek(int week) {
    //         this.week = week;
    //     }

    //     public void setScheduleTimes(List<ScheduleTime> scheduleTimes) {
    //         this.scheduleTimes = scheduleTimes;
    //     }
        

    //     public ScheduleWeek(int week, List<ScheduleTime> scheduleTimes) {
    //         this.week = week;
    //         this.scheduleTimes = scheduleTimes;
    //     }


    //     @NoArgsConstructor
    //     @Getter
    //     @Setter
    //     public class ScheduleTime {

    //         @NonNull
    //         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //         private LocalTime startTime;

    //         @NonNull
    //         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //         private LocalTime endTime;

    //         public ScheduleTime(@NonNull LocalTime startTime, @NonNull LocalTime endTime) {
    //             this.startTime = startTime;
    //             this.endTime = endTime;
    //         }

            

    //     }

    // }

}
