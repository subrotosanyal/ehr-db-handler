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
@EqualsAndHashCode(callSuper=false)
@Table(name = "service_categories")
@Audited
public class ServiceCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceCategoryId;

    @Column(nullable = false, unique = true)
    private String serviceCategoryName;  // e.g., "Cardiology", "Dermatology"
}
