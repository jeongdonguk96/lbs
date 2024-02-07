package io.spring.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Dummy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int nameCode;
    private int addressCode;
    private String useYn;
    private Timestamp create_dt;

    public void setCreate_dt() {
        Date date = new Date();
        this.create_dt = new Timestamp(date.getTime());
    }
}