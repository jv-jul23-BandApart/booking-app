package com.bookingapp.service;

import com.bookingapp.model.Payment;
import com.bookingapp.repository.PaymentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final PaymentRepository repository;

    @Scheduled(cron = "${cron.payments.interval}")
    private void trackExpiredStripeSession() {
        List<Payment> payments = repository.findAllExpiredPayments()
                .stream().peek(p -> p.setStatus(Payment.Status.EXPIRED)).toList();
        repository.saveAll(payments);
    }
}
