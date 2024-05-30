package com.egt.gateway.rest;

import com.egt.gateway.service.XmlProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/xml")
public class XmlProcessController {

    private final XmlProcessorService xmlProcessorService;

    @PostMapping(value = "/command",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<?> getLatestExchangeRate(@RequestBody String request){
        log.info("Received xml request in raw format: {}", request);
        String response = xmlProcessorService.processRequest(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}