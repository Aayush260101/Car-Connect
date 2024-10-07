package util;

import entity.Reservation;

import entity.Vehicle;

import java.sql.*;

public class ReportGenerator {
	/*
	 * private DatabaseContext dbContext;
	 * 
	 * public ReportGenerator() { dbContext = new DatabaseContext(); }
	 */

    public void generateReservationReport() {
        String query = "SELECT * FROM Reservation";
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties")); // Get a new connection
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Reservation Report:");
            while (rs.next()) {
                System.out.println("Reservation ID: " + rs.getInt("ReservationID"));
                System.out.println("Customer ID: " + rs.getInt("CustomerID"));
                System.out.println("Vehicle ID: " + rs.getInt("VehicleID"));
                System.out.println("Start Date: " + rs.getTimestamp("StartDate"));
                System.out.println("End Date: " + rs.getTimestamp("EndDate"));
                System.out.println("Total Cost: " + rs.getDouble("TotalCost"));
                System.out.println("Status: " + rs.getString("Status"));
                System.out.println("---------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateVehicleReport() {
        String query = "SELECT v.VehicleID, v.Model, v.Make,v.Year,v.Color,v.RegistrationNumber,v.DailyRate,COUNT(r.ReservationID) AS TotalBookings, SUM(r.TotalCost) AS TotalRevenue FROM Vehicle v LEFT JOIN Reservation r ON v.VehicleID = r.VehicleID GROUP BY v.VehicleID";
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties")); // Get a new connection
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Vehicle Report:");
            while (rs.next()) {
                System.out.println("Vehicle ID: " + rs.getInt("VehicleID"));
                System.out.println("Model: " + rs.getString("Model"));
                System.out.println("Make: " + rs.getString("Make"));
                System.out.println("Year: " + rs.getInt("Year"));
                System.out.println("Color: " + rs.getString("Color"));
                System.out.println("Registration Number: " + rs.getString("RegistrationNumber"));
                System.out.println("Daily Rate: " + rs.getDouble("DailyRate"));
                System.out.println("No. of Bookings Done: "+ rs.getInt("TotalBookings"));
                System.out.println("Total Revenue Generated: "+ rs.getDouble("TotalRevenue"));
                System.out.println("---------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
