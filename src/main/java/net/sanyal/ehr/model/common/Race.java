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
@Table(name = "races")
@EqualsAndHashCode(callSuper=false)
public class Race extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long raceId;

    @Column(nullable = false, unique = true)
    private String raceName;  // e.g., "Asian", "Black", "Caucasian", "Hispanic"
}

