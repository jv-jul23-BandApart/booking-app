package com.bookingapp.service.impl;

import com.bookingapp.dto.booking.BookingDto;
import com.bookingapp.dto.booking.BookingRequestDto;
import com.bookingapp.dto.booking.BookingUpdateDto;
import com.bookingapp.exception.BookingException;
import com.bookingapp.exception.EntityNotFoundException;
import com.bookingapp.mapper.BookingMapper;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Booking;
import com.bookingapp.model.User;
import com.bookingapp.repository.AccommodationRepository;
import com.bookingapp.repository.BookingRepository;
import com.bookingapp.service.BookingService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public BookingDto createBooking(BookingRequestDto bookingRequestDto, User user) {
        Accommodation accommodation = accommodationRepository.findById(
                bookingRequestDto.accommodationId())
                .orElseThrow(() -> new BookingException(
                        String.format("Accommodation with id: %d not found",
                                bookingRequestDto.accommodationId())
                ));
        validateDatesAndAvailability(accommodation,
                bookingRequestDto.checkInDate(), bookingRequestDto.checkOutDate()
        );
        Booking booking = bookingMapper.toBooking(bookingRequestDto);
        booking.setUser(user);
        booking.setAccommodation(accommodation);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> getAll(Long userId) {
        return bookingRepository.findAllByUserId(userId).stream()
                .map(bookingMapper::toDto).toList();
    }

    @Override
    public BookingDto findById(Long id, Long userId) {
        return bookingMapper.toDto(bookingRepository.findByIdAndUserId(id, userId).orElseThrow(()
                -> new EntityNotFoundException("You haven't booking with id: %d".formatted(id))));
    }

    @Transactional
    @Override
    public BookingDto updateById(Long id, Long userId, BookingUpdateDto bookingRequestDto) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId).orElseThrow(()
                -> new EntityNotFoundException("You haven't booking with id: %d".formatted(id))
        );
        validateDatesAndAvailability(booking.getAccommodation(),
                bookingRequestDto.checkInDate(), bookingRequestDto.checkOutDate()
        );
        bookingMapper.update(bookingRequestDto, booking);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteById(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId).orElseThrow(()
                -> new EntityNotFoundException("You haven't booking with id: %d".formatted(id))
        );
        bookingRepository.delete(booking);
    }

    @Override
    public List<BookingDto> findByUserIdAndStatus(Long userId, Booking.Status status) {
        return bookingRepository.findAllByUserIdAndStatus(userId, status).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    private void validateDatesAndAvailability(Accommodation accommodation,
                                              LocalDate checkInDate,LocalDate checkOutDate) {
        Integer count = bookingRepository.countBookingsBetweenDates(
                accommodation.getId(), checkInDate, checkOutDate);
        if (accommodation.getAvailability() <= count) {
            throw new BookingException("This accommodation is unavailable for this dates");
        }
    }
}
