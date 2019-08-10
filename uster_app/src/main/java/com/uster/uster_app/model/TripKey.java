package com.uster.uster_app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Embeddable
public class TripKey implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "driver_id")
	public long driver_id;
	@Column(name = "vehicle_id")
	public long vehicle_id;
	
	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date dateId;
	
	public TripKey() {
		
	}
	
	public TripKey(long driver_id, long vehicle_id,Date date) {
		super();
		this.driver_id = driver_id;
		this.vehicle_id = vehicle_id;
		this.dateId= date;
	}
	
	
	
	public long getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(long driver_id) {
		this.driver_id = driver_id;
	}

	public long getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(long vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public Date getTripDate() {
		return dateId;
	}

	public void setTripDate(Date tripDate) {
		this.dateId = tripDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateId == null) ? 0 : dateId.hashCode());
		result = prime * result + (int) (driver_id ^ (driver_id >>> 32));
		result = prime * result + (int) (vehicle_id ^ (vehicle_id >>> 32));
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
		TripKey other = (TripKey) obj;
		if (dateId == null) {
			if (other.dateId != null)
				return false;
		} else if (!dateId.equals(other.dateId))
			return false;
		if (driver_id != other.driver_id)
			return false;
		if (vehicle_id != other.vehicle_id)
			return false;
		return true;
	}

	
	
	
	
	
	
	
}
