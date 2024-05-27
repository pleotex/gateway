package com.egt.gateway.repo;

import com.egt.gateway.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query(value= """
            SELECT er.id, er.currency_code_from, er.currency_code_to, er.from_to_conversion_rate, er.to_from_conversion_rate, er.created_at
            FROM exchange_rates er
            JOIN currencies ca ON er.currency_code_from = ca.currency_code
            JOIN currencies cb ON er.currency_code_to = cb.currency_code
            JOIN (
                SELECT currency_code_to, MAX(created_at) AS max_created_at
                FROM exchange_rates
                WHERE currency_code_from = ?1
                GROUP BY currency_code_from, currency_code_to
            ) latest
            ON er.currency_code_to = latest.currency_code_to
            AND er.created_at = latest.max_created_at
            WHERE er.currency_code_from = ?1
            """, nativeQuery = true)
    List<ExchangeRate> findLatestExchangeRatesByCurrencyFrom(String baseCurrency);

    @Query(value= """
            SELECT er.id, er.currency_code_from, er.currency_code_to, er.from_to_conversion_rate, er.to_from_conversion_rate, er.created_at
            FROM exchange_rates er
            JOIN currencies ca ON er.currency_code_from = ca.currency_code
            JOIN currencies cb ON er.currency_code_to = cb.currency_code
            WHERE er.currency_code_from = ?1
            AND er.created_at > ?2
            ORDER BY created_at
            """, nativeQuery = true)
    List<ExchangeRate> findLatestExchangeRates (String baseCurrency, LocalDateTime fromTime);
}