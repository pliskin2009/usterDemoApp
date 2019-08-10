package com.uster.uster_app.model;

import java.util.HashSet;
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

import org.hibernate.validator.constraints.Length;

@Entity
@Table (name="drivers")
public class Driver {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotEmpty
	@Size(max=255)
	private String name;
	@NotEmpty
	@Size(min=1,max=255)
	private String surname;
	
	@NotEmpty(message="license.notempty")
	@Size(max=1)
	private String license;
	
	//Viajes
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private Set<Trip> Trips = new HashSet<>();
	
	public Driver(){
		
	}
	
	public Driver(String name,String surname,String license) {
		this.name=name;
		this.surname=surname;
		this.license=license;
	}
	
	
        public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
 
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "surname", nullable = false)
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    @Column(name = "license", nullable = false)
    public String getLicense() {
        return license;
    }
    public void setLicense(String license) {
        this.license = license;
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
		Driver other = (Driver) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "Driver [id=" + id + ", Name=" + name + ", SurName=" + surname + ", License=" + license
       + "]";
    }
	
}
