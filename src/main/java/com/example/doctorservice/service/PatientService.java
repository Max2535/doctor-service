package com.example.doctorservice.service;

import com.example.doctorservice.model.Patient;
import com.example.doctorservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
