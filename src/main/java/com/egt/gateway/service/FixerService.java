package com.egt.gateway.service;

import com.egt.gateway.Constants;
import com.egt.gateway.dto.fixer.LatestRatesResponseDto;
import com.egt.gateway.dto.fixer.SymbolResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FixerService {

    @Value("$fixer.api.uri")
    private String fixerUrl;

    @Value("$fixer.api.key")
    private String fixerApiKey;

    private final RestTemplate restTemplate;

    public SymbolResponseDto getSymbols(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(fixerUrl)
                .path("/symbol");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("access-key", fixerApiKey);

        HttpEntity<SymbolResponseDto> request = new HttpEntity<>(headers);

        ResponseEntity<SymbolResponseDto> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                SymbolResponseDto.class
        );

        if(response.hasBody() && response.getBody().isSuccess()){
            log.info(String.format(Constants.RETRIEVED_FIXER_RESULTS_MSG,
                    response.getBody().getSymbols().size(), "currency symbols"));
            return response.getBody();
        } else {
//            TODO: Throw exception
            throw new RuntimeException("Error");
        }
    }

    public List<LatestRatesResponseDto> getLatestRates(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(fixerUrl)
                .path("/latest");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("access-key", fixerApiKey);

        HttpEntity<LatestRatesResponseDto> request = new HttpEntity<>(headers);

        ResponseEntity<List<LatestRatesResponseDto>> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>(){}
        );

        List<LatestRatesResponseDto> latestRates = response.getBody();

        if(null != latestRates && !latestRates.isEmpty()){

//            TODO: Change log
            log.info(String.format(Constants.RETRIEVED_FIXER_RESULTS_MSG,
                    latestRates.size(), "currency symbols"));
            return latestRates;
        } else {
//            TODO: Throw exception
            throw new RuntimeException("Error");
        }
    }

    public LatestRatesResponseDto getLatestRates(String base){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(fixerUrl)
                .path("/latest")
                .queryParam("base", base);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("access-key", fixerApiKey);

        HttpEntity<LatestRatesResponseDto> request = new HttpEntity<>(headers);

        ResponseEntity<LatestRatesResponseDto> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,LatestRatesResponseDto.class);

        if(response.hasBody() && response.getBody().isSuccess()){
            log.info(String.format(Constants.RETRIEVED_FIXER_RESULTS_MSG,
                    response.getBody().getRates().size(), "currency symbols"));
            return response.getBody();
        } else {
//            TODO: Throw exception
            throw new RuntimeException("Error");
        }
    }
}
