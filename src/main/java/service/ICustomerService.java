package service;

import entity.Customer;
import exception.AuthenticationException;

import java.util.List;

public interface ICustomerService {
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();
    void registerCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerId);
    Customer getCustomerByUsername(String username) throws AuthenticationException;
}
