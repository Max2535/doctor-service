package com.example.doctorservice.repository;

import com.example.doctorservice.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PatientRepository {

    private final Map<Long, Patient> patients = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public PatientRepository() {
        // sample data (optional)
        Patient p1 = new Patient(idGenerator.getAndIncrement(), "Alice", "Walker");
        p1.setEmail("alice@example.com");
        p1.setPhone("081-111-2222");
        patients.put(p1.getId(), p1);

        Patient p2 = new Patient(idGenerator.getAndIncrement(), "Bob", "Martin");
        p2.setEmail("bob@example.com");
        p2.setPhone("082-222-3333");
        patients.put(p2.getId(), p2);
    }

    public List<Patient> findAll() {
        return new ArrayList<>(patients.values());
    }

    public Optional<Patient> findById(Long id) {
        return Optional.ofNullable(patients.get(id));
    }

    public Patient save(Patient p) {
        if (p.getId() == null) {
            p.setId(idGenerator.getAndIncrement());
        }
        patients.put(p.getId(), p);
        return p;
    }

    public boolean deleteById(Long id) {
        return patients.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return patients.containsKey(id);
    }

    public boolean existsByEmail(String email) {
        return patients.values().stream()
                .anyMatch(p -> p.getEmail() != null && p.getEmail().equalsIgnoreCase(email));
    }

    public long count() { return patients.size(); }

    public List<Patient> searchByName(String name) {
        String t = name == null ? "" : name.toLowerCase();
        return patients.values().stream()
                .filter(p -> (p.getFirstName() != null && p.getFirstName().toLowerCase().contains(t)) ||
                             (p.getLastName() != null && p.getLastName().toLowerCase().contains(t)))
                .collect(Collectors.toList());
    }

    public void deleteAll() { patients.clear(); }
}
