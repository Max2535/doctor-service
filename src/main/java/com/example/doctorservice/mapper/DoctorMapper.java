package com.example.doctorservice.mapper;

import com.example.doctorservice.dto.CreateDoctorRequest;
import com.example.doctorservice.dto.DoctorDTO;
import com.example.doctorservice.model.Doctor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    // Entity -> DTO
    public DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) return null;

        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFullName(doctor.getFullName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setEmail(doctor.getEmail());
        dto.setPhone(doctor.getPhone());
        dto.setJoinedDate(doctor.getJoinedDate());
        dto.setActive(doctor.isActive());
        return dto;
    }

    // List<Entity> -> List<DTO>
    public List<DoctorDTO> toDTOList(List<Doctor> doctors) {
        return doctors.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // CreateRequest -> Entity
    public Doctor toEntity(CreateDoctorRequest request) {
        if (request == null) return null;

        Doctor doctor = new Doctor();
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        return doctor;
    }
}