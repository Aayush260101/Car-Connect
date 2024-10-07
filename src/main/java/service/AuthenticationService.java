package service;

import entity.Admin;



import entity.Customer;
import util.DBConnUtil;
import util.DBPropertyUtil;


import java.sql.*;

public class AuthenticationService {
	

    public Customer authenticateCustomer(String username, String password) {
        Customer customer = null;
        String query = "SELECT * FROM Customer WHERE Username = ? AND Password = ?";
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"));
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Ideally, use hashed password
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

    public Admin authenticateAdmin(String username, String password) {
        Admin admin = null;
        String query = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"));
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Ideally, use hashed password
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                admin = new Admin(rs.getInt("AdminID"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Email"), rs.getString("PhoneNumber"),
                        rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Role"), rs.getDate("JoinDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }
}
