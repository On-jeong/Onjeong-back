package com.example.onjeong.Config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EmptyStringToNullConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String string) {
        // Use defaultIfEmpty to preserve Strings consisting only of whitespaces
        return "".equals(string) ? null : string;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        //If you want to keep it null otherwise transform to empty String
        return dbData;
    }
}