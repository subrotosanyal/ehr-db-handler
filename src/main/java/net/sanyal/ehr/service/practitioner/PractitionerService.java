package net.sanyal.ehr.service.practitioner;

import net.sanyal.ehr.db.repository.practitioner.PractitionerRepository;
import net.sanyal.ehr.model.practitioner.Practitioner;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class PractitionerService {
    @Autowired
    private ObjectMapper objectMapper;

    private final PractitionerRepository practitionerRepository;

    public PractitionerService(PractitionerRepository practitionerRepository) {
        this.practitionerRepository = practitionerRepository;
    }

    public Practitioner createPractitioner(Practitioner practitioner) {
        return practitionerRepository.save(practitioner);
    }

    public Page<Practitioner> searchPractitioners(Specification<Practitioner> spec, Pageable pageable) {
        return practitionerRepository.findAll(spec, pageable);
    }

    public Practitioner getPractitioner(Long id) {
        return practitionerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Practitioner not found"));
    }

    public Page<Practitioner> findAllPractitioners(Pageable pageable) {
        return practitionerRepository.findAll(pageable);
    }

    public Practitioner updatePractitioner(Long id, Map<String, Object> updates) {
        Practitioner updatedPractitioner = objectMapper.convertValue(updates, Practitioner.class);
        return practitionerRepository.saveAndFlush(updatedPractitioner);
    }

    public void deletePractitioner(Long id) {
        practitionerRepository.deleteById(id);
    }
}
