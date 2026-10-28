package com.example.api.repository;

import com.example.api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByStudent_Id(Integer studentId);

    // Tìm môn học của 1 sinh viên theo tên (chứa chuỗi) và khoảng điểm (min/max có thể null)
    @Query("SELECT c FROM Course c WHERE c.student.id = :studentId"
            + " AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))"
            + " AND (:minScore IS NULL OR c.score >= :minScore)"
            + " AND (:maxScore IS NULL OR c.score <= :maxScore)")
    List<Course> search(@Param("studentId") Integer studentId,
                        @Param("name") String name,
                        @Param("minScore") Double minScore,
                        @Param("maxScore") Double maxScore);
}
