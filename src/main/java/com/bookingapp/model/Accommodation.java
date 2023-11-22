package com.bookingapp.model;

import com.bookingapp.converter.AmenityConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "amenities")
@Table(name = "accommodations")
@SQLDelete(sql = "UPDATE accommodations SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String size;

    @Convert(converter = AmenityConverter.class)
    @Column(name = "amenities", nullable = false)
    private List<Amenity> amenities = new ArrayList<>();

    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;

    @Column(nullable = false)
    private Integer availability;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public enum Type {
        HOUSE,
        APARTMENT,
        CONDO,
        VACATION_HOME
    }

    public enum Amenity {
        PARKING,
        POOL,
        WASHING_MACHINE,
        GYM,
        WIFI,
        PETS_ALLOWED,
        BALCONY
    }
}
