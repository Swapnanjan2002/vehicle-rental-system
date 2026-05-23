package model;

public class Motorcycle extends Vehicle {
    private boolean includesHelmet;

    public Motorcycle(String vehicleId, String model, double baseDailyRate, boolean includesHelmet) {
        super(vehicleId, model, baseDailyRate);
        this.includesHelmet = includesHelmet;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseDailyRate() * days;
        if (includesHelmet) {
            cost += (50.0 * days); // Rs. 50/day gear rental fee
        }
        return cost;
    }
}