package com.egt.gateway.service;

import com.egt.gateway.mapper.MapperUtils;
import com.egt.gateway.model.Currency;
import com.egt.gateway.repo.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.egt.gateway.Constants.CURRENCY_SYMBOLS_CACHE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyCollectorService {
    private final CurrencyRepository currencyRepository;
    private final FixerService fixerService;
    private final ObjectMapper objectMapper;

//    Could be checked on some time and a check if there are changes to be made
//    @Scheduled(cron = "${fetch.interval.cron}")
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
}
