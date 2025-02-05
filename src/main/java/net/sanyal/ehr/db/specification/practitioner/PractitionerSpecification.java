package net.sanyal.ehr.db.specification.practitioner;

import net.sanyal.ehr.model.common.Address;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.practitioner.Practitioner;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import java.util.Optional;

public class PractitionerSpecification {

    public static Specification<Practitioner> withNpi(String npi) {
        return (root, query, builder) ->
                Optional.ofNullable(npi)
                        .map(s -> builder.like(root.get("npi"), "%" + s + "%"))
                        .orElse(null);
    }

    public static Specification<Practitioner> withSpecialty(String specialty) {
        return (root, query, builder) ->
                Optional.ofNullable(specialty)
                        .map(s -> builder.like(root.get("specialty"), "%" + s + "%"))
                        .orElse(null);
    }

    public static Specification<Practitioner> withLastName(String lastName) {
        return (root, query, builder) -> 
                Optional.ofNullable(lastName)
                        .map(l -> builder.like(root.get("name").get("lastName"), "%" + l + "%"))
                        .orElse(null);
    }

    public static Specification<Practitioner> withFirstName(String firstName) {
        return (root, query, builder) -> 
                Optional.ofNullable(firstName)
                        .map(f -> builder.like(root.get("name").get("firstName"), "%" + f + "%"))
                        .orElse(null);
    }

    public static Specification<Practitioner> withAddress(String address) {
        return (root, query, builder) -> {
            Join<Object, Address> addressJoin = root.join("contactDetail").join("address", JoinType.LEFT);
            return Optional.ofNullable(address)
                    .map(a -> builder.or(
                            builder.like(addressJoin.get("houseNumber"), "%" + a + "%"),
                            builder.like(addressJoin.get("street"), "%" + a + "%"),
                            builder.like(addressJoin.get("city"), "%" + a + "%"),
                            builder.like(addressJoin.get("district"), "%" + a + "%"),
                            builder.like(addressJoin.get("province"), "%" + a + "%"),
                            builder.like(addressJoin.get("country"), "%" + a + "%"),
                            builder.like(addressJoin.get("postalCode"), "%" + a + "%")
                    ))
                    .orElse(null);
        };
    }

    public static Specification<Practitioner> withPhoneNumber(String phone) {
        return (root, query, builder) -> {
            Join<Object, ContactDetail> contactDetailJoin = root.join("contactDetail");
            return Optional.ofNullable(phone)
                    .map(p -> builder.or(
                            builder.like(contactDetailJoin.get("phoneNumber"), "%" + p + "%"),
                            builder.like(contactDetailJoin.get("alternatePhoneNumber"), "%" + p + "%"),
                            builder.like(contactDetailJoin.get("workPhoneNumber"), "%" + p + "%")
                    ))
                    .orElse(null);
        };
    }

    public static Specification<Practitioner> withEmail(String email) {
        return (root, query, builder) -> {
            Join<Object, ContactDetail> contactDetailJoin = root.join("contactDetail");
            return Optional.ofNullable(email)
                    .map(e -> builder.or(
                            builder.like(contactDetailJoin.get("email"), "%" + e + "%"),
                            builder.like(contactDetailJoin.get("workEmail"), "%" + e + "%")
                    ))
                    .orElse(null);
        };
    }
}
