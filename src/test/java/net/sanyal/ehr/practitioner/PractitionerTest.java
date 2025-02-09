package net.sanyal.ehr.practitioner;

import net.sanyal.ehr.common.BaseTest;
import net.sanyal.ehr.common.TestUtil;
import net.sanyal.ehr.model.common.Address;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.practitioner.Practitioner;
import net.sanyal.ehr.service.practitioner.PractitionerService;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PractitionerTest extends BaseTest {

        @Autowired
        private PractitionerService practitionerService;

        private Practitioner practitioner;

        @BeforeEach
        void setup() {
                practitioner = Practitioner.builder()
                                .name(Name.builder().salutation("Dr.").firstName("Alice").lastName("Smith").build())
                                .npi("NPI-12345")
                                .dateOfBirth(LocalDate.of(1980, 10, 15))
                                .specialty("Cardiology")
                                .licenseNumber("LIC-98765")
                                .active(true)
                                .effectiveFrom(LocalDate.of(2020, 1, 1))
                                .contactDetail(ContactDetail.builder()
                                                .phoneNumber("123-456-7890")
                                                .email("alice.smith@example.com")
                                                .address(Address.builder()
                                                                .houseNumber("12A")
                                                                .street("Maple Street")
                                                                .city("Springfield")
                                                                .postalCode("12345")
                                                                .build())
                                                .build())
                                .build();
        }

        @AfterEach
        void cleanUp() {
                practitionerRepository.deleteAll();
        }

        @Test
        @DisplayName("✅ Create a practitioner")
        void createPractitioner() throws Exception {
                String jsonRequest = objectMapper.writeValueAsString(practitioner);

                MvcResult result = mockMvc.perform(post("/api/practitioners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isOk())
                                .andReturn();

                String jsonResponse = result.getResponse().getContentAsString();
                Practitioner createdPractitioner = objectMapper.readValue(jsonResponse, Practitioner.class);

                assertThat(createdPractitioner.getPractitionerId()).isNotNull();
                assertThat(createdPractitioner.getName().getFirstName())
                                .isEqualTo(practitioner.getName().getFirstName());
        }

        @Test
        @DisplayName("✅ Get a practitioner by ID")
        void getPractitionerById() throws Exception {
                // First, create the practitioner
                String jsonRequest = objectMapper.writeValueAsString(practitioner);
                MvcResult createResult = mockMvc.perform(post("/api/practitioners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isOk())
                                .andReturn();

                Practitioner createdPractitioner = objectMapper.readValue(
                                createResult.getResponse().getContentAsString(),
                                Practitioner.class);

                // Retrieve it by ID
                mockMvc.perform(get("/api/practitioners/" + createdPractitioner.getPractitionerId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name.firstName").value(practitioner.getName().getFirstName()));
        }

        @Test
        @DisplayName("✅ Search practitioners by NPI")
        void searchPractitionersByNpi() throws Exception {
                // First, create the practitioner
                String jsonRequest = objectMapper.writeValueAsString(practitioner);
                mockMvc.perform(post("/api/practitioners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isOk());

                // Search by NPI
                MvcResult searchResult = mockMvc.perform(get("/api/practitioners/search?npi=NPI-12345")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Practitioner> practitioners = (List<Practitioner>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(searchResult, objectMapper, Practitioner.class);

                assertThat(practitioners).hasSize(1);
                assertThat(practitioners.get(0).getNpi()).isEqualTo(practitioner.getNpi());
        }

        @Test
        @DisplayName("✅ Search practitioners by specialty")
        void searchPractitionersBySpecialty() throws Exception {
                // First, create the practitioner
                String jsonRequest = objectMapper.writeValueAsString(practitioner);
                mockMvc.perform(post("/api/practitioners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isOk());

                // Search by specialty
                MvcResult searchResult = mockMvc.perform(get("/api/practitioners/search?specialty=Cardiology")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Practitioner> practitioners = (List<Practitioner>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(searchResult, objectMapper, Practitioner.class);

                // Assertions
                assertThat(practitioners).hasSize(1);
                assertThat(practitioners.get(0).getSpecialty()).isEqualTo(practitioner.getSpecialty());
        }

        @Test
        void shouldUpdatePractitioner() throws Exception {
                // Setup practitioner entity
                Practitioner practitioner = Practitioner.builder()
                                .name(Name.builder().firstName(null).lastName(null).build())
                                .npi("NPI-12345")
                                .licenseNumber("123-45-6789")
                                .dateOfBirth(LocalDate.of(1985, 5, 20))
                                .build();
                // Save initial data
                practitioner = practitionerService.createPractitioner(practitioner);

                // Update the practitioner
                Practitioner updatedPractitioner = Practitioner.builder()
                                .name(Name.builder().firstName("Alice Updated").lastName(null).build())
                                .npi("NPI-22345")
                                .licenseNumber("123-45-6789")
                                .contactDetail(ContactDetail.builder()
                                                .address(Address.builder().houseNumber("789").street("Oak St")
                                                                .city("Anytown").province("CA").postalCode("12345")
                                                                .build())
                                                .build())
                                .dateOfBirth(LocalDate.of(1985, 5, 20))
                                .build();
                // Save the updated practitioner
                String jsonResponse = mockMvc.perform(patch("/api/practitioners/" + practitioner.getPractitionerId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedPractitioner)))
                                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

                // Validate that the practitioner was updated
                Practitioner updatedPractitionerFromHttp = objectMapper.readValue(jsonResponse, Practitioner.class);
                assertEquals("Alice Updated", updatedPractitionerFromHttp.getName().getFirstName());
                assertEquals("789", updatedPractitionerFromHttp.getContactDetail().getAddress().getHouseNumber());
                assertEquals("Oak St", updatedPractitionerFromHttp.getContactDetail().getAddress().getStreet());
                assertEquals("Anytown", updatedPractitionerFromHttp.getContactDetail().getAddress().getCity());
                assertEquals("CA", updatedPractitionerFromHttp.getContactDetail().getAddress().getProvince());
                assertEquals("12345", updatedPractitionerFromHttp.getContactDetail().getAddress().getPostalCode());
                assertEquals("123-45-6789", updatedPractitionerFromHttp.getLicenseNumber());
                assertEquals("NPI-22345", updatedPractitionerFromHttp.getNpi());
        }

        @Test
        @DisplayName("✅ Search practitioners by phone number")
        void searchPractitionersByPhoneNumber() throws Exception {
                // First, create the practitioner
                String jsonRequest = objectMapper.writeValueAsString(practitioner);
                mockMvc.perform(post("/api/practitioners")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isOk());

                // Search by phone number
                MvcResult searchResult = mockMvc.perform(get("/api/practitioners/search?phoneNumber=123-456-7890")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                List<Practitioner> practitioners = (List<Practitioner>) TestUtil
                                .getBaseEntityFromSearchJsonResponse(searchResult, objectMapper, Practitioner.class);

                assertThat(practitioners).hasSize(1);
                assertThat(practitioners.get(0).getContactDetail().getPhoneNumber())
                                .isEqualTo(practitioner.getContactDetail().getPhoneNumber());
        }

        @Test
        void shouldDeletePractitioner() throws Exception {
                // Arrange: Create and save a test practitioner
                Practitioner practitioner = Practitioner.builder()
                                .name(Name.builder().firstName(null).lastName(null).build())
                                .npi("NPI-12345")
                                .licenseNumber("123-45-6789")
                                .dateOfBirth(LocalDate.of(1985, 5, 20))
                                .build();
                // Save initial data
                practitioner = practitionerService.createPractitioner(practitioner);

                // Act: Send DELETE request
                mockMvc.perform(delete("/api/practitioners/{id}", practitioner.getPractitionerId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                // Assert: Verify the practitioner is no longer in the database
                assertFalse(practitionerRepository.existsById(practitioner.getPractitionerId()));
        }
}
