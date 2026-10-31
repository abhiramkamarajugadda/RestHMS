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

import com.hms.model.Doctor;
import com.hms.services.DoctorService;

/**
 * Doctor REST API.
 * 200 OK for reads and updates, 201 Created (with a Location header) for creates,
 * 204 No Content for deletes, 404 Not Found for unknown ids and 400 for a missing id on update.
 */
@RestController
@RequestMapping("/api/hms/doctors")
public class DoctorController {
	private final DoctorService service;

	public DoctorController(DoctorService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Doctor> saveDoctor(@RequestBody Doctor doctor) {
		doctor.setId(null);
		Doctor saved = service.saveDoctor(doctor);
		return ResponseEntity.created(URI.create("/api/hms/doctors/" + saved.getId())).body(saved);
	}

	@GetMapping
	public ResponseEntity<List<Doctor>> getAllDoctors() {
		return ResponseEntity.ok(service.getAllDoctors());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> searchDoctor(@PathVariable Integer id) {
		return service.searchDoctor(id)
				.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> notFound(id));
	}

	@PutMapping
	public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor) {
		if (doctor.getId() == null) {
			return ResponseEntity.badRequest().body("The doctor id is required for an update.");
		}
		if (!service.exists(doctor.getId())) {
			return notFound(doctor.getId());
		}
		return ResponseEntity.ok(service.saveDoctor(doctor));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {
		if (!service.exists(id)) {
			return notFound(id);
		}
		service.deleteDoctor(id);
		return ResponseEntity.noContent().build();
	}

	private ResponseEntity<?> notFound(Integer id) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id " + id + " was not found.");
	}
}
