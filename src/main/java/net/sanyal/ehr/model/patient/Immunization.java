package net.sanyal.ehr.model.patient;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.sanyal.ehr.model.common.BaseEntity;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "immunizations")
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Immunization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long immunizationId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(nullable = false)
    private String vaccineName;

    @Column(nullable = false)
    private LocalDate dateAdministered;
}
