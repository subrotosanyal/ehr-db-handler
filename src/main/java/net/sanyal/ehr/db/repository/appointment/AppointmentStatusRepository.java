package net.sanyal.ehr.db.repository.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.appointment.AppointmentStatus;

@Repository
@RepositoryRestResource
public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Long> {}