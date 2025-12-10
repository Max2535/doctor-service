package com.example.doctorservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

    // Get all doctors with optional filtering and pagination
    public Page<Doctor> getAllDoctors(String specialization, Boolean active, String search, int page, int size) {
        List<Doctor> filtered = getAllDoctors(specialization, active, search);
        int total = filtered.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<Doctor> pageContent = filtered.subList(fromIndex, toIndex);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), total);
    }

    // Get all doctors with optional filtering, sorting and pagination
    public Page<Doctor> getAllDoctors(String specialization, Boolean active, String search, int page, int size, String sortBy, String sortDir) {
        List<Doctor> filtered = getAllDoctors(specialization, active, search);

        // Sorting
        java.util.Comparator<Doctor> comparator = (d1, d2) -> 0;
        String key = (sortBy == null) ? "id" : sortBy.toLowerCase();
        boolean asc = sortDir == null || !sortDir.equalsIgnoreCase("desc");

        switch (key) {
            case "firstname":
            case "firstName":
            case "first_name":
                comparator = java.util.Comparator.comparing(d -> nullableLower(d.getFirstName()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "lastname":
            case "lastName":
            case "last_name":
                comparator = java.util.Comparator.comparing(d -> nullableLower(d.getLastName()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "specialization":
                comparator = java.util.Comparator.comparing(d -> nullableLower(d.getSpecialization()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "email":
                comparator = java.util.Comparator.comparing(d -> nullableLower(d.getEmail()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "joineddate":
            case "joined_date":
            case "joinedDate":
                comparator = java.util.Comparator.comparing(Doctor::getJoinedDate, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "active":
                comparator = java.util.Comparator.comparing(Doctor::isActive, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "id":
            default:
                comparator = java.util.Comparator.comparing(Doctor::getId, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
        }

        if (!asc) {
            comparator = comparator.reversed();
        }

        filtered.sort(comparator);

        // Pagination
        int total = filtered.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<Doctor> pageContent = filtered.subList(fromIndex, toIndex);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), total);
    }

    private String nullableLower(String s) {
        return s == null ? "" : s.toLowerCase();
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
