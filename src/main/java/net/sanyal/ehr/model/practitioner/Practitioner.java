package net.sanyal.ehr.model.practitioner;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sanyal.ehr.model.appointment.Appointment;
import net.sanyal.ehr.model.common.BaseEntity;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.common.Ethnicity;
import net.sanyal.ehr.model.common.Gender;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.common.Race;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.envers.Audited;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "practitioners")
@Audited
@JsonIdentityInfo(scope = Practitioner.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "practitionerId")
public class Practitioner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long practitionerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id", referencedColumnName = "nameId")
    private Name name;

    @NotBlank(message = "NPI or practitioner identifier is required")
    @Column(name = "npi", nullable = false, unique = true)
    private String npi;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "gender_id", referencedColumnName = "genderId")
    private Gender gender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "race_id", referencedColumnName = "raceId")
    private Race race;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ethnicity_id", referencedColumnName = "ethnicityId")
    private Ethnicity ethnicity;
    
    @Builder.Default
    private Boolean active = true;

    private LocalDate effectiveFrom;

    private LocalDate effectiveUntil;

    private String specialty;

    private String licenseNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    @Builder.Default
    private ContactDetail contactDetail = new ContactDetail();

    @OneToMany(mappedBy = "practitioner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Appointment> appointments;
}
