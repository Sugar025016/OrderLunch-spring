package com.order_lunch.service.Impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order_lunch.entity.Schedule;
import com.order_lunch.entity.Shop;
import com.order_lunch.model.request.SchedulesRequest;
import com.order_lunch.repository.IScheduleRepository;
import com.order_lunch.repository.IShopRepository;
import com.order_lunch.service.ISchedulesService;

@Service
public class ScheduleService implements ISchedulesService {

    @Autowired
    IShopRepository iShopRepository;
    @Autowired
    IScheduleRepository iScheduleRepository;

    @Autowired
    ShopService shopService;

    @Transactional
    @Override
    public boolean putSchedule(int userId, int shopId, SchedulesRequest schedulesRequest) {

        Shop shop = shopService.getShop(userId, shopId);

        // List<Schedule> schedules = shop.getSchedules();

        List<Schedule> arrayList = new ArrayList<Schedule>();
        // schedules = schedules.stream().filter(v -> v.getType() ==
        // schedulesRequest.getType() &&
        // schedulesRequest.getScheduleWeeks().stream().anyMatch(w->{
        // System.out.println(w.getWeek()+"=="+v.getWeek());
        // System.out.println(w.getWeek()==v.getWeek());
        // return w.getWeek()==v.getWeek();
        // })).collect(Collectors.toList());
        final List<Integer> weekList = schedulesRequest.getScheduleWeeks().stream().map(v -> v.getWeek())
                .collect(Collectors.toList());

        iScheduleRepository.deleteByShopIdAndTypeAndWeek(shopId, schedulesRequest.getType(), weekList);
        // arrayList.addAll(schedules);

        // schedulesRequest.getScheduleWeeks().forEach(v -> {
        // if (v.getTimePeriods().size() > 0) {
        // List<Schedule> collect = v.getTimePeriods().stream()
        // .map(t -> new Schedule(v.getWeek(), t,
        // schedulesRequest.getType(),shop))
        // .collect(Collectors.toList());
        // arrayList.addAll(collect);
        // }
        // });
        // int r=0;

        // schedulesRequest.getScheduleWeeks().forEach(v->{
        // v.getTimePeriods().forEach(t->setEndTime59(t.getEndTime()));
        // });
        schedulesRequest.getScheduleWeeks().forEach(v -> {
            if (v.getTimePeriods().size() > 0) {

                // IntStream.range(0, v.getTimePeriods().size()-r)

                // .forEach(index -> {

                for (int index = 0; index < v.getTimePeriods().size(); index++) {
                    LocalTime start1 = v.getTimePeriods().get(index).getStartTime();
                    LocalTime end1 = setEndTime59(v.getTimePeriods().get(index).getEndTime());

                    for (int i = index + 1; i < v.getTimePeriods().size(); i++) {
                        LocalTime start2 = v.getTimePeriods().get(i).getStartTime();
                        LocalTime end2 = setEndTime59(v.getTimePeriods().get(i).getEndTime());

                        if (start1.compareTo(end2) <= 0 && end1.compareTo(start2) >= 0) {
                            // r--;
                            if (start1.compareTo(start2) > 0) {
                                v.getTimePeriods().get(index).setStartTime(v.getTimePeriods().get(i).getStartTime());
                            }
                            if (end1.compareTo(end2) < 0) {
                                v.getTimePeriods().get(index).setEndTime(v.getTimePeriods().get(i).getEndTime());
                            }
                            v.getTimePeriods().remove(i);
                        }

                    }
                }
                // });

                List<Schedule> collect = v.getTimePeriods().stream()
                        .map(t -> {
                            t.setEndTime(t.getEndTime());
                            return new Schedule(v.getWeek(), t,
                                    schedulesRequest.getType(), shop);
                        })
                        .collect(Collectors.toList());
                arrayList.addAll(collect);
            }
        });

        // Schedule schedule = arrayList.get(0);
        // schedule.setShop(shop);
        List<Schedule> saveAll = iScheduleRepository.saveAll(arrayList);

        // shop.setSchedules(arrayList);
        // iShopRepository.save(shop);

        // iScheduleRepository.flush();
        if (saveAll != null && !saveAll.isEmpty() && saveAll.size() == arrayList.size()) {
            // 保存成功
            for (Schedule savedSchedule : saveAll) {
                System.out.println(savedSchedule.getId());
                if (savedSchedule.getId() == 0) {
                    return false;
                }
            }
        }
        // 保存失败
        return true;

    }

    LocalTime setEndTime59(LocalTime endTime) {
        if (endTime.toString().equals("00:00")) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime localTime1 = LocalTime.parse("23:59", formatter);
            return localTime1;

        }
        return endTime;
    }

}
