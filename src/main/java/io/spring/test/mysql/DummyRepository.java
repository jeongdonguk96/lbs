package io.spring.test.mysql;

import io.spring.test.entity.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyRepository extends JpaRepository<Dummy, Integer> {
}
