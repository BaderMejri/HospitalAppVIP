package com.badr.demo;

import com.badr.demo.entities.Patient;
import com.badr.demo.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class PatientMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientMvcApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandLineRunner(PatientRepository patientRepository){
		return args -> {
			patientRepository.save(
					new Patient(null,"Badr",new Date(),false,290));
			patientRepository.save(
					new Patient(null,"Aida",new Date(),false,300));
			patientRepository.save(
					new Patient(null,"Lara",new Date(),false,430));
			patientRepository.save(
					new Patient(null,"Mohamed",new Date(),true,175));

			patientRepository.findAll().forEach(patient -> System.out.println(patient.getName()));
		};
	}

}
