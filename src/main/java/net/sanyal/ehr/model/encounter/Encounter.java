package net.sanyal.ehr.model.encounter;

import jakarta.persistence.*;
import lombok.*;
import net.sanyal.ehr.model.appointment.Appointment;
import net.sanyal.ehr.model.common.BaseEntity;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.model.practitioner.Practitioner;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "encounter")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class Encounter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "practitioner_id", nullable = false)
    private Practitioner practitioner;

    @ManyToOne
    @JoinColumn(name = "parent_encounter_id")
    private Encounter parentEncounter;

    @ManyToOne
    @JoinColumn(name = "encounter_status_id", referencedColumnName = "encounterStatusId", nullable = false)
    private EncounterStatus status;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "encounter_type_id", referencedColumnName = "encounterTypeId", nullable = false)
    private EncounterType encounterType;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration lengthOfStay;

    private String externalId;
    private String location;
}
