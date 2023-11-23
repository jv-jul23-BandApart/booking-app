package com.bookingapp.repository;

import com.bookingapp.model.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserIdAndStatus(Long userId, Booking.Status status);

    List<Booking> findAllByUserId(Long id);

    List<Booking> findAllByUserIdAndAccommodationId(Long userId, Long accommodationId);

    Optional<Booking> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT COUNT(b.id) FROM Booking b WHERE b.accommodation.id = :accommodationId
            AND ((b.checkInDate BETWEEN :checkIn AND :checkOut)
            OR (b.checkOutDate BETWEEN :checkIn AND :checkOut))
            """)
    Integer countBookingsBetweenDates(@Param("accommodationId") Long id,
                                      @Param("checkIn") LocalDate checkInDate,
                                      @Param("checkOut") LocalDate checkOutDate);
}
