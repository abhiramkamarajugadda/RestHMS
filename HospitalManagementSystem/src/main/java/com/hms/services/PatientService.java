package com.hms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hms.model.Patient;
import com.hms.repository.PatientRepository;

@Service
public class PatientService {
	private PatientRepository repository;

	public PatientService(PatientRepository repository) {
		super();
		this.repository = repository;
	}

	public Patient savePatient(Patient patient) {
		return repository.save(patient);
	}

	public List<Patient> getAllPAtients() {
		return repository.findAll();
	}

	public Optional<Patient> searchPatient(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	public boolean exists(Integer id) {
		return id != null && repository.existsById(id);
	}

	public void deletePatient(Integer id) {
		repository.deleteById(id);
	}
	

}
