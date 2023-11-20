package com.bookingapp.service;

import com.bookingapp.dto.accommodation.AccommodationDto;
import com.bookingapp.dto.accommodation.AccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationDto createAccommodation(AccommodationRequestDto accommodationDto);

    List<AccommodationDto> getAll(Pageable pageable);

    AccommodationDto findById(Long id);

    AccommodationDto updateById(Long id, AccommodationRequestDto requestDto);

    void deleteById(Long id);
}
