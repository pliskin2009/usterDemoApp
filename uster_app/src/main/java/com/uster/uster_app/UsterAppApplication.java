package com.uster.uster_app;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.uster.uster_app.model.Driver;
import com.uster.uster_app.model.Trip;
import com.uster.uster_app.model.Vehicle;
import com.uster.uster_app.repository.DriverRepository;
import com.uster.uster_app.repository.TripRepository;
import com.uster.uster_app.repository.VehicleRepository;


@SpringBootApplication
public class UsterAppApplication {
	
	private static final Logger log = LoggerFactory.getLogger(UsterAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UsterAppApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(DriverRepository driverRepository,VehicleRepository vehicleRepository,TripRepository tripRepository) {
		return (args) -> {
			
			// Create a couple of Drivers, Vehicles and Trips
			 
	        Driver driverA = new Driver("Pedro","Perez","B");
	        Driver driverB = new Driver("Raquel","Solis","A");
	        Driver driverC = new Driver("Vicente","Perez","A");
	        
	        Vehicle vehicleA= new Vehicle("Ford","Fiesta","5092GFX","B");
	        Vehicle vehicleB= new Vehicle("Mazda","2","6553GFC","B");
	        Vehicle vehicleC= new Vehicle("Yamaha","Raptor","3453GFC","A");
	        Vehicle vehicleD= new Vehicle("Harley Davidson","Street 500","3456HDE","A");
	        
	        driverRepository.saveAll(Arrays.asList(driverA, driverB,driverC));

	        vehicleRepository.save(vehicleA);
	        vehicleRepository.save(vehicleB);
	        vehicleRepository.save(vehicleC);
	        vehicleRepository.save(vehicleD);
	        
	        Trip trip1= new Trip(vehicleA,driverA,new Date());
	        tripRepository.save(trip1);
	        Date newDate = new SimpleDateFormat("dd/MM/yyyy").parse("21/08/2019");
	        trip1 = new Trip(vehicleC,driverB,newDate);
	        tripRepository.save(trip1);

			// fetch all drivers
			log.info("Drivers found with findAll():");
			log.info("-------------------------------");
			for (Driver driver : driverRepository.findAll()) {
				log.info(driver.toString());
			}
			log.info("");

			// fetch an individual driver by ID
			driverRepository.findById(1L)
				.ifPresent(driver -> {
					log.info("Driver found with findById(1L):");
					log.info("--------------------------------");
					log.info(driver.toString());
					log.info("");
				});

			// fetch drivers by surname
			log.info("Driver found with findByLastName('Perez'):");
			log.info("--------------------------------------------");
			driverRepository.findBySurname("Perez").forEach(perez -> {
				log.info(perez.toString());
			});
			
			log.info("");
		};
	}

}
