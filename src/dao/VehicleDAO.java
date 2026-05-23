package dao;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Car;
import model.Motorcycle;
import model.Truck;
import model.Vehicle;

public class VehicleDAO {
    
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> fleet = new ArrayList<>();
        String query = "SELECT * FROM fleet WHERE is_rented = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("vehicle_type");
                String id = rs.getString("vehicle_id");
                String model = rs.getString("model");
                double rate = rs.getDouble("base_rate");

                if (type.equals("CAR")) {
                    fleet.add(new Car(id, model, rate, rs.getBoolean("has_ac")));
                } else if (type.equals("TRUCK")) {
                    fleet.add(new Truck(id, model, rate, rs.getDouble("cargo_capacity")));
                } else if (type.equals("MOTORCYCLE")) {
                    fleet.add(new Motorcycle(id, model, rate, false)); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fleet;
    }

    public List<Vehicle> getAvailableVehiclesByType(String vehicleType) {
        List<Vehicle> fleet = new ArrayList<>();
        String query = "SELECT * FROM fleet WHERE is_rented = FALSE AND vehicle_type = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, vehicleType);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("vehicle_type");
                    String id = rs.getString("vehicle_id");
                    String model = rs.getString("model");
                    double rate = rs.getDouble("base_rate");

                    if (type.equals("CAR")) {
                        fleet.add(new Car(id, model, rate, rs.getBoolean("has_ac")));
                    } else if (type.equals("TRUCK")) {
                        fleet.add(new Truck(id, model, rate, rs.getDouble("cargo_capacity")));
                    } else if (type.equals("MOTORCYCLE")) {
                        fleet.add(new Motorcycle(id, model, rate, false)); 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fleet;
    }
    
    public boolean rentVehicle(String vehicleId) {
        String query = "UPDATE fleet SET is_rented = TRUE WHERE vehicle_id = ? AND is_rented = FALSE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, vehicleId);
            int rowsAffected = stmt.executeUpdate(); 
            
            return rowsAffected > 0; 
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Vehicle getRentedVehicle(String vehicleId) {
        String query = "SELECT * FROM fleet WHERE vehicle_id = ? AND is_rented = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String type = rs.getString("vehicle_type");
                String id = rs.getString("vehicle_id");
                String model = rs.getString("model");
                double rate = rs.getDouble("base_rate");

                if (type.equals("CAR")) {
                    return new Car(id, model, rate, rs.getBoolean("has_ac"));
                } else if (type.equals("TRUCK")) {
                    return new Truck(id, model, rate, rs.getDouble("cargo_capacity"));
                } else if (type.equals("MOTORCYCLE")) {
                    return new Motorcycle(id, model, rate, false); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }


    public boolean returnVehicle(String vehicleId, String customerName, int days, double totalCost) {
        String updateFleet = "UPDATE fleet SET is_rented = FALSE WHERE vehicle_id = ?";
        String insertRental = "INSERT INTO rentals (vehicle_id, customer_name, rental_days, total_cost) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); 

            try (PreparedStatement updateStmt = conn.prepareStatement(updateFleet);
                 PreparedStatement insertStmt = conn.prepareStatement(insertRental)) {
                 
                updateStmt.setString(1, vehicleId);
                updateStmt.executeUpdate();

                insertStmt.setString(1, vehicleId);
                insertStmt.setString(2, customerName);
                insertStmt.setInt(3, days);
                insertStmt.setDouble(4, totalCost);
                insertStmt.executeUpdate();

                conn.commit(); 
                return true;
                
            } catch (SQLException ex) {
                conn.rollback(); 
                ex.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
