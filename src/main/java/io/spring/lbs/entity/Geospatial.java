package io.spring.lbs.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Geospatial {
    @Id
    private int id;
    private int nameCode;
    @Embedded
    private Location location;
    private String useYn;
    private Date create_dt;

    @Getter
    @Setter
    @ToString
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String type;
        private String coordinates;
    }
}