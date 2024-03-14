package io.spring.lbs.mysql;

import io.spring.lbs.entity.Geospatial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeospatialRepository extends JpaRepository<Geospatial, Integer> {
}
