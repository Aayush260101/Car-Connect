package service;

import entity.Customer;


import util.DBConnUtil;
import util.DBPropertyUtil;
import exception.AuthenticationException;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class CustomerServiceImpl implements ICustomerService {
    @Override
    public Customer getCustomerById(int customerId) {
        Customer customer = null;
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Customer WHERE CustomerID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = new Customer(rs.getInt("CustomerID"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Email"), rs.getString("PhoneNumber"),
                        rs.getString("Address"), rs.getString("Username"), rs.getString("Password"),
                        rs.getDate("RegistrationDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Customer";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("CustomerID"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Email"), rs.getString("PhoneNumber"),
                        rs.getString("Address"), rs.getString("Username"), rs.getString("Password"),
                        rs.getDate("RegistrationDate"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void registerCustomer(Customer customer) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "INSERT INTO Customer (FirstName, LastName, Email, PhoneNumber, Address, Username, Password, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getAddress());
            stmt.setString(6, customer.getUsername());
            stmt.setString(7, customer.getPassword());
            stmt.setDate(8, new java.sql.Date(customer.getRegistrationDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "UPDATE Customer SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ?, Username = ?, Password = ? WHERE CustomerID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getAddress());
            stmt.setString(6, customer.getUsername());
            stmt.setString(7, customer.getPassword());
            stmt.setInt(8, customer.getCustomerID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(int customerId) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "DELETE FROM Customer WHERE CustomerID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    @Override
    public Customer getCustomerByUsername(String username) throws AuthenticationException {
        Customer customer = null;
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Customer WHERE Username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = new Customer(rs.getInt("CustomerID"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Email"), rs.getString("PhoneNumber"),
                        rs.getString("Address"), rs.getString("Username"), rs.getString("Password"),
                        rs.getDate("RegistrationDate"));
            } else {
                throw new AuthenticationException("Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
