package net.sanyal.ehr.controller.patient;

import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.service.patient.PatientService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientService.getAllPatients(pageable);
    }

    @PostMapping(consumes = { "application/json", "application/json;charset=UTF-8" })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patient));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Patient patient = patientService.updatePatient(id, updates);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/search")
    public Page<Patient> searchPatients(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String insurance,
            Pageable pageable) {
        return patientService.searchPatients(firstName, lastName, address, phone, email, insurance, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Ensure 204 No Content
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}
