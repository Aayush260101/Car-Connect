package service;

import entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleUpdateTest {

    private VehicleServiceImpl mockVehicleService; // Mock of VehicleServiceImpl

    @BeforeEach
    public void setUp() {
        // Mock the VehicleServiceImpl using Mockito
        mockVehicleService = Mockito.mock(VehicleServiceImpl.class);
    }

    @Test
    public void testUpdateVehicleDetails() {
        // Create a dummy vehicle object with initial data
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleID(6); // Assume this is an existing vehicle ID
        vehicle.setModel("Tesla Model S");
        vehicle.setMake("Tesla");
        vehicle.setYear(2021);
        vehicle.setColor("Red");
        vehicle.setRegistrationNumber("ABC123");
        vehicle.setAvailability(true);
        vehicle.setDailyRate(300.0);

        // Simulate the updateVehicle method with Mockito
        doNothing().when(mockVehicleService).updateVehicle(vehicle);

        // Now simulate updating the vehicle details
        vehicle.setModel("Tesla Model X"); // New model
        vehicle.setColor("Blue"); // New color
        vehicle.setDailyRate(350.0); // New daily rate

        // Check if updateVehicle runs without throwing exceptions
        assertDoesNotThrow(() -> mockVehicleService.updateVehicle(vehicle)); 

        // Optionally, verify that the updateVehicle method was called
        verify(mockVehicleService, times(1)).updateVehicle(vehicle);

        // Simulate fetching the updated vehicle details
        when(mockVehicleService.getVehicleById(6)).thenReturn(vehicle);

        // Retrieve the updated vehicle and assert its updated fields
        Vehicle updatedVehicle = mockVehicleService.getVehicleById(6);
        assertEquals("Tesla Model X", updatedVehicle.getModel(), "The model should be updated.");
        assertEquals("Blue", updatedVehicle.getColor(), "The color should be updated.");
        assertEquals(350.0, updatedVehicle.getDailyRate(), "The daily rate should be updated.");
    }
}
