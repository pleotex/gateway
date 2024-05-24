package com.egt.gateway.service;

import com.egt.gateway.exception.DuplicateRequestException;
import com.egt.gateway.model.RequestLog;
import com.egt.gateway.repo.RequestLogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.egt.gateway.utils.DateTimeUtils.getFromMillis;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestLogService {

    private final RequestLogRepo requestLogRepo;

    public void processRequest(String requestId, String serviceName, long timestamp, long clientId){
        log.info("Started processing request={} from client={} and service={}", requestId, clientId, serviceName);

        if(requestLogRepo.existsById(requestId)){
            throw new DuplicateRequestException(requestId);
        }

        saveRequest(requestId, serviceName, timestamp, clientId);
    }

    public void saveRequest(String requestId, String serviceName, long timestamp, long clientId) {
        RequestLog requestLog = new RequestLog();
        requestLog.setServiceName(serviceName);
        requestLog.setRequestId(requestId);
        requestLog.setTimestamp(getFromMillis(timestamp));
        requestLog.setClientId(clientId);

        requestLogRepo.save(requestLog);
        log.info("Request={} from client={} and service={} has been saved", requestId, clientId, serviceName);
    }
}