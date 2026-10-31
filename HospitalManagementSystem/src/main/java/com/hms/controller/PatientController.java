package com.hms.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.model.Patient;
import com.hms.services.PatientService;

/**
 * Patient REST API.
 * 200 OK for reads and updates, 201 Created (with a Location header) for creates,
 * 204 No Content for deletes, 404 Not Found for unknown ids and 400 for a missing id on update.
 */
@RestController
@RequestMapping("/api/hms/patients")
public class PatientController {
	private final PatientService service;

	public PatientController(PatientService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Patient> savePatient(@RequestBody Patient patient) {
		patient.setId(null);
		Patient saved = service.savePatient(patient);
		return ResponseEntity.created(URI.create("/api/hms/patients/" + saved.getId())).body(saved);
	}

	@GetMapping
	public ResponseEntity<List<Patient>> getAllPAtients() {
		return ResponseEntity.ok(service.getAllPAtients());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> searchPatient(@PathVariable Integer id) {
		return service.searchPatient(id)
				.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> notFound(id));
	}

	@PutMapping
	public ResponseEntity<?> updatePatient(@RequestBody Patient patient) {
		if (patient.getId() == null) {
			return ResponseEntity.badRequest().body("The patient id is required for an update.");
		}
		if (!service.exists(patient.getId())) {
			return notFound(patient.getId());
		}
		return ResponseEntity.ok(service.savePatient(patient));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePatient(@PathVariable Integer id) {
		if (!service.exists(id)) {
			return notFound(id);
		}
		service.deletePatient(id);
		return ResponseEntity.noContent().build();
	}

	private ResponseEntity<?> notFound(Integer id) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " was not found.");
	}
}
