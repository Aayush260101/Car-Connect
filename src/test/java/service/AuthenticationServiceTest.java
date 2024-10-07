package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import entity.Customer;
import entity.Admin;


public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        authenticationService = new AuthenticationService();
    }

    @Test
    public void testAuthenticateCustomerWithValidCredentials() {
        Customer customer = authenticationService.authenticateCustomer("aayush", "aayush12");
        assertNotNull(customer, "Customer should not be null for valid credentials.");
        assertEquals("aayush", customer.getUsername());
    }

    @Test
    public void testAuthenticateCustomerWithInvalidCredentials() {
        Customer customer = authenticationService.authenticateCustomer("invalidUsername", "invalidPassword");
        assertNull(customer, "Customer should be null for invalid credentials.");
    }

    @Test
    public void testAuthenticateAdminWithValidCredentials() {
        Admin admin = authenticationService.authenticateAdmin("admin_user", "adminpass");
        assertNotNull(admin, "Admin should not be null for valid credentials.");
        assertEquals("admin_user", admin.getUsername());
    }

    @Test
    public void testAuthenticateAdminWithInvalidCredentials() {
        Admin admin = authenticationService.authenticateAdmin("invalidAdmin", "invalidPassword");
        assertNull(admin, "Admin should be null for invalid credentials.");
    }
}