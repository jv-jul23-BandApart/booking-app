package com.bookingapp.repository;

import com.bookingapp.model.Accommodation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findAllByAvailabilityGreaterThan(Integer availability);
}
