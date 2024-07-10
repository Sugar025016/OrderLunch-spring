package com.order_lunch.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum NewErrorStatus {

    NOT_LOGIN( 0, "not login", "未登入，請先登入"),
    HAVE_LOGIN( 1, "have login", "已登入，請勿重複註冊"),
    ACCOUNT_OR_PASSWORD_MISTAKE( 401, "account or password mistake", "帳號或密碼錯誤"),
    PASSWORD_MISTAKE( 402, "password mistake", "密碼錯誤"),
    CAPTCHA_MISTAKE( 411, "captcha error", "驗證碼錯誤"),
    CAPTCHA_ERROR( 411, "captcha error", "驗證碼錯誤"),

    // ACCOUNT_EXISTS( 411, "account exists", "帳號已存在"),
    ACCOUNT_EXISTS( 405, "account exists", "帳號已存在"),


    ORDER_ERROR( 501, "order error", "訂單時間錯誤"),
    SHOP_NOT_FOUND( 611, "shop not found", "商店不存在"),

    CART_PRICE_IS_INSUFFICIENT( 612, "Cart price is insufficient", "購物車裡的商平價格不足"),
    GET_PRODUCT_TIME_ERROR( 613, "Get product time error", "取餐時間錯誤"),
    
    CART_NULL( 614, "cart null", "購物車空的"),

    // SHOP_DUPLICATE_NAME( 411, "shop duplicate name", "商店名稱重複"),
    SHOP_DUPLICATE_NAME( 701, "shop duplicate name", "商店名稱重複"),
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
