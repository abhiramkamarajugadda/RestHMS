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

import com.hms.model.Doctor;
import com.hms.services.DoctorService;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

	private static final String JSON = "{\"name\":\"Dr Rao\",\"gender\":\"Male\",\"specialization\":\"Cardiology\",\"salary\":90000,\"age\":45}";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DoctorService service;

	private Doctor sample(Integer id) {
		return new Doctor(id, "Dr Rao", "Male", "Cardiology", 90000.0, 45, null);
	}

	@Test
	void createReturns201WithLocation() throws Exception {
		when(service.saveDoctor(any(Doctor.class))).thenReturn(sample(1));

		mockMvc.perform(post("/api/hms/doctors").contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/hms/doctors/1"))
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void listReturns200EvenWhenEmpty() throws Exception {
		when(service.getAllDoctors()).thenReturn(List.of());
		mockMvc.perform(get("/api/hms/doctors")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

		when(service.getAllDoctors()).thenReturn(List.of(sample(1), sample(2)));
		mockMvc.perform(get("/api/hms/doctors")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void getByIdReturns200Or404() throws Exception {
		when(service.searchDoctor(1)).thenReturn(Optional.of(sample(1)));
		when(service.searchDoctor(99)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/hms/doctors/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
		mockMvc.perform(get("/api/hms/doctors/99")).andExpect(status().isNotFound());
	}

	@Test
	void updateReturns200Or404AndRejectsMissingId() throws Exception {
		when(service.exists(1)).thenReturn(true);
		when(service.exists(99)).thenReturn(false);
		when(service.saveDoctor(any(Doctor.class))).thenReturn(sample(1));

		mockMvc.perform(put("/api/hms/doctors").contentType(MediaType.APPLICATION_JSON).content(JSON.replace("{", "{\"id\":1,")))
				.andExpect(status().isOk());
		mockMvc.perform(put("/api/hms/doctors").contentType(MediaType.APPLICATION_JSON).content(JSON.replace("{", "{\"id\":99,")))
				.andExpect(status().isNotFound());
		mockMvc.perform(put("/api/hms/doctors").contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deleteReturns204Or404() throws Exception {
		when(service.exists(1)).thenReturn(true);
		when(service.exists(99)).thenReturn(false);

		mockMvc.perform(delete("/api/hms/doctors/1")).andExpect(status().isNoContent());
		verify(service).deleteDoctor(1);

		mockMvc.perform(delete("/api/hms/doctors/99")).andExpect(status().isNotFound());
		verify(service, never()).deleteDoctor(99);
	}
}
