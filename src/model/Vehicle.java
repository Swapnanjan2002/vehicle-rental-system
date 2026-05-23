package model;

public abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseDailyRate;

    public Vehicle(String vehicleId, String model, double baseDailyRate) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseDailyRate = baseDailyRate;
    }

    public abstract double calculateRentalCost(int days);

    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getModel() { return model; }
    public double getBaseDailyRate() { return baseDailyRate; }
}