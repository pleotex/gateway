package com.egt.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentRateJsonResponseDto {
    private String baseCurrency;
    private Map<String,Double> exchangeRates;
}
