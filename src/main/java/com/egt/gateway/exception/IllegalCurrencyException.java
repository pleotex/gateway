package com.egt.gateway.exception;

import static com.egt.gateway.Constants.ILLEGAL_CURRENCY_EXCEPTION;

public class IllegalCurrencyException extends RuntimeException{

    public IllegalCurrencyException(String currency){
        super(String.format(ILLEGAL_CURRENCY_EXCEPTION,currency));
    }
}
