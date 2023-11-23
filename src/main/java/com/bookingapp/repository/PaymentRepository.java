package com.bookingapp.repository;

import com.bookingapp.model.Payment;
import com.bookingapp.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);

    boolean existsPaymentByBookingUserAndStatus(User bookingUser, Payment.Status status);

    @Query(value = """
            SELECT * FROM payments WHERE created_at <= DATE_SUB(NOW(), interval 1 day)
            AND NOT status = 'EXPIRED'""",
            nativeQuery = true)
    List<Payment> findAllExpiredPayments();
}
