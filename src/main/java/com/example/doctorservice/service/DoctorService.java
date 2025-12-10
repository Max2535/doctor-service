package com.example.doctorservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorservice.config.AppProperties;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.repository.DoctorRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class DoctorService {

    private final AppProperties appProperties;
    private final EmailService emailService;
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(AppProperties appProperties,
            EmailService emailService,
            DoctorRepository doctorRepository) {
        this.appProperties = appProperties;
        this.emailService = emailService;
        this.doctorRepository = doctorRepository;
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Get all doctors with optional filtering
    public List<Doctor> getAllDoctors(String specialization, Boolean active, String search) {
        return doctorRepository.findAll().stream()
                .filter(d -> {
                    if (specialization == null || specialization.isBlank()) return true;
                    return d.getSpecialization() != null && d.getSpecialization().equalsIgnoreCase(specialization);
                })
                .filter(d -> {
                    if (active == null) return true;
                    return d.isActive() == active;
                })
                .filter(d -> {
                    if (search == null || search.isBlank()) return true;
                    String term = search.toLowerCase();
                    if (d.getFirstName() != null && d.getFirstName().toLowerCase().contains(term)) return true;
                    if (d.getLastName() != null && d.getLastName().toLowerCase().contains(term)) return true;
                    if (d.getEmail() != null && d.getEmail().toLowerCase().contains(term)) return true;
                    if (d.getSpecialization() != null && d.getSpecialization().toLowerCase().contains(term)) return true;
                    return false;
                })
                .collect(Collectors.toList());
    }

    // Get doctor by ID
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    // Get doctors by specialization
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    // Get active doctors
    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByActive(true);
    }

    // Search doctors by name
    public List<Doctor> searchDoctors(String name) {
        return doctorRepository.searchByName(name);
    }

    // Create new doctor
    public Doctor createDoctor(Doctor doctor) {
        // Business validation
        if (doctor.getFirstName() == null || doctor.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (doctor.getLastName() == null || doctor.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (doctor.getSpecialization() == null || doctor.getSpecialization().isBlank()) {
            throw new IllegalArgumentException("Specialization is required");
        }

        // Check duplicate email
        if (doctor.getEmail() != null &&
                doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        return doctorRepository.save(doctor);
    }

    // Update existing doctor
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        // Update fields
        if (updatedDoctor.getFirstName() != null) {
            existingDoctor.setFirstName(updatedDoctor.getFirstName());
        }
        if (updatedDoctor.getLastName() != null) {
            existingDoctor.setLastName(updatedDoctor.getLastName());
        }
        if (updatedDoctor.getSpecialization() != null) {
            existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        }
        if (updatedDoctor.getEmail() != null) {
            existingDoctor.setEmail(updatedDoctor.getEmail());
        }
        if (updatedDoctor.getPhone() != null) {
            existingDoctor.setPhone(updatedDoctor.getPhone());
        }

        return doctorRepository.save(existingDoctor);
    }

    // Partially update doctor (PATCH)
    public Doctor patchDoctor(Long id, Doctor partialDoctor) {
        return updateDoctor(id, partialDoctor); // Same logic for this example
    }

    // Delete doctor
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    // Soft delete (deactivate)
    public Doctor deactivateDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        doctor.setActive(false);
        return doctorRepository.save(doctor);
    }

    // Get total count
    public long getTotalCount() {
        return doctorRepository.count();
    }

    @PostConstruct
    public void init() {
        System.out.println("=== DoctorService Initialized ===");
        System.out.println("Max Doctors: " + appProperties.getMaxDoctors());
        System.out.println("Notifications: " + appProperties.isEnableNotifications());
    }

    public void registerDoctor(String doctorName) {
        System.out.println("Registering doctor: " + doctorName);

        if (appProperties.isEnableNotifications()) {
            emailService.sendNotification(
                    appProperties.getAdmin().getEmail(),
                    "New Doctor Registered",
                    "Doctor " + doctorName + " has been registered.");
        }
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("=== DoctorService Cleanup ===");
    }
}
