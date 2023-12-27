package com.order_lunch.model.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.order_lunch.entity.Shop;
import com.order_lunch.model.AddressResponse;
import com.order_lunch.model.TimePeriod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SellShopResponse {

    private Integer id;

    private String name;
    // private String phone;
    private AddressResponse address;
    private String description;
    private int imgId;
    private String imgUrl;
    private String phone;
    private boolean isOrderable;
    private boolean isDisable;
    private List<Schedule> schedules;

    @JsonProperty("tabProducts")
    private List<TabProductResponse> tabProductResponses;
    
    @JsonProperty("products")
    private List<ProductResponse> productResponses;

    // private int qty;

    // private int total;

    // private int totalOriginPrice;
    public SellShopResponse(Shop shop) {
        BeanUtils.copyProperties(shop, this);
        address=new AddressResponse(shop.getAddress());
        if (shop.getFileData() != null) {
            this.imgId = shop.getFileData().getId();
            this.imgUrl = shop.getFileData().getFileName();
        }
        // this.address = shop.getAddress();
        List<Schedule> arrayList = new ArrayList<Schedule>();
        for (int i = 0; i < 7; i++) {
            arrayList.add(new Schedule(i));
        }

        arrayList.stream().forEach(v -> {
            List<TimePeriod> collect = shop.getSchedulesForOpen().stream()
                    .filter(v2 -> v2.getWeek() == v.getWeek() && v2.getType()==0  )
                    .map(v3 -> new TimePeriod(v3.getStartTime(), v3.getEndTime()))
                    .collect(Collectors.toList());

            v.setTimePeriods(collect);
        });
        this.schedules = arrayList;
        tabProductResponses = shop.getTabs().stream().map(v -> new TabProductResponse(v)).collect(Collectors.toList());
        productResponses = shop.getProductsForNotDelete().stream().map(v -> new ProductResponse(v)).collect(Collectors.toList());
        
    }

    public class Schedule {

        int week;

        List<TimePeriod> timePeriods;

        public Schedule(int week) {
            this.week = week;
        }

        public int getWeek() {
            return week;
        }

        public List<TimePeriod> getTimePeriods() {
            return timePeriods;
        }

        public void setTimePeriods(List<TimePeriod> timePeriods) {
            this.timePeriods = timePeriods;
        }

    }

    // public class TimePeriod {

    //     @NonNull
    //     LocalTime startTime;

    //     @NonNull
    //     LocalTime endTime;

    //     TimePeriod(LocalTime startTime, LocalTime endTime) {
    //         this.startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
    //         this.endTime = endTime.truncatedTo(ChronoUnit.MINUTES);
    //     }

    //     public LocalTime getStartTime() {
    //         return startTime;
    //     }

    //     public LocalTime getEndTime() {
    //         return endTime;
    //     }

    // }

}
