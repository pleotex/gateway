package com.egt.gateway.exception;


import static com.egt.gateway.Constants.DUPLICATE_REQUEST_MGG;

public class DuplicateRequestException extends RuntimeException{

    public DuplicateRequestException(String requestId){
        super(String.format(DUPLICATE_REQUEST_MGG, requestId));
    }
}