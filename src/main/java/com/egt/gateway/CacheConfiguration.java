package com.egt.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.egt.gateway.Constants.CURRENCY_SYMBOLS_CACHE;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfiguration {
    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager(CURRENCY_SYMBOLS_CACHE);
    }

//    @Scheduled(cron = "")
    @CacheEvict(value = CURRENCY_SYMBOLS_CACHE, allEntries = true)
    public void clearCurrenciesCache(){
        log.info("Currencies cache has been evicted at: {}", LocalDateTime.now(ZoneOffset.UTC));
    }
}
