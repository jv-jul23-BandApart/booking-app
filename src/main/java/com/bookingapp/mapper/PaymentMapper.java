package com.bookingapp.mapper;

import com.bookingapp.dto.payment.PaymentResponseDto;
import com.bookingapp.model.Payment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PaymentMapper {
    @Mapping(source = "booking.id", target = "bookingId")
    PaymentResponseDto toDto(Payment payment);
}
