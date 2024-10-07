package main;

import entity.Customer;


import entity.Vehicle;
import entity.Reservation;
import entity.Admin;
import service.CustomerServiceImpl;
import service.VehicleServiceImpl;
import service.ReservationServiceImpl;
import service.AdminServiceImpl;
import util.ReportGenerator;
import exception.AuthenticationException;
import exception.ReservationException;
import exception.VehicleNotFoundException;
import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CustomerServiceImpl customerService = new CustomerServiceImpl();
    private static final VehicleServiceImpl vehicleService = new VehicleServiceImpl();
    private static final ReservationServiceImpl reservationService = new ReservationServiceImpl();
    private static final AdminServiceImpl adminService = new AdminServiceImpl();
    private static final ReportGenerator reportGenerator = new ReportGenerator();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to CarConnect!");
            System.out.println("1. Customer Login");
            System.out.println("2. Admin Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    customerLogin();
                    break;
                case 2:
                    adminLogin();
                    break;
                case 3:
                    System.out.println("Exiting the application...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerLogin() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        try {
            Customer customer = customerService.getCustomerByUsername(username);
            if (customer != null && customer.authenticate(password)) {
                System.out.println("Login successful! Welcome, " + customer.getFirstName());
                customerMenu(customer);
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("Customer Menu:");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Make a Reservation");
            System.out.println("3. View Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAvailableVehicles();
                    break;
                case 2:
                    makeReservation(customer);
                    break;
                case 3:
                    viewReservations(customer);
                    break;
                case 4:
                	cancelReservation();
                	break;
                case 5:
                    System.out.println("Logging out...");
                    return; // return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAvailableVehicles() {
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
        if (availableVehicles.isEmpty()) {
            System.out.println("No vehicles available.");
        } else {
            System.out.println("Available Vehicles:");
            for (Vehicle vehicle : availableVehicles) {
                System.out.printf("ID: %d, Model: %s, Daily Rate: %.2f\n",
                        vehicle.getVehicleID(), vehicle.getModel(), vehicle.getDailyRate());
            }
        }
    }

    private static void makeReservation(Customer customer) {
        System.out.print("Enter Vehicle ID to reserve: ");
        int vehicleId = scanner.nextInt();

        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startDate = scanner.next();
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String endDate = scanner.next();

        try {
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            if (vehicle == null || !vehicle.isAvailability()) {
                throw new VehicleNotFoundException("Vehicle not found or not available.");
            }
            // Check if the vehicle is available for the given dates
            boolean isAvailable = reservationService.isVehicleAvailable(vehicleId, startDate, endDate);
            if (!isAvailable) {
                System.out.println("The vehicle is not available for the selected dates.");
                return;
            }

            Reservation reservation = new Reservation();
            reservation.setCustomerID(customer.getCustomerID());
            reservation.setVehicleID(vehicleId);
            reservation.setStartDate(java.sql.Date.valueOf(startDate));
            reservation.setEndDate(java.sql.Date.valueOf(endDate));
            reservation.setStatus("Pending");

            // Calculate total cost
            long duration = java.sql.Date.valueOf(endDate).getTime() - java.sql.Date.valueOf(startDate).getTime();
            int days = (int) (duration / (1000 * 60 * 60 * 24));
            reservation.setTotalCost(days * vehicle.getDailyRate());

            // Call createReservation method which might throw ReservationException
            reservationService.createReservation(reservation);
            System.out.println("Reservation successful! Total Cost: " + reservation.getTotalCost());
        } catch (VehicleNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ReservationException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage()); // Handle SQLException
        }
    }


    private static void viewReservations(Customer customer) {
        List<Reservation> reservations = reservationService.getReservationsByCustomerId(customer.getCustomerID());
        if (reservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            System.out.println("Your Reservations:");
            for (Reservation reservation : reservations) {
                System.out.printf("Reservation ID: %d, Vehicle ID: %d, Start Date: %s, End Date: %s, Status: %s\n",
                        reservation.getReservationID(), reservation.getVehicleID(), reservation.getStartDate(),
                        reservation.getEndDate(), reservation.getStatus());
            }
        }
    }
    
    private static void cancelReservation(){
    	System.out.print("Enter the Reservation ID which you want to cancel :");
    	int reservationId = scanner.nextInt();
    	scanner.nextLine();
    	try {
    		reservationService.cancelReservation(reservationId);
    		System.out.println("Reservation Cancelled Sucessfully");
    	}catch (SQLException e){
    		System.out.println("Error Cancelling Reservation : "+e.getMessage());
    	}
    }

    private static void adminLogin() {
        System.out.print("Enter admin username: ");
        String username = scanner.next();
        System.out.print("Enter admin password: ");
        String password = scanner.next();

        Admin admin = adminService.getAdminByUsername(username);
        if (admin != null && admin.authenticate(password)) {
            System.out.println("Admin login successful! Welcome, " + admin.getFirstName());
            adminMenu();
        } else {
            System.out.println("Invalid admin username or password.");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. View Reports");
            System.out.println("2. Manage Vehicles");
            System.out.println("3. Manage Customers");
            System.out.println("4. Update Reservation Status");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    reportGenerator.generateReservationReport();
                    reportGenerator.generateVehicleReport();
                    break;
                case 2:
                    manageVehicles();
                    break;
                case 3:
                    manageCustomers();
                    break;
                case 4:
                	updateReservationStatus();
                	break;
                case 5:
                    System.out.println("Logging out...");
                    return; // return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageVehicles() {
        while (true) {
            System.out.println("Vehicle Management Menu:");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Update Vehicle");
            System.out.println("3. Remove Vehicle");
            System.out.println("4. View All Vehicles");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addVehicle();
                    break;
                case 2:
                    updateVehicle();
                    break;
                case 3:
                    removeVehicle();
                    break;
                case 4:
                    viewAllVehicles();
                    break;
                case 5:
                    return; // Go back to Admin Menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addVehicle() {
        System.out.print("Enter Model: ");
        String model = scanner.next();
        System.out.print("Enter Make: ");
        String make = scanner.next();
        System.out.print("Enter Year: ");
        int year = scanner.nextInt();
        System.out.print("Enter Color: ");
        String color = scanner.next();
        System.out.print("Enter Registration Number: ");
        String registrationNumber = scanner.next();
        System.out.print("Is the vehicle available? (true/false): ");
        boolean availability = scanner.nextBoolean();
        System.out.print("Enter Daily Rate: ");
        double dailyRate = scanner.nextDouble();

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(model);
        vehicle.setMake(make);
        vehicle.setYear(year);
        vehicle.setColor(color);
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setAvailability(availability);
        vehicle.setDailyRate(dailyRate);

        vehicleService.addVehicle(vehicle);
        System.out.println("Vehicle added successfully!");
    }

    private static void updateVehicle() {
        System.out.print("Enter Vehicle ID to update: ");
        int vehicleId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left by nextInt()
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

        if (vehicle != null) {
            System.out.print("Enter new Model (leave empty to keep current): ");
            String model = scanner.nextLine(); // Use nextLine() to allow empty input
            if (!model.isEmpty()) vehicle.setModel(model);

            System.out.print("Enter new Make (leave empty to keep current): ");
            String make = scanner.nextLine(); // Use nextLine()
            if (!make.isEmpty()) vehicle.setMake(make);

            System.out.print("Enter new Year (leave empty to keep current): ");
            String yearInput = scanner.nextLine(); // Use nextLine()
            if (!yearInput.isEmpty()) vehicle.setYear(Integer.parseInt(yearInput));

            System.out.print("Enter new Color (leave empty to keep current): ");
            String color = scanner.nextLine(); // Use nextLine()
            if (!color.isEmpty()) vehicle.setColor(color);

            System.out.print("Enter new Registration Number (leave empty to keep current): ");
            String registrationNumber = scanner.nextLine(); // Use nextLine()
            if (!registrationNumber.isEmpty()) vehicle.setRegistrationNumber(registrationNumber);

            System.out.print("Is the vehicle available? (true/false, leave empty to keep current): ");
            String availabilityInput = scanner.nextLine(); // Use nextLine()
            if (!availabilityInput.isEmpty()) vehicle.setAvailability(Boolean.parseBoolean(availabilityInput));

            System.out.print("Enter new Daily Rate (leave empty to keep current): ");
            String dailyRateInput = scanner.nextLine(); // Use nextLine()
            if (!dailyRateInput.isEmpty()) vehicle.setDailyRate(Double.parseDouble(dailyRateInput));

            vehicleService.updateVehicle(vehicle);
            System.out.println("Vehicle updated successfully!");
        } else {
            System.out.println("Vehicle not found.");
        }
    }


    private static void removeVehicle() {
        System.out.print("Enter Vehicle ID to remove: ");
        int vehicleId = scanner.nextInt();
        vehicleService.removeVehicle(vehicleId);
        System.out.println("Vehicle removed successfully!");
    }

    private static void viewAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available.");
        } else {
            System.out.println("All Vehicles:");
            for (Vehicle vehicle : vehicles) {
                System.out.printf("ID: %d, Model: %s, Make: %s, Year: %d, Color: %s, Registration Number: %s, Available: %s, Daily Rate: %.2f\n",
                        vehicle.getVehicleID(), vehicle.getModel(), vehicle.getMake(), vehicle.getYear(), vehicle.getColor(),
                        vehicle.getRegistrationNumber(), vehicle.isAvailability(), vehicle.getDailyRate());
            }
        }
    }


    private static void manageCustomers() {
        while (true) {
            System.out.println("Customer Management Menu:");
            System.out.println("1. Add Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. Remove Customer");
            System.out.println("4. View All Customers");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    updateCustomer();
                    break;
                case 3:
                    removeCustomer();
                    break;
                case 4:
                    viewAllCustomers();
                    break;
                case 5:
                    return; // Go back to Admin Menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCustomer() {
        System.out.print("Enter First Name: ");
        String firstName = scanner.next();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.next();
        System.out.print("Enter Email: ");
        String email = scanner.next();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.next();
        System.out.print("Enter Address: ");
        String address = scanner.next();
        System.out.print("Enter Username: ");
        String username = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();
        
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setRegistrationDate(new java.util.Date());

        customerService.registerCustomer(customer);
        System.out.println("Customer added successfully!");
    }

    private static void updateCustomer() {
        System.out.print("Enter Customer ID to update: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left by nextInt()
        Customer customer = customerService.getCustomerById(customerId);

        if (customer != null) {
            System.out.print("Enter new First Name (leave empty to keep current): ");
            String firstName = scanner.nextLine(); // Use nextLine to read the entire line
            if (!firstName.isEmpty()) customer.setFirstName(firstName);

            System.out.print("Enter new Last Name (leave empty to keep current): ");
            String lastName = scanner.nextLine(); // Use nextLine
            if (!lastName.isEmpty()) customer.setLastName(lastName);

            System.out.print("Enter new Email (leave empty to keep current): ");
            String email = scanner.nextLine(); // Use nextLine
            if (!email.isEmpty()) customer.setEmail(email);

            System.out.print("Enter new Phone Number (leave empty to keep current): ");
            String phoneNumber = scanner.nextLine(); // Use nextLine
            if (!phoneNumber.isEmpty()) customer.setPhoneNumber(phoneNumber);

            System.out.print("Enter new Address (leave empty to keep current): ");
            String address = scanner.nextLine(); // Use nextLine
            if (!address.isEmpty()) customer.setAddress(address);

            System.out.print("Enter new Username (leave empty to keep current): ");
            String username = scanner.nextLine(); // Use nextLine
            if (!username.isEmpty()) customer.setUsername(username);

            System.out.print("Enter new Password (leave empty to keep current): ");
            String password = scanner.nextLine(); // Use nextLine
            if (!password.isEmpty()) customer.setPassword(password);

            customerService.updateCustomer(customer);
            System.out.println("Customer updated successfully!");
        } else {
            System.out.println("Customer not found.");
        }
    }


    private static void removeCustomer() {
        System.out.print("Enter Customer ID to remove: ");
        int customerId = scanner.nextInt();
        customerService.deleteCustomer(customerId);
        System.out.println("Customer removed successfully!");
    }

    private static void viewAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers available.");
        } else {
            System.out.println("All Customers:");
            for (Customer customer : customers) {
                System.out.printf("ID: %d, Name: %s %s, Email: %s, Phone: %s\n",
                        customer.getCustomerID(), customer.getFirstName(), customer.getLastName(),
                        customer.getEmail(), customer.getPhoneNumber());
            }
        }
    }
    
    private static void updateReservationStatus() {
    	System.out.print("Enter Reservation ID whose status you want to update");
    	int reservationId=scanner.nextInt();
    	scanner.nextLine();
    	System.out.print("Enter new status (e.g., Confirmed, Cancelled, Completed): ");
    	String newStatus = scanner.nextLine();
    	
    	try {
    		reservationService.updateReservationStatus(reservationId, newStatus);
    		System.out.println("Reservation status updated successfully!");
    	}catch (SQLException e){
    		System.out.println("Error updating reservation status: " + e.getMessage());
    	}
    }

}
