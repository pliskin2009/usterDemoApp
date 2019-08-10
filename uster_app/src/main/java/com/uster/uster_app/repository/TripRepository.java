package com.uster.uster_app.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uster.uster_app.model.Driver;
import com.uster.uster_app.model.Trip;
import com.uster.uster_app.model.TripKey;
import com.uster.uster_app.model.Vehicle;

@Repository
public interface TripRepository extends JpaRepository<Trip,TripKey> {

	List<Trip> findByDriver(Driver driver);

	List<Trip> findByDate(Date date);
	
	List<Trip> findByVehicle(Vehicle vehicle);
	
	@Query(value="SELECT * FROM TRIPS t WHERE t.Date = ?1 AND t.vehicle_id = ?2",  nativeQuery = true)
	List<Trip>findByDateAndVehicle(Date date,Long vehicleId);

	@Query(value="SELECT * FROM TRIPS t WHERE t.Date = ?1 AND t.driver_id = ?2",  nativeQuery = true)
	List<Trip> findByDateAndDriver(Date date, long driverid);

	Optional<Trip> findById(TripKey tripId);
	
}
