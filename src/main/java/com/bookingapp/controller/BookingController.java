package com.bookingapp.controller;

import com.bookingapp.dto.booking.BookingDto;
import com.bookingapp.dto.booking.BookingRequestDto;
import com.bookingapp.dto.booking.BookingUpdateDto;
import com.bookingapp.model.Booking;
import com.bookingapp.model.User;
import com.bookingapp.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Booking management", description = "Endpoints for booking")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @Operation(
            summary = "Create new booking",
            description = "Create new booking. 'USER' role required"
    )
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public BookingDto createBooking(@RequestBody @Valid BookingRequestDto requestDto,
                                    Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return bookingService.createBooking(requestDto, user);
    }

    @Operation(
            summary = "Get all booking for user",
            description = "Get booking for current user. 'USER' role required"
    )
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public List<BookingDto> getBookingsOfUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return bookingService.getAll(user.getId());
    }

    @Operation(
            summary = "Get 1 booking",
            description = "Get booking by id. 'USER' role required"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public BookingDto getById(@PathVariable @Positive Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return bookingService.findById(id, user.getId());
    }

    @Operation(
            summary = "Update booking",
            description = "Update booking by id. 'USER' role required"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public BookingDto updateById(@PathVariable @Positive Long id,
                                 @RequestBody @Valid BookingUpdateDto bookingUpdateDto,
                                 Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return bookingService.updateById(id, user.getId(), bookingUpdateDto);
    }

    @Operation(
            summary = "Delete booking",
            description = "Delete booking by id. 'USER' role required"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void deleteById(@PathVariable @Positive Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        bookingService.deleteById(id, user.getId());
    }

    @Operation(
            summary = "Get bookings by user id and status",
            description = "Get bookings by user id and status. 'ADMIN' role required"
    )
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BookingDto> getAllByStatusAndUserId(
            @RequestParam(name = "user_id", required = false) @Positive Long userId,
            @RequestParam(required = false) Booking.Status status) {
        return bookingService.findByUserIdAndStatus(userId, status);
    }
}
