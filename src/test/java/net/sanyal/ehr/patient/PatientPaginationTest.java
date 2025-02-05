package net.sanyal.ehr.patient;

import com.fasterxml.jackson.core.type.TypeReference;

import net.sanyal.ehr.common.BaseTest;
import net.sanyal.ehr.common.PageResponse;
import net.sanyal.ehr.model.common.Address;
import net.sanyal.ehr.model.common.ContactDetail;
import net.sanyal.ehr.model.common.Name;
import net.sanyal.ehr.model.patient.Patient;
import net.sanyal.ehr.service.patient.PatientService;

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

public class PatientPaginationTest extends BaseTest {

    @BeforeAll
    static void setupPatients(@Autowired PatientService patientService) {
        IntStream.rangeClosed(1, 50).forEach(i -> patientService.createPatient(
                Patient.builder()
                        .name(Name.builder().salutation("Mr.").firstName("John" + i).lastName("Doe" + i).build())
                        .socialSecurityNumber(String.format("%03d-45-6789", i))
                        .purposeOfVisit("Routine Checkup")
                        .dateOfBirth(LocalDate.of(1990, 5, 20))
                        .contactDetail(ContactDetail.builder()
                                .phoneNumber("111-222-" + String.format("%04d", i))
                                .email("john" + i + "@example.com")
                                .address(Address.builder()
                                        .houseNumber(String.valueOf(i))
                                        .street("Main St")
                                        .city("City" + i)
                                        .build())
                                .build())
                        .build()));
    }

    @Test
    @DisplayName("Verify pagination with different page sizes")
    void verifyPagination() throws Exception {
        verifyPage(0, 5, 5, "John1"); // Page 0 with 5 elements per page, first patient should be John1
        verifyPage(1, 5, 5, "John6"); // Page 1 with 5 elements per page, first patient should be John6
        verifyPage(2, 10, 10, "John21"); // Page 2 with 10 elements per page, first patient should be John21
    }

    @Test
    @DisplayName("Traverse through multiple pages and verify patients")
    void traverseMultiplePages() throws Exception {
        int pageSize = 10;
        for (int page = 0; page < 5; page++) {
            MvcResult result = mockMvc.perform(get("/api/patients/search?page=" + page + "&size=" + pageSize)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            // Manually deserialize the content field
            PageResponse<Patient> pageResponse = objectMapper.readValue(jsonResponse,
                    new TypeReference<>() {
                    });

            List<Patient> patients = pageResponse.getContent();
            assertThat(patients).hasSize(pageSize);
            assertThat(patients.get(0).getName().getFirstName()).isEqualTo("John" + (page * pageSize + 1));
        }
    }

    private void verifyPage(int page, int size, int expectedSize, String expectedFirstPatientName) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/patients/search?page=" + page + "&size=" + size)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PageResponse<Patient> pageResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        List<Patient> patients = pageResponse.getContent();
        assertThat(patients).hasSize(expectedSize);
        assertThat(patients.get(0).getName().getFirstName()).isEqualTo(expectedFirstPatientName);
    }

}
