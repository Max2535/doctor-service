package com.example.doctorservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.exception.DoctorNotFoundException;
import com.example.doctorservice.exception.DuplicateEmailException;
import com.example.doctorservice.exception.DuplicateLicenseException;
import com.example.doctorservice.repository.DoctorRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)  // Default เป็น read-only
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // ============================================
    // Read Operations (Read-only)
    // ============================================

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
    }

    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with email: " + email));
    }

    public Doctor getDoctorByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with license number: " + licenseNumber));
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByActive(true);
    }

    public List<Doctor> getActiveDoctorsBySpecialization(String specialization) {
        return doctorRepository.findActiveDoctorsBySpecialization(specialization);
    }

    public List<Doctor> searchDoctorsByName(String searchTerm) {
        return doctorRepository.searchByName(searchTerm);
    }

    public List<Doctor> getDoctorsByExperienceRange(Integer minYears, Integer maxYears) {
        return doctorRepository.findByExperienceRange(minYears, maxYears);
    }

    public List<Doctor> getDoctorsByFeeRange(Double minFee, Double maxFee) {
        return doctorRepository.findByConsultationFeeBetween(minFee, maxFee);
    }

    public long getTotalDoctorsCount() {
        return doctorRepository.count();
    }

    public long getActiveDoctorsCount() {
        return doctorRepository.countByActive(true);
    }

    public long getDoctorsCountBySpecialization(String specialization) {
        return doctorRepository.countBySpecialization(specialization);
    }

    // ============================================
    // Write Operations (Transactional)
    // ============================================

    @Transactional
    public Doctor createDoctor(Doctor doctor) {
        // Validate email uniqueness
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + doctor.getEmail());
        }

        // Validate license uniqueness
        if (doctorRepository.existsByLicenseNumber(doctor.getLicenseNumber())) {
            throw new DuplicateLicenseException(
                    "License number already exists: " + doctor.getLicenseNumber());
        }

        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = getDoctorById(id);

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
        if (updatedDoctor.getEmail() != null &&
            !updatedDoctor.getEmail().equals(existingDoctor.getEmail())) {
            if (doctorRepository.existsByEmail(updatedDoctor.getEmail())) {
                throw new DuplicateEmailException("Email already exists: " + updatedDoctor.getEmail());
            }
            existingDoctor.setEmail(updatedDoctor.getEmail());
        }
        if (updatedDoctor.getPhone() != null) {
            existingDoctor.setPhone(updatedDoctor.getPhone());
        }
        if (updatedDoctor.getDateOfBirth() != null) {
            existingDoctor.setDateOfBirth(updatedDoctor.getDateOfBirth());
        }
        if (updatedDoctor.getYearsOfExperience() != null) {
            existingDoctor.setYearsOfExperience(updatedDoctor.getYearsOfExperience());
        }
        if (updatedDoctor.getConsultationFee() != null) {
            existingDoctor.setConsultationFee(updatedDoctor.getConsultationFee());
        }
        if (updatedDoctor.getBiography() != null) {
            existingDoctor.setBiography(updatedDoctor.getBiography());
        }

        return doctorRepository.save(existingDoctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new DoctorNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    @Transactional
    public Doctor deactivateDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctor.setActive(false);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor activateDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctor.setActive(true);
        return doctorRepository.save(doctor);
    }
}