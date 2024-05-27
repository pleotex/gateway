package com.egt.gateway.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentRateJsonResponseDto {
    private String baseCurrency;
    private Map<String,Double> exchangeRates;
    private LocalDateTime createdAt;
}
