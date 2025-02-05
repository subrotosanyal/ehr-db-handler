package net.sanyal.ehr.db.repository.practitioner;

import net.sanyal.ehr.model.practitioner.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PractitionerRepository extends JpaRepository<Practitioner, Long>, JpaSpecificationExecutor<Practitioner> {
}
