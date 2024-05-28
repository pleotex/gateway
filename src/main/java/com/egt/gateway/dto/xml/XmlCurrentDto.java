package com.egt.gateway.dto.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCurrentDto {
    @XmlAttribute
    private long consumer;

    @XmlElement(name = "currency")
    private String currency;
}
