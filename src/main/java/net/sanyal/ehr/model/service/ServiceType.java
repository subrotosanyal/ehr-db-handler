package net.sanyal.ehr.model.service;

import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sanyal.ehr.model.common.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "service_types")
@EqualsAndHashCode(callSuper=false)
@Audited
public class ServiceType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceTypeId;

    @Column(nullable = false, unique = true)
    private String serviceTypeName;  // e.g., "ECG", "Skin Check"
}
