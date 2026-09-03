package com.hms.services;

import java.util.List;

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
		// TODO Auto-generated method stub
		return repository.save(doctor);
	}

	public List<Doctor> getAllDoctors() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	public Doctor searchDoctor(Integer id) {
		// TODO Auto-generated method stub
		return repository.findById(id).orElse(new Doctor());
	}

	public void deleteDoctor(Integer id) {
		// TODO Auto-generated method stub
		repository.deleteById(id);
	}

	

	
	

}
