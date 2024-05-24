package com.egt.gateway.dto.fixer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SymbolResponseDto {
    private boolean success;
    private Map<String, String> symbols = new HashMap<>();
}
