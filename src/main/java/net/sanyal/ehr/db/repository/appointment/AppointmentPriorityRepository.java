package net.sanyal.ehr.db.repository.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.appointment.AppointmentPriority;

@Repository
public interface AppointmentPriorityRepository extends JpaRepository<AppointmentPriority, Long> {}