package net.sanyal.ehr.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "contact_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid alternate phone number")
    private String alternatePhoneNumber;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid work phone number")
    private String workPhoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    @Email(message = "Invalid work email format")
    private String workEmail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address address;
}
