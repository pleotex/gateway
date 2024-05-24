package com.egt.gateway.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConversionUtils {

    private ConversionUtils(){}

    public static double round(double number, int scale){
        return BigDecimal.valueOf(number)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }
}