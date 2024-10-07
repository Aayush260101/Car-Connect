package service;

import entity.Vehicle;
import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements IVehicleService {
	static Connection conn;
	public static void getConn()
	{
		if(conn==null)
			conn=DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"));
	}
    @Override
    public Vehicle getVehicleById(int vehicleId) {
        Vehicle vehicle = null;
        try{
        	getConn();
            String query = "SELECT * FROM Vehicle WHERE VehicleID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                vehicle = new Vehicle(rs.getInt("VehicleID"), rs.getString("Model"),
                        rs.getString("Make"), rs.getInt("Year"), rs.getString("Color"),
                        rs.getString("RegistrationNumber"), rs.getBoolean("Availability"),
                        rs.getDouble("DailyRate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }
    
    public List<Vehicle> getAllVehicles(){
    	List<Vehicle> vehicles = new ArrayList<>();
    	try(Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))){
    		String query = "Select * from Vehicle";
    		PreparedStatement stmt = conn.prepareStatement(query);
    		ResultSet rs = stmt.executeQuery();
    		
    		while(rs.next()) {
    			Vehicle vehicle = new Vehicle(rs.getInt("VehicleID"), rs.getString("Model"),
                        rs.getString("Make"), rs.getInt("Year"), rs.getString("Color"),
                        rs.getString("RegistrationNumber"), rs.getBoolean("Availability"),
                        rs.getDouble("DailyRate"));
    			vehicles.add(vehicle);
    		}
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return vehicles;
    }

    @Override
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "SELECT * FROM Vehicle WHERE Availability = true";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(rs.getInt("VehicleID"), rs.getString("Model"),
                        rs.getString("Make"), rs.getInt("Year"), rs.getString("Color"),
                        rs.getString("RegistrationNumber"), rs.getBoolean("Availability"),
                        rs.getDouble("DailyRate"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "INSERT INTO Vehicle (Model, Make, Year, Color, RegistrationNumber, Availability, DailyRate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, vehicle.getModel());
            stmt.setString(2, vehicle.getMake());
            stmt.setInt(3, vehicle.getYear());
            stmt.setString(4, vehicle.getColor());
            stmt.setString(5, vehicle.getRegistrationNumber());
            stmt.setBoolean(6, vehicle.isAvailability());
            stmt.setDouble(7, vehicle.getDailyRate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "UPDATE Vehicle SET Model = ?, Make = ?, Year = ?, Color = ?, RegistrationNumber = ?, Availability = ?, DailyRate = ? WHERE VehicleID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, vehicle.getModel());
            stmt.setString(2, vehicle.getMake());
            stmt.setInt(3, vehicle.getYear());
            stmt.setString(4, vehicle.getColor());
            stmt.setString(5, vehicle.getRegistrationNumber());
            stmt.setBoolean(6, vehicle.isAvailability());
            stmt.setDouble(7, vehicle.getDailyRate());
            stmt.setInt(8, vehicle.getVehicleID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeVehicle(int vehicleId) {
        try (Connection conn = DBConnUtil.getConnection(DBPropertyUtil.getConnectionFromProperties("db.properties"))) {
            String query = "DELETE FROM Vehicle WHERE VehicleID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, vehicleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
