package com.hms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hms.model.Doctor;
import com.hms.repository.DoctorRepository;

@Service
public class DoctorService {
	private DoctorRepository repository;

	public DoctorService(DoctorRepository repository) {
		super();
		this.repository = repository;
	}

	public Doctor saveDoctor(Doctor doctor) {
		return repository.save(doctor);
	}

	public List<Doctor> getAllDoctors() {
		return repository.findAll();
	}

	public Optional<Doctor> searchDoctor(Integer id) {
		return id == null ? Optional.empty() : repository.findById(id);
	}

	public boolean exists(Integer id) {
		return id != null && repository.existsById(id);
	}

	public void deleteDoctor(Integer id) {
		repository.deleteById(id);
	}

	

	
	

}
