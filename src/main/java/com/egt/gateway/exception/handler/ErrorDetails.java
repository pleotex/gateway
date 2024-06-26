package com.egt.gateway.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class ErrorDetails {
    private final String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private final LocalDateTime time;

    private final String uriRequested;

    private final HttpStatus error;

    private final int status;

    public ErrorDetails(Exception e, HttpStatus error, int status, String uriRequested) {
        this.time = LocalDateTime.now(ZoneOffset.UTC);
        this.error = error;
        this.status = status;
        this.message = e.getMessage();
        this.uriRequested = uriRequested;
    }
}