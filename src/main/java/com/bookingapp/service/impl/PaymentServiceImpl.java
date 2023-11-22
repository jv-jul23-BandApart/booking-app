package com.bookingapp.service.impl;

import com.bookingapp.exception.EntityNotFoundException;
import com.bookingapp.mapper.PaymentMapper;
import com.bookingapp.model.Booking;
import com.bookingapp.model.Payment;
import com.bookingapp.repository.BookingRepository;
import com.bookingapp.repository.PaymentRepository;
import com.bookingapp.service.PaymentService;
import com.bookingapp.stripe.StripeSessionProvider;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final StripeSessionProvider stripeSessionProvider;
    private final PaymentMapper paymentMapper;

    @Transactional
    @Override
    public void createPayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException("Can't find booking with this id: " + bookingId));
        Payment payment = new Payment();
        payment.setStatus(Payment.Status.PENDING);
        payment.setAmountToPay(calculateAmountToPay(booking));
        try {
            Session session = stripeSessionProvider.createSession(payment, booking);
            String status = session.getStatus();
            payment.setSessionId(session.getId());
            payment.setUrl(session.getUrl());
            Payment save = paymentRepository.save(payment);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    private BigDecimal calculateAmountToPay(Booking booking) {
        BigDecimal amountOfDays = BigDecimal.valueOf(booking.getCheckInDate()
                .datesUntil(booking.getCheckOutDate()).count());
        return booking.getAccommodation().getDailyRate().multiply(amountOfDays);
    }

}
