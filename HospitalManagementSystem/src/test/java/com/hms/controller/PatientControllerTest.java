package com.hms.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.hms.model.Patient;
import com.hms.services.PatientService;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

	private static final String JSON = "{\"name\":\"Sita\",\"age\":34,\"weight\":58.5,\"gender\":\"Female\",\"disease\":\"Fever\"}";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PatientService service;

	private Patient sample(Integer id) {
		return new Patient(id, "Sita", 34, 58.5, "Female", "Fever", null);
	}

	@Test
	void createReturns201WithLocation() throws Exception {
		when(service.savePatient(any(Patient.class))).thenReturn(sample(1));

		mockMvc.perform(post("/api/hms/patients").contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/hms/patients/1"))
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void listReturns200EvenWhenEmpty() throws Exception {
		when(service.getAllPAtients()).thenReturn(List.of());
		mockMvc.perform(get("/api/hms/patients")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

		when(service.getAllPAtients()).thenReturn(List.of(sample(1), sample(2)));
		mockMvc.perform(get("/api/hms/patients")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void getByIdReturns200Or404() throws Exception {
		when(service.searchPatient(1)).thenReturn(Optional.of(sample(1)));
		when(service.searchPatient(99)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/hms/patients/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
		mockMvc.perform(get("/api/hms/patients/99")).andExpect(status().isNotFound());
	}

	@Test
	void updateReturns200Or404AndRejectsMissingId() throws Exception {
		when(service.exists(1)).thenReturn(true);
		when(service.exists(99)).thenReturn(false);
		when(service.savePatient(any(Patient.class))).thenReturn(sample(1));

		mockMvc.perform(put("/api/hms/patients").contentType(MediaType.APPLICATION_JSON).content(JSON.replace("{", "{\"id\":1,")))
				.andExpect(status().isOk());
		mockMvc.perform(put("/api/hms/patients").contentType(MediaType.APPLICATION_JSON).content(JSON.replace("{", "{\"id\":99,")))
				.andExpect(status().isNotFound());
		mockMvc.perform(put("/api/hms/patients").contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deleteReturns204Or404() throws Exception {
		when(service.exists(1)).thenReturn(true);
		when(service.exists(99)).thenReturn(false);

		mockMvc.perform(delete("/api/hms/patients/1")).andExpect(status().isNoContent());
		verify(service).deletePatient(1);

		mockMvc.perform(delete("/api/hms/patients/99")).andExpect(status().isNotFound());
		verify(service, never()).deletePatient(99);
	}
}
