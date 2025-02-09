package net.sanyal.ehr.model.patient;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.sanyal.ehr.model.common.BaseEntity;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "immunizations")
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Immunization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long immunizationId;

    @Column(nullable = false)
    private String vaccineName;

    @Column(nullable = false)
    private LocalDate dateAdministered;
}
