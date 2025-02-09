package net.sanyal.ehr.patient;

import com.fasterxml.jackson.core.type.TypeReference;

import net.sanyal.ehr.common.BaseTest;
import net.sanyal.ehr.model.common.Address;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.patient.InsuranceDetail;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.service.patient.PatientService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PatientSearchTest extends BaseTest {

        @Autowired
        private PatientService patientService;

        private Patient patient1;
        private Patient patient2;

        @BeforeEach
        void setUp() {
                patient1 = Patient.builder()
                                .socialSecurityNumber("123-45-6789")
                                .purposeOfVisit("Routine Checkup")
                                .healthGoals("Maintain good health")
                                .consent(true)
                                .insuranceDetails(List.of(
                                                InsuranceDetail.builder()
                                                                .provider("HealthCare Inc.")
                                                                .policyNumber("POL123")
                                                                .build()))
                                .build();
                patient1.setName(Name.builder().salutation("Mr.").firstName("John").lastName("Doe").build());
                patient1.setDateOfBirth(LocalDate.of(1990, 5, 20));
                patient1.setContactDetail(ContactDetail.builder()
                                .phoneNumber("111-222-3333")
                                .alternatePhoneNumber("444-555-6666")
                                .workPhoneNumber("777-888-9999")
                                .email("john.doe@example.com")
                                .workEmail("john.doe@work.com")
                                .address(Address.builder()
                                                .houseNumber("123")
                                                .street("Main St")
                                                .city("Springfield")
                                                .postalCode("12345")
                                                .build())
                                .build());
                patient2 = Patient.builder().socialSecurityNumber("987-65-4321")
                                .purposeOfVisit("Specialist Consultation")
                                .healthGoals("Manage existing conditions")
                                .consent(true)
                                .insuranceDetails(List.of(
                                                InsuranceDetail.builder()
                                                                .provider("SecondaryHealth Inc.")
                                                                .policyNumber("POL456")
                                                                .build()))
                                .build();

                patient2.setName(Name.builder().salutation("Ms.").firstName("Jane").lastName("Smith").build());
                patient2.setDateOfBirth(LocalDate.of(1985, 11, 15));
                patient2.setContactDetail(ContactDetail.builder()
                                .phoneNumber("999-888-7777")
                                .alternatePhoneNumber("666-555-4444")
                                .workPhoneNumber("333-222-1111")
                                .email("jane.smith@example.com")
                                .workEmail("jane.smith@work.com")
                                .address(Address.builder()
                                                .houseNumber("456")
                                                .street("Oak St")
                                                .city("Riverside")
                                                .postalCode("54321")
                                                .build())
                                .build());
                // Save both patients
                patientService.createPatient(patient1);
                patientService.createPatient(patient2);
        }

        @AfterEach
        public void cleanup() {
                patientRepository.deleteAll();
        }

        @Test
        @DisplayName("🔍 Search patients by various criteria")
        void searchPatientsByVariousCriteria() throws Exception {
                // Search by first name
                performSearchAndAssert("firstName=John", patient1);

                // Search by last name
                performSearchAndAssert("lastName=Smith", patient2);

                // Search by phone number
                performSearchAndAssert("phone=111-222-3333", patient1);

                // Search by work phone number
                performSearchAndAssert("phone=777-888-9999", patient1);

                // Search by email
                performSearchAndAssert("email=jane.smith@example.com", patient2);

                // Search by work email
                performSearchAndAssert("email=john.doe@work.com", patient1);

                // Search by insurance provider
                performSearchAndAssert("insurance=HealthCare Inc.", patient1);

                // Composite search: last name and insurance provider
                performSearchAndAssert("lastName=Doe&insurance=HealthCare Inc.", patient1);
        }

        private void performSearchAndAssert(String criteria, Patient expectedPatient) throws Exception {
                MvcResult result = mockMvc.perform(get("/api/patients/search?" + criteria)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                String jsonResponse = result.getResponse().getContentAsString();

                List<Patient> patients = objectMapper.readTree(jsonResponse)
                                .get("content")
                                .traverse(objectMapper)
                                .readValueAs(new TypeReference<List<Patient>>() {
                                });

                // Assert that the correct patient is returned in the content list
                assertThat(patients)
                                .hasSize(1)
                                .extracting(Patient::getSocialSecurityNumber)
                                .containsExactly(expectedPatient.getSocialSecurityNumber());
        }
}
