package io.spring.test.mysql;

import io.spring.test.entity.Geospatial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeospatialRepository extends JpaRepository<Geospatial, Integer> {
}
