package net.sanyal.ehr.controller.practitioner;

import net.sanyal.ehr.db.specification.practitioner.PractitionerSpecification;
import net.sanyal.ehr.model.practitioner.Practitioner;
import net.sanyal.ehr.service.practitioner.PractitionerService;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/practitioners")
public class PractitionerController {

    private final PractitionerService practitionerService;

    public PractitionerController(PractitionerService practitionerService) {
        this.practitionerService = practitionerService;
    }

    @PostMapping
    public ResponseEntity<Practitioner> createPractitioner(@RequestBody Practitioner practitioner) {
        Practitioner createdPractitioner = practitionerService.createPractitioner(practitioner);
        return ResponseEntity.ok(createdPractitioner);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practitioner> getPractitioner(@PathVariable Long id) {
        Practitioner practitioner = practitionerService.getPractitioner(id);
        return ResponseEntity.ok(practitioner);
    }

    @GetMapping
    public Page<Practitioner> getAllPractitioners(Pageable pageable) {
        return practitionerService.findAllPractitioners(pageable);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Practitioner> updatePractitioner(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Practitioner practitioner = practitionerService.updatePractitioner(id, updates);
        return ResponseEntity.ok(practitioner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractitioner(@PathVariable Long id) {
        practitionerService.deletePractitioner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Practitioner>> searchPractitioners(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String npi,
            @RequestParam(required = false) String specialty,
            Pageable pageable) {

                Specification<Practitioner> spec = Specification.where(PractitionerSpecification.withAddress(address))
                .and(PractitionerSpecification.withPhoneNumber(phone))
                .and(PractitionerSpecification.withEmail(email))
                .and(PractitionerSpecification.withLastName(lastName))
                .and(PractitionerSpecification.withFirstName(firstName))
                .and(PractitionerSpecification.withNpi(npi))
                .and(PractitionerSpecification.withSpecialty(specialty));

        Page<Practitioner> practitioners = practitionerService.searchPractitioners(spec, pageable);
        return ResponseEntity.ok(practitioners);
    }
}
