package com.example.doctorservice.controller;

import com.example.doctorservice.entity.Department;
import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // ============================================
    // GET Endpoints
    // ============================================

    /**
     * GET /api/departments
     * Get all departments
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    /**
     * GET /api/departments/{id}
     * Get department by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    /**
     * GET /api/departments/{id}/with-doctors
     * Get department with all doctors (avoid N+1)
     */
    @GetMapping("/{id}/with-doctors")
    public ResponseEntity<Department> getDepartmentWithDoctors(@PathVariable Long id) {
        Department department = departmentService.getDepartmentWithDoctors(id);
        return ResponseEntity.ok(department);
    }

    /**
     * GET /api/departments/{id}/doctors
     * Get only doctors in department
     */
    @GetMapping("/{id}/doctors")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartment(@PathVariable Long id) {
        List<Doctor> doctors = departmentService.getDoctorsByDepartment(id);
        return ResponseEntity.ok(doctors);
    }

    /**
     * GET /api/departments/active
     * Get active departments
     */
    @GetMapping("/active")
    public ResponseEntity<List<Department>> getActiveDepartments() {
        List<Department> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(departments);
    }

    // ============================================
    // POST Endpoints
    // ============================================

    /**
     * POST /api/departments
     * Create new department
     */
    @PostMapping
    public ResponseEntity<Department> createDepartment(
            @Valid @RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    // ============================================
    // PUT Endpoints
    // ============================================

    /**
     * PUT /api/departments/{id}
     * Update department
     */
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(updatedDepartment);
    }

    // ============================================
    // DELETE Endpoints
    // ============================================

    /**
     * DELETE /api/departments/{id}
     * Delete department
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}