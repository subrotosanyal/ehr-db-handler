package net.sanyal.ehr.model.appointment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.sanyal.ehr.model.common.BaseEntity;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.model.practitioner.Practitioner;
import net.sanyal.ehr.model.service.ServiceCategory;
import net.sanyal.ehr.model.service.ServiceType;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "appointments")
@EqualsAndHashCode(callSuper = false)
@Audited
@JsonIdentityInfo(scope = Appointment.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "appointmentId")
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @NotBlank(message = "Appointment description is required")
    private String description;

    @NotNull(message = "Start time is required")
    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    @ManyToOne
    @JoinColumn(name = "appointment_status_id", referencedColumnName = "appointmentStatusId", nullable = false)
    private AppointmentStatus appointmentStatus; // Can be mapped to enums as needed (e.g., "Pending", "Booked")

    @ManyToOne
    @JoinColumn(name = "appointment_type_id", referencedColumnName = "appointmentTypeId", nullable = false)
    private AppointmentType appointmentType; // e.g., "Routine", "Checkup", "Emergency"

    private String patientInstructions;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @ToString.Exclude
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "practitioner_id", nullable = false)
    @ToString.Exclude
    private Practitioner practitioner;

    private String location; // e.g., "Room 5, 2nd Floor"

    @ManyToOne
    @JoinColumn(name = "appointment_priority_id", referencedColumnName = "appointmentPriorityId", nullable = false)
    private AppointmentPriority priority; // e.g., "High", "Low"

    @ManyToOne
    @JoinColumn(name = "service_category_id", referencedColumnName = "serviceCategoryId", nullable = false)
    private ServiceCategory serviceCategory; // e.g., "Cardiology", "Dermatology"

    @ManyToOne
    @JoinColumn(name = "service_type_id", referencedColumnName = "serviceTypeId", nullable = false)
    private ServiceType serviceType; // Specific service such as "ECG", "Skin Check"

    private LocalDateTime requestedStartTime;

    private LocalDateTime requestedEndTime;

    private String additionalComments;
}
