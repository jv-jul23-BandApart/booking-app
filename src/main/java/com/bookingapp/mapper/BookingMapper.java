package com.bookingapp.mapper;

import com.bookingapp.dto.booking.BookingDto;
import com.bookingapp.dto.booking.BookingRequestDto;
import com.bookingapp.dto.booking.BookingUpdateDto;
import com.bookingapp.model.Booking;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BookingMapper {

    Booking toBooking(BookingRequestDto bookingRequestDto);

    @Mapping(source = "booking.accommodation.id", target = "accommodationId")
    @Mapping(source = "booking.user.id", target = "userId")
    BookingDto toDto(Booking booking);

    void update(BookingUpdateDto requestDto,
                                    @MappingTarget Booking entity);
}
