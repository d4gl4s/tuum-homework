package com.tuum.tuumhomework.converter;

import com.tuum.tuumhomework.enums.TransactionDirection;
import org.springframework.core.convert.converter.Converter;

public class StringToTransactionDirectionConverter implements Converter<String, TransactionDirection> {
    @Override
    public TransactionDirection convert(String source) {
        try{
            return TransactionDirection.valueOf(source.toUpperCase().trim());
        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Invalid transaction direction provided");
        }
    }
}
