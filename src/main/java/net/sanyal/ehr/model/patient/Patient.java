package net.sanyal.ehr.model.patient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import net.sanyal.ehr.model.appointment.Appointment;
import net.sanyal.ehr.model.common.HumanEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "patients")
@Audited
@JsonIdentityInfo(scope = Patient.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "patientId")
public class Patient extends HumanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @NotNull(message = "Social Security Number is required")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "SSN must be in format XXX-XX-XXXX")
    @Column(nullable = false)
    private String socialSecurityNumber;

    @NotBlank(message = "Purpose of visit is required")
    private String purposeOfVisit;

    private String healthGoals;

    @Builder.Default
    private Boolean active = true;

    private boolean consent;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<InsuranceDetail> insuranceDetails = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_history_id", referencedColumnName = "medical_history_id")
    @JsonManagedReference
    private MedicalHistory medicalHistory;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<SubstanceConsumption> substanceConsumptions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();
}
