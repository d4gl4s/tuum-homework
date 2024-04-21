package com.tuum.tuumhomework.converter;

import com.tuum.tuumhomework.enums.Currency;
import org.springframework.core.convert.converter.Converter;

public class StringToCurrencyConverter implements Converter<String, Currency> {
    @Override
    public Currency convert(String source) {
        try{
            return Currency.valueOf(source.toUpperCase().trim());
        }catch (IllegalArgumentException ex){
           throw new IllegalArgumentException("Invalid currency provided");
        }
    }
}
