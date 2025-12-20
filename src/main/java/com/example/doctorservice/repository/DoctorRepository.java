package com.example.doctorservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorservice.entity.Doctor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // ============================================
    // Query Methods (Spring Data JPA จะ generate SQL ให้)
    // ============================================

    // Find by email
    Optional<Doctor> findByEmail(String email);

    // Find by license number
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    // Find by specialization
    List<Doctor> findBySpecialization(String specialization);

    // Find by active status
    List<Doctor> findByActive(Boolean active);

    // Find by specialization and active
    List<Doctor> findBySpecializationAndActive(String specialization, Boolean active);

    // Find by first name or last name (case insensitive)
    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    // Find by years of experience greater than
    List<Doctor> findByYearsOfExperienceGreaterThan(Integer years);

    // Find by consultation fee between
    List<Doctor> findByConsultationFeeBetween(Double minFee, Double maxFee);

    // Find by joined date after
    List<Doctor> findByJoinedDateAfter(LocalDate date);

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if license number exists
    boolean existsByLicenseNumber(String licenseNumber);

    // Count by specialization
    long countBySpecialization(String specialization);

    // Count active doctors
    long countByActive(Boolean active);

    // Delete by email
    void deleteByEmail(String email);

    // ============================================
    // Custom JPQL Queries
    // ============================================

    // Find doctors by specialization (custom JPQL)
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization AND d.active = true")
    List<Doctor> findActiveDoctorsBySpecialization(@Param("specialization") String specialization);

    // Search by name
    @Query("SELECT d FROM Doctor d WHERE " +
           "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Doctor> searchByName(@Param("searchTerm") String searchTerm);

    // Find top doctors by consultation fee
    @Query("SELECT d FROM Doctor d WHERE d.active = true ORDER BY d.consultationFee DESC")
    List<Doctor> findTopDoctorsByFee();

    // Find doctors with experience range
    @Query("SELECT d FROM Doctor d WHERE d.yearsOfExperience BETWEEN :minYears AND :maxYears")
    List<Doctor> findByExperienceRange(@Param("minYears") Integer minYears,
                                       @Param("maxYears") Integer maxYears);

    // Get specialization list with count
    @Query("SELECT d.specialization, COUNT(d) FROM Doctor d GROUP BY d.specialization")
    List<Object[]> getSpecializationStatistics();

    // ============================================
    // Native SQL Queries
    // ============================================

    // Find doctors joined in specific year (Native SQL)
    @Query(value = "SELECT * FROM doctors WHERE YEAR(joined_date) = :year",
           nativeQuery = true)
    List<Doctor> findDoctorsJoinedInYear(@Param("year") int year);

    // Get average consultation fee by specialization
    @Query(value = "SELECT specialization, AVG(consultation_fee) as avg_fee " +
                   "FROM doctors " +
                   "WHERE active = true " +
                   "GROUP BY specialization",
           nativeQuery = true)
    List<Object[]> getAverageConsultationFeeBySpecialization();

    // Find doctors with birthdays in current month
    @Query(value = "SELECT * FROM doctors WHERE MONTH(date_of_birth) = MONTH(CURRENT_DATE)",
           nativeQuery = true)
    List<Doctor> findDoctorsWithBirthdayThisMonth();
}