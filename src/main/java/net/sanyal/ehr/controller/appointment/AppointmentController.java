package net.sanyal.ehr.controller.appointment;

import net.sanyal.ehr.model.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.sanyal.ehr.db.specification.appointment.AppointmentCriteria;
import net.sanyal.ehr.db.specification.appointment.AppointmentSpecification;
import net.sanyal.ehr.model.appointment.Appointment;
import net.sanyal.ehr.service.appointment.AppointmentService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return appointmentService.getAllAppointments(pageable);
    }
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Appointment appointment = appointmentService.updateAppointment(id, updates);
        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Appointment>> searchAppointments(
            AppointmentCriteria criteria,
            Pageable pageable) {
        Specification<Appointment> specification = AppointmentSpecification.buildSpecification(criteria);
        return ResponseEntity.ok(appointmentService.searchAppointments(specification, pageable));
    }
}