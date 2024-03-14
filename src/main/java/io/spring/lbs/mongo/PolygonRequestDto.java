package io.spring.lbs.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolygonRequestDto {

    private String gu;
    private List<List<Double>> polygonCoordinates;

}
