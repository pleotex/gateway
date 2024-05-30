package com.egt.gateway.service;

import com.egt.gateway.dto.RatesResponseDto;
import com.egt.gateway.dto.json.CurrentRateJsonResponseDto;
import com.egt.gateway.dto.xml.XmlRequestDto;
import com.egt.gateway.exception.IllegalXmlFormatException;
import com.egt.gateway.mapper.XmlParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.egt.gateway.Constants.EXT_SERVICE_2_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class XmlProcessorService {
    private final RequestLogService requestLogService;
    private final StatisticsCollectorService statisticsCollectorService;

    public String processRequest(String request){
        XmlRequestDto requestDto = XmlParser.unmarshal(request, XmlRequestDto.class);

        if(null != requestDto.getCurrentDto()) {
            CurrentRateJsonResponseDto response = processCurrentRequest(requestDto);
            return XmlParser.marshal(response);
        }
        else if(null != requestDto.getHistoryDto()){
            List<CurrentRateJsonResponseDto> response = processHistoryRequest(requestDto);
            return XmlParser.marshal(new RatesResponseDto(response));
        }
        else {
            throw new IllegalXmlFormatException("Invalid xml format");
        }
    }

    private CurrentRateJsonResponseDto processCurrentRequest(XmlRequestDto requestDto) {
        LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);
        requestLogService.processRequest(requestDto.getId(), EXT_SERVICE_2_NAME,
                timestamp, requestDto.getCurrentDto().getConsumer());

        return statisticsCollectorService.getCurrentRates(requestDto.getCurrentDto().getCurrency());
    }

    private List<CurrentRateJsonResponseDto> processHistoryRequest(XmlRequestDto requestDto) {
        LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);
        requestLogService.processRequest(requestDto.getId(), EXT_SERVICE_2_NAME,
                timestamp, requestDto.getHistoryDto().getConsumer());

        return statisticsCollectorService.getHistoryRates(requestDto.getHistoryDto().getCurrency(),
                requestDto.getHistoryDto().getPeriod());
    }
}
