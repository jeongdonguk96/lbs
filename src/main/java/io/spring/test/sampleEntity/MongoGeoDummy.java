package io.spring.test.sampleEntity;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MongoGeoDummy {
    private int id;
    private String nameCode;
    private Location location;
    private String useYn;
    private Timestamp create_dt;

    public void setCreate_dt() {
        Date date = new Date();
        this.create_dt = new Timestamp(date.getTime());
    }

    public void setLocation(String type, double longitude, double latitude) {
        this.location = new Location(type, new double[]{longitude, latitude});
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String type;
        private double[] coordinates;
    }
}