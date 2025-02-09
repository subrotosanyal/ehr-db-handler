package net.sanyal.ehr.patient;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sanyal.ehr.common.BaseTest;
import net.sanyal.ehr.model.common.Address;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.patient.Immunization;
import net.sanyal.ehr.model.patient.InsuranceDetail;
import net.sanyal.ehr.model.patient.MedicalHistory;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.service.patient.PatientService;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientTest extends BaseTest {

        @Autowired
        private PatientService patientService;

        @Autowired
        private ObjectMapper objectMapper;

        @AfterEach
        void cleanUp() {
                patientRepository.deleteAll();
        }

        @Test
        @DisplayName("✅ Add a new patient")
        void addPatient() throws Exception {
                String patientJson = """
                                    {
                                        "name" : {
                                            "salutation": "Mr.",
                                            "firstName": "John",
                                            "lastName": "Doe"
                                        },
                                        "socialSecurityNumber": "123-45-6789",
                                        "purposeOfVisit": "Routine Checkup",
                                        "healthGoals": "Maintain good health",
                                        "dateOfBirth": "1985-02-15",
                                        "consent": true,
                                        "gender": {
                                            "genderId": 1
                                        },
                                        "race": {
                                            "raceId": 2
                                        },
                                        "ethnicity": {
                                            "ethnicityId": 3
                                        },
                                        "contactDetail": {
                                            "phoneNumber": "1234567890",
                                            "email": "john.doe@example.com",
                                            "address": {"houseNumber": "123", "street": "Main St", "city": "Anytown", "province": "CA", "postalCode": "12345"}
                                        },
                                        "insuranceDetails": [
                                            {
                                                "provider": "HealthCare Inc.",
                                                "policyNumber": "123456789"
                                            },
                                            {
                                                "provider": "SecondaryHealth Inc.",
                                                "policyNumber": "987654321"
                                            }
                                        ],
                                        "medicalHistory": {
                                            "allergies": "Peanuts, Shellfish",
                                            "immunizations": [
                                                {
                                                    "vaccineName": "Hepatitis B",
                                                    "dateAdministered": "2015-05-10"
                                                },
                                                {
                                                    "vaccineName": "Influenza",
                                                    "dateAdministered": "2021-11-01"
                                                }
                                            ]
                                        },
                                        "substanceConsumptions": [
                                            {
                                                "type": "Alcohol",
                                                "frequency": "Occasionally",
                                                "amount": "Drinking 2-3 glasses a day"
                                            },
                                            {
                                                "type": "Caffeine",
                                                "frequency": "Daily",
                                                "amount": "Consuming 1-2 cups a day"
                                            }
                                        ]
                                    }
                                """;

                mockMvc.perform(post("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patientJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name.firstName").value("John"));
        }

        @Test
        @DisplayName("✅ Delete a Patient")
        void deletePatient() throws Exception {
                // Create a patient
                Patient patient = new Patient();
                patient.setName(Name.builder().salutation("Ms.").firstName("Alice").lastName("Brown").build());
                patient.setSocialSecurityNumber("345-67-8901");
                patient.setPurposeOfVisit("Routine Checkup");
                patient.setDateOfBirth(LocalDate.of(1987, 5, 15));
                patient.setConsent(true);
                patient = patientService.createPatient(patient);

                mockMvc.perform(delete("/api/patients/" + patient.getPatientId()))
                                .andExpect(status().isNoContent());

                Optional<Patient> deletedPatient = patientRepository.findById(patient.getPatientId());
                Assertions.assertTrue(deletedPatient.isEmpty(), "Patient should be deleted");
        }

        @Test
        @DisplayName("✅ List all patients")
        void listAllPatients() throws Exception {
                // Create multiple patients
                Patient patient1 = new Patient();
                patient1.setName(Name.builder().salutation("Ms.").firstName("Alice").lastName("Brown").build());
                patient1.setSocialSecurityNumber("567-89-0123");
                patient1.setPurposeOfVisit("Eye Checkup");
                patient1.setDateOfBirth(LocalDate.of(1992, 8, 20));
                patient1.setConsent(true);
                patientRepository.save(patient1);

                Patient patient2 = new Patient();
                patient2.setName(Name.builder().salutation("Mr.").firstName("Bob").lastName("Green").build());
                patient2.setSocialSecurityNumber("678-90-1234");
                patient2.setPurposeOfVisit("Dental Cleaning");
                patient2.setDateOfBirth(LocalDate.of(1985, 11, 12));
                patient2.setConsent(true);
                patientRepository.save(patient2);

                String jsonResponse = mockMvc.perform(get("/api/patients"))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                List<Patient> patients = objectMapper.readTree(jsonResponse)
                                .get("content")
                                .traverse(objectMapper)
                                .readValueAs(new TypeReference<List<Patient>>() {
                                });
                assertThat(patients).hasSize(2);
        }

        @Test
        @DisplayName("✅ Update multiple fields of Patient")
        void updateMultipleFields() throws Exception {
                // Create a patient first
                Patient patient = Patient.builder()
                                .socialSecurityNumber("234-56-7890")
                                .purposeOfVisit("Flu Checkup")
                                .consent(true)
                                .medicalHistory(MedicalHistory.builder()
                                                .allergies("Asthma")
                                                .immunizations(new ArrayList<>(List.of(
                                                                Immunization.builder()
                                                                                .vaccineName("Hepatitis B")
                                                                                .dateAdministered(LocalDate.of(2015, 5,
                                                                                                10))
                                                                                .build())))
                                                .build())
                                .insuranceDetails(new ArrayList<>(List.of(
                                                InsuranceDetail.builder()
                                                                .provider("ProviderX")
                                                                .policyNumber("123456789")
                                                                .build())))
                                .build();
                patient.setName(Name.builder().salutation("Ms.").firstName("Jane").lastName("Doe").build());
                patient.setGender(female);
                patient.setDateOfBirth(LocalDate.of(1990, 3, 10));
                patient.setContactDetail(ContactDetail.builder()
                                .phoneNumber("1234567890")
                                .email("jane.doe@example.com")
                                .address(Address.builder().houseNumber("23").street("Main St")
                                                .city("Anytown").province("CA").postalCode("12345")
                                                .build())
                                .build());
                patient = patientRepository.save(patient);

                Patient updatedPatient = patient.toBuilder().build();
                updatedPatient.setName(Name.builder().firstName("Janet").lastName("Smith").build());
                updatedPatient.setGender(female);
                updatedPatient.setContactDetail(ContactDetail.builder()
                                .phoneNumber("9876543210")
                                .email("janet.smith@example.com")
                                .address(Address.builder().houseNumber("789").street("Oak St")
                                                .city("Anytown").province("CA").postalCode("12345")
                                                .build())
                                .build());
                updatedPatient.setMedicalHistory(MedicalHistory.builder()
                                .allergies("Diabetes")
                                .immunizations(new ArrayList<>(List.of(Immunization.builder()
                                                .vaccineName("Influenza")
                                                .dateAdministered(LocalDate.of(2021, 12, 1))
                                                .build())))
                                .build());
                updatedPatient.setInsuranceDetails(new ArrayList<>(List.of(InsuranceDetail.builder()
                                .provider("ProviderY")
                                .policyNumber("987654321")
                                .build())));
                String updateJson = objectMapper.writeValueAsString(updatedPatient);
                String jsonResponse = mockMvc.perform(patch("/api/patients/" + patient.getPatientId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                Patient updatedPatientFromHttp = objectMapper.readValue(jsonResponse, Patient.class);
                Assertions.assertEquals(updatedPatientFromHttp.getName().getFirstName(), "Janet",
                                "Patient name should be updated");
                Assertions.assertEquals(updatedPatientFromHttp.getName().getLastName(), "Smith",
                                "Patient last name should be updated");
                Assertions.assertEquals(updatedPatientFromHttp.getContactDetail().getPhoneNumber(), "9876543210",
                                "Patient phone number should be updated");
                Assertions.assertEquals(updatedPatientFromHttp.getContactDetail().getEmail(), "janet.smith@example.com",
                                "Patient email should be updated");
                Assertions.assertEquals(updatedPatientFromHttp.getContactDetail().getAddress(),
                                Address.builder()
                                                .addressId(updatedPatientFromHttp.getContactDetail().getAddress()
                                                                .getAddressId())
                                                .houseNumber("789").street("Oak St").city("Anytown").province("CA")
                                                .postalCode("12345").build(),
                                "Patient address should be updated");
                Assertions.assertEquals(updatedPatientFromHttp.getMedicalHistory().getAllergies(), "Diabetes",
                                "Patient allergies should be updated");
                Assertions.assertEquals(
                                updatedPatientFromHttp.getMedicalHistory().getImmunizations().get(0).getVaccineName(),
                                "Influenza", "Patient immunization name should be updated");
                Assertions.assertEquals(
                                updatedPatientFromHttp.getMedicalHistory().getImmunizations().get(0)
                                                .getDateAdministered(),
                                LocalDate.of(2021, 12, 1), "Patient immunization date should be updated");
        }

        @Test
        @DisplayName("✅ Round Trip Test Create and Update")
        void roundTripTestCreateUpdate() throws Exception {
                // Create a patient first
                String patientCreateJson = """
                                {"name":{"salutation":"Mr.","firstName":"Awesome","lastName":"Dude"},"dateOfBirth":"2025-02-03","socialSecurityNumber":"111-11-111","purposeOfVisit":"ccc","healthGoals":"","consent":false,"contactDetail":{"phoneNumber":"","alternatePhoneNumber":"","workPhoneNumber":"","email":"","workEmail":"","address":{"houseNumber":"","street":"","city":"","district":"","province":"","country":"","postalCode":""}},"insuranceDetails":[],"medicalHistory":{"childhoodIllness":"","surgeries":"","historyBloodTransfusion":"","allergies":"","exerciseRoutine":"","diet":"","mentalHealthQuestions":"","familyHealthHistory":"","changes":"","currentSymptoms":"","healthGoals":"","medications":[],"immunizations":[]},"gender":{"genderId":1,"genderName":"Male"},"race":{"raceId":2,"raceName":"Non-Hispanic or Latino"},"ethnicity":{"ethnicityId":3,"ethnicityName":"White"}}
                                """;
                String jsonResponse = mockMvc.perform(post("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patientCreateJson))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Patient createdPatient = objectMapper.readValue(jsonResponse, Patient.class);
                createdPatient.setGender(female);

                jsonResponse = mockMvc.perform(patch("/api/patients/" + createdPatient.getPatientId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createdPatient)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                Patient updatedPatient = objectMapper.readValue(jsonResponse, Patient.class);
                assertThat(updatedPatient.getGender().getGenderId()).isEqualTo(2);
        }
}
