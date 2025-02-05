package net.sanyal.ehr.service.patient;

import net.sanyal.ehr.db.repository.common.EthnicityRepository;
import net.sanyal.ehr.db.repository.common.GenderRepository;
import net.sanyal.ehr.db.repository.common.RaceRepository;
import net.sanyal.ehr.db.repository.patient.PatientRepository;
import net.sanyal.ehr.db.specification.patient.PatientSpecifications;
import net.sanyal.ehr.exception.ResourceNotFoundException;
import net.sanyal.ehr.model.common.Ethnicity;
import net.sanyal.ehr.model.common.Gender;
import net.sanyal.ehr.model.common.Race;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.service.util.SerDeUtils;

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
    private EthnicityRepository ethnicityRepository;
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private RaceRepository raceRepository;
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

        if (null != patient.getEthnicity()) {
            Ethnicity existingEthnicity = ethnicityRepository.findById(patient.getEthnicity().getEthnicityId())
                    .orElseGet(() -> {
                        log.warn("Ethnicity not found for patient {}", patient);
                        return null;
                    });
            patient.setEthnicity(existingEthnicity);
        }
        if (null != patient.getGender()) {
            Gender existingGender = genderRepository.findById(patient.getGender().getGenderId())
                    .orElseGet(() -> {
                        log.warn("Gender not found for patient {}", patient);
                        return null;
                    });
            patient.setGender(existingGender);
        }
        if (null != patient.getRace()) {
            Race existingRace = raceRepository.findById(patient.getRace().getRaceId())
                    .orElseGet(() -> {
                        log.warn("Race not found for patient {}", patient);
                        return null;
                    });

            // Set the managed entities
            patient.setRace(existingRace);
        }
        return patientRepository.saveAndFlush(patient);
    }

    public Patient updatePatient(Long id, Map<String, Object> updates) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        SerDeUtils.applyUpdates(objectMapper, patient, updates);
        return patientRepository.saveAndFlush(patient);
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

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
