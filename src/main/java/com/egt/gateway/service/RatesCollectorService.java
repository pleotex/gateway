package com.egt.gateway.service;

import com.egt.gateway.ExchangeRateDto;
import com.egt.gateway.dto.fixer.LatestRatesResponseDto;
import com.egt.gateway.mapper.MapperUtils;
import com.egt.gateway.messaging.RabbitMqProducer;
import com.egt.gateway.model.Currency;
import com.egt.gateway.model.ExchangeRate;
import com.egt.gateway.repo.CurrencyRepository;
import com.egt.gateway.repo.ExchangeRateRepository;
import com.egt.gateway.utils.ConversionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.egt.gateway.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatesCollectorService {

    private final CurrencyCollectorService currencyCollectorService;
    private final ExchangeRateRepository exchangeRateRepo;
    private final FixerService fixerService;
    private final ObjectMapper objectMapper;
    private final RabbitMqProducer rabbitMqProducer;

    public List<ExchangeRate> getLatestExchangeRates(String baseCurrency){
        return exchangeRateRepo.findLatestExchangeRatesByCurrencyFrom(baseCurrency);
    }

    public List<ExchangeRate> getLatestExchangeRates(String baseCurrency, LocalDateTime fromTime){
        return exchangeRateRepo.findLatestExchangeRates(baseCurrency, fromTime);
    }

//    @Scheduled(cron = "${fetch.interval.cron}")
    public void saveLatestRatesList(){
        List<LatestRatesResponseDto> exchangeRates = fixerService.getLatestRates();
//        mapping and storing logic
    }

    @Scheduled(cron = "${fetch.interval.cron}")
    public void saveLatestRates(){
        Map<String, String> currencies = currencyCollectorService.getSymbols();

//        only EUR currency can be fetched from fixer latest rates
//        List<String> euroCurrency = List.of("EUR");
        for(String baseCurrency : currencies.keySet()){
            LatestRatesResponseDto rates = fixerService.getLatestRates(baseCurrency);
            List<ExchangeRate> exchangeRates = mapExchangeRate(rates, currencies);
            exchangeRateRepo.saveAll(exchangeRates);
            log.info("{} exchange rates for {} are stored in DB", exchangeRates.size(), baseCurrency);
            sendMessage(objectMapper.convertValue(exchangeRates, new TypeReference<>(){}));
        }
    }

    private List<ExchangeRate> mapExchangeRate(LatestRatesResponseDto latestRate, Map<String, String> currencies){
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        LocalDateTime ratesCollectionTime = LocalDateTime.now(ZoneOffset.UTC).plusNanos(latestRate.getTimestamp());
        Currency baseCurrency = new Currency(latestRate.getBase(), currencies.get(latestRate.getBase()));

        for(Map.Entry<String, Double> rates : latestRate.getRates().entrySet()){

            if(rates.getValue().equals(0d)){
                throw new RuntimeException(String.format("Invalid conversion rate for %s", baseCurrency));
            }

            ExchangeRateDto exchangeRate = new ExchangeRateDto();
            Currency convertedCurrency = new Currency(rates.getKey(), currencies.get(rates.getKey()));
            exchangeRate.setCurrencyCodeFrom(baseCurrency);
            exchangeRate.setCurrencyCodeTo(convertedCurrency);
            exchangeRate.setFromToConversionRate(rates.getValue());
            exchangeRate.setToFromConversionRate(ConversionUtils.round(1/rates.getValue(), 6));
            exchangeRate.setCreatedAt(ratesCollectionTime);
            exchangeRates.add(objectMapper.convertValue(exchangeRate, ExchangeRate.class));
        }
        return exchangeRates;
    }

    private void sendMessage(List<ExchangeRateDto> exchangeRateDtos){
        try {
            String exchangeRateDtoMsg = objectMapper.writeValueAsString(exchangeRateDtos);
            rabbitMqProducer.sendMessage(EXCHANGE_RATE_QUEUE_NAME, exchangeRateDtoMsg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
