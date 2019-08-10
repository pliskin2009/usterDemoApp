package com.uster.uster_app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table ( name = "vehicles")
public class Vehicle {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotEmpty
	@Size(max=255)
	private String brand;
	@NotEmpty
	@Size(max=255)
	private String model;
	@NotEmpty
	@Size(min=7,max=7)
	private String plate;
	@NotEmpty
	@Size(max=1)
	private String licenseRequired;
	
	//Viajes
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private Set<Trip> Trips;
	
	public Vehicle() {
		
	}

	public Vehicle(String brand, String model, String plate, String licenseRequired) {
		super();
		this.brand = brand;
		this.model = model;
		this.plate = plate;
		this.licenseRequired = licenseRequired;
	}
	
	
	public long getId() {
		return id;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "brand", nullable = false)
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "model", nullable = false)
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(name = "plate", nullable = false)
	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	@Column(name = "licenseRequired", nullable = false)
	public String getLicenseRequired() {
		return licenseRequired;
	}

	public void setLicenseRequired(String licenseRequired) {
		this.licenseRequired = licenseRequired;
	}
	
	
	
	 @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Vehicle other = (Vehicle) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	    public String toString() {
	        return "Vehicle [id=" + id + ", Brand=" + brand + ", Model=" + model + ", Plate=" + plate +", LicenseRequired=" + licenseRequired
	       + "]";
	    }
	
	
}
