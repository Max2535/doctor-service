package com.example.doctorservice.repository;

import com.example.doctorservice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    List<Patient> findByActive(Boolean active);

    boolean existsByEmail(String email);

    // Search by name
    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Patient> searchByName(@Param("searchTerm") String searchTerm);

    // Find by blood group
    List<Patient> findByBloodGroup(String bloodGroup);

    // Count by active
    long countByActive(Boolean active);

    // Find patients with birthdays this month
    @Query(value = "SELECT * FROM patients WHERE MONTH(date_of_birth) = MONTH(CURRENT_DATE)",
           nativeQuery = true)
    List<Patient> findPatientsWithBirthdayThisMonth();
}