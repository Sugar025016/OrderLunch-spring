package com.order_lunch.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Status {

    NOT_LOGIN( 0, "not login", "請先登入"),
    CAPTCHA_MISTAKE( 0, "captcha mistake", "請先登入"),
    OK( 200, "ok", "登入成功");

    
    Status( int key, String name, String chinese) {
        this.key = key;
        this.chinese = chinese;
        this.name = name;
    }
    

    private final int key;
    private final String name;
    private final String chinese;



    public int getKey() {
        return this.key;
    }

    public String getChinese() {
        return this.chinese;
    }

  

    public static List<Integer> getKeyByClassify(int classify) {
        List<Integer> arrayList = new ArrayList<Integer>();
        

        return arrayList;
    }

    public static Set<Integer> getBeforeByStatus(int Status) {
        Set<Integer> arrayList = new HashSet<Integer>();
        

        return arrayList;
    }

}
