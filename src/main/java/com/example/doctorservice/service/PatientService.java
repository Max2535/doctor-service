package com.example.doctorservice.service;

import com.example.doctorservice.model.Patient;
import com.example.doctorservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get all patients with optional sorting and pagination
    public Page<Patient> getAllPatients(int page, int size, String sortBy, String sortDir) {
        List<Patient> all = patientRepository.findAll();

        // Determine comparator
        String key = (sortBy == null) ? "id" : sortBy.toLowerCase();
        boolean asc = sortDir == null || !sortDir.equalsIgnoreCase("desc");

        java.util.Comparator<Patient> comparator;
        switch (key) {
            case "firstname":
            case "first_name":
            case "firstName":
                comparator = java.util.Comparator.comparing(p -> nullableLower(p.getFirstName()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "lastname":
            case "last_name":
            case "lastName":
                comparator = java.util.Comparator.comparing(p -> nullableLower(p.getLastName()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "email":
                comparator = java.util.Comparator.comparing(p -> nullableLower(p.getEmail()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "dateofbirth":
            case "date_of_birth":
            case "dateOfBirth":
                comparator = java.util.Comparator.comparing(Patient::getDateOfBirth, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "gender":
                comparator = java.util.Comparator.comparing(p -> nullableLower(p.getGender()), java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
            case "id":
            default:
                comparator = java.util.Comparator.comparing(Patient::getId, java.util.Comparator.nullsFirst(java.util.Comparator.naturalOrder()));
                break;
        }

        if (!asc) comparator = comparator.reversed();

        all.sort(comparator);

        int total = all.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<Patient> content = all.subList(fromIndex, toIndex);
        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    private String nullableLower(String s) { return s == null ? "" : s.toLowerCase(); }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient createPatient(Patient p) {
        if (p.getFirstName() == null || p.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (p.getLastName() == null || p.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (p.getEmail() != null && patientRepository.existsByEmail(p.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return patientRepository.save(p);
    }

    public Patient updatePatient(Long id, Patient updated) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        if (updated.getFirstName() != null) existing.setFirstName(updated.getFirstName());
        if (updated.getLastName() != null) existing.setLastName(updated.getLastName());
        if (updated.getDateOfBirth() != null) existing.setDateOfBirth(updated.getDateOfBirth());
        if (updated.getGender() != null) existing.setGender(updated.getGender());
        if (updated.getPhone() != null) existing.setPhone(updated.getPhone());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
        if (updated.getAddress() != null) existing.setAddress(updated.getAddress());

        return patientRepository.save(existing);
    }

    public Patient patchPatient(Long id, Patient partial) {
        return updatePatient(id, partial);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    public long getTotalCount() { return patientRepository.count(); }

    public List<Patient> searchPatients(String name) { return patientRepository.searchByName(name); }
}
