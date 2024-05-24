package com.egt.gateway.repo;


import com.egt.gateway.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestLogRepo extends JpaRepository<RequestLog, String> {
    Optional<RequestLog> findByRequestId(String requestId);
}