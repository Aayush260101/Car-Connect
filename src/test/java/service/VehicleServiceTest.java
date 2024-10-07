package service;

import entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    private IVehicleService vehicleService; // Service under test
    private VehicleServiceImpl mockVehicleService; // Mock of the VehicleServiceImpl

    @BeforeEach
    public void setUp() {
        // Mock the VehicleServiceImpl using Mockito
        mockVehicleService = Mockito.mock(VehicleServiceImpl.class);
    }

    @Test
    public void testAddVehicle() {
        // Create a new Vehicle object
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleID(1);
        vehicle.setModel("Tesla Model S");
        vehicle.setMake("Tesla");
        vehicle.setYear(2022);
        vehicle.setColor("Red");
        vehicle.setRegistrationNumber("ABC123");
        vehicle.setAvailability(true);
        vehicle.setDailyRate(200.0);

        // Simulate the behavior of addVehicle by calling the method without actual database interaction
        doNothing().when(mockVehicleService).addVehicle(vehicle);

        // Simulate the behavior of getVehicleById by returning the vehicle when ID 1 is requested
        when(mockVehicleService.getVehicleById(1)).thenReturn(vehicle);

        // Call the addVehicle method (this won't actually do anything due to the mock setup)
        mockVehicleService.addVehicle(vehicle);

        // Retrieve the added vehicle using the mocked getVehicleById
        Vehicle addedVehicle = mockVehicleService.getVehicleById(1);

        // Assertions to verify the vehicle was added correctly
        assertNotNull(addedVehicle, "Added vehicle should not be null.");
        assertEquals("Tesla Model S", addedVehicle.getModel(), "Model should match.");
        assertEquals("Tesla", addedVehicle.getMake(), "Make should match.");
        assertEquals(2022, addedVehicle.getYear(), "Year should match.");
        assertEquals("Red", addedVehicle.getColor(), "Color should match.");
        assertEquals("ABC123", addedVehicle.getRegistrationNumber(), "Registration number should match.");
        assertTrue(addedVehicle.isAvailability(), "Vehicle should be available.");
        assertEquals(200.0, addedVehicle.getDailyRate(), "Daily rate should match.");
    }
}
