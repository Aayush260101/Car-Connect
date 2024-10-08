package service;

import entity.Reservation;



import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import exception.ReservationException;

public class ReservationServiceImpl implements IReservationService {
    @Override
    public Reservation getReservationById(int reservationId) {
        Reservation reservation = null;
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Reservation WHERE ReservationID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                reservation = new Reservation(rs.getInt("ReservationID"), rs.getInt("CustomerID"),
                        rs.getInt("VehicleID"), rs.getTimestamp("StartDate"),
                        rs.getTimestamp("EndDate"), rs.getDouble("TotalCost"),
                        rs.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    @Override
    public List<Reservation> getReservationsByCustomerId(int customerId) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Reservation WHERE CustomerID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation(rs.getInt("ReservationID"), rs.getInt("CustomerID"),
                        rs.getInt("VehicleID"), rs.getTimestamp("StartDate"),
                        rs.getTimestamp("EndDate"), rs.getDouble("TotalCost"),
                        rs.getString("Status"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    // Method to check if the vehicle is available for the given dates
    public boolean isVehicleAvailable(int vehicleId, String startDate, String endDate) throws SQLException {
    	
    	try(Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))){
        String query = "SELECT COUNT(*) FROM Reservation WHERE VehicleID = ? " +
                       "AND ((StartDate <= ? AND EndDate >= ?) OR (StartDate <= ? AND EndDate >= ?))";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            stmt.setDate(2, java.sql.Date.valueOf(endDate)); // End date of the requested reservation
            stmt.setDate(3, java.sql.Date.valueOf(startDate)); // Start date of the requested reservation
            stmt.setDate(4, java.sql.Date.valueOf(endDate)); // End date of the requested reservation
            stmt.setDate(5, java.sql.Date.valueOf(startDate)); // Start date of the requested reservation

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // If count is 0, the vehicle is available
                }
            }
        }
        return false;
    	}
    }

    @Override
    public void createReservation(Reservation reservation) throws ReservationException {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "INSERT INTO Reservation (CustomerID, VehicleID, StartDate, EndDate, TotalCost, Status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reservation.getCustomerID());
            stmt.setInt(2, reservation.getVehicleID());
            stmt.setTimestamp(3, new Timestamp(reservation.getStartDate().getTime()));
            stmt.setTimestamp(4, new Timestamp(reservation.getEndDate().getTime()));
            stmt.setDouble(5, reservation.getTotalCost());
            stmt.setString(6, reservation.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Throwing a ReservationException if there's an issue with SQL execution
            throw new ReservationException("Failed to create reservation: " + e.getMessage());
        }
    }


    @Override
    public void updateReservation(Reservation reservation) throws ReservationException {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "UPDATE Reservation SET CustomerID = ?, VehicleID = ?, StartDate = ?, EndDate = ?, TotalCost = ?, Status = ? WHERE ReservationID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reservation.getCustomerID());
            stmt.setInt(2, reservation.getVehicleID());
            stmt.setTimestamp(3, new Timestamp(reservation.getStartDate().getTime()));
            stmt.setTimestamp(4, new Timestamp(reservation.getEndDate().getTime()));
            stmt.setDouble(5, reservation.getTotalCost());
            stmt.setString(6, reservation.getStatus());
            stmt.setInt(7, reservation.getReservationID());
            stmt.executeUpdate();
        } catch (SQLException e) {
        	throw new ReservationException("Failed to update reservation: " + e.getMessage());
        }
    }
    
 
    public void updateReservationStatus(int reservationId, String newStatus) throws ReservationException {
        
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"));
             PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET Status = ? WHERE ReservationID = ?")) {
            
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, reservationId);

           
            int rowsAffected = stmt.executeUpdate();

            // If no rows were affected, throw a custom exception
            if (rowsAffected == 0) {
                throw new ReservationException("Failed to update reservation status. Reservation not found.");
            }

        } catch (SQLException e) {
            
            throw new ReservationException("Database error while updating reservation status: " + e.getMessage());
        }
    }


    @Override
    public void cancelReservation(int reservationId) throws ReservationException {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"));
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Reservation WHERE ReservationID = ?")) {
            
            stmt.setInt(1, reservationId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new ReservationException("Failed to delete reservation. Reservation not found.");
            }

        } catch (SQLException e) {
            // Wrap the SQLException in a custom ReservationException
            throw new ReservationException("Database error while cancelling reservation: " + e.getMessage());
        }
    }

}
