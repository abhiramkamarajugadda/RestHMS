package com.hms.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hms.model.Doctor;
import com.hms.model.Patient;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;

/** Seeds sample (fictional) doctors and patients when the "demo" profile is active. */
@Component
@Profile("demo")
public class DemoDataSeeder implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public DemoDataSeeder(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        if (doctorRepository.count() > 0) {
            return;
        }
        Doctor cardio = doctorRepository.save(new Doctor(null, "Dr. Ananya Rao", "Female", "Cardiology", 185000.0, 42, null));
        Doctor ortho = doctorRepository.save(new Doctor(null, "Dr. Vikram Mehta", "Male", "Orthopedics", 160000.0, 38, null));
        Doctor peds = doctorRepository.save(new Doctor(null, "Dr. Sneha Iyer", "Female", "Pediatrics", 150000.0, 35, null));
        Doctor gp = doctorRepository.save(new Doctor(null, "Dr. Arjun Nair", "Male", "General Medicine", 120000.0, 45, null));

        patientRepository.saveAll(List.of(
                new Patient(null, "Ravi Kumar", 58, 72.5, "Male", "Hypertension", cardio),
                new Patient(null, "Lakshmi Devi", 64, 61.0, "Female", "Arrhythmia", cardio),
                new Patient(null, "Karthik Reddy", 29, 78.0, "Male", "Fractured wrist", ortho),
                new Patient(null, "Meera Joshi", 47, 66.2, "Female", "Knee osteoarthritis", ortho),
                new Patient(null, "Aarav Singh", 7, 22.4, "Male", "Asthma", peds),
                new Patient(null, "Diya Sharma", 5, 18.9, "Female", "Viral fever", peds),
                new Patient(null, "Suresh Babu", 52, 80.1, "Male", "Type 2 diabetes", gp),
                new Patient(null, "Priya Menon", 33, 58.7, "Female", "Migraine", gp)));
    }
}
