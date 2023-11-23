package com.bookingapp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.bookingapp.dto.booking.BookingDto;
import com.bookingapp.dto.booking.BookingRequestDto;
import com.bookingapp.exception.EntityNotFoundException;
import com.bookingapp.mapper.BookingMapper;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Booking;
import com.bookingapp.model.Payment;
import com.bookingapp.model.User;
import com.bookingapp.repository.AccommodationRepository;
import com.bookingapp.repository.BookingRepository;
import com.bookingapp.repository.PaymentRepository;
import com.bookingapp.service.impl.BookingServiceImpl;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private NotificationService notificationService;

    @Spy
    private BookingMapper mapper = Mappers.getMapper(BookingMapper.class);

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBooking_ValidRequestAndUser_Ok() {
        Accommodation accommodation = defaultAccommodation();
        User user = defaultUser();

        final Booking unsavedBooking = defaultBooking(accommodation, user);

        Booking savedBooking = defaultBooking(accommodation, user);
        savedBooking.setId(1L);

        final BookingRequestDto requestDto = new BookingRequestDto(LocalDate.of(2023, 11, 24),
                LocalDate.of(2023, 11, 26), 1L);

        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.of(accommodation));
        when(bookingRepository.countBookingsBetweenDates(anyLong(), any(LocalDate.class),
                any(LocalDate.class))).thenReturn(1);
        when(bookingRepository.findAllByUserIdAndAccommodationId(anyLong(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(paymentRepository.existsPaymentByBookingUserAndStatus(user,
                Payment.Status.PENDING)).thenReturn(false);
        when(bookingRepository.save(unsavedBooking)).thenReturn(savedBooking);

        BookingDto expected = dtoFromBooking(savedBooking);
        BookingDto actual = bookingService.createBooking(requestDto, user);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAll_Booking_ShouldReturnListOfDto() {
        Booking booking = defaultBooking(defaultAccommodation(), defaultUser());
        booking.setId(1L);

        when(bookingRepository.findAllByUserId(anyLong())).thenReturn(List.of(booking));

        List<BookingDto> actual = bookingService.getAll(booking.getUser().getId());

        Assertions.assertAll("actual",
                () -> Assertions.assertEquals(1, actual.size()),
                () -> Assertions.assertTrue(actual.contains(dtoFromBooking(booking))));
    }

    @Test
    void findById_WithCorrectUserIdAndBookingId_Ok() {
        Booking booking = defaultBooking(defaultAccommodation(), defaultUser());
        booking.setId(1L);
        BookingDto expected = dtoFromBooking(booking);

        when(bookingRepository.findByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDto actual = bookingService.findById(1L, 1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_WithIncorrectUserIdAndBookingId_NotOk() {
        when(bookingRepository.findByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookingService.findById(10L, 10L));
    }

    private Accommodation defaultAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setAvailability(2);
        return accommodation;
    }

    private User defaultUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    private Booking defaultBooking(Accommodation accommodation, User user) {
        Booking booking = new Booking();
        booking.setCheckInDate(LocalDate.of(2023, 11, 24));
        booking.setCheckOutDate(LocalDate.of(2023, 11, 26));
        booking.setAccommodation(accommodation);
        booking.setUser(user);
        booking.setStatus(Booking.Status.PENDING);
        return booking;
    }

    private BookingDto dtoFromBooking(Booking booking) {
        return new BookingDto(booking.getId(), booking.getCheckInDate().toString(),
                booking.getCheckOutDate().toString(), booking.getAccommodation().getId(),
                booking.getUser().getId(), booking.getStatus().name());
    }
}
