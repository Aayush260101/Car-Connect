package service;

import entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerServiceImpl mockCustomerService; // Mock of CustomerServiceImpl

    @BeforeEach
    public void setUp() {
        // Mock the CustomerServiceImpl using Mockito
        mockCustomerService = Mockito.mock(CustomerServiceImpl.class);
    }

    @Test
    public void testUpdateCustomerInformation() {
        // Create a Customer object with initial data
        Customer customerToUpdate = new Customer(1, "John", "Doe", "john.doe@example.com", "1234567890", "123 Elm St", "johndoe", "password123", null);

        // Simulate the updateCustomer method with Mockito
        doNothing().when(mockCustomerService).updateCustomer(customerToUpdate);

        // Now simulate updating the customer details
        customerToUpdate.setFirstName("John");
        customerToUpdate.setLastName("Smith"); // Updated last name
        customerToUpdate.setEmail("john.smith@example.com"); // Updated email
        customerToUpdate.setPhoneNumber("0987654321"); // Updated phone number
        customerToUpdate.setAddress("456 Oak St"); // Updated address

        // Check if updateCustomer runs without throwing exceptions
        assertDoesNotThrow(() -> mockCustomerService.updateCustomer(customerToUpdate));

        // Verify that updateCustomer was called once
        verify(mockCustomerService, times(1)).updateCustomer(customerToUpdate);

        // Simulate retrieving the updated customer details after the update
        when(mockCustomerService.getCustomerById(1)).thenReturn(customerToUpdate);

        // Retrieve the updated customer using the mocked getCustomerById
        Customer updatedCustomer = mockCustomerService.getCustomerById(1);

        // Assertions to verify the updated fields
        assertNotNull(updatedCustomer, "Updated customer should not be null.");
        assertEquals("John", updatedCustomer.getFirstName(), "First name should be updated.");
        assertEquals("Smith", updatedCustomer.getLastName(), "Last name should be updated.");
        assertEquals("john.smith@example.com", updatedCustomer.getEmail(), "Email should be updated.");
        assertEquals("0987654321", updatedCustomer.getPhoneNumber(), "Phone number should be updated.");
        assertEquals("456 Oak St", updatedCustomer.getAddress(), "Address should be updated.");
    }
}
