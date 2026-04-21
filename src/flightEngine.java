package src;
import java.util.Scanner;

/**
 * HAT-F Backend Computational Engine v6.3
 * Purpose: High-precision numerical validation of mission parameters.
 */
public class FlightEngine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- HAT-F BACKEND: MISSION ANALYZER ---");

        // These variables match the HTML input IDs
        System.out.print("Enter Thrust (N): ");
        double thrustS1 = sc.nextDouble();
        
        System.out.print("Enter Fuel Mass (kg): ");
        double fuelS1 = sc.nextDouble();

        System.out.print("Enter Drag Coefficient (Cd): ");
        double cd = sc.nextDouble();

        // Execution Logic
        double y = 0, v = 0, t = 0, dt = 0.05;
        double mass = 12000 + fuelS1; // Assuming constant S2 mass for summary

        System.out.println("\nCALCULATING TRAJECTORY...");
        
        while (y >= 0) {
            double rho = AtmosphericModel.getAirDensity(y);
            double dragForce = 0.5 * rho * (v * v) * cd * 4.5;
            double netForce = (fuelS1 > 0 ? thrustS1 : 0) - dragForce - (9.81 * mass);
            double acceleration = netForce / mass;

            v += acceleration * dt;
            y += v * dt;
            t += dt;
            if (fuelS1 > 0) fuelS1 -= 550 * dt;

            if (Math.round(t * 20) % 100 == 0) {
                System.out.printf("T: %.1fs | Alt: %.2f m | Vel: %.2f m/s\n", t, y, v);
            }
            if (t > 1000) break; // Time-out safety
        }
        
        System.out.println("\n--- SIMULATION SUCCESSFUL ---");
        System.out.printf("Final Flight Time: %.2f seconds\n", t);
    }
}
