package com.example.doctorservice.mapper;

import com.example.doctorservice.dto.CreatePatientRequest;
import com.example.doctorservice.dto.PatientDTO;
import com.example.doctorservice.model.Patient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapper {

    public PatientDTO toDTO(Patient p) {
        if (p == null) return null;
        PatientDTO dto = new PatientDTO();
        dto.setId(p.getId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setDateOfBirth(p.getDateOfBirth());
        dto.setGender(p.getGender());
        dto.setPhone(p.getPhone());
        dto.setEmail(p.getEmail());
        dto.setAddress(p.getAddress());
        return dto;
    }

    public List<PatientDTO> toDTOList(List<Patient> patients) {
        return patients.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Patient toEntity(CreatePatientRequest req) {
        if (req == null) return null;
        Patient p = new Patient();
        p.setFirstName(req.getFirstName());
        p.setLastName(req.getLastName());
        if (req.getDateOfBirth() != null && !req.getDateOfBirth().isBlank()) {
            try {
                p.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
            } catch (Exception ignored) {
            }
        }
        p.setGender(req.getGender());
        p.setPhone(req.getPhone());
        p.setEmail(req.getEmail());
        p.setAddress(req.getAddress());
        return p;
    }
}
