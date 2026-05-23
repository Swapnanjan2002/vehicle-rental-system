package model;

public class Truck extends Vehicle {
    private double cargoCapacity;

    public Truck(String vehicleId, String model, double baseDailyRate, double cargoCapacity) {
        super(vehicleId, model, baseDailyRate);
        this.cargoCapacity = cargoCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        // Truck adds 10% premium per ton of capacity
        double baseCost = getBaseDailyRate() * days;
        return baseCost + (baseCost * 0.10 * cargoCapacity);
    }
}