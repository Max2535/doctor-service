package com.example.doctorservice.service;

import com.example.doctorservice.entity.Patient;  // ✅ ใช้ entity.Patient
import com.example.doctorservice.exception.PatientNotFoundException;
import com.example.doctorservice.exception.DuplicatePatientException;
import com.example.doctorservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ============================================
    // Read Operations
    // ============================================

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(
                        "Patient not found with id: " + id));
    }

    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(
                        "Patient not found with email: " + email));
    }

    public List<Patient> getActivePatients() {
        return patientRepository.findByActive(true);
    }

    public List<Patient> searchPatientsByName(String searchTerm) {
        return patientRepository.searchByName(searchTerm);
    }

    public List<Patient> getPatientsByBloodGroup(String bloodGroup) {
        return patientRepository.findByBloodGroup(bloodGroup);
    }

    public List<Patient> getPatientsWithBirthdayThisMonth() {
        return patientRepository.findPatientsWithBirthdayThisMonth();
    }

    public long getTotalPatientsCount() {
        return patientRepository.count();
    }

    public long getActivePatientsCount() {
        return patientRepository.countByActive(true);
    }

    // ============================================
    // Write Operations
    // ============================================

    @Transactional
    public Patient createPatient(Patient patient) {
        // Validate email uniqueness
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new DuplicatePatientException(
                    "Email already exists: " + patient.getEmail());
        }

        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);

        // Update fields
        if (updatedPatient.getFirstName() != null) {
            existingPatient.setFirstName(updatedPatient.getFirstName());
        }
        if (updatedPatient.getLastName() != null) {
            existingPatient.setLastName(updatedPatient.getLastName());
        }
        if (updatedPatient.getDateOfBirth() != null) {
            existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        }
        if (updatedPatient.getGender() != null) {
            existingPatient.setGender(updatedPatient.getGender());
        }
        if (updatedPatient.getEmail() != null &&
            !updatedPatient.getEmail().equals(existingPatient.getEmail())) {
            if (patientRepository.existsByEmail(updatedPatient.getEmail())) {
                throw new DuplicatePatientException(
                        "Email already exists: " + updatedPatient.getEmail());
            }
            existingPatient.setEmail(updatedPatient.getEmail());
        }
        if (updatedPatient.getPhone() != null) {
            existingPatient.setPhone(updatedPatient.getPhone());
        }
        if (updatedPatient.getAddress() != null) {
            existingPatient.setAddress(updatedPatient.getAddress());
        }
        if (updatedPatient.getBloodGroup() != null) {
            existingPatient.setBloodGroup(updatedPatient.getBloodGroup());
        }
        if (updatedPatient.getEmergencyContactName() != null) {
            existingPatient.setEmergencyContactName(
                    updatedPatient.getEmergencyContactName());
        }
        if (updatedPatient.getEmergencyContactPhone() != null) {
            existingPatient.setEmergencyContactPhone(
                    updatedPatient.getEmergencyContactPhone());
        }
        if (updatedPatient.getMedicalHistory() != null) {
            existingPatient.setMedicalHistory(updatedPatient.getMedicalHistory());
        }

        return patientRepository.save(existingPatient);
    }

    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    @Transactional
    public Patient deactivatePatient(Long id) {
        Patient patient = getPatientById(id);
        patient.setActive(false);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient activatePatient(Long id) {
        Patient patient = getPatientById(id);
        patient.setActive(true);
        return patientRepository.save(patient);
    }
}