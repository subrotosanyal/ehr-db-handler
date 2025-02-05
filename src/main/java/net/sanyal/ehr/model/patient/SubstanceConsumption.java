package net.sanyal.ehr.model.patient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sanyal.ehr.model.common.BaseEntity;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false, exclude = {"patient"})
@Entity
@Table(name = "substance_consumptions")
public class SubstanceConsumption extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long substanceId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String amount;

    @Column(nullable = false)
    private String frequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Patient patient;
}
