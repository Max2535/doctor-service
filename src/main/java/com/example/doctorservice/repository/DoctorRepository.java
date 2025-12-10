package com.example.doctorservice.repository;

import com.example.doctorservice.model.Doctor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class DoctorRepository {

    // In-memory storage (จะเปลี่ยนเป็น Database ใน Week 2)
    private final Map<Long, Doctor> doctors = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Constructor - สร้างข้อมูลตัวอย่าง
    public DoctorRepository() {
        initializeSampleData();
    }

    private void initializeSampleData() {
        Doctor d1 = new Doctor(idGenerator.getAndIncrement(),
                "John", "Smith", "Cardiology");
        d1.setEmail("john.smith@hospital.com");
        d1.setPhone("081-234-5678");
        d1.setJoinedDate(LocalDate.of(2020, 1, 15));
        doctors.put(d1.getId(), d1);

        Doctor d2 = new Doctor(idGenerator.getAndIncrement(),
                "Sarah", "Johnson", "Neurology");
        d2.setEmail("sarah.j@hospital.com");
        d2.setPhone("082-345-6789");
        d2.setJoinedDate(LocalDate.of(2019, 5, 20));
        doctors.put(d2.getId(), d2);

        Doctor d3 = new Doctor(idGenerator.getAndIncrement(),
                "Michael", "Brown", "Pediatrics");
        d3.setEmail("michael.b@hospital.com");
        d3.setPhone("083-456-7890");
        d3.setJoinedDate(LocalDate.of(2021, 3, 10));
        doctors.put(d3.getId(), d3);
    }

    // Find all
    public List<Doctor> findAll() {
        return new ArrayList<>(doctors.values());
    }

    // Find by ID
    public Optional<Doctor> findById(Long id) {
        return Optional.ofNullable(doctors.get(id));
    }

    // Find by specialization
    public List<Doctor> findBySpecialization(String specialization) {
        return doctors.values().stream()
                .filter(d -> d.getSpecialization()
                        .equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }

    // Find by active status
    public List<Doctor> findByActive(boolean active) {
        return doctors.values().stream()
                .filter(d -> d.isActive() == active)
                .collect(Collectors.toList());
    }

    // Search by name (firstName or lastName contains)
    public List<Doctor> searchByName(String name) {
        String searchTerm = name.toLowerCase();
        return doctors.values().stream()
                .filter(d -> d.getFirstName().toLowerCase().contains(searchTerm) ||
                           d.getLastName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    // Save (create or update)
    public Doctor save(Doctor doctor) {
        if (doctor.getId() == null) {
            // Create new
            doctor.setId(idGenerator.getAndIncrement());
        }
        doctors.put(doctor.getId(), doctor);
        return doctor;
    }

    // Delete by ID
    public boolean deleteById(Long id) {
        return doctors.remove(id) != null;
    }

    // Check if exists by ID
    public boolean existsById(Long id) {
        return doctors.containsKey(id);
    }

    // Check if email exists
    public boolean existsByEmail(String email) {
        return doctors.values().stream()
                .anyMatch(d -> d.getEmail().equalsIgnoreCase(email));
    }

    // Count
    public long count() {
        return doctors.size();
    }

    // Clear all (for testing)
    public void deleteAll() {
        doctors.clear();
    }
}