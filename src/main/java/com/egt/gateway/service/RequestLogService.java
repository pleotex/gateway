package com.egt.gateway.service;

import com.egt.gateway.dto.RequestLogDto;
import com.egt.gateway.exception.DuplicateRequestException;
import com.egt.gateway.messaging.RabbitMqProducer;
import com.egt.gateway.model.RequestLog;
import com.egt.gateway.repo.RequestLogRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.egt.gateway.Constants.REQUEST_LOG_QUEUE_NAME;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestLogService {

    private final RequestLogRepo requestLogRepo;
    private final RabbitMqProducer rabbitMqProducer;
    private final ObjectMapper objectMapper;

    public void processRequest(String requestId, String serviceName, LocalDateTime timestamp, long clientId){
        log.info("Started processing request={} from client={} and service={}", requestId, clientId, serviceName);

        if(requestLogRepo.existsById(requestId)){
            throw new DuplicateRequestException(requestId);
        }

        RequestLogDto requestLogDto = objectMapper.convertValue(
                saveRequest(requestId, serviceName, timestamp, clientId), RequestLogDto.class);

        log.info("Request={} from client={} and service={} has been saved", requestId, clientId, serviceName);
        sendMessage(requestLogDto);
    }

    public RequestLog saveRequest(String requestId, String serviceName, LocalDateTime timestamp, long clientId) {
        RequestLog requestLog = new RequestLog();
        requestLog.setServiceName(serviceName);
        requestLog.setRequestId(requestId);
        requestLog.setTimestamp(timestamp);
        requestLog.setClientId(clientId);

        return requestLogRepo.save(requestLog);
    }

    private void sendMessage(RequestLogDto requestLog){
        try {
            String requestLogMsg = objectMapper.writeValueAsString(requestLog);
            rabbitMqProducer.sendMessage(REQUEST_LOG_QUEUE_NAME, requestLogMsg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}