package net.sanyal.ehr.model.appointment;

import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sanyal.ehr.model.common.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appointment_statuses")
@EqualsAndHashCode(callSuper=false)
@Audited
public class AppointmentStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentStatusId;

    @Column(nullable = false, unique = true)
    private String appointmentStatusName;  // e.g., "Pending", "Booked", "Completed", "Cancelled"
}
