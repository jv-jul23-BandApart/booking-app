package com.bookingapp.repository;

import com.bookingapp.model.Payment;
import java.util.Optional;

import com.bookingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);

    boolean existsPaymentByBookingUserAndStatus(User bookingUser, Payment.Status status);
}
