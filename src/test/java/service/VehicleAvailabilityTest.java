package service;
import entity.Vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleAvailabilityTest {
    private IVehicleService vehicleService; 
    private VehicleServiceImpl vehicleServiceImpl; 

    @BeforeEach
    void setUp() {
        vehicleServiceImpl = Mockito.mock(VehicleServiceImpl.class); 
        vehicleService = vehicleServiceImpl; 
    }

    @Test
    void testGetAvailableVehicles() {
        
        List<Vehicle> mockAvailableVehicles = new ArrayList<>();
        mockAvailableVehicles.add(new Vehicle(1, "Nissan", "Altima", 2023, "Blue", "ABC123", true, 50.0));
        

        
        when(vehicleServiceImpl.getAvailableVehicles()).thenReturn(mockAvailableVehicles);

      
        List<Vehicle> availableVehicles = vehicleServiceImpl.getAvailableVehicles();

        
        assertEquals(1, availableVehicles.size(), "There should be one vehicle in the list"); 
        assertTrue(availableVehicles.stream().anyMatch(v -> v.getModel().equals("Nissan")), "Available vehicles should include Nissan Altima");
    }

    @Test
    void testGetAllVehicles() {
        
        List<Vehicle> mockAllVehicles = new ArrayList<>();
        mockAllVehicles.add(new Vehicle(1, "Chevrolet", "Malibu", 2020, "White", "LMN789", true, 55.0));
        mockAllVehicles.add(new Vehicle(2, "Kia", "Optima", 2020, "Black", "OPQ012", false, 40.0));

        
        when(vehicleServiceImpl.getAllVehicles()).thenReturn(mockAllVehicles);

        
        List<Vehicle> allVehicles = vehicleServiceImpl.getAllVehicles();

        
        assertEquals(2, allVehicles.size(), "There should be two vehicles in the list");
        assertTrue(allVehicles.stream().anyMatch(v -> v.getModel().equals("Chevrolet")), "Vehicle list should contain Chevrolet Malibu");
        assertTrue(allVehicles.stream().anyMatch(v -> v.getModel().equals("Kia")), "Vehicle list should contain Kia Optima");
    }
}
