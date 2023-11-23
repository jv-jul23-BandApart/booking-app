package com.bookingapp.service;

import com.bookingapp.model.Booking;
import com.bookingapp.model.Payment;
import com.bookingapp.repository.BookingRepository;
import com.bookingapp.repository.PaymentRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Scheduled(cron = "${cron.payments.interval}")
    private void trackExpiredStripeSession() {
        List<Payment> payments = paymentRepository.findAllExpiredPayments()
                .stream().peek(p -> p.setStatus(Payment.Status.EXPIRED)).toList();
        paymentRepository.saveAll(payments);
    }

    @Scheduled(cron = "${cron.payments.interval}")
    private void setCanceledStatus() {
        List<Booking> bookings = bookingRepository
                .findAllByCheckOutDateEqualsAndStatusIs(LocalDate.now(),
                        Booking.Status.CONFIRMED)
                .stream().peek(p -> p.setStatus(Booking.Status.CANCELED)).toList();
        bookingRepository.saveAll(bookings);
    }

    @Scheduled(cron = "${cron.payments.interval}")
    private void setExpiredStatus() {
        List<Booking> bookings = bookingRepository
                .findAllByCheckInDateEqualsAndStatusIs(LocalDate.now(),
                        Booking.Status.PENDING).stream()
                .peek(p -> p.setStatus(Booking.Status.CANCELED)).toList();
        bookingRepository.saveAll(bookings);
    }
}
