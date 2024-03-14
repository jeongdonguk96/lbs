package io.spring.lbs.vo;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RedisGeoDummy {
    private int id;
    private int nameCode;
    private double latitude;
    private double longitude;
    private String useYn;
    private Timestamp create_dt;

    public void setCreate_dt() {
        Date date = new Date();
        this.create_dt = new Timestamp(date.getTime());
    }

    public RedisGeoDummy(int id,int nameCode,double latitude,double longitude) {
        this.id = id;
        this.nameCode = nameCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}