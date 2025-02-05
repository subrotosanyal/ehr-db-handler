package net.sanyal.ehr.model.common;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "names")
@EqualsAndHashCode(callSuper=false)
@JsonIdentityInfo(scope = Name.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "nameId")
public class Name extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nameId;

    private String salutation;  // e.g., "Mr.", "Mrs.", "Dr."
    private String firstName;
    private String lastName;
}
