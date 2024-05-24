package com.egt.gateway.dto.fixer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class LatestRatesResponseDto {
    private boolean success;
    private long timestamp;
    private String base;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Map<String, Double> rates = new HashMap<>();
}