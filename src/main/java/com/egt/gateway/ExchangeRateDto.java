package com.egt.gateway;

import com.egt.gateway.model.Currency;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRateDto {
    private long id;
    private Currency currencyCodeFrom;
    private Currency currencyCodeTo;
    private double fromToConversionRate;
    private double toFromConversionRate;
    private LocalDateTime createdAt;
}
