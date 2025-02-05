package net.sanyal.ehr.db.specification.appointment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentCriteria {
    private Long patientId;
    private Long practitionerId;
    private String appointmentType;
    private String priority;
    private String status;
}
