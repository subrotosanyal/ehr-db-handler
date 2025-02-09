package net.sanyal.ehr.service.patient;

import net.sanyal.ehr.aspect.log.Log;
import net.sanyal.ehr.db.repository.patient.PatientRepository;
import net.sanyal.ehr.db.specification.patient.PatientSpecifications;
import net.sanyal.ehr.exception.ResourceNotFoundException;
import net.sanyal.ehr.model.patient.Patient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public Patient createPatient(Patient patient) {
        // Ensure InsuranceDetail is linked to Patient
        if (patient.getInsuranceDetails() != null) {
            patient.getInsuranceDetails().forEach(insurance -> insurance.setPatient(patient));
        }
        if (patient.getSubstanceConsumptions() != null) {
            patient.getSubstanceConsumptions()
                    .forEach(substanceConsumption -> substanceConsumption.setPatient(patient));
        }
        if (patient.getMedicalHistory() != null) {
            patient.getMedicalHistory().setPatient(patient);
        }
        return patientRepository.saveAndFlush(patient);
    }

    @Transactional
    @Log
    public Patient updatePatient(Long id, Map<String, Object> updates) {
        Patient updatedPatient = objectMapper.convertValue(updates, Patient.class);
        return patientRepository.saveAndFlush(updatedPatient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    public Page<Patient> searchPatients(String firstName, String lastName, String address, String phone, String email, String insurance, Pageable pageable) {
        Specification<Patient> spec = Specification.where(PatientSpecifications.withAddress(address))
                .and(PatientSpecifications.withPhoneNumber(phone))
                .and(PatientSpecifications.withEmail(email))
                .and(PatientSpecifications.withLastName(lastName))
                .and(PatientSpecifications.withFirstName(firstName))
                .and(PatientSpecifications.withInsuranceProvider(insurance));
        return patientRepository.findAll(spec, pageable);
    }

    @Log
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
