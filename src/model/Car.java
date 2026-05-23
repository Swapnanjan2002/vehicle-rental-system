package model;

public class Car extends Vehicle {
    private boolean hasAc;

    public Car(String vehicleId, String model, double baseDailyRate, boolean hasAc) {
        super(vehicleId, model, baseDailyRate);
        this.hasAc = hasAc;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseDailyRate() * days;
        if (hasAc) {
            cost += (200.0 * days); // Add 200 per day for AC
        }
        return cost;
    }
}