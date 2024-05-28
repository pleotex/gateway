package com.egt.gateway.rest;

import com.egt.gateway.dto.json.CurrentRateJsonResponseDto;
import com.egt.gateway.dto.xml.XmlRequestDto;
import com.egt.gateway.mapper.XmlParser;
import com.egt.gateway.service.RequestLogService;
import com.egt.gateway.service.StatisticsCollectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.egt.gateway.Constants.EXT_SERVICE_2_NAME;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/xml")
public class XmlProcessController {

    private final RequestLogService requestLogService;
    private final StatisticsCollectorService statisticsCollectorService;

    @PostMapping(value = "/command",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<?> getLatestExchangeRate(@RequestBody String request){
        log.info("Received xml request in raw format: {}", request);
//        TODO: Create xml processing service and return xml
        XmlRequestDto requestDto = XmlParser.parseXml(request, XmlRequestDto.class);

        if(null != requestDto.getCurrentDto()) {
            CurrentRateJsonResponseDto response = processCurrentRequest(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else if(null != requestDto.getHistoryDto()){
            List<CurrentRateJsonResponseDto> response = processHistoryRequest(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            return ResponseEntity.badRequest().body("Invalid xml format");
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