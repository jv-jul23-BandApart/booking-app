package com.bookingapp.converter;

import static java.util.Collections.emptyList;

import com.bookingapp.model.Accommodation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class AmenityConverter implements AttributeConverter<List<Accommodation.Amenity>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<Accommodation.Amenity> stringList) {
        return stringList != null
                ? String.join(SPLIT_CHAR, stringList.stream().map(Enum::name).toList()) : "";
    }

    @Override
    public List<Accommodation.Amenity> convertToEntityAttribute(String string) {
        return string != null ? Arrays.stream(string.split(SPLIT_CHAR))
                .map(Accommodation.Amenity::valueOf).collect(Collectors.toList()) : emptyList();
    }
}
