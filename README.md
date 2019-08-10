

## Uster Demo APP developed by @ViX

   Demo Application for Learning and Test purposes presented on Java JDK8 + Spring MVC + Spring JPA + HTML5 + BOOTSTRAP + THYMELEAF
               
   ## DESCRIPTION ##
   
   Basic concept simulation of Trip Check-in  using Drivers and Vehicles that store in H2 on memory Database, special features:
                  
   Must be an 3 phases process: 
   
   * Date Selection,Vehicle Selection and Driver Selection.
   
   * Selecting Vehicles, you can only select an vehicle that is free on that date (First time you select one, the app only load free ones)
   
   * Selecting Driver, you can only select one free for that date and also  that his license its the required one for the selected vehicle (as above application will only load the free and correct ones after first check)
					
   Navigation: You can navigate across the listings and make CRUD with them or push 'New Trip' to check an Trip.
   
   Example : 
   
   * http://Your-IP/uster/
   
   * http://Your-IP/uster/drivers
   
   * http://Your-IP/uster/index

## Access to H2 Console ( In Memory Database )

http://Your-IP/h2-console/

Jdbc URL: jdbc:h2:mem:testdb 

## Features

- Demo of CRUD operation for the tables ( Drivers, Vehicles, Trips* )
- Custom validation Messages
- Bootstrap 
- Thymeleaf tags, fragments and templates
- Jdk8
- JPA with annotations and some native queries examples.
	
	* Trips can only be listed, added or deleted purposily.

