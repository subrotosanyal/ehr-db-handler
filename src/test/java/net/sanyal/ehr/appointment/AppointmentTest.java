package net.sanyal.ehr.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.sanyal.ehr.common.BaseTest;
import net.sanyal.ehr.model.appointment.Appointment;
import net.sanyal.ehr.model.appointment.AppointmentPriority;
import net.sanyal.ehr.model.appointment.AppointmentStatus;
import net.sanyal.ehr.model.appointment.AppointmentType;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.model.practitioner.Practitioner;
import net.sanyal.ehr.model.service.ServiceCategory;
import net.sanyal.ehr.model.service.ServiceType;

@Slf4j
@Transactional
public class AppointmentTest extends BaseTest {

        private Appointment testAppointment;

        @BeforeEach
        public void setUp() {
                Patient testPatient = Patient.builder()
                                .name(Name.builder().salutation("Mr.").firstName("John").lastName("Doe").build())
                                .socialSecurityNumber("123-45-6789")
                                .gender(male)
                                .race(asian)
                                .ethnicity(other)
                                .purposeOfVisit("Regular checkup")
                                .build();

                Practitioner testPractitioner = Practitioner.builder()
                                .name(Name.builder().salutation("Dr.").firstName("Jane").lastName("Doe").build())
                                .npi("NPI-12345")
                                .gender(female)
                                .race(asian)
                                .ethnicity(other)
                                .dateOfBirth(LocalDate.now().minusYears(30))
                                .licenseNumber("LICENSE-12345")
                                .build();

                testPractitioner = practitionerRepository.saveAndFlush(testPractitioner);
                testPatient = patientRepository.saveAndFlush(testPatient);

                // Build the Appointment
                testAppointment = Appointment.builder()
                                .patient(testPatient)
                                .practitioner(testPractitioner)
                                .appointmentType(AppointmentType.builder().appointmentTypeId(1L)
                                                .appointmentTypeName("Regular").build())
                                .priority(
                                                AppointmentPriority.builder().appointmentPriorityId(1L)
                                                                .appointmentPriorityName("High").build())
                                .appointmentStatus(
                                                AppointmentStatus.builder().appointmentStatusId(1L)
                                                                .appointmentStatusName("Scheduled").build())
                                .serviceCategory(
                                                ServiceCategory.builder().serviceCategoryId(1L)
                                                                .serviceCategoryName("Opthalmology").build())
                                .serviceType(ServiceType.builder().serviceTypeId(1L).serviceTypeName("Consultation")
                                                .build())
                                .build();

                testAppointment = appointmentRepository.saveAndFlush(testAppointment);
                log.info("Inserted a test Appointment record: {}", testAppointment);
        }

        @AfterEach
        public void cleanup() {
                log.info("Cleaning up all the resources/entities created for testing");
                patientRepository.deleteAll();
                practitionerRepository.deleteAll();
                appointmentRepository.deleteAll();
        }

        @Test
        public void shouldCreateAppointment() throws Exception {
                Appointment newAppointment = Appointment.builder()
                                .appointmentId(null)
                                .patient(patientRepository.findById(testAppointment.getPatient().getPatientId()).orElseThrow())
                                .practitioner(practitionerRepository.findById(testAppointment.getPractitioner().getPractitionerId()).orElseThrow())
                                .appointmentType(testAppointment.getAppointmentType())
                                .priority(testAppointment.getPriority())
                                .appointmentStatus(testAppointment.getAppointmentStatus())
                                .serviceCategory(testAppointment.getServiceCategory())
                                .serviceType(testAppointment.getServiceType())
                                .build();

                String writeValueAsString = objectMapper.writeValueAsString(newAppointment);
                String response = mockMvc.perform(post("/api/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeValueAsString))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                Appointment createdAppointment = objectMapper.readValue(response, Appointment.class);
                assertNotNull(createdAppointment);
                assertEquals(createdAppointment.getPatient().getName().getFirstName(), "John");
                assertEquals(createdAppointment.getPractitioner().getName().getFirstName(), "Jane");
                assertEquals(createdAppointment.getServiceCategory().getServiceCategoryName(),
                                testAppointment.getServiceCategory().getServiceCategoryName());
                assertEquals(createdAppointment.getServiceType().getServiceTypeName(),
                                testAppointment.getServiceType().getServiceTypeName());
        }

        @Test
        public void shouldGetAppointment() throws Exception {
                String response = mockMvc.perform(get("/api/appointments/" + testAppointment.getAppointmentId()))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                Appointment fetchedAppointment = objectMapper.readValue(response, Appointment.class);
                assertEquals(fetchedAppointment.getAppointmentId(), testAppointment.getAppointmentId());
                assertEquals(fetchedAppointment.getPatient().getName().getFirstName(), "John");
                assertEquals(fetchedAppointment.getPractitioner().getName().getFirstName(), "Jane");
                assertEquals(fetchedAppointment.getServiceCategory().getServiceCategoryName(),
                                testAppointment.getServiceCategory().getServiceCategoryName());
                assertEquals(fetchedAppointment.getServiceType().getServiceTypeName(),
                                testAppointment.getServiceType().getServiceTypeName());
        }

        @Test
        public void shouldUpdateAppointment() throws Exception {
                Appointment updatedAppointment = testAppointment.toBuilder()
                                .appointmentStatus(
                                                AppointmentStatus.builder().appointmentStatusId(2L)
                                                                .appointmentStatusName("Completed").build())
                                .build();
                String updateMap = objectMapper.writeValueAsString(updatedAppointment);
                String response = mockMvc.perform(patch("/api/appointments/" + testAppointment.getAppointmentId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateMap))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                Appointment updatedAppointmentAfterUpdate = objectMapper.readValue(response, Appointment.class);
                assertEquals(updatedAppointmentAfterUpdate.getAppointmentStatus().getAppointmentStatusName(),
                                "Completed");
        }

        @Test
        public void shouldDeleteAppointment() throws Exception {
                mockMvc.perform(delete("/api/appointments/" + testAppointment.getAppointmentId()))
                                .andExpect(status().isNoContent());

                Optional<Appointment> deletedAppointment = appointmentRepository
                                .findById(testAppointment.getAppointmentId());
                assertTrue(deletedAppointment.isEmpty());
        }
}
