package com.order_lunch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.order_lunch.entity.Schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
// @Setter
public class ScheduleWeeks {

    List<ScheduleWeek> scheduleWeeks;

    public ScheduleWeeks(List<Schedule> schedules) {
        List<ScheduleWeek> arrayList = new ArrayList<ScheduleWeek>();
        for (int i = 0; i < 7; i++) {
            arrayList.add(new ScheduleWeek(i));
        }

        arrayList.stream().forEach(v -> {
            List<TimePeriod> collect = schedules.stream()
                    .filter(v2 -> v2.getWeek() == v.getWeek())
                    .map(v3 -> new TimePeriod(v3.getStartTime(), v3.getEndTime()))
                    .collect(Collectors.toList());

            v.setTimePeriods(collect);
        });
        this.scheduleWeeks = arrayList;

    }

}