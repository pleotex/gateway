package com.egt.gateway.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeUtils {

    private DateTimeUtils(){}

    public static LocalDateTime getFromMillis(long millis){
       return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(millis),
                ZoneOffset.UTC);
    }
}
