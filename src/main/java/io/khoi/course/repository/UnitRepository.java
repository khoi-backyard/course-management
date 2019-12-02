package io.khoi.course.repository;

import io.khoi.course.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Unit findByCodeAndCourseId(String code, Long courseId);
    List<Unit> findByCourseId(Long courseId);
}
