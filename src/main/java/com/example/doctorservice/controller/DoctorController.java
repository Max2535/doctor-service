package com.example.doctorservice.controller;

import com.example.doctorservice.config.AppProperties;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.service.DoctorService;
import com.example.doctorservice.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final EmailService emailService;
    private final AppProperties appProperties;

    // Constructor Injection - Best Practice
    @Autowired
    public DoctorController(EmailService emailService,
            AppProperties appProperties,
            DoctorService doctorService) {
        this.emailService = emailService;
        this.appProperties = appProperties;
        this.doctorService = doctorService;
    }

    // ==================== GET Methods ====================

    /**
     * GET /api/doctors
     * Get all doctors
     */
    // @GetMapping
    // public ResponseEntity<List<Doctor>> getAllDoctors() {
    // List<Doctor> doctors = doctorService.getAllDoctors();
    // return ResponseEntity.ok(doctors);
    // }
    // produces: กำหนดว่า endpoint นี้ส่งข้อมูลประเภทไหนได้บ้าง
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * GET /api/doctors/{id}
     * Get doctor by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/doctors/specialization/{specialization}
     * Get doctors by specialization
     */
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(
            @PathVariable String specialization) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    /**
     * GET /api/doctors/active
     * Get only active doctors
     */
    @GetMapping("/active")
    public ResponseEntity<List<Doctor>> getActiveDoctors() {
        List<Doctor> doctors = doctorService.getActiveDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * GET /api/doctors/search?name=john
     * Search doctors by name (query parameter)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(
            @RequestParam String name) {
        List<Doctor> doctors = doctorService.searchDoctors(name);
        return ResponseEntity.ok(doctors);
    }

    /**
     * GET /api/doctors/count
     * Get total count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getTotalCount() {
        long count = doctorService.getTotalCount();
        return ResponseEntity.ok(Map.of("totalDoctors", count));
    }

    // ==================== POST Methods ====================

    /**
     * POST /api/doctors
     * Create new doctor
     */
    // @PostMapping
    // public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
    // try {
    // Doctor createdDoctor = doctorService.createDoctor(doctor);
    // return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    // } catch (IllegalArgumentException e) {
    // return ResponseEntity.badRequest().build();
    // }
    // }
    // consumes: กำหนดว่า endpoint นี้รับข้อมูลประเภทไหนได้บ้าง
    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    }, produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor created = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    // ==================== PUT Methods ====================

    /**
     * PUT /api/doctors/{id}
     * Update entire doctor (replace)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {
        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
            return ResponseEntity.ok(updatedDoctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== PATCH Methods ====================

    /**
     * PATCH /api/doctors/{id}
     * Partially update doctor
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Doctor> patchDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {
        try {
            Doctor patchedDoctor = doctorService.patchDoctor(id, doctor);
            return ResponseEntity.ok(patchedDoctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PATCH /api/doctors/{id}/deactivate
     * Soft delete (deactivate)
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Doctor> deactivateDoctor(@PathVariable Long id) {
        try {
            Doctor deactivatedDoctor = doctorService.deactivateDoctor(id);
            return ResponseEntity.ok(deactivatedDoctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== DELETE Methods ====================

    /**
     * DELETE /api/doctors/{id}
     * Delete doctor permanently
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}