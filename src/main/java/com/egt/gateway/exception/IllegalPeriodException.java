package com.egt.gateway.exception;

import static com.egt.gateway.Constants.ILLEGAL_PERIOD_EXCEPTION;

public class IllegalPeriodException extends RuntimeException{

    public IllegalPeriodException(int providedPeriod, int maxPeriod){
        super(String.format(ILLEGAL_PERIOD_EXCEPTION, providedPeriod, maxPeriod));
    }
}
