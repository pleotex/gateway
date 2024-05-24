package com.egt.gateway.mapper;

import com.egt.gateway.model.Currency;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapperUtils {

    private MapperUtils(){}

    public static List<Currency> mapCurrencies(Map<String, String> symbols) {
        List<Currency> currencies = new ArrayList<>();
        for(Map.Entry<String, String> entrySet : symbols.entrySet()) {
            currencies.add(mapCurrency(entrySet));
        }
        return currencies;
    }
    private static Currency mapCurrency(Map.Entry<String, String> entrySet){
        return new Currency(entrySet.getKey(), entrySet.getValue());
    }
}
