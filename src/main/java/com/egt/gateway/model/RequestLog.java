package com.egt.gateway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// If we have User/Client table we can link the tables for better information
public class RequestLog {
        @Id
        @Column(name = "request_id")
        private String requestId;

        @Column(name = "service_name")
        private String serviceName;

        @Column(name = "timestamp")
        private LocalDateTime timestamp;

        @Column(name = "client_id")
        private long clientId;
}
