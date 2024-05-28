package com.egt.gateway.dto.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@Data
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlRequestDto {
    @XmlAttribute
    private String id;

    @XmlElement(name = "get")
    private XmlCurrentDto currentDto;

    @XmlElement(name = "history")
    private XmlHistoryDto historyDto;
}