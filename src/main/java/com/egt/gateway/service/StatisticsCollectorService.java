package com.egt.gateway.service;

import com.egt.gateway.dto.json.CurrentRateJsonResponseDto;
import com.egt.gateway.exception.IllegalCurrencyException;
import com.egt.gateway.exception.IllegalPeriodException;
import com.egt.gateway.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class StatisticsCollectorService {

    private final RatesCollectorService ratesCollectorService;
    public static final int MAX_PERIOD = 48;

    public CurrentRateJsonResponseDto getCurrentRates(String baseCurrency){

        if(!isaValidBaseCurrency(baseCurrency)){
            throw new IllegalCurrencyException(baseCurrency);
        }

        List<ExchangeRate> rates = ratesCollectorService.getLatestExchangeRates(baseCurrency);
        Map<String, Double> exchangeRates = createRatesTable(rates);

        return new CurrentRateJsonResponseDto(baseCurrency, exchangeRates, rates.getFirst().getCreatedAt());
    }

    public List<CurrentRateJsonResponseDto> getHistoryRates(String baseCurrency, int period){

        if(period < 0 || period > MAX_PERIOD){
            throw new IllegalPeriodException(period, MAX_PERIOD);
        }
        if(!isaValidBaseCurrency(baseCurrency)){
            throw new IllegalCurrencyException(baseCurrency);
        }

        LocalDateTime fromTime = LocalDateTime.now(ZoneOffset.UTC).minusHours(period);

        return ratesCollectorService.getLatestExchangeRates(baseCurrency, fromTime).stream()
                .collect(groupingBy(ExchangeRate::getCreatedAt))
                .entrySet().stream()
                .map(groupedRate -> new CurrentRateJsonResponseDto(
                        baseCurrency,
                        createRatesTable(groupedRate.getValue()),
                        groupedRate.getKey()
                ))
                .collect(Collectors.toList());
    }

    private boolean isaValidBaseCurrency(String baseCurrency){
        return ratesCollectorService.getSymbols().containsKey(baseCurrency);
    }

    private Map<String, Double> createRatesTable(List<ExchangeRate> rates) {
        return rates.stream()
                .collect(Collectors.toMap(exchangeRate -> exchangeRate.getCurrencyCodeTo().getCurrencyCode(),
                        ExchangeRate::getFromToConversionRate));
    }
}
