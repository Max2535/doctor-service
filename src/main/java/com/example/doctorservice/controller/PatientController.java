package com.example.doctorservice.controller;

import com.example.doctorservice.entity.Patient;
import com.example.doctorservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // GET all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    // GET patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    // GET patient by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        Patient patient = patientService.getPatientByEmail(email);
        return ResponseEntity.ok(patient);
    }

    // GET active patients
    @GetMapping("/active")
    public ResponseEntity<List<Patient>> getActivePatients() {
        List<Patient> patients = patientService.getActivePatients();
        return ResponseEntity.ok(patients);
    }

    // SEARCH patients
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String name) {
        List<Patient> patients = patientService.searchPatientsByName(name);
        return ResponseEntity.ok(patients);
    }

    // GET by blood group
    @GetMapping("/blood-group/{bloodGroup}")
    public ResponseEntity<List<Patient>> getPatientsByBloodGroup(
            @PathVariable String bloodGroup) {
        List<Patient> patients = patientService.getPatientsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(patients);
    }

    // POST create patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    // PUT update patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    // PATCH deactivate
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Patient> deactivatePatient(@PathVariable Long id) {
        Patient patient = patientService.deactivatePatient(id);
        return ResponseEntity.ok(patient);
    }

    // PATCH activate
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Patient> activatePatient(@PathVariable Long id) {
        Patient patient = patientService.activatePatient(id);
        return ResponseEntity.ok(patient);
    }

    // DELETE patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    // GET count
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        long count = patientService.getTotalPatientsCount();
        return ResponseEntity.ok(count);
    }
}