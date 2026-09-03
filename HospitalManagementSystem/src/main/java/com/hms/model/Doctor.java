package com.hms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
@Entity
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	private String name;
	private String gender;
	private String specialization;
	private Double salary;
	private Integer age;
	@OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Patient> patients;
	public Doctor() {
		// TODO Auto-generated constructor stub
	}
	public Doctor(Integer id, String name, String gender, String specialization, Double salary, Integer age,
			List<Patient> patients) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.specialization = specialization;
		this.salary = salary;
		this.age = age;
		this.patients = patients;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public List<Patient> getPatients() {
		return patients;
	}
	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}
}
