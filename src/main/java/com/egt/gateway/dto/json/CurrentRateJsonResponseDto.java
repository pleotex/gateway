package com.egt.gateway.dto.json;

import com.egt.gateway.helper.LocalDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "exchangeRates")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrentRateJsonResponseDto {
    @XmlElement
    @JsonProperty("currency")
    private String baseCurrency;
    @XmlElement
    private Map<String,Double> exchangeRates;
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}
