package com.bookingapp.service;

import static com.bookingapp.model.Accommodation.Amenity.POOL;
import static com.bookingapp.model.Accommodation.Amenity.WIFI;
import static com.bookingapp.model.Accommodation.Type.HOUSE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookingapp.dto.accommodation.AccommodationDto;
import com.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.bookingapp.exception.EntityNotFoundException;
import com.bookingapp.mapper.AccommodationMapper;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Accommodation.Amenity;
import com.bookingapp.repository.AccommodationRepository;
import com.bookingapp.service.impl.AccommodationServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {
    @Mock
    private AccommodationRepository repository;

    @Spy
    private AccommodationMapper mapper = Mappers.getMapper(AccommodationMapper.class);

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Test
    @DisplayName("Create accommodation with valid Dto")
    void createAccommodation_WithValidRequestDto_Ok() {
        AccommodationRequestDto requestDto = new AccommodationRequestDto("HOUSE", "location",
                "big", List.of("POOL", "WIFI"), BigDecimal.ONE, 1);

        Accommodation unsaved = defaultAccommodation();
        Accommodation saved = defaultAccommodation();
        saved.setId(1L);

        when(repository.save(unsaved)).thenReturn(saved);

        AccommodationDto expected = dtoFromAccommodation(saved);
        AccommodationDto actual = accommodationService.createAccommodation(requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Getting an accommodation by an existing id Ok")
    void findById_WithValidId_Ok() {
        Accommodation fromDb = defaultAccommodation();
        fromDb.setId(1L);

        when(repository.findById(anyLong())).thenReturn(Optional.of(fromDb));

        AccommodationDto expected = dtoFromAccommodation(fromDb);
        AccommodationDto actual = accommodationService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Getting an accommodation by invalid id resulted in an exception")
    void findById_InvalidId_ShouldThrowException() {
        Long invalidId = -1L;

        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> accommodationService.findById(invalidId));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, -1L})
    void deleteById_WithPositiveAndNegativeId_Ok(Long id) {

        accommodationService.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Update existing accommodation with new amenities and daily rate ok")
    void updateById_WithValidIdAndDto_Ok() {
        AccommodationRequestDto requestDto = new AccommodationRequestDto("HOUSE", "location",
                "big", List.of("POOL"), BigDecimal.TEN, 1);

        Accommodation fromDb = defaultAccommodation();
        fromDb.setId(1L);

        when(repository.findById(anyLong())).thenReturn(Optional.of(fromDb));
        fromDb.setAmenities(stringToAmenity(requestDto.amenities()));
        fromDb.setDailyRate(requestDto.dailyRate());

        when(repository.save(fromDb)).thenReturn(fromDb);

        AccommodationDto expected = dtoFromAccommodation(fromDb);
        AccommodationDto actual = accommodationService.updateById(1L, requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updating accommodation with an invalid id will throw an exception")
    void updateById_WithInvalidId_ShouldThrowException() {
        Long invalidId = -1L;

        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> accommodationService.findById(invalidId));
    }

    @Test
    void getAll_Accommodation_ShouldReturnListOfDto() {
        Accommodation accommodation = defaultAccommodation();
        accommodation.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);
        List<Accommodation> accommodations = Collections.singletonList(accommodation);
        Page<Accommodation> resultPage = new PageImpl<>(accommodations, pageable,
                accommodations.size());

        when(repository.findAllByAvailabilityGreaterThan(0, pageable)).thenReturn(resultPage);

        List<AccommodationDto> actual = accommodationService.getAll(pageable);
        System.out.println(dtoFromAccommodation(accommodation));
        System.out.println(actual);

        assertAll("actual",
                () -> assertEquals(1, actual.size()),
                () -> assertTrue(actual.contains(dtoFromAccommodation(accommodation))));
    }

    private Accommodation defaultAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setType(HOUSE);
        accommodation.setLocation("location");
        accommodation.setSize("big");
        accommodation.setAmenities(List.of(POOL, WIFI));
        accommodation.setDailyRate(BigDecimal.ONE);
        accommodation.setAvailability(1);
        return accommodation;
    }

    private AccommodationDto dtoFromAccommodation(Accommodation accommodation) {
        return new AccommodationDto(accommodation.getId(), accommodation.getType().name(),
                accommodation.getLocation(), accommodation.getSize(),
                amenitiesToString(accommodation.getAmenities()),
                accommodation.getDailyRate(), accommodation.getAvailability());
    }

    private List<String> amenitiesToString(List<Amenity> amenities) {
        return amenities.stream()
                .map(Amenity::name)
                .toList();
    }

    private List<Amenity> stringToAmenity(List<String> amenities) {
        return amenities.stream()
                .map(Amenity::valueOf)
                .collect(Collectors.toList());
    }
}
