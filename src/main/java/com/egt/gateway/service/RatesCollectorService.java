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

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepo;
    private final FixerService fixerService;
    private final ObjectMapper objectMapper;
    private final RabbitMqProducer rabbitMqProducer;


    public void saveCurrencySymbols(){
        Map<String, String> symbols = fixerService.getSymbols().getSymbols();
        List<Currency> currencySymbols = MapperUtils.mapCurrencies(symbols);
        currencyRepository.saveAll(currencySymbols);
        log.info("{} currency symbols are stored in DB", currencySymbols.size());
    }

    public List<Currency> findAll(){
        return currencyRepository.findAll();
    }

    @Cacheable(value = CURRENCY_SYMBOLS_CACHE)
    public Map<String, String> getSymbols(){
        return currencyRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Currency::getCurrencyCode, Currency::getCurrencyName));
    }

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
    @Async
    public void saveLatestRates(){

        for(String baseCurrency : getSymbols().keySet()){
            LatestRatesResponseDto rates = fixerService.getLatestRates(baseCurrency);
            List<ExchangeRate> exchangeRates = mapExchangeRate(rates);
            exchangeRateRepo.saveAll(exchangeRates);
            log.info("{} exchange rates for {} are stored in DB", exchangeRates.size(), baseCurrency);
            sendMessage(objectMapper.convertValue(exchangeRates, new TypeReference<>(){}));
        }
    }

    private List<ExchangeRate> mapExchangeRate(LatestRatesResponseDto latestRate){
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        LocalDateTime ratesCollectionTime = LocalDateTime.now(ZoneOffset.UTC).plusNanos(latestRate.getTimestamp());
        Currency baseCurrency = new Currency(latestRate.getBase(), getSymbols().get(latestRate.getBase()));

        for(Map.Entry<String, Double> rates : latestRate.getRates().entrySet()){

            if(rates.getValue().equals(0d)){
                throw new RuntimeException(String.format("Invalid conversion rate for %s", baseCurrency));
            }

            ExchangeRateDto exchangeRate = new ExchangeRateDto();
            Currency convertedCurrency = new Currency(rates.getKey(), getSymbols().get(rates.getKey()));
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
