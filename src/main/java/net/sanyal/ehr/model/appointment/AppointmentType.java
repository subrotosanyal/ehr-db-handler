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
@Table(name = "appointment_types")
@EqualsAndHashCode(callSuper=false)
@Audited
public class AppointmentType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentTypeId;

    @Column(nullable = false, unique = true)
    private String appointmentTypeName;  // e.g., "Routine", "Checkup", "Emergency"
}
