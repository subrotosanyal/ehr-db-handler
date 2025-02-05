package net.sanyal.ehr.db.specification.appointment;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;
import net.sanyal.ehr.model.appointment.Appointment;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
public class AppointmentSpecification {

    public static Specification<Appointment> buildSpecification(AppointmentCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getPatientId() != null) {
                predicates.add(cb.equal(root.join("patient", JoinType.LEFT).get("id"), criteria.getPatientId()));
            }
            if (criteria.getPractitionerId() != null) {
                predicates.add(cb.equal(root.join("practitioner", JoinType.LEFT).get("id"), criteria.getPractitionerId()));
            }
            if (criteria.getAppointmentType() != null) {
                predicates.add(cb.equal(root.get("appointmentType").get("appointmentTypeName"), criteria.getAppointmentType()));
            }
            if (criteria.getPriority() != null) {
                predicates.add(cb.equal(root.get("priority").get("appointmentPriorityName"), criteria.getPriority()));
            }
            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("appointmentStatus").get("appointmentStatusName"), criteria.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
