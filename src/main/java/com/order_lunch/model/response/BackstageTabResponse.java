package com.order_lunch.model.response;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.Tab;

import lombok.Getter;
import lombok.Setter;

public class BackstageTabResponse {
    

    @Getter
    @Setter
    public class Tabs {

        private Integer id;

        private String name;

        public Tabs() {

        }

        public Tabs(Tab tab) {
            BeanUtils.copyProperties(tab, this);

        }
    }
}
