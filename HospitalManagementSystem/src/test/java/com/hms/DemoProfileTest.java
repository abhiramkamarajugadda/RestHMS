package com.hms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;

/** The "demo" profile boots on H2 with sample data and serves the OpenAPI docs. */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:hmsdemo-test;MODE=MySQL;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
@ActiveProfiles("demo")
class DemoProfileTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;

    @Test
    void seedsSampleDoctorsAndPatients() throws Exception {
        assertThat(doctorRepository.count()).isEqualTo(4);
        assertThat(patientRepository.count()).isEqualTo(8);
        mockMvc.perform(get("/api/hms/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void servesOpenApiDocsAndRedirectsRootToSwagger() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("RestHMS: Hospital Management REST API"));
        mockMvc.perform(get("/")).andExpect(redirectedUrl("/swagger-ui.html"));
    }
}
