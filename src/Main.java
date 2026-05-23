import dao.VehicleDAO;
import java.util.List;
import java.util.Scanner;
import model.Vehicle;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VehicleDAO dao = new VehicleDAO();
        boolean running = true;

        while (running) {
            System.out.println("\n=== RENTAL DASHBOARD ===");
            System.out.println("1. View Available Fleet");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. Return a Vehicle & Generate Invoice");
            System.out.println("4. View Rented Vehicles (Admin)");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            String input = scanner.nextLine();
            int choice = -1;
            
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please type a number from the menu.");
                continue; 
            }

            switch(choice) {
                case 1:
                    List<Vehicle> fleet = dao.getAvailableVehicles();
                    System.out.println("\n--- Available Vehicles ---");
                    if (fleet.isEmpty()) {
                        System.out.println("No vehicles are currently available.");
                    } else {
                        for (Vehicle v : fleet) {
                            System.out.printf("[%s] %s - Rs.%.2f/day%n", 
                                    v.getVehicleId(), v.getModel(), v.getBaseDailyRate());
                        }
                    }
                    break;

                case 2:
                    System.out.print("\nEnter the Vehicle ID you want to rent: ");
                    String idToRent = scanner.nextLine();
                    
                    if (dao.rentVehicle(idToRent)) {
                        System.out.println("Success! Vehicle " + idToRent + " has been booked.");
                    } else {
                        System.out.println("Failed. Vehicle is either invalid or already rented.");
                    }
                    break;

                case 3:
                    System.out.print("\nEnter the Vehicle ID being returned: ");
                    String returnId = scanner.nextLine();
                    
                    Vehicle rentedVehicle = dao.getRentedVehicle(returnId);
                    
                    if (rentedVehicle == null) {
                        System.out.println("Error: Vehicle not found or is not currently rented out.");
                        break;
                    }

                    System.out.print("Enter Customer Name: ");
                    String customerName = scanner.nextLine();
                    
                    System.out.print("How many days was it rented for? ");
                    int days = 0;
                    try {
                        days = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Days must be a valid number. Return cancelled.");
                        break;
                    }
                    
                    double totalCost = rentedVehicle.calculateRentalCost(days);
                    
                    if (dao.returnVehicle(returnId, customerName, days, totalCost)) {
                        System.out.println("\n--- INVOICE GENERATED ---");
                        System.out.printf("Customer: %s%n", customerName);
                        System.out.printf("Vehicle: %s (%s)%n", rentedVehicle.getModel(), rentedVehicle.getVehicleId());
                        System.out.printf("Days Rented: %d%n", days);
                        System.out.printf("Total Amount Due: Rs. %.2f%n", totalCost);
                        System.out.println("-------------------------");
                        System.out.println("Vehicle successfully returned to the fleet.");
                    } else {
                        System.out.println("System Error: Could not process the return.");
                    }
                    break;

                case 4:
                    List<Vehicle> rentedFleet = dao.getRentedVehicles();
                    System.out.println("\n--- Currently Rented Vehicles ---");
                    if (rentedFleet.isEmpty()) {
                        System.out.println("There are no vehicles currently out on rent.");
                    } else {
                        for (Vehicle v : rentedFleet) {
                            System.out.printf("[%s] %s%n", v.getVehicleId(), v.getModel());
                        }
                    }
                    break;

                case 5:
                    running = false;
                    System.out.println("Shutting down system...");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1, 2, 3, 4, or 5.");
            }
        }
        scanner.close();
    }
}