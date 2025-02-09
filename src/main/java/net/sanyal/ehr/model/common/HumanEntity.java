package net.sanyal.ehr.model.common;

import java.time.LocalDate;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Audited
public abstract class HumanEntity extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id", referencedColumnName = "nameId")
    private Name name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "genderId")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "race_id", referencedColumnName = "raceId")
    private Race race;

    @ManyToOne
    @JoinColumn(name = "ethnicity_id", referencedColumnName = "ethnicityId")
    private Ethnicity ethnicity;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    private ContactDetail contactDetail;
}
