package io.spring.test.entity;

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

//    public Geospatial convertToGeospatial(Document document) {
//        Geospatial geospatial = new Geospatial();
//        geospatial.setId(Integer.parseInt(document.getObjectId("_id").toString()));
//        geospatial.setNameCode(Integer.parseInt(document.getString("name_code")));
//        Document locationDocument = (Document) document.get("location");
//        String type = locationDocument.getString("type");
//        List<Double> coordinatesList = locationDocument.get("coordinates", List.class);
//        double[] coordinates = coordinatesList.stream().mapToDouble(Double::doubleValue).toArray();
//        geospatial.setLocation(new Location(type, coordinates));
//        geospatial.setUseYn(document.getString("use_yn"));
//        geospatial.setCreate_dt((Timestamp) document.getDate("create_dt"));
//
//        return geospatial;
//    }
}