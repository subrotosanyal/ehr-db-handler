package net.sanyal.ehr.model.common;

import org.hibernate.envers.Audited;

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
@Table(name = "genders")
@EqualsAndHashCode(callSuper=false)
@Audited
public class Gender extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genderId;

    @Column(nullable = false, unique = true)
    private String genderName;  // e.g., "Male", "Female", "Other"
}
