package com.egt.gateway.dto.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentRateJsonRequestDto {

    private String requestId;
    private long timestamp;
    @JsonProperty("client")
    private long clientId;
    private String currency;
}
