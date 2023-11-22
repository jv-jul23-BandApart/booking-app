package com.bookingapp.controller;

import com.bookingapp.dto.accommodation.AccommodationDto;
import com.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.bookingapp.service.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accommodation management", description = "Endpoints for managing accommodation")
@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
@Validated
public class AccommodationController {
    private final AccommodationService service;

    @Operation(
            summary = "Create new accommodation",
            description = "Create new accommodation. 'ADMIN' role required"
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccommodationDto createAccommodation(
            @RequestBody @Valid AccommodationRequestDto accommodationDto) {
        return service.createAccommodation(accommodationDto);
    }

    @Operation(
            summary = "Get all accommodation",
            description = """
                    Getting all available accommodations with availability greater than 0,
                    default pagination (10 books per page) and sorting"""
    )
    @GetMapping
    public List<AccommodationDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return service.getAll(pageable);
    }

    @Operation(
            summary = "Get accommodation",
            description = """
                    Get an accommodation by its id. Parameter to be specified: accommodation id""")
    @GetMapping("/{id}")
    public AccommodationDto getById(@PathVariable @Positive Long id) {
        return service.findById(id);
    }

    @Operation(
            summary = "Update accommodation",
            description = "Update accommodation with a new values"
    )
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccommodationDto updateById(@PathVariable @Positive Long id,
                                       @RequestBody @Valid AccommodationRequestDto requestDto) {
        return service.updateById(id, requestDto);
    }

    @Operation(
            summary = "Delete accommodation",
            description = "Delete accommodation. Parameters to be specified: accommodation id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteById(@PathVariable @Positive Long id) {
        service.deleteById(id);
    }
}
