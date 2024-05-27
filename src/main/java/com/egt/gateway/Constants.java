package com.egt.gateway;

public class Constants {
    private Constants(){}

    public static final String RETRIEVED_FIXER_RESULTS_MSG = "Retrieved %d %s from Fixer API";
    public static final String EXT_SERVICE_1_NAME = "EXT_SERVICE_1";
    public static final String EXT_SERVICE_2_NAME = "EXT_SERVICE_2";
    public static final String DUPLICATE_REQUEST_MGG = "Request with id=%s already exists";
    public static final String ILLEGAL_PERIOD_EXCEPTION = "Provided period of %d hours is invalid. Should be between 1 and %d hours";
    public static final String ILLEGAL_CURRENCY_EXCEPTION = "Currency=%s is not in the currency list";
}