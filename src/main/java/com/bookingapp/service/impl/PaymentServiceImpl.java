package com.bookingapp.service.impl;

import com.bookingapp.dto.payment.PaymentResponseDto;
import com.bookingapp.exception.EntityNotFoundException;
import com.bookingapp.exception.PaymentException;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final StripeSessionProvider stripeSessionProvider;
    private final PaymentMapper paymentMapper;

    @Transactional
    @Override
    public PaymentResponseDto createPayment(Long bookingId, Long userId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException("Can't find booking with this id: " + bookingId));

        if (!userId.equals(booking.getUser().getId())) {
            throw new PaymentException(String.format("Invalid booking id: %d for userId: %d",
                    bookingId, userId));
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setStatus(Payment.Status.PENDING);
        payment.setAmountToPay(calculateAmountToPay(booking));

        try {
            Session session = stripeSessionProvider.createSession(payment, booking);
            payment.setSessionId(session.getId());
            payment.setUrl(session.getUrl());
            return paymentMapper.toDto(paymentRepository.save(payment));
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    @Override
    public void setSuccessStatus(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Can't find payment by this session id: "));
        Booking booking = bookingRepository.findById(payment.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find payment by this session id: "));
        booking.setStatus(Booking.Status.CONFIRMED);
        payment.setStatus(Payment.Status.PAID);
    }

    @Override
    public RedirectView completePayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Cant find payment by this session id: "
                        + sessionId));

        if (payment.getStatus().equals(Payment.Status.EXPIRED)
                || payment.getStatus().equals(Payment.Status.PAID)) {
            throw new PaymentException("This payment is expired or already paid");
        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(payment.getUrl());
        return redirectView;
    }

    @Override
    public List<PaymentResponseDto> getPaymentHistory(Long userId) {
        List<Long> bookingIds = bookingRepository.findAllByUserId(userId).stream()
                .map(Booking::getId)
                .toList();

        return paymentRepository.findAllById(bookingIds).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    private BigDecimal calculateAmountToPay(Booking booking) {
        BigDecimal amountOfDays = BigDecimal.valueOf(booking.getCheckInDate()
                .datesUntil(booking.getCheckOutDate()).count());
        return booking.getAccommodation().getDailyRate().multiply(amountOfDays);
    }

}
