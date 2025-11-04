package com.hms.hospital_management_system.converter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import java.util.Map;

@Converter
public class ListMapToJsonConverter implements AttributeConverter<List<Map<String,String>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Map<String,String>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert list of maps to JSON", e);
        }
    }

    @Override
    public List<Map<String,String>> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) return null;
            return objectMapper.readValue(dbData, new TypeReference<List<Map<String,String>>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to list of maps", e);
        }
    }
}
