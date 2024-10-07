package service;

import entity.Admin;


import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class AdminServiceImpl implements IAdminService {
    @Override
    public Admin getAdminById(int adminId) {
        Admin admin = null;
        
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Admin WHERE AdminID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, adminId);
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

    @Override
    public Admin getAdminByUsername(String username) {
        Admin admin = null;
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Admin WHERE Username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
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

    @Override
    public void registerAdmin(Admin admin) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "INSERT INTO Admin (FirstName, LastName, Email, PhoneNumber, Username, Password, Role, JoinDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, admin.getFirstName());
            stmt.setString(2, admin.getLastName());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPhoneNumber());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getPassword());
            stmt.setString(7, admin.getRole());
            stmt.setDate(8, new java.sql.Date(admin.getJoinDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAdmin(Admin admin) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "UPDATE Admin SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Username = ?, Password = ?, Role = ? WHERE AdminID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, admin.getFirstName());
            stmt.setString(2, admin.getLastName());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPhoneNumber());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getPassword());
            stmt.setString(7, admin.getRole());
            stmt.setInt(8, admin.getAdminID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAdmin(int adminId) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "DELETE FROM Admin WHERE AdminID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, adminId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
