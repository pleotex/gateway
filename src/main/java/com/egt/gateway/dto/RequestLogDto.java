package com.egt.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestLogDto {

    private String requestId;
    private String serviceName;
    private LocalDateTime timestamp;
    private long clientId;
}
