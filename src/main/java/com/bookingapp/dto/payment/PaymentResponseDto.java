package com.bookingapp.dto.payment;

import com.bookingapp.model.Payment;
import java.math.BigDecimal;

public record PaymentResponseDto(
        Long id,
        Payment.Status status,
        Long bookingId,
        BigDecimal amountToPay,
        String sessionId) {
}
