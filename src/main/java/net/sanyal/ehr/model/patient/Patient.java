package net.sanyal.ehr.model.patient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.sanyal.ehr.model.appointment.Appointment;
import net.sanyal.ehr.model.common.BaseEntity;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.common.Ethnicity;
import net.sanyal.ehr.model.common.Gender;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.common.Race;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "patients")
@JsonIdentityInfo(scope = Patient.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "patientId")
public class Patient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id", referencedColumnName = "nameId")
    private Name name;

    @NotNull(message = "Social Security Number is required")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "SSN must be in format XXX-XX-XXXX")
    @Column(nullable = false)
    private String socialSecurityNumber;

    @NotBlank(message = "Purpose of visit is required")
    private String purposeOfVisit;

    private String healthGoals;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private boolean consent;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "gender_id", referencedColumnName = "genderId")
    private Gender gender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "race_id", referencedColumnName = "raceId")
    private Race race;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ethnicity_id", referencedColumnName = "ethnicityId")
    private Ethnicity ethnicity;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    private ContactDetail contactDetail;

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

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Appointment> appointments;
}
