package net.sanyal.ehr.service.appointment;

import net.sanyal.ehr.db.repository.patient.PatientRepository;
import net.sanyal.ehr.db.repository.practitioner.PractitionerRepository;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.model.practitioner.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sanyal.ehr.db.repository.appointment.AppointmentRepository;
import net.sanyal.ehr.exception.ResourceNotFoundException;
import net.sanyal.ehr.model.appointment.Appointment;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PractitionerRepository practitionerRepository;

    public Appointment createAppointment(Appointment appointment) {
        Patient managedPatient = patientRepository.findById(appointment.getPatient().getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        appointment.setPatient(managedPatient);

        // Ensure the practitioner is managed
        Practitioner managedPractitioner = practitionerRepository.findById(appointment.getPractitioner().getPractitionerId())
                .orElseThrow(() -> new ResourceNotFoundException("Practitioner not found"));
        appointment.setPractitioner(managedPractitioner);
        return appointmentRepository.saveAndFlush(appointment);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment updateAppointment(Long id, Map<String, Object> updates) {
        Appointment updatedAppointment = objectMapper.convertValue(updates, Appointment.class);
        return appointmentRepository.saveAndFlush(updatedAppointment);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public Page<Appointment> searchAppointments(Specification<Appointment> specification, Pageable pageable) {
        return appointmentRepository.findAll(specification, pageable);
    }
}