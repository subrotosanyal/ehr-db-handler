package net.sanyal.ehr.model.patient;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sanyal.ehr.model.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "insurance_details")
@EqualsAndHashCode(callSuper=false, exclude = {"patient"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Audited
public class InsuranceDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceId;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String policyNumber;

    @Column(nullable = true)
    private String coverageDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = true)
    @JsonBackReference
    @ToString.Exclude
    private Patient patient;
}
