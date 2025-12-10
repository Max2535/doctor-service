package com.example.doctorservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorservice.dto.ApiResponse;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.service.DoctorService;

@RestController
@RequestMapping("/api/v3/doctors")
public class DoctorV3Controller {

    private final DoctorService doctorService;

    @Autowired
    public DoctorV3Controller(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Doctor>>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> getDoctorById(
            @PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(doctor -> ResponseEntity.ok(
                    ApiResponse.success("Doctor found", doctor)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Doctor not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Doctor>> createDoctor(
            @RequestBody Doctor doctor) {
        try {
            Doctor created = doctorService.createDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Doctor created successfully", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(
            @PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok(
                ApiResponse.success("Doctor deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}