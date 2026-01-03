package com.example.doctorservice.mapper;

import com.example.doctorservice.dto.AppointmentDTO;
import com.example.doctorservice.entity.Appointment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    public AppointmentDTO toDTO(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setReason(appointment.getReason());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());

        // Map Doctor info
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
            dto.setDoctorName(appointment.getDoctor().getFullName());
            dto.setDoctorSpecialization(appointment.getDoctor().getSpecialization());
        }

        // Map Patient info
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
            dto.setPatientName(appointment.getPatient().getFullName());
            dto.setPatientEmail(appointment.getPatient().getEmail());
        }

        return dto;
    }

    public List<AppointmentDTO> toDTOList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}