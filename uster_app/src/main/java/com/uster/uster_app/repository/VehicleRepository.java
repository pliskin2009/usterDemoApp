package com.uster.uster_app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uster.uster_app.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

	@Query(value="SELECT id,brand,model,plate,license_Required FROM VEHICLES v LEFT JOIN TRIPS t on v.id=t.vehicle_id  "
			+ "WHERE v.id not in (SELECT vehicle_id FROM TRIPS WHERE Date = TO_DATE(?1,'yyyy-MM-dd') )",  nativeQuery = true)
	List<Vehicle> findAvaliableVehiclesByDate(Date date);

}
