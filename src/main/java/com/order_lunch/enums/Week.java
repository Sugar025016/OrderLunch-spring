package com.order_lunch.enums;

public enum Week {

    SUN(0,"星期日"),
    MON(1,"星期一"),
    TUE(2,"星期二"),
    WED(3,"星期三"),
    THU(4,"星期四"),
    FRI(5,"星期五"),
    SAT(6,"星期六");


    Week(int key, String chinese){
        this.key = key;
        this.chinese = chinese;
    }

    private final int key;
    private final String chinese;

    public static Week getWeek(int i){

        switch(i){
            case 0: return SUN;
            case 1: return MON;
            case 2: return TUE;
            case 3: return WED;
            case 4: return THU;
            case 5: return FRI;
            case 6: return SAT;
            default: return null;
        }

    }

    public int getKey(){
        return this.key;
    }

    public String getChinese(){
        return this.chinese;
    }

}
