package com.example.doctorservice.controller;

import com.example.doctorservice.dto.ApiResponse;
import com.example.doctorservice.dto.CreatePatientRequest;
import com.example.doctorservice.dto.PatientDTO;
import com.example.doctorservice.mapper.PatientMapper;
import com.example.doctorservice.model.Patient;
import com.example.doctorservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @Autowired
    public PatientController(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getAllPatients() {
        List<PatientDTO> dtos = patientMapper.toDTOList(patientService.getAllPatients());
        return ResponseEntity.ok(ApiResponse.success("Success", dtos));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PatientDTO>> getPatientById(@PathVariable Long id) {
        Patient p = patientService.getPatientById(id);
        if (p == null) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Patient not found"));
        return ResponseEntity.ok(ApiResponse.success(patientMapper.toDTO(p)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PatientDTO>> createPatient(@RequestBody CreatePatientRequest req) {
        try {
            Patient p = patientMapper.toEntity(req);
            Patient created = patientService.createPatient(p);
            PatientDTO dto = patientMapper.toDTO(created);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Patient created", dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create patient"));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PatientDTO>> updatePatient(@PathVariable Long id,
                                                                 @RequestBody CreatePatientRequest req) {
        try {
            Patient p = patientMapper.toEntity(req);
            Patient updated = patientService.updatePatient(id, p);
            return ResponseEntity.ok(ApiResponse.success("Patient updated", patientMapper.toDTO(updated)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PatientDTO>> patchPatient(@PathVariable Long id,
                                                                @RequestBody CreatePatientRequest req) {
        try {
            Patient p = patientMapper.toEntity(req);
            Patient patched = patientService.patchPatient(id, p);
            return ResponseEntity.ok(ApiResponse.success("Patient patched", patientMapper.toDTO(patched)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok(ApiResponse.success("Patient deleted", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> getTotalCount() {
        long c = patientService.getTotalCount();
        return ResponseEntity.ok(ApiResponse.success("Success", c));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<PatientDTO>>> searchPatients(@RequestParam String name) {
        List<PatientDTO> dtos = patientMapper.toDTOList(patientService.searchPatients(name));
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }
}
