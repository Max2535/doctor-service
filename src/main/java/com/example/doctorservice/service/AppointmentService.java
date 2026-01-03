package com.example.doctorservice.service;

import com.example.doctorservice.entity.Appointment;
import com.example.doctorservice.entity.Appointment.AppointmentStatus;
import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.entity.Patient;
import com.example.doctorservice.exception.AppointmentNotFoundException;
import com.example.doctorservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                             DoctorService doctorService,
                             PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // ============================================
    // Read Operations
    // ============================================

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        // Force initialization of lazy-loaded entities
        appointments.forEach(this::initializeLazyEntities);

        return appointments;
    }

    public Appointment getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with id: " + id));

        // Force load lazy entities
        initializeLazyEntities(appointment);

        return appointment;
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        appointments.forEach(this::initializeLazyEntities);
        return appointments;
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        appointments.forEach(this::initializeLazyEntities);
        return appointments;
    }

    public List<Appointment> getUpcomingAppointmentsByDoctor(Long doctorId) {
        List<Appointment> appointments =
            appointmentRepository.findUpcomingByDoctor(doctorId, LocalDateTime.now());
        appointments.forEach(this::initializeLazyEntities);
        return appointments;
    }

    // Helper method to force load lazy entities
    private void initializeLazyEntities(Appointment appointment) {
        if (appointment.getDoctor() != null) {
            // Trigger lazy loading
            appointment.getDoctor().getFirstName();
        }
        if (appointment.getPatient() != null) {
            // Trigger lazy loading
            appointment.getPatient().getFirstName();
        }
    }

    // ============================================
    // Write Operations
    // ============================================

    @Transactional
    public Appointment createAppointment(Long doctorId, Long patientId,
                                        Appointment appointment) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        Patient patient = patientService.getPatientById(patientId);

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        Appointment saved = appointmentRepository.save(appointment);

        // Force load for response
        initializeLazyEntities(saved);

        return saved;
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        initializeLazyEntities(updated);
        return updated;
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}