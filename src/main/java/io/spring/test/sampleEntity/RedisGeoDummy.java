package io.spring.test.sampleEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class RedisGeoDummy {
    private int id;
    private int nameCode;
    private Location location;
    private String useYn;
    private Timestamp create_dt;

    public void setCreate_dt() {
        Date date = new Date();
        this.create_dt = new Timestamp(date.getTime());
    }

    public void setLocation(double latitude, double longitude) {
        this.location = new Location(new double[]{latitude, longitude});
    }

    public RedisGeoDummy(String json) throws JsonProcessingException {
        this.id = dummy.id;
        this.nameCode = dummy.nameCode;
        this.location = dummy.location;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private double[] coordinates;
    }
}