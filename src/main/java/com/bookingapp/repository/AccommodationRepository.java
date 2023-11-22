package com.bookingapp.repository;

import com.bookingapp.model.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    Page<Accommodation> findAllByAvailabilityGreaterThan(Integer availability, Pageable pageable);
}
