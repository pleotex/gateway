package com.egt.gateway.service;

import com.egt.gateway.dto.CurrentRateJsonResponseDto;
import com.egt.gateway.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsCollectorService {

    private final RatesCollectorService ratesCollectorService;

    public CurrentRateJsonResponseDto getCurrentRates(String baseCurrency){

        Map<String, Double> exchangeRates = ratesCollectorService.getLatestForBaseCurrency(baseCurrency)
                .stream()
                .collect(Collectors.toMap(exchangeRate -> exchangeRate.getCurrencyCodeTo().getCurrencyCode(),
                ExchangeRate::getFromToConversionRate));

        return new CurrentRateJsonResponseDto(baseCurrency, exchangeRates);
    }
}
