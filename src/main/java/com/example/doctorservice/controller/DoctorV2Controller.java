package com.example.doctorservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorservice.dto.CreateDoctorRequest;
import com.example.doctorservice.dto.DoctorDTO;
import com.example.doctorservice.mapper.DoctorMapper;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.service.DoctorService;

@RestController
@RequestMapping("/api/v2/doctors")
public class DoctorV2Controller {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @Autowired
    public DoctorV2Controller(DoctorService doctorService,
            DoctorMapper doctorMapper) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE
    }, produces = {
            MediaType.APPLICATION_JSON_VALUE
    })

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> dtos = doctorMapper.toDTOList(doctors);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(doctorMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(
            @RequestBody CreateDoctorRequest request) {
        try {
            Doctor doctor = doctorMapper.toEntity(request);
            Doctor created = doctorService.createDoctor(doctor);
            DoctorDTO dto = doctorMapper.toDTO(created);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
