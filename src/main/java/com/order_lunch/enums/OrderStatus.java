package com.order_lunch.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum OrderStatus {

    WAIT_STORE_ACCEPT(1, 11, "waitStoreAccept", "等待店家接收中"),
    STORE_RECEIPT(2, 12, "storeReceipt", "店家已接收"),
    TAKE_MEAL(2, 13, "takeMeal", "待取餐"),

    ONGOING(2, 30, "ongoing", "進行中"),

    CANCEL(9, 91, "cancel", "訂單取消"),
    STORE_NOT_ACCEPT(9, 92, "storeNotAccept", "店家未接單"),
    STORE_REFUSED(9, 93, "storeRefused", "店家拒接單"),
    OK(9, 99, "ok", "訂單完成");

    
    OrderStatus(int classify, int key, String name, String chinese) {
        this.classify = classify;
        this.key = key;
        this.chinese = chinese;
        this.name = name;
    }
    

    private final int key;
    private final int classify;
    private final String name;
    private final String chinese;

    public static OrderStatus getStatus(int i) {
        switch (i) {
            case 99:
                return OK;
            case 11:
                return WAIT_STORE_ACCEPT;
            case 12:
                return STORE_RECEIPT;
            case 13:
                return TAKE_MEAL;
            case 91:
                return CANCEL;
            case 92:
                return STORE_NOT_ACCEPT;
            case 93:
                return STORE_REFUSED;
            default:
                return null;
        }
    }

    public int getKey() {
        return this.key;
    }

    public String getChinese() {
        return this.chinese;
    }

    public int getClassify() {
        return this.classify;
    }

    

    // public static List<Integer> getKeyByClassify(int classify) {
    // List<Integer> arrayList = new ArrayList<Integer>();
    // switch (classify) {
    // case 0:
    // arrayList.add(OK.key);
    // arrayList.add(CANCEL.key);
    // arrayList.add(STORE_NOT_ACCEPT.key);
    // arrayList.add(STORE_REFUSED.key);
    // case 1:
    // arrayList.add(WAIT_STORE_ACCEPT.key);
    // case 2:
    // arrayList.add(STORE_RECEIPT.key);
    // default:
    // return null;
    // }

    // return arrayList;
    // }

    public static List<Integer> getKeyByClassify(int classify) {
        List<Integer> arrayList = new ArrayList<Integer>();
        switch (classify) {
            case 0:
                arrayList.add(OK.key);
                arrayList.add(CANCEL.key);
                arrayList.add(STORE_NOT_ACCEPT.key);
                arrayList.add(STORE_REFUSED.key);
                break;
            case 1:
                arrayList.add(WAIT_STORE_ACCEPT.key);
                break;
            case 2:
                arrayList.add(STORE_RECEIPT.key);
                break;
            default:
                return new ArrayList<Integer>();
        }

        return arrayList;
    }

    public static Set<Integer> getBeforeByStatus(int Status) {
        Set<Integer> arrayList = new HashSet<Integer>();
        switch (Status) {
            case 12:
                arrayList.add(WAIT_STORE_ACCEPT.key);
                break;
            case 13:
                arrayList.add(STORE_RECEIPT.key);
                break;
            case 91:
                arrayList.add(WAIT_STORE_ACCEPT.key);
                break;
            case 93:
                arrayList.add(WAIT_STORE_ACCEPT.key);
                break;
            case 99:
                arrayList.add(TAKE_MEAL.key);
                break;
            default:
                return new HashSet<Integer>();
        }

        return arrayList;
    }

}
