package net.sanyal.ehr.practitioner;

import net.sanyal.ehr.common.BaseTest;
import net.sanyal.ehr.common.TestUtil;
import net.sanyal.ehr.model.common.Address;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.practitioner.Practitioner;
import net.sanyal.ehr.service.practitioner.PractitionerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PractitionerSearchTest extends BaseTest {

    @BeforeAll
    static void setupPractitioners(@Autowired PractitionerService practitionerService) {
        IntStream.rangeClosed(1, 10).forEach(i -> practitionerService.createPractitioner(
                Practitioner.builder()
                        .name(net.sanyal.ehr.model.common.Name.builder()
                                .salutation("Dr.")
                                .firstName("Alice" + i)
                                .lastName("Smith" + i)
                                .build())
                        .npi("NPI-12345-" + i)
                        .specialty("Cardiology")
                        .effectiveFrom(LocalDate.of(2020, 1, 1))
                        .licenseNumber("LIC-98765-" + i)
                        .contactDetail(ContactDetail.builder()
                                .phoneNumber("123-456-7890")
                                .email("alice.smith@example.com")
                                .address(Address.builder()
                                        .houseNumber("12A" + i)
                                        .street("Maple Street")
                                        .city("Springfield")
                                        .postalCode("12345")
                                        .build())
                                .build())
                        .active(true)
                        .build()));
    }

    @Test
    @DisplayName("✅ Verify pagination of practitioners")
    void verifyPagination() throws Exception {
        int pageSize = 5;
        for (int page = 0; page < 2; page++) {
            MvcResult result = mockMvc.perform(get("/api/practitioners?page=" + page + "&size=" + pageSize)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            List<Practitioner> practitioners = (List<Practitioner>) TestUtil.getBaseEntityFromSearchJsonResponse(result, objectMapper, Practitioner.class);
            assertThat(practitioners).hasSize(pageSize);
            assertThat(practitioners.get(0).getName().getFirstName()).contains("Alice" + (page * pageSize + 1));
        }
    }

    @Test
    @DisplayName("🔍 Search practitioners by NPI")
    void searchPractitionersByNpi() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/practitioners/search?npi=NPI-12345-5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Practitioner> practitioners = (List<Practitioner>) TestUtil.getBaseEntityFromSearchJsonResponse(result, objectMapper, Practitioner.class);
        assertThat(practitioners).hasSize(1);
        assertThat(practitioners.get(0).getNpi()).isEqualTo("NPI-12345-5");
    }

    @Test
    @DisplayName("🔍 Search practitioners by name")
    void searchPractitionersByName() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/practitioners/search?name=Alice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Practitioner> practitioners = (List<Practitioner>) TestUtil.getBaseEntityFromSearchJsonResponse(result, objectMapper, Practitioner.class);
        assertThat(practitioners).hasSize(10);
        assertThat(practitioners.get(0).getName().getFirstName()).contains("Alice");
    }

    @Test
    @DisplayName("🔍 Search practitioners by specialty")
    void searchPractitionersBySpecialty() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/practitioners/search?specialty=Cardiology")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Practitioner> practitioners = (List<Practitioner>) TestUtil.getBaseEntityFromSearchJsonResponse(result, objectMapper, Practitioner.class);
        assertThat(practitioners).isNotEmpty();
        assertThat(practitioners.get(0).getSpecialty()).isEqualTo("Cardiology");
    }
}
