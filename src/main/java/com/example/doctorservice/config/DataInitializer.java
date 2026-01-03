package com.example.doctorservice.config;

import com.example.doctorservice.entity.*;
import com.example.doctorservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public void run(String... args) throws Exception {
        // ตรวจสอบว่ามีข้อมูลแล้วหรือยัง
        if (departmentRepository.count() > 0) {
            System.out.println("Data already initialized. Skipping...");
            return;
        }

        System.out.println("Initializing sample data...");

        // Create Departments
        Department cardiology = new Department("Cardiology",
                "Department of heart and cardiovascular system");
        cardiology.setBuildingName("Building A");
        cardiology.setFloorNumber(3);

        Department neurology = new Department("Neurology",
                "Department of nervous system");
        neurology.setBuildingName("Building B");
        neurology.setFloorNumber(2);

        departmentRepository.save(cardiology);
        departmentRepository.save(neurology);

        // Create Doctors
        Doctor doctor1 = new Doctor("John", "Smith", "Cardiology", "john.smith@hospital.com");
        doctor1.setPhone("081-234-5678");
        doctor1.setLicenseNumber("LIC-001");
        doctor1.setDateOfBirth(LocalDate.of(1975, 5, 15));
        doctor1.setYearsOfExperience(20);
        doctor1.setConsultationFee(2000.0);
        doctor1.setDepartment(cardiology);

        Doctor doctor2 = new Doctor("Sarah", "Johnson", "Neurology", "sarah.j@hospital.com");
        doctor2.setPhone("082-345-6789");
        doctor2.setLicenseNumber("LIC-002");
        doctor2.setDateOfBirth(LocalDate.of(1980, 8, 20));
        doctor2.setYearsOfExperience(15);
        doctor2.setConsultationFee(1800.0);
        doctor2.setDepartment(neurology);

        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);

        // Create Patients
        Patient patient1 = new Patient("Michael", "Brown",
                LocalDate.of(1990, 3, 10), "MALE", "michael.b@email.com");
        patient1.setPhone("083-456-7890");
        patient1.setAddress("123 Main St, Bangkok");
        patient1.setBloodGroup("A+");

        Patient patient2 = new Patient("Emily", "Davis",
                LocalDate.of(1995, 7, 25), "FEMALE", "emily.d@email.com");
        patient2.setPhone("084-567-8901");
        patient2.setAddress("456 Oak Ave, Bangkok");
        patient2.setBloodGroup("B+");

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Create Appointments
        Appointment apt1 = new Appointment(doctor1, patient1,
                LocalDateTime.now().plusDays(1), "Regular checkup");
        apt1.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        Appointment apt2 = new Appointment(doctor2, patient2,
                LocalDateTime.now().plusDays(2), "Headache consultation");
        apt2.setStatus(Appointment.AppointmentStatus.CONFIRMED);

        appointmentRepository.save(apt1);
        appointmentRepository.save(apt2);

        System.out.println("Sample data initialized successfully!");
    }
}