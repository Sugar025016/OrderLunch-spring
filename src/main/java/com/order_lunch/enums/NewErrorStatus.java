package com.order_lunch.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum NewErrorStatus {

    NOT_LOGIN( 0, "not login", "請先登入"),
    CAPTCHA_MISTAKE( 411, "captcha error", "驗證碼錯誤"),
    CAPTCHA_ERROR( 411, "captcha error", "驗證碼錯誤"),
    ACCOUNT_EXISTS( 411, "account exists", "帳號已存在"),
    SHOP_DUPLICATE_NAME( 411, "shop duplicate name", "商店名稱重複"),
    ACCOUNT_OR_PASSWORD_MISTAKE( 401, "captcha error", "照號或密碼錯誤"),
    OK( 200, "ok", "登入成功");

    
    NewErrorStatus( int key, String name, String chinese) {
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

    public String getChinese(int key) {
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
