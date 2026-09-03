package com.hms.services;

import java.util.List;

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
		// TODO Auto-generated method stub
		return repository.save(patient);
	}

	public List<Patient> getAllPAtients() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	public Patient searchPatient(Integer id) {
		// TODO Auto-generated method stub
		return repository.findById(id).orElse(new Patient());
	}

	public void deletePatient(Integer id) {
		// TODO Auto-generated method stub
		repository.deleteById(id);
	}
	

}
