package com.uster.uster_app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uster.uster_app.model.Driver;
import com.uster.uster_app.model.Vehicle;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

	Iterable<Driver> findBySurname(String string);

	@Query(value="SELECT DISTINCT id,name,surname,license FROM DRIVERS d LEFT JOIN TRIPS t on d.id=t.driver_id  "
			+ "WHERE d.id not in (SELECT driver_id FROM TRIPS WHERE Date = TO_DATE(?1,'yyyy-MM-dd') )",  nativeQuery = true)
	List<Driver> findAvaliableDriversByDate(Date date);

	@Query(value="SELECT DISTINCT d.id,name,surname,license FROM DRIVERS d LEFT JOIN TRIPS t on d.id=t.driver_id  "
			+ "WHERE d.id not in (SELECT driver_id FROM TRIPS WHERE Date = TO_DATE(?1,'yyyy-MM-dd')) AND d.license LIKE ?2",  nativeQuery = true)
	List<Driver> findAvaliableDriversByDateAndLicenseRequired(Date date, String licenseRequired);

}
