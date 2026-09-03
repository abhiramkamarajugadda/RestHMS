package com.hms.controller;

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
import com.hms.model.Patient;
import com.hms.services.PatientService;

@RestController
@RequestMapping("/api/hms/patients")
public class PatientController {
	private PatientService service;

	public PatientController(PatientService service) {
		super();
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> savePatient(@RequestBody Patient patient) {
		Patient obj = service.savePatient(patient);
		if (obj != null) {
			return new ResponseEntity<>("Patient added successfully...", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Error in adding patient details.....", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllPAtients() {
		List<Patient> patList = service.getAllPAtients();
		if (patList.size() != 0) {
			return new ResponseEntity<>(patList, HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(patList, HttpStatus.NO_CONTENT);

		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> searchPatient(@PathVariable Integer id) {
		Patient patient = service.searchPatient(id);
		if (patient.getId() != 0) {
			return new ResponseEntity<>(patient, HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>("Patient id not found.....", HttpStatus.NOT_FOUND);
		}
	}
	@PutMapping
	public ResponseEntity<?> updatePatient(@RequestBody Patient patient){
		Patient obj = service.searchPatient(patient.getId());
		if(obj.getId()!=null) {
			service.savePatient(patient);
			return new ResponseEntity<> ("Patient Updated Successfully....",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Invalid patient id.......",HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePatient(@PathVariable Integer id){
		Patient patient = service.searchPatient(id);
		if(patient.getId()!=0) {
			service.deletePatient(id);
			return new ResponseEntity<> ("patient deleted Successfully.....",HttpStatus.FOUND);
		}else {
			return new ResponseEntity<>("Invalid patient id.......",HttpStatus.NOT_FOUND);
		}
	}
}
