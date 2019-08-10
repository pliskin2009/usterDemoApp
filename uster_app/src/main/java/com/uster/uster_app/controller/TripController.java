package com.uster.uster_app.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uster.uster_app.exception.ResourceNotFoundException;
import com.uster.uster_app.model.Driver;
import com.uster.uster_app.model.Trip;
import com.uster.uster_app.model.TripKey;
import com.uster.uster_app.model.Vehicle;
import com.uster.uster_app.repository.DriverRepository;
import com.uster.uster_app.repository.TripRepository;
import com.uster.uster_app.repository.VehicleRepository;

@Controller
@RequestMapping("/uster")
public class TripController {
	
	@Autowired
	TripRepository tripRepository;
	@Autowired
	VehicleRepository vehicleRepository;
	@Autowired
	DriverRepository driverRepository;
	
	@RequestMapping(value= {"uster","index"},method = RequestMethod.GET)
	private String index(Model model) {
	
		return "uster/index";
	}
	
	@RequestMapping(value={"trips","trips/list"},method=RequestMethod.GET)
	private String listTrips(Model model) {
		if(!tripRepository.findAll().isEmpty()) {
			model.addAttribute("trips",tripRepository.findAll());
		}else {
			model.addAttribute("trips",null);
		}
		return "uster/trips/listTrips";
	}
	
	@RequestMapping(value= {"newTrip"},method = RequestMethod.GET)
	private String newTrip(Model model) {
		model.addAttribute("trip", new Trip());
		model.addAttribute("vehicles",vehicleRepository.findAll());
		model.addAttribute("drivers",driverRepository.findAll());
		model.addAttribute("newForm","0");
		model.addAttribute("error",null);
		return "uster/trips/newTrip";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@RequestMapping(value= {"reloadNewTrip","editTrip/reloadNewTrip"},method = RequestMethod.POST)
	private String reloadDataTrip(Model model,Trip trip,BindingResult result) {
		
		 System.out.println(">ReloadNewTrip doValidation " + trip);
		 
		 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		 Date date = new Date();
		 System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		 
		 String selectedVehicle="--";
		 String selectedDriver="--";
		 List<Vehicle> availableVehicles;
		 List<Driver> availableDrivers;

		 if (result.hasErrors()) {
		        System.out.println("errors");
		        
		 }
		//new Form Phase 0 = New Trip DEFAULT
		model.addAttribute("newForm","0");
		String errorMessage = "";
		
		List<Trip> existingTrips = tripRepository.findByDate(trip.getDate());
		
		List<Vehicle> anyAvailableVehicles = vehicleRepository.findAvaliableVehiclesByDate(trip.getDate());
		List<Driver> anyAvailableDrivers = driverRepository.findAvaliableDriversByDate(trip.getDate());
		
		//Phase 1
		//If there arent any Drivers or Vehicles Availables that day we should inform to the user that the combos will be empty
		if(anyAvailableVehicles.isEmpty() || anyAvailableDrivers.isEmpty()) {
			model.addAttribute("error","Cannot select date, they arent any Drivers nor Vehicles that day, please change the trip date");
			model.addAttribute("selectedVehicle","--");
			model.addAttribute("selectedDriver","--");
			model.addAttribute("vehicles",vehicleRepository.findAll());
			model.addAttribute("drivers",driverRepository.findAll());
			return "uster/trips/newTrip";
			
			
		}else {
			//new Form Phase 1 = Date Selected
			model.addAttribute("newForm","1");
		}
						
		//At least there is some trip on that Date or you have an Vehicle/Driver Selected
		if(!existingTrips.isEmpty() || null!=trip.getVehicle() || null!=trip.getDriver() ) {
			
			date = trip.getDate();
			
			 //Shows all vehicles First time (This is for check Purposes only), next time will load only availables
			 availableVehicles = vehicleRepository.findAll();
			 availableDrivers = driverRepository.findAll();
			
			//Phase 2 Check vehicles
			if(null!=trip.getVehicle()) {
				Long vehicleId = trip.getVehicle().getId();
				selectedVehicle = vehicleId.toString();
				existingTrips = tripRepository.findByDateAndVehicle(trip.getDate(), trip.getVehicle().getId());
				
				//new Form Phase 2 = Vehicle Selected
				model.addAttribute("newForm","2");
				
				if(!existingTrips.isEmpty()) {
					errorMessage="Vehicle already has an Programed Trip on "+dateFormat.format(date)+" check avaliable Vehicles";
				}else if(null!=trip.getDriver()) {
					//Phase 3 Check drivers
					Long driverId = trip.getDriver().getId();
					selectedDriver = driverId.toString();
					existingTrips = tripRepository.findByDateAndDriver(trip.getDate(), trip.getDriver().getId());
					
					//new Form Phase 3 = Driver Selected
					model.addAttribute("newForm","3");
					
					if(!existingTrips.isEmpty()) {
						errorMessage="Driver already has an Programed Trip on "+dateFormat.format(date)+" check available Drivers";
					}else {
						//Available Drivers when you selected one
						availableDrivers = driverRepository.findAvaliableDriversByDate(trip.getDate());
						
						//Phase 3-1 Check that Driver has the LicenseRequired to Drive the Vehicle
						if(!trip.getDriver().getLicense().equals(trip.getVehicle().getLicenseRequired())) {
							model.addAttribute("error","Driver cannot drive selected vehicle "+trip.getVehicle().getBrand()+" "+trip.getVehicle().getModel()+" , Check the new Drivers Available for License "+trip.getVehicle().getLicenseRequired());
							availableDrivers =  driverRepository.findAvaliableDriversByDateAndLicenseRequired(trip.getDate(),trip.getVehicle().getLicenseRequired() );
							availableVehicles = vehicleRepository.findAvaliableVehiclesByDate(trip.getDate());
							
							model.addAttribute("selectedVehicle",selectedVehicle);
							model.addAttribute("drivers",availableDrivers);
							model.addAttribute("vehicles",availableVehicles);
							
							//cHECK NEW AVAILABLE dRIVERS
							model.addAttribute("newForm","2");
							return "uster/trips/newTrip";
						}
					}
					
					
				}
				
				//Check that existingTrips with the same filters are empty or throw an error
				if(!existingTrips.isEmpty()) {
					model.addAttribute("error","Cannot Save that Trip, check :  "+errorMessage);
					model.addAttribute("selectedVehicle","--");
					model.addAttribute("selectedDriver","--");
					model.addAttribute("newForm","1");
				}else {
					model.addAttribute("selectedVehicle",selectedVehicle);
					model.addAttribute("selectedDriver",selectedDriver);
				}
				
				//Available vehicles when you selected one
				availableVehicles = vehicleRepository.findAvaliableVehiclesByDate(trip.getDate());
				
			}
			
						
			//Selected Date, Loads Only Vehicles and Drivers available that day
		   			
			model.addAttribute("vehicles",availableVehicles);
			model.addAttribute("drivers",availableDrivers);
			
		}else {
		//No Trips on Date Selected
		
			//All fields This is first check on Date, no vehicles selected, list all Drivers and Vehicles
			model.addAttribute("error",null);
			model.addAttribute("trip", trip);
			model.addAttribute("vehicles",vehicleRepository.findAll());
			model.addAttribute("drivers",driverRepository.findAll());
			
		}
		model.addAttribute("trip", trip);
		return "uster/trips/newTrip";
	}
	
	
	@RequestMapping(value= {"saveTrip"},method = RequestMethod.POST)
	private String saveTrip(Model model,Trip trip,BindingResult result) {
		
		//Check empty
		String emptyValue = "--";
		boolean errorCheck=false;
		
		if(null==trip.getDriver() || null==trip.getVehicle() ) {
			errorCheck=true;
		}
		
		//If errors reload New Trip form without the combo previous values
		if (result.hasErrors()||errorCheck) {
	        System.out.println("errors");
	        model.addAttribute("error","Error before saving Trip, Please check all your fields");
	        List<Vehicle> availableVehicles = vehicleRepository.findAvaliableVehiclesByDate(trip.getDate());
	        List<Driver> availableDrivers = driverRepository.findAvaliableDriversByDate(trip.getDate());
	        model.addAttribute("vehicles",availableVehicles);
			model.addAttribute("drivers",availableDrivers);
			model.addAttribute("newForm","0");
	        return "uster/trips/newTrip";
	        
		}
		
		TripKey pk_trip = new TripKey(trip.getDriver().getId(), trip.getVehicle().getId(),trip.getDate());
		
		trip.setId(pk_trip);
		model.addAttribute("trip", trip);
		
		tripRepository.save(trip);
		model.addAttribute("trips",tripRepository.findAll());
		return "uster/trips/listTrips";
	}
	
	
	
	@RequestMapping(value="deleteTrip/{id}", method=RequestMethod.GET)
    public  String  deleteTrip(Model model,@PathVariable(value="id")String TripId) throws ResourceNotFoundException, ParseException {
    	    	
		 System.out.println(TripId.toString());
		 
		 TripKey id = new TripKey();
		 
		 String[] compositeKey = TripId.split("_");
		 
		 Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(compositeKey[2]);
		 
		 id.setVehicle_id(Long.valueOf(compositeKey[0]));
		 id.setDriver_id(Long.valueOf(compositeKey[1]));
		 id.setTripDate(newDate);
		
        Trip trip = tripRepository.findById(id)
       .orElseThrow(() -> new ResourceNotFoundException("Trip not found for this id :: " + id));
              
        tripRepository.delete(trip);
              
        model.addAttribute("trips",tripRepository.findAll());
      
        return "uster/trips/deletedTrip";
    }
	
	@RequestMapping(value="editTrip/{id}", method=RequestMethod.GET)
    public  String  editTrip(Model model,@PathVariable(value="id")String TripId) throws ResourceNotFoundException, ParseException {
    	    	
		 System.out.println(TripId.toString());
		 
		 TripKey id = new TripKey();
		 
		 String[] compositeKey = TripId.split("_");
		 
		 Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(compositeKey[2]);
		 
		 id.setVehicle_id(Long.valueOf(compositeKey[0]));
		 id.setDriver_id(Long.valueOf(compositeKey[1]));
		 id.setTripDate(newDate);
		
        Trip trip = tripRepository.findById(id)
       .orElseThrow(() -> new ResourceNotFoundException("Trip not found for this id :: " + id));
              
        //tripRepository.delete(trip);
              
        model.addAttribute("trip",trip);
        model.addAttribute("selectedvehicle",trip.getVehicle());
        model.addAttribute("selecteddriver",trip.getDriver());
        model.addAttribute("drivers",driverRepository.findAvaliableDriversByDate(trip.getDate()));
        model.addAttribute("vehicles",vehicleRepository.findAvaliableVehiclesByDate(trip.getDate()));
        model.addAttribute("newForm","1");
        
      
        return "uster/trips/newTrip";
    }
	
	

}
