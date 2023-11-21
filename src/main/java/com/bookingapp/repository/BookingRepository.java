package com.bookingapp.repository;

import com.bookingapp.model.Booking;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @EntityGraph(attributePaths = "user")
    List<Booking> findAllByUserId(Long id);

    Integer getAllByCheckInDateAfterAndCheckOutDateBefore(LocalDate checkInDate,
                                                          LocalDate checkOutDate);
}
