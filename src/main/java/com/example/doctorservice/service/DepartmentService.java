package com.example.doctorservice.service;

import com.example.doctorservice.entity.Department;
import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.exception.DepartmentNotFoundException;
import com.example.doctorservice.exception.DuplicateDepartmentException;
import com.example.doctorservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // ============================================
    // Read Operations
    // ============================================

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public List<Department> getActiveDepartments() {
        return departmentRepository.findByActive(true);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));
    }

    public Department getDepartmentWithDoctors(Long id) {
        return departmentRepository.findByIdWithDoctors(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));
    }

    public List<Doctor> getDoctorsByDepartment(Long departmentId) {
        Department department = getDepartmentWithDoctors(departmentId);
        return department.getDoctors();
    }

    // ============================================
    // Write Operations
    // ============================================

    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateDepartmentException(
                    "Department already exists: " + department.getName());
        }
        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(Long id, Department updatedDept) {
        Department existingDept = getDepartmentById(id);

        if (updatedDept.getName() != null) {
            existingDept.setName(updatedDept.getName());
        }
        if (updatedDept.getDescription() != null) {
            existingDept.setDescription(updatedDept.getDescription());
        }
        if (updatedDept.getBuildingName() != null) {
            existingDept.setBuildingName(updatedDept.getBuildingName());
        }
        if (updatedDept.getFloorNumber() != null) {
            existingDept.setFloorNumber(updatedDept.getFloorNumber());
        }
        if (updatedDept.getPhoneNumber() != null) {
            existingDept.setPhoneNumber(updatedDept.getPhoneNumber());
        }

        return departmentRepository.save(existingDept);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}