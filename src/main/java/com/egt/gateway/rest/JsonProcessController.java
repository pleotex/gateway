package com.egt.gateway.rest;

import com.egt.gateway.dto.CurrentRateJsonRequestDto;
import com.egt.gateway.dto.CurrentRateJsonResponseDto;
import com.egt.gateway.service.RequestLogService;
import com.egt.gateway.service.StatisticsCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.egt.gateway.Constants.EXT_SERVICE_1_NAME;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/json")
public class JsonProcessController {

    private final RequestLogService requestLogService;
    private final StatisticsCollectorService statisticsCollectorService;

    @PostMapping(value = "/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CurrentRateJsonResponseDto> getLatestExchangeRate(@RequestBody CurrentRateJsonRequestDto requestDto){

        requestLogService.processRequest(requestDto.getRequestId(), EXT_SERVICE_1_NAME,
                requestDto.getTimestamp(), requestDto.getClientId());

        CurrentRateJsonResponseDto response = statisticsCollectorService.getCurrentRates(requestDto.getCurrency());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
