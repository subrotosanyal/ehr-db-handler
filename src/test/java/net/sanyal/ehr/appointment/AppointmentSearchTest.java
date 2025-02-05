package net.sanyal.ehr.appointment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import lombok.extern.slf4j.Slf4j;
import net.sanyal.ehr.common.BaseTest;
import net.sanyal.ehr.common.TestUtil;
import net.sanyal.ehr.model.appointment.Appointment;
import net.sanyal.ehr.model.appointment.AppointmentPriority;
import net.sanyal.ehr.model.appointment.AppointmentStatus;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.model.practitioner.Practitioner;
import net.sanyal.ehr.model.service.ServiceCategory;
import net.sanyal.ehr.model.service.ServiceType;
import net.sanyal.ehr.service.appointment.AppointmentService;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AppointmentSearchTest extends BaseTest {
        private static Patient testPatient;
        private static Practitioner testPractitioner;

        @BeforeAll
        static void setupAppointments(@Autowired AppointmentService appointmentService) {
                testPatient = Patient.builder()
                                .name(Name.builder().salutation("Mr.").firstName("John").lastName("Doe").build())
                                .socialSecurityNumber("123-45-6789")
                                .gender(male)
                                .race(asian)
                                .ethnicity(other)
                                .build();

                testPractitioner = Practitioner.builder()
                                .name(Name.builder().salutation("Dr.").firstName("Jane").lastName("Doe").build())
                                .npi("NPI-12345")
                                .gender(female)
                                .race(asian)
                                .ethnicity(other)
                                .licenseNumber("LICENSE-12345")
                                .build();

                final Practitioner practitioner = practitionerRepository.save(testPractitioner);
                final Patient patient = patientRepository.save(testPatient);
                IntStream.rangeClosed(1, 15).forEach(i -> appointmentService.createAppointment(
                                Appointment.builder()
                                                .patient(patient)
                                                .practitioner(practitioner)
                                                .appointmentType(routineAppointmentType)
                                                .priority(
                                                                AppointmentPriority.builder().appointmentPriorityId(3L)
                                                                                .appointmentPriorityName("High")
                                                                                .build())
                                                .appointmentStatus(
                                                                AppointmentStatus.builder().appointmentStatusId(1L)
                                                                                .appointmentStatusName("Scheduled")
                                                                                .build())
                                                .serviceCategory(
                                                                ServiceCategory.builder().serviceCategoryId(1L)
                                                                                .serviceCategoryName("Opthalmology")
                                                                                .build())
                                                .serviceType(ServiceType.builder().serviceTypeId(1L)
                                                                .serviceTypeName("Consultation").build())
                                                .description("Test Appointment " + i)
                                                .actualStartTime(LocalDateTime.of(2023, 1, i % 20 + 1, i % 10 + 1,
                                                                i % 40 + 1))
                                                .actualEndTime(LocalDateTime.of(2023, 1, i % 20 + 1, i % 10 + 1,
                                                                i % 40 + 1))
                                                .patientInstructions("Test Instructions " + i)
                                                .location("Test Location " + i)
                                                .build()));
        }

        @Test
        @DisplayName("✅ Verify pagination of appointments")
        void verifyPagination() throws Exception {
                int pageSize = 5;
                for (int page = 0; page < 2; page++) {
                        MvcResult result = mockMvc
                                        .perform(get("/api/appointments/search?page=" + page + "&size=" + pageSize)
                                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isOk())
                                        .andReturn();

                        List<Appointment> appointments = (List<Appointment>) TestUtil
                                        .getBaseEntityFromSearchJsonResponse(result, objectMapper, Appointment.class);
                        assertThat(appointments).hasSize(pageSize);
                }
        }

        @Test
        @DisplayName("✅ Search appointments by patient ID")
        void searchByPatientId() throws Exception {
                MvcResult result = mockMvc.perform(get("/api/appointments/search")
                                .param("patientId", testPatient.getPatientId().toString())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Appointment> appointments = (List<Appointment>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(result, objectMapper, Appointment.class);

                assertThat(appointments).hasSize(15);
        }

        @Test
        @DisplayName("✅ Search appointments by practitioner ID")
        void searchByPractitionerId() throws Exception {
                MvcResult result = mockMvc.perform(get("/api/appointments/search")
                                .param("practitionerId", testPractitioner.getPractitionerId().toString())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Appointment> appointments = (List<Appointment>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(result, objectMapper, Appointment.class);

                assertThat(appointments).hasSize(15);
        }

        @Test
        @DisplayName("✅ Search appointments by appointment type")
        void searchByAppointmentType() throws Exception {

                MvcResult result = mockMvc.perform(get("/api/appointments/search")
                                .param("appointmentType", routineAppointmentType.getAppointmentTypeName())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Appointment> appointments = (List<Appointment>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(result, objectMapper, Appointment.class);

                assertThat(appointments).hasSize(15);
        }

        @Test
        @DisplayName("✅ Search appointments by composite criteria: patient and practitioner ID")
        void searchByPatientAndPractitioner() throws Exception {

                MvcResult result = mockMvc.perform(get("/api/appointments/search")
                                .param("patientId", testPatient.getPatientId().toString())
                                .param("practitionerId", testPractitioner.getPractitionerId().toString())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Appointment> appointments = (List<Appointment>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(result, objectMapper, Appointment.class);

                assertThat(appointments).hasSize(15);
        }

        @Test
        @DisplayName("✅ Search appointments by composite criteria: priority and status")
        void searchByPriorityAndStatus() throws Exception {
                String priorityName = "High"; // Matching the setup
                String statusName = "Scheduled"; // Matching the setup

                MvcResult result = mockMvc.perform(get("/api/appointments/search")
                                .param("priority", priorityName)
                                .param("status", statusName)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Appointment> appointments = (List<Appointment>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(result, objectMapper, Appointment.class);

                assertThat(appointments).hasSize(15);
        }

        @Test
        @DisplayName("✅ Search appointments by multiple composite criteria")
        void searchByMultipleCriteria() throws Exception {
                String appointmentTypeName = "Routine"; // Matching the setup
                String statusName = "Scheduled"; // Matching the setup

                MvcResult result = mockMvc.perform(get("/api/appointments/search")
                                .param("patientId", testPatient.getPatientId().toString())
                                .param("practitionerId", testPractitioner.getPractitionerId().toString())
                                .param("appointmentType", appointmentTypeName)
                                .param("status", statusName)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Appointment> appointments = (List<Appointment>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(result, objectMapper, Appointment.class);

                assertThat(appointments).hasSize(15);
        }

}