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
@Table(name = "addresses")
@EqualsAndHashCode(callSuper=false)
public class Address extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String houseNumber;
    private String street;
    private String city;
    private String district;
    private String province;
    private String country;
    private String postalCode;
}
