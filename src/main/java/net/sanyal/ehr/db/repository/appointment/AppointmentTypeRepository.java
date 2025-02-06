package net.sanyal.ehr.db.repository.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.appointment.AppointmentType;

@Repository
@RepositoryRestResource
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {}