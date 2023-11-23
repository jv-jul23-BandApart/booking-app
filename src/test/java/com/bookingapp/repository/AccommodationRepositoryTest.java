package com.bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookingapp.model.Accommodation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/delete-all-accommodation.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/add-default-accommodation.sql")
class AccommodationRepositoryTest {
    @Autowired
    private AccommodationRepository repository;

    @Test
    void findAllByAvailabilityGreaterThan_ReturnNotEmptyList() {
        Page<Accommodation> actual = repository.findAllByAvailabilityGreaterThan(
                0, PageRequest.of(0, 10)
        );
        assertAll("actual",
                () -> assertEquals(1, actual.getTotalElements()),
                () -> assertEquals(1, actual.getContent().get(0).getId()));
    }
}
