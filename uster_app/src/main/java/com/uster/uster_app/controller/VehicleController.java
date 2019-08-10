package com.uster.uster_app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uster.uster_app.exception.ResourceNotFoundException;
import com.uster.uster_app.model.Vehicle;
import com.uster.uster_app.model.Driver;
import com.uster.uster_app.model.Trip;
import com.uster.uster_app.model.Vehicle;
import com.uster.uster_app.repository.TripRepository;
import com.uster.uster_app.repository.VehicleRepository;

@Controller
@RequestMapping("/uster")
public class VehicleController {
	
	@Autowired
	VehicleRepository vehicleRepository;
	@Autowired
	TripRepository tripRepository;
	
	
	@RequestMapping(value={"vehicles","vehicles/list"},method=RequestMethod.GET)
	private String listVehicles(Model model) {
		if(!vehicleRepository.findAll().isEmpty()) {
			model.addAttribute("vehicles",vehicleRepository.findAll());
		}else {
			model.addAttribute("vehicles",null);
		}
		return "uster/vehicles/listVehicles";
	}
	
	@GetMapping({"addVehicle"})
	public String VehicleAdd(Model model) {
		model.addAttribute("vehicle", new Vehicle());
		return "uster/vehicles/add-vehicle";
	}

       
    @PostMapping("saveVehicle")
    public String createVehicle(Model modelo,@Valid Vehicle vehicle,BindingResult results) {
    	if(results.hasErrors()) {
    		return "uster/vehicles/add-vehicle";
    	}
        vehicleRepository.save(vehicle);
        modelo.addAttribute("vehicles",vehicleRepository.findAll());
        return "uster/vehicles/listVehicles";
    }
    
    /*Update endpoint*/
	@RequestMapping(value="editVehicle/{id}",method=RequestMethod.GET)
	public String VehicleEdit(Model model,@PathVariable(value="id")Long VehicleId) {
		model.addAttribute("vehicle", vehicleRepository.findById(VehicleId));
		return "uster/vehicles/update-vehicle";
	}
    
	  @RequestMapping(value= {"updateVehicle","vehicles/updateVehicle"}, method=RequestMethod.POST)
	    public String updateVehicle(Model modelo,@Valid Vehicle vehicle,BindingResult results) throws ResourceNotFoundException{
	    	if(results.hasErrors()) {
	    		return "uster/vehicles/update-vehicle";
	    	}
       System.out.println("Vehicle Update: "+vehicle.getId());
       Vehicle vehicleDetail = vehicleRepository.findById(vehicle.getId()).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for this id :: " + vehicle.getId()));
       

       vehicleDetail.setBrand(vehicle.getBrand());
       vehicleDetail.setModel(vehicle.getModel());
       vehicleDetail.setPlate(vehicle.getPlate());
       vehicleDetail.setLicenseRequired(vehicle.getLicenseRequired());
       vehicleRepository.save(vehicleDetail);
     
        modelo.addAttribute("vehicles",vehicleRepository.findAll());
        return "uster/vehicles/listVehicles";
    }

    @RequestMapping(value="deleteVehicle/{id}", method=RequestMethod.GET)
    public  String  deleteVehicle(Model model,@PathVariable(value="id")Long VehicleId) throws ResourceNotFoundException {
    	    	
        Vehicle vehicle = vehicleRepository.findById(VehicleId)
       .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for this id :: " + VehicleId));
        
        List<Trip> trips = tripRepository.findByVehicle(vehicle);
        if(!trips.isEmpty()) {
        	trips.forEach(trip -> tripRepository.delete(trip));
        }
        
        vehicleRepository.delete(vehicle);
        model.addAttribute("vehicles",vehicleRepository.findAll());
      
        return "uster/vehicles/deletedVehicle";
    }

}
