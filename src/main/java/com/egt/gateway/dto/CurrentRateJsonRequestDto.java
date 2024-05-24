package com.egt.gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentRateJsonRequestDto {

    private String requestId;
    private long timestamp;
    private long clientId;
    private String currency;
}
