package com.egt.gateway.dto;

import com.egt.gateway.dto.json.CurrentRateJsonResponseDto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "exchangeRatesList")
@XmlAccessorType(XmlAccessType.FIELD)
public class RatesResponseDto {
    @XmlElement(name = "exchangeRates", type = CurrentRateJsonResponseDto.class)
    private List<CurrentRateJsonResponseDto> exchangeRatesList = new ArrayList<>();
}
