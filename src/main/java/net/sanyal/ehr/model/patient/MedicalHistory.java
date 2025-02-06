package net.sanyal.ehr.model.patient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import net.sanyal.ehr.model.common.BaseEntity;

import java.util.List;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "medical_histories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false, exclude = {"patient"})
@Audited
public class MedicalHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_history_id")
    private Long historyId;

    @Column(name = "patient_id", insertable = false, updatable = false)
    private Long patientId;

    private String childhoodIllness;
    private String surgeries;
    private String historyBloodTransfusion;
    private String allergies;
    private String exerciseRoutine;
    private String diet;
    private String mentalHealthQuestions;
    private String familyHealthHistory;
    private String changes;
    private String currentSymptoms;
    private String healthGoals;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_history_id")
    private List<Medication> medications;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_history_id")
    private List<Immunization> immunizations;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_history_id")
    private List<SubstanceConsumption> substanceConsumptions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_history_id")
    private List<MenstruationPregnancyRecord> menstruationPregnancyRecords;

    @OneToOne(mappedBy = "medicalHistory")
    @JsonBackReference
    @ToString.Exclude
    private Patient patient;
}
