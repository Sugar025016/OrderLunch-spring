package com.order_lunch.entity;

import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;

import com.order_lunch.model.TimePeriod;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
// @EqualsAndHashCode(callSuper = false)
@Data
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator="auto_increment")
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id",nullable=false)
    private Integer id;

    @Column(name = "week",nullable=false)
    private int week;

    @Column(name = "start_time",nullable=false)
    private LocalTime startTime;

    @Column(name = "end_time",nullable=false)
    private LocalTime endTime;

    @Column(name = "type")
    private Integer type;

    @JsonIgnore
    @JoinColumn(name = "shop_id")
    @ManyToOne(fetch = FetchType.LAZY ,cascade=CascadeType.REFRESH)
    private Shop shop;


    public Schedule() {
    }

    public Schedule(int week,TimePeriod timePeriod ,int type , Shop shop){
        BeanUtils.copyProperties(timePeriod,this);
        this.type=type;
        this.week=week;
        this.shop=shop;
    }

    

}

