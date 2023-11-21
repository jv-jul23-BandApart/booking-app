package com.bookingapp.service.impl;

import com.bookingapp.dto.accommodation.AccommodationDto;
import com.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.bookingapp.exception.EntityNotFoundException;
import com.bookingapp.mapper.AccommodationMapper;
import com.bookingapp.model.Accommodation;
import com.bookingapp.repository.AccommodationRepository;
import com.bookingapp.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private static final String NOT_FOUND_MESSAGE =
            "No such accommodation with id: %d";
    private final AccommodationMapper mapper;
    private final AccommodationRepository repository;

    @Override
    public AccommodationDto createAccommodation(AccommodationRequestDto accommodationDto) {
        Accommodation accommodation = mapper.toAccommodation(accommodationDto);
        return mapper.toDto(repository.save(accommodation));
    }

    @Override
    public List<AccommodationDto> getAll(Pageable pageable) {
        return mapper.toDtoList(repository.findAllByAvailabilityGreaterThan(0));
    }

    @Override
    public AccommodationDto findById(Long id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE.formatted(id))));
    }

    @Override
    public AccommodationDto updateById(Long id, AccommodationRequestDto requestDto) {
        Accommodation accommodation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE.formatted(id)));
        mapper.updateAccommodationFromDto(requestDto, accommodation);
        return mapper.toDto(repository.save(accommodation));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
