package net.sanyal.ehr.model.patient;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.sanyal.ehr.model.common.BaseEntity;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "menstruation_pregnancy_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MenstruationPregnancyRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(nullable = false)
    private String detail;
}
