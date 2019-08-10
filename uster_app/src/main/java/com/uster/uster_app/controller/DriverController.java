package com.uster.uster_app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uster.uster_app.exception.ResourceNotFoundException;
import com.uster.uster_app.model.Driver;
import com.uster.uster_app.model.Trip;
import com.uster.uster_app.repository.DriverRepository;
import com.uster.uster_app.repository.TripRepository;

@Controller
@RequestMapping("/uster")
public class DriverController {
	
	@Autowired
	DriverRepository driverRepository;
	@Autowired
	TripRepository tripRepository;
	
	@RequestMapping(value={"","drivers/index"},method = RequestMethod.GET)
	public String goIndex(Model model) {
		
		return "uster/index";
	}
	
	@RequestMapping(value={"drivers","drivers/list"},method = RequestMethod.GET)
	public String Driverindex(Model model) {
		if(!driverRepository.findAll().isEmpty()) {
			model.addAttribute("drivers", driverRepository.findAll());
		}else {
			model.addAttribute("drivers", null);
		}
		return "uster/drivers/listDrivers";
	}
	
	/*Update endpoint*/
	@RequestMapping(value="editDriver/{id}",method=RequestMethod.GET)
	public String DriverEdit(Model model,@PathVariable(value="id")Long DriverId) {
		model.addAttribute("driver", driverRepository.findById(DriverId));
		return "uster/drivers/update-driver";
	}
	
	@GetMapping({"addDriver"})
	public String DriverAdd(Model model) {
		model.addAttribute("driver", new Driver());
		return "uster/drivers/add-driver";
	}

       
    @PostMapping("saveDriver")
    public String createDriver(Model modelo,@Valid Driver driver,BindingResult results) {
    	if(results.hasErrors()) {
    		return "uster/drivers/add-driver";
    	}
        driverRepository.save(driver);
        modelo.addAttribute("drivers",driverRepository.findAll());
        return "uster/drivers/listDrivers";
    }

    @RequestMapping(value= {"updateDriver","drivers/updateDriver"}, method=RequestMethod.POST)
    public String updateDriver(Model modelo,@Valid Driver driver,BindingResult results) throws ResourceNotFoundException{
    	if(results.hasErrors()) {
    		return "uster/drivers/update-driver";
    	}
       System.out.println("Driver Update: "+driver.getId());
       Driver driverDetail = driverRepository.findById(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("Driver not found for this id :: " + driver.getId()));
       

       driverDetail.setLicense(driver.getLicense());
       driverDetail.setSurname(driver.getSurname());
       driverDetail.setName(driver.getName());
       driverRepository.save(driverDetail);
     
        modelo.addAttribute("drivers",driverRepository.findAll());
        return "uster/drivers/listDrivers";
    }

    @RequestMapping(value="deleteDriver/{id}", method=RequestMethod.GET)
    public  String  deleteDriver(Model model,@PathVariable(value="id")Long DriverId) throws ResourceNotFoundException {
    	    	
        Driver driver = driverRepository.findById(DriverId)
       .orElseThrow(() -> new ResourceNotFoundException("Driver not found for this id :: " + DriverId));
        
        List<Trip> trips = tripRepository.findByDriver(driver);
        if(!trips.isEmpty()) {
        	trips.forEach(trip -> tripRepository.delete(trip));
        }
        
        driverRepository.delete(driver);
        model.addAttribute("drivers",driverRepository.findAll());
      
        return "uster/drivers/deletedDriver";
    }
	
}

