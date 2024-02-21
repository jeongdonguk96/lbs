package io.spring.test.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenterSphereRequestDto {

    private String place;
    private List<Double> coordinates;
    private double kmRadius;
    
}
