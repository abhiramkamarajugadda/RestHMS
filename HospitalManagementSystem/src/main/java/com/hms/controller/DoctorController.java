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
import com.hms.services.DoctorService;
@RestController
@RequestMapping("/api/hms/doctors")
public class DoctorController {
	private DoctorService service;

	public DoctorController(DoctorService service) {
		super();
		this.service = service;
	}
	@PostMapping
	public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor){
		Doctor obj = service.saveDoctor(doctor);
		if(obj !=null) {
			return new ResponseEntity<>("Doctor added successfully.......",HttpStatus.ACCEPTED);
		}else {
			return new ResponseEntity<>("Error in adding doctor.....",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllDoctors(){
		List<Doctor> docList=service.getAllDoctors();
		if(docList.size()!=0) {
			return new ResponseEntity<> (docList,HttpStatus.FOUND);
		}
		else {
			return new ResponseEntity<> (docList,HttpStatus.NO_CONTENT);
		}
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> searchDoctor(@PathVariable Integer id){
		Doctor doctor = service.searchDoctor(id);
		if(doctor.getId()!=0) {
			return new ResponseEntity<> (doctor,HttpStatus.FOUND);
		}else {
			return new ResponseEntity<>("Invalid doctor id.......",HttpStatus.NOT_FOUND);
		}
	}
	@PutMapping
	public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor){
		Doctor obj = service.searchDoctor(doctor.getId());
		if(obj.getId()!=null) {
			service.saveDoctor(doctor);
			return new ResponseEntity<> ("Doctor Updated Successfully....",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Invalid doctor id.......",HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDoctor(@PathVariable Integer id){
		Doctor doctor = service.searchDoctor(id);
		if(doctor.getId()!=0) {
			service.deleteDoctor(id);
			return new ResponseEntity<> ("doctor deleted Successfully.....",HttpStatus.FOUND);
		}else {
			return new ResponseEntity<>("Invalid doctor id.......",HttpStatus.NOT_FOUND);
		}
	}

}
