package com.order_lunch.service;

import com.order_lunch.model.request.SchedulesRequest;

public interface ISchedulesService {


    boolean putSchedule(int userId, int shopId, SchedulesRequest schedulesRequest);


    
}
