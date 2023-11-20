package com.bookingapp.mapper;

import com.bookingapp.dto.accommodation.AccommodationDto;
import com.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.bookingapp.model.Accommodation;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AccommodationMapper {
    @Mapping(target = "type", source = "accommodationDto.type")
    Accommodation toAccommodation(AccommodationRequestDto accommodationDto);

    AccommodationDto toDto(Accommodation accommodation);

    List<AccommodationDto> toDtoList(List<Accommodation> accommodations);

    @Mapping(target = "type", source = "requestDto.type")
    void updateAccommodationFromDto(AccommodationRequestDto requestDto,
                                    @MappingTarget Accommodation entity);
}
