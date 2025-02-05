package net.sanyal.ehr.model.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ethnicities")
@EqualsAndHashCode(callSuper=false)
public class Ethnicity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ethnicityId;

    @Column(nullable = false, unique = true)
    private String ethnicityName;  // e.g., "Hispanic or Latino", "Not Hispanic or Latino"
}
