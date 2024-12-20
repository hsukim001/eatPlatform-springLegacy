package com.eatplatform.web.util;


public class BusinessHourUtil {

    public static String[] splitBusinessHour(String businessHour) {
        if (businessHour == null || !businessHour.contains(" - ")) {
            throw new IllegalArgumentException("비정상적인 시간 포맷");
        }
        
        return businessHour.split(" - ");
    }
}