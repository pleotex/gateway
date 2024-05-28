package com.egt.gateway.dto.xml;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlHistoryDto {
    @XmlAttribute
    private long consumer;

    @XmlAttribute
    private String currency;

    @XmlAttribute
    private int period;
}
