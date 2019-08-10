package com.uster.uster_app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table (name="trips")
public class Trip implements Serializable {

	private static final long serialVersionUID = 1L;

	//Composite Key
	@EmbeddedId
	private TripKey id;
	
	//Vehicles
	
	@ManyToOne
	@MapsId("vehicle_id")
    @JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;
	
	//Drivers
	
	@ManyToOne
	@MapsId("driver_id")
    @JoinColumn(name = "driver_id")
	private Driver driver;
	
	//Date
	@Column(insertable=false, updatable=false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	public Trip() {
		
	}

	public Trip(Vehicle vehicle, Driver driver,Date date) {
		super();
		this.vehicle = vehicle;
		this.driver = driver;
		this.date= date;
		this.id = new TripKey(driver.getId(),vehicle.getId(),date);
	}
	
	


	public TripKey getId() {
		return id;
	}

	public void setId(TripKey id) {
		this.id = id;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	
	
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + ((vehicle == null) ? 0 : vehicle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trip other = (Trip) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (driver == null) {
			if (other.driver != null)
				return false;
		} else if (!driver.equals(other.driver))
			return false;
		if (vehicle == null) {
			if (other.vehicle != null)
				return false;
		} else if (!vehicle.equals(other.vehicle))
			return false;
		return true;
	}
	
	
	
	

}
