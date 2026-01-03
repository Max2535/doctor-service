package com.example.doctorservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorservice.entity.Appointment;
import com.example.doctorservice.entity.Appointment.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find by doctor
    List<Appointment> findByDoctorId(Long doctorId);

    // Find by patient
    List<Appointment> findByPatientId(Long patientId);

    // Find by status
    List<Appointment> findByStatus(AppointmentStatus status);

    // Find by doctor and status
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // Find upcoming appointments for doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDateTime > :now " +
           "ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findUpcomingByDoctor(@Param("doctorId") Long doctorId,
                                           @Param("now") LocalDateTime now);

    // Find appointments in date range
    @Query("SELECT a FROM Appointment a WHERE " +
           "a.appointmentDateTime BETWEEN :start AND :end " +
           "ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findByDateRange(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    // Find appointments with doctor and patient (avoid N+1)
    @Query("SELECT a FROM Appointment a " +
           "JOIN FETCH a.doctor " +
           "JOIN FETCH a.patient " +
           "WHERE a.id = :id")
    Appointment findByIdWithDoctorAndPatient(@Param("id") Long id);

    // Count appointments by status
    long countByStatus(AppointmentStatus status);
}