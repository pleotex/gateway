package com.egt.gateway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency_code_from", referencedColumnName = "currency_code", nullable = false)
    private Currency currencyCodeFrom;

    @ManyToOne
    @JoinColumn(name = "currency_code_to", referencedColumnName = "currency_code", nullable = false)
    private Currency currencyCodeTo;

    @Column(name = "from_to_conversion_rate")
    private double fromToConversionRate;

    @Column(name = "to_from_conversion_rate")
    private double toFromConversionRate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}