package com.example.doctorservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorservice.entity.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    List<Department> findByActive(Boolean active);

    boolean existsByName(String name);

    // Fetch department with doctors (avoid N+1)
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.doctors WHERE d.id = :id")
    Optional<Department> findByIdWithDoctors(@Param("id") Long id);

    // Get departments with doctor count
    @Query("SELECT d.name, COUNT(doc) FROM Department d " +
           "LEFT JOIN d.doctors doc " +
           "GROUP BY d.name")
    List<Object[]> getDepartmentDoctorCount();
}