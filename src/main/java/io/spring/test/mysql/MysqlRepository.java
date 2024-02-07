package io.spring.test.mysql;

import io.spring.test.entity.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MysqlRepository extends JpaRepository<Dummy, Integer> {
}
