package com.order_lunch.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    // @CreatedDate
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    // @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;


    public BaseEntity() {

        // 自定義日期時間格式
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

}

