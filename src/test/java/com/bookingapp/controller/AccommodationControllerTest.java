package com.bookingapp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookingapp.dto.accommodation.AccommodationDto;
import com.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.bookingapp.errors.ErrorDto;
import com.bookingapp.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:database/delete-all-accommodation.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/add-default-accommodation.sql")
class AccommodationControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createAccommodation_WithValidDto_Ok() throws Exception {
        AccommodationRequestDto requestDto = new AccommodationRequestDto("HOUSE", "location", "big",
                List.of("WIFI"), BigDecimal.TEN, 2);

        MvcResult result = mockMvc.perform(post("/accommodations")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        AccommodationDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AccommodationDto.class
        );
        assertAll("actual",
                () -> assertEquals(requestDto.type(), actual.type()),
                () -> assertEquals(requestDto.location(), actual.location()),
                () -> assertEquals(requestDto.size(), actual.size()),
                () -> assertIterableEquals(requestDto.amenities(), actual.amenities()),
                () -> assertEquals(requestDto.dailyRate(), actual.dailyRate()),
                () -> assertEquals(requestDto.availability(), actual.availability()),
                () -> assertNotNull(actual.id()));
    }

    @ParameterizedTest
    @MethodSource("invalidRequestDto")
    void createAccommodation_WithInvalidRequestDto_NotOk(AccommodationRequestDto requestDto,
                                                         String errorMessage) throws Exception {
        MvcResult result = mockMvc.perform(post("/accommodations")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ErrorDto.class);
        List<String> errorMessages = (List<String>) errorDto.errorPayload();
        assertTrue(errorMessages.get(0).contains(errorMessage),
                errorMessages.get(0));
    }

    @Test
    void getAll_ValidRequest_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<AccommodationDto> actual = Arrays.stream(objectMapper.readValue(result.getResponse()
                .getContentAsString(), AccommodationDto[].class)).toList();
        AccommodationDto expected = new AccommodationDto(1L, "HOUSE", "location", "big",
                List.of("WIFI"), BigDecimal.valueOf(10.99), 2);

        assertAll("actual",
                () -> assertEquals(actual.size(), 1),
                () -> assertEquals(actual.get(0), expected));
    }

    @Test
    void getById_WithValidId_Ok() throws Exception {
        long id = 1L;

        AccommodationDto expected = new AccommodationDto(1L, "HOUSE", "location", "big",
                List.of("WIFI"), BigDecimal.valueOf(10.99), 2);

        MvcResult result = mockMvc.perform(get("/accommodations/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AccommodationDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AccommodationDto.class
        );

        assertEquals(expected, actual);
    }

    @Test
    void getById_InvalidId_NotOk() throws Exception {
        long id = 2L;

        mockMvc.perform(get("/accommodations/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result1 -> assertTrue(result1.getResolvedException()
                        instanceof EntityNotFoundException))
                .andExpect(result1 -> assertEquals("No such accommodation with id: %d"
                                .formatted(id),
                        result1.getResolvedException().getMessage()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateById_WithValidIdAndDto_Ok() throws Exception {
        AccommodationRequestDto requestDto = new AccommodationRequestDto("APARTMENT", "location",
                "small", List.of("BALCONY", "WIFI"), BigDecimal.valueOf(10.99), 1);
        long id = 1L;

        MvcResult result = mockMvc.perform(put("/accommodations/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var expected = new AccommodationDto(1L, requestDto.type(), requestDto.location(),
                requestDto.size(), requestDto.amenities(),
                requestDto.dailyRate(), requestDto.availability());

        var actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                AccommodationDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void updateById_ByNonExistingId_ThrowAnException() throws Exception {
        AccommodationRequestDto requestDto = new AccommodationRequestDto("APARTMENT", "location",
                "small", List.of("BALCONY", "WIFI"), BigDecimal.valueOf(10.99), 1);
        long id = 2L;

        mockMvc.perform(put("/accommodations/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result1 -> assertTrue(result1.getResolvedException()
                        instanceof EntityNotFoundException))
                .andExpect(result1 -> assertEquals("No such accommodation with id: %d"
                                .formatted(id),
                        result1.getResolvedException().getMessage()));
    }

    @ParameterizedTest
    @MethodSource("invalidRequestDto")
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void updateById_WithInvalidRequest_NotOk(AccommodationRequestDto requestDto,
                                             String errorMessage) throws Exception {
        MvcResult result = mockMvc.perform(put("/accommodations/" + 1)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                ErrorDto.class);
        List<String> errorMessages = (List<String>) errorDto.errorPayload();
        assertTrue(errorMessages.get(0).contains(errorMessage),
                errorMessages.get(0));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void deleteById_ExistingId_Ok() throws Exception {
        mockMvc.perform(delete("/accommodations/" + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void deleteById_NonExistingId_Ok() throws Exception {
        mockMvc.perform(delete("/accommodations/" + 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private static Stream<Arguments> invalidRequestDto() {
        return Stream.of(
                Arguments.of(new AccommodationRequestDto(null, "loc", "big",
                        List.of("WIFI"), BigDecimal.TEN, 2), "Type should not be blank or empty"),
                Arguments.of((new AccommodationRequestDto("HOUSE", null, "big",
                        List.of("WIFI"), BigDecimal.TEN, 2)),
                        "Location should not be blank or empty"),
                Arguments.of(new AccommodationRequestDto("HOUSE", "loc", null,
                        List.of("WIFI"), BigDecimal.TEN, 2), "Size should not be blank or empty"),
                Arguments.of(new AccommodationRequestDto("HOUSE", "loc", "big",
                        List.of("WIFI"), BigDecimal.ZERO, 2), "Only positive value allowed"),
                Arguments.of(new AccommodationRequestDto("HOUSE", "loc", "big",
                        List.of("WIFI"), null, 2), "Daily rate must not be null"),
                Arguments.of(new AccommodationRequestDto("HOUSE", "loc", "big",
                        List.of("WIFI"), BigDecimal.TEN, 0), "Only positive value allowed"),
                Arguments.of(new AccommodationRequestDto("HOUSE", "loc", "big",
                        List.of("WIFI"), BigDecimal.TEN, null), "Availability must not be null")
        );
    }
}
