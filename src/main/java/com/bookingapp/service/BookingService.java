package com.bookingapp.service;

import com.bookingapp.dto.booking.BookingDto;
import com.bookingapp.dto.booking.BookingRequestDto;
import com.bookingapp.dto.booking.BookingUpdateDto;
import com.bookingapp.model.Booking;
import com.bookingapp.model.User;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingRequestDto requestDto, User user);

    List<BookingDto> getAll(Long userId);

    BookingDto findById(Long id, Long userId);

    BookingDto updateById(Long id, Long userId, BookingUpdateDto bookingRequestDto);

    void deleteById(Long id, Long userId);

    List<BookingDto> findByUserIdAndStatus(Long userId, Booking.Status status);
}
